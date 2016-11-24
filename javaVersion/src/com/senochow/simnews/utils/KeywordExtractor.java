package com.senochow.simnews.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.util.Map.Entry;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.senochow.simnews.main.SimHasher;

public class KeywordExtractor {

	private static boolean is_loadfile = false;
	private static Set<String> stopwords = new HashSet<>();
	private static HashMap<String, Double> idf_dic = new HashMap<>();
	private static double idf_avg = 0.0;
	public static JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
	
	/**
	 * init dict file once
	 */
	private static void init_dicfile() {
		stopwords = load_word_file(GlobalConfig.stopwords_file);
		idf_dic = load_idf_file(GlobalConfig.idf_file);
	}
	
	
	public KeywordExtractor () {
		if (is_loadfile) return;
		else {
			is_loadfile = true;
			init_dicfile();
		}
	}
	/**
	 * load idf value dict, the voo is set to idf_avg when computation in online
	 * @param filename
	 * @return
	 */
	public static HashMap<String, Double> load_idf_file(String filename) {
		double idf_sum = 0.0;
		int lineno = 0;
		HashMap<String, Double> hashMap = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"));
			String line = null;
			while ((line = reader.readLine())!=null) {
				String[] arr = line.split(" ");
				double val =  Double.parseDouble(arr[1]);
				hashMap.put(arr[0], val);
				idf_sum += val;
				lineno++;
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		idf_avg = idf_sum/lineno;
		return hashMap;
	}
	public static Set<String> load_word_file(String filename){
		Set<String> words_set = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"));
			String line = null;
			while ((line = reader.readLine())!=null) {
				words_set.add(line);
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words_set;
	}
	/**
	 * filter single words and stop words
	 * @param term
	 * @return
	 */
	private boolean is_filter(String word) {
		if (word.length() == 1)
			return true;
		if (stopwords.contains(word)) 
			return true;
		return false;
	}
	private List<String> get_words(List<SegToken> terms) {
		List<String> words = new ArrayList<>();
		for (SegToken token : terms) {
			words.add(token.word);
		}
		return words;
	}
	
	public List<WeightPair> extract_word(String text, int topK) {
		List<WeightPair> wordweights = new ArrayList<>();
//		List<SegToken> terms = jiebaSegmenter.process(text, SegMode.SEARCH);
//		List<String> words = get_words(terms);
		List<String> words = jiebaSegmenter.sentenceProcess(text);
		// set tf value, and filter words
		HashMap<String, Integer> wordmap = new HashMap<>();
		for (String term : words) {
			if (is_filter(term)) continue;
			if (wordmap.containsKey(term)) {
				int val = wordmap.get(term);
				wordmap.put(term, val+1);
			}else {
				wordmap.put(term, 1);
			}
		}
		// set tfidf value
		Iterator<Entry<String, Integer>> iter = wordmap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			double tfidf = 0.0;
			if (idf_dic.containsKey(entry.getKey())) tfidf = entry.getValue()*idf_dic.get(entry.getKey());
			else tfidf = entry.getValue()*idf_avg;
			WeightPair weightPair = new WeightPair(entry.getKey(), tfidf);
			wordweights.add(weightPair);
		}
		Collections.sort(wordweights);
		topK = Math.min(topK, wordweights.size());
		wordweights = wordweights.subList(0, topK);
//		for (WeightPair weightPair : wordweights) {
//			System.out.println(weightPair.toString());
//		}
		return wordweights;
	}
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GlobalConfig globalConfig = new GlobalConfig("./file/global.properties");	
		KeywordExtractor keywordExtractor = new KeywordExtractor();
		SimHasher simHasher = new SimHasher(keywordExtractor);
		String str1 = "【法轮功_维权_营救_反迫害】女孩为父申冤 一万五千人红手印支持";
		List<String> terms = jiebaSegmenter.sentenceProcess(str1);
		for (String term : terms) {
			System.out.print(term+"\t");
		}
		System.out.println("**************");
		String str2 = "【中国真相最新热点新闻】女孩为父申冤 一万五千人红手印支持";
		List<SegToken> terms1 = jiebaSegmenter.process(str2, SegMode.SEARCH);//(str2);
		for (SegToken term : terms1) {
			System.out.print(term.word+"\t");
		}
//		String str2 = "【许志永_中国禁闻_中国新闻评论】许志永受审 律师全程沉默 表抗议";
		long val1 = simHasher.simhash64(str1, 6);
		long val2 = simHasher.simhash64(str2, 6);
//		simHasher.simhash64(str2, 5, val2);
		long u1 = 0x0000000000000001;
		for (int i = 0; i < 64; i++) {
			if (((u1<<i)&val1) != 0) {
				System.out.print("1");
			}else {
				System.out.print("0");
			}
		}
		System.out.println();
//		for (int i = 0; i < 64; i++) {
//			if (((u1<<i)&val2) != 0) {
//				System.out.print("1");
//			}else {
//				System.out.print("0");
//			}
//		}
//		System.out.println();
		System.out.println("distance"+simHasher.get_distance(val1, val2));
		
	}

}
