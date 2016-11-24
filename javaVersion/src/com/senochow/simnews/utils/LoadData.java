package com.senochow.simnews.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/**
 * load local news data into memory, not used for online service
 * ClassName: LoadData
 * Function: 
 * date: 2015年12月7日 上午11:05:09 
 * @author senochow
 * 修改人：senochow   
 * 修改时间：2015年12月7日 上午11:05:09   
 * 修改备注：
 */
public class LoadData {

	public Map<String, News> load_data(String filename) {
		Map<String, News> id_news = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
			String line = null;
			while ((line = reader.readLine())!= null) {
				String[] arr = line.split("\t");
				if (arr.length != 4) System.out.println("error...");
				News news = new News();
				news.setNid(arr[0]);
				news.setTitle(arr[1]);
				news.setContent(arr[2]);
				news.setUrl(arr[3]);
				id_news.put(news.getNid(), news);
			}
			reader.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id_news;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
