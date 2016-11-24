package com.senochow.simnews.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Set;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.senochow.simnews.utils.GlobalConfig;
import com.senochow.simnews.utils.KeywordExtractor;
import com.senochow.simnews.utils.LoadData;
import com.senochow.simnews.utils.News;
import com.senochow.simnews.utils.TextProcess;
import com.senochow.simnews.utils.WeightPair;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

/**
 * Given a query document, find it's most relevent doc in local
 * 
 * ClassName: SimNews
 * Function: 
 * date: 2015年12月7日 上午10:33:00 
 * @author senochow
 * 修改人：senochow   
 * 修改时间：2015年12月7日 上午10:33:00   
 * 修改备注：
 */
public class SimNews {

	public SimHasher simHasher = new SimHasher(new KeywordExtractor());
	public boolean debug = true; // true for local test, set false for online
	public Map<String, News> all_news = new HashMap<>();
	public Map<String, Long> news_hash = new HashMap<>();
	public int topkeywords; // extract topk words for simhash
	public final int topnews = 20; // top n news for reranking
	public double first_simval = 0.33; 
	public double min_simval = 0.35;
	
	public SimNews(int topkeywords) {
		this.topkeywords = topkeywords;
	}
	/**
	 * Initialize: calculate all news hash value
	 * @return
	 */
	public Map<String, Long> calculate_allnews_hash() {
		Map<String, Long> news_hash = new HashMap<>();
		Iterator<Entry<String, News>> iter = all_news.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, News> entry = iter.next();
			long hval = simHasher.simhash64(entry.getValue().getTitle(), topkeywords);
			news_hash.put(entry.getKey(), hval);  
		}
		return news_hash;
	}
	 

	/**
	 *  return similary news list
	 * @param query
	 * @return
	 */
	public List<Entry<String, Double>> get_similary_news(String query) {
		List<Entry<String, Integer>> res = new ArrayList<>();
		Map<String, Integer> simnews = new HashMap<>();
//		long q_hash = news_hash.get(qid);
		long q_hash = simHasher.simhash64(query, topkeywords);
		Iterator<Entry<String, Long>> iter = news_hash.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Long> entry = iter.next();
//			if (entry.getKey().equals(qid)) continue;
			simnews.put(entry.getKey(), simHasher.get_distance(q_hash, entry.getValue()));
		}
		List<Entry<String, Integer>> sorted_list = TextProcess.getSortList(simnews);
		// test for jacarrd
		int topn = Math.min(this.topnews, sorted_list.size());
		for (int i = 0; i < topn; ++i) {
			res.add(sorted_list.get(i));
		}
		List<Entry<String, Double>> ranknews = reranking_by_sim(res, query);
		return ranknews;
		
	}
	/**
	 * rerank news by jacarrd similarity
	 * @param topnews
	 * @param ori_news
	 * @return
	 */
	public List<Entry<String, Double>> reranking_by_sim(List<Entry<String, Integer>> topnews, String ori_news) {
		HashMap<String, Double> ranknews = new HashMap<>();
		List<String> ori_words = simHasher.extractor.jiebaSegmenter.sentenceProcess(ori_news);
		for (Entry<String, Integer> entry : topnews) {
			String title = all_news.get(entry.getKey()).getTitle();
			List<String> words = simHasher.extractor.jiebaSegmenter.sentenceProcess(title);
			double val = TextProcess.get_sim_val(ori_words, words);
			ranknews.put(entry.getKey(), val);			
		}
		List<Entry<String, Double>> rank_list = TextProcess.getSortListReverse(ranknews);
		if (debug) {
			rank_list.remove(0);
		}		
		List<Entry<String, Double>> res_list = new ArrayList<>();
		// filter news and return most similarity
		int i = 0;
		for (Entry<String, Double> entry : rank_list) {
			if (i > 5) break;
			if (i == 0 && entry.getValue() > first_simval) res_list.add(entry);
			else if (entry.getValue() >= min_simval){
				res_list.add(entry);
			}
			i++;
		}
		return res_list;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GlobalConfig globalConfig = new GlobalConfig("./file/global.properties");
		int topwords = GlobalConfig.topKeywords;
		String xml_file = "./news.txt";
		// 1. load local data into file, not used for online services
		LoadData loadData = new LoadData();
		SimNews simNews = new SimNews(topwords);
		simNews.all_news = loadData.load_data(xml_file);
		System.out.println(simNews.all_news.size());	
		// 2. calculate simhash value for each document...
		long startTime=System.currentTimeMillis(); 
		simNews.news_hash = simNews.calculate_allnews_hash();
		long endTime=System.currentTimeMillis(); 
		System.out.println((endTime-startTime)+"ms");
		String qid = "";
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("query...");
			qid = scanner.nextLine();				
			if (!simNews.news_hash.containsKey(qid)) {
				System.out.println("input error");
				continue;
			}
			startTime=System.currentTimeMillis(); 
			String query = simNews.all_news.get(qid).getTitle();
			List<Entry<String, Double>> simnews = simNews.get_similary_news(query);
			System.out.println("origin: \t"+simNews.all_news.get(qid).getTitle());
			if (simnews == null || simnews.size() == 0) {
				System.out.println("No similarity news...");
				continue;
			}
			for (Entry<String, Double> entry : simnews) {
				System.out.println(simNews.all_news.get(entry.getKey()).getTitle() + '\t' + entry.getValue());
			}
			endTime=System.currentTimeMillis(); 
			System.out.println((endTime-startTime)+"ms");
		}
		
			
		
		
	}

}
