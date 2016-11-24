package com.senochow.simnews.main;

import java.util.ArrayList;
import java.util.List;
import com.senochow.hash.JenkinsHash;
import com.senochow.simnews.utils.KeywordExtractor;
import com.senochow.simnews.utils.Pair;
import com.senochow.simnews.utils.WeightPair;

/**
 * Implemention of simhash algorithms
 * ClassName: SimHasher
 * Function: 
 * date: 2015年12月7日 上午10:35:15 
 * @author senochow
 * 修改人：senochow   
 * 修改时间：2015年12月7日 上午10:35:15   
 * 修改备注：
 */
public class SimHasher {
	private JenkinsHash hasher;
	public KeywordExtractor extractor;
	private int BITS_LENGTH = 64;
	
	public SimHasher(KeywordExtractor keywordExtractor) {
		hasher = new JenkinsHash();
		this.extractor = keywordExtractor;
	}
	/**
	 * extract word,weight pair from text
	 * @param text
	 * @param topK
	 * @param word_weights
	 * @return
	 */
	private List<WeightPair> extract(String text, int topK) {
		// TODO Auto-generated method stub
		return extractor.extract_word(text, topK);	
		
	}
	/**
	 * get hash,weight from text
	 * @param text
	 * @param topK
	 * @param hashvalues
	 * @return
	 */
	public boolean make(String text, int topK, List<Pair> hashvalues) {
		List<WeightPair> word_weights = extract(text, topK);
		if (word_weights == null) {
			System.out.println("extract failed..");
			return false;
		}

		for (int i = 0; i < word_weights.size(); i++) {
			long word_hash = hasher.hash64(word_weights.get(i).first.getBytes());
			double weight = word_weights.get(i).second;
			Pair pair = new Pair(word_hash, weight);
			hashvalues.add(pair);
		}		
		return true;
	}
	
	/**
	 * get simhash value 64 bits
	 * @param text
	 * @param topK
	 * @param h64
	 * @return
	 */
	public long simhash64(String text, int topK) {
		List<Pair> hashvalues = new ArrayList<>();
		if (!make(text, topK, hashvalues)) {
			System.out.println("simhash64 failed.");
			return -1;
		}
		double[] weights = new double[BITS_LENGTH];
		for (int i = 0; i < BITS_LENGTH; i++) weights[i] = 0.0;
		// merge all single word hash values
		long u64_1 = 0x0000000000000001;		          
		for (int i = 0; i < hashvalues.size(); i++) {
			for (int j = 0; j < BITS_LENGTH; j++) {
				weights[j] += (((u64_1 << j) & hashvalues.get(i).first) != 0 ? 1:-1) * hashvalues.get(i).second;
			}
		}
		long h64 = 0;
		for (int i = 0; i < BITS_LENGTH; i++) {
			if (weights[i] > 0.0) 			
				h64 |= (u64_1 << i); 				
		}
		return h64;
		
	}
	/**
	 * get two hash values hanming distance
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public int get_distance(long lhs, long rhs) {
		int cnt = 0;
		lhs ^= rhs;
		while (lhs != 0) {
			lhs &= lhs -1;
			cnt++;
		}
		return cnt;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
