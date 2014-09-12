package com.searchengine.executables;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class APIObject {

	private String nameAPI;
	
	/**
	private String root_url;
	private String publisher_id;
	private String query;
	private String location;
	private String country;
	private String user_agent;
	private String api_version;
	private String limit;
	**/
	
	String root_url;
	String publisher_id;
	String query;
	String location;
	String country;
	String user_agent;
	String api_version;
	String limit;
	
	Element item;

	public APIObject(Document xml, String name){
		
		item = xml.getElementById(name);
        
        Element api = item.getElementsByTag("api").first();
        
        this.nameAPI = item.select("name").first().text();
 	    
        this.root_url = api.select("root_url").first().text();
 	   	this.publisher_id = api.select("publisher_id").first().text();
 	   	this.query = api.select("query").first().text();
 	   	this.location = api.select("location").first().text();
 	   	this.country = api.select("country").first().text();
 	   	this.user_agent = api.select("user_agent").first().text();
 	   	this.api_version = api.select("api_version").first().text();
 		this.limit = api.select("limit").first().text();
 		
	}
	
}
