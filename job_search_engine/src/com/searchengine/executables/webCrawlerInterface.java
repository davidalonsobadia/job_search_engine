package com.searchengine.executables;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface webCrawlerInterface {

	void getwebCrawlerDataRequest();
	
	void setwebCrawlerRequest();
	
	Document getwebCrawlerResponse(Element web_url);
	
	ArrayList<jobPost> setwebCrawlerResponse();
}
