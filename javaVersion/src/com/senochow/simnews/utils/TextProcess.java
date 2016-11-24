package com.senochow.simnews.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TextProcess {
	public static List<Entry<String, Integer>> getSortList(Map<String, Integer> tmplist){
		 Set<Entry<String, Integer>> set = tmplist.entrySet();
		 List<Entry<String, Integer>> sortList = new ArrayList<Entry<String, Integer>>(set); 
		 Collections.sort(sortList, new Comparator<Entry<String, Integer>>() {  
		          			    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {     
		          				return (o1.getValue() - o2.getValue());	                         
		          		}
		                  });
		  return sortList;
	}
	
	public static List<Entry<String, Double>> getSortListReverse(Map<String, Double> tmplist){
		 Set<Entry<String, Double>> set = tmplist.entrySet();
		 List<Entry<String, Double>> sortList = new ArrayList<Entry<String, Double>>(set); 
		 Collections.sort(sortList, new Comparator<Entry<String, Double>>() {  
		          			    public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {     
		          			    	 if ((o2.getValue() - o1.getValue())>0) 
		          			            return 1; 
		          			          else if((o2.getValue() - o1.getValue())==0) 
		          			            return 0; 
		          			          else 
		          			            return -1; 
		          			       
		          			    	                         
		          		}
		                  });
		  return sortList;
	}
	/**
	 * get jacarrd similarity
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static double get_sim_val (List<String> s1, List<String> s2) {
		double val = 0;
		if (s1.size() <= s2.size()) {
			for (String w1 : s1) {
				if (s2.contains(w1)) val++;
			}
			val /= (s1.size()+s2.size());
		}else {
			return get_sim_val(s2, s1);
		}
		return val;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
