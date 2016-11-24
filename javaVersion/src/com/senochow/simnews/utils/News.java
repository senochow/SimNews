package com.senochow.simnews.utils;

public class News {
	private String title;
	private String content;
	private String nid;
	private String url;
	
	public News(String title, String content, String nid, String url) {
		this.title = title;
		this.content = content;
		this.nid = nid;
		this.url = url;
	}
	
	public News() {
		this.title = "";
		this.content = "";
		this.nid = "";
		this.url = "";
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
