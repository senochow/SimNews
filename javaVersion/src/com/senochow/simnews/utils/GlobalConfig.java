package com.senochow.simnews.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class GlobalConfig {
	
	public static int topKeywords;
	public static String stopwords_file;
	public static String idf_file;
	public static String userdict_file;
	
	public GlobalConfig(String filename){
		config(filename);
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
	public static void config(String filename) {
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(filename));
			Properties config = new Properties();
			config.load(in);
			topKeywords = Integer.parseInt(config.getProperty("TopKeywords"));
			stopwords_file = config.getProperty("stopwords_file");
			idf_file = config.getProperty("idf_file");
			userdict_file = config.getProperty("user_dic");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
}
