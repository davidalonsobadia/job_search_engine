package com.searchengine.executables;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
 		
 		try {
 			
 			String search_adapted = URLEncoder.encode(search.getCompleteSearch().replace(" ", "+"), "UTF-8");
 			String user_agent_adapted = URLEncoder.encode(api.user_agent, "UTF-8");
 					
 			String query_api =  root_url + "&" + publisher_id + "&" +query 
 					+ search_adapted + "&" + api.location + "&" + api.country 
 					+ "&" + user_agent_adapted + "&" + api.api_version  + "&" + api.limit ;
 			
	        System.out.println(query_api);

 			
 			// MAKE THE QUERY
 			
 			Document doc = Jsoup.connect(query_api).userAgent("Mozilla").get();
 			
	        //System.out.println(doc);	

 			// GET RESPONSE AND MANIPULATE IT
 			
 			Elements tags = item.getElementsByTag("post-tags");
 			
 			String tag_container = tags.select("post-container").first().text();
 			String tag_title = tags.select("post-title").first().text();
 			String tag_description = tags.select("post-description").first().text();
 			String tag_date = tags.select("post-date").first().text();
 			String tag_link = tags.select("post-link").first().text();
 			String tag_company = tags.select("post-company").first().text(); 
 			
 		
	 		Element loop = doc.getElementsByTag(tag_container).first();
	        
		    // Get all posts
	        Elements posts = loop.children();
	        
	        //For each post
	        for (Element post : posts) {
		        
		        // Get title 
	        	String title = post.getElementsByTag(tag_title).text();
		               
		        //description
		        String description = post.getElementsByTag(tag_description).text();
	
		        // Get date
		        String datePost = post.getElementsByTag(tag_date).text();
		        Date date = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz", Locale.ENGLISH).parse(datePost);
		        
		        //link
		        String link = post.getElementsByTag(tag_link).text();
		        
		        // company
		        String company = post.getElementsByTag(tag_company).text();
		        
		        System.out.println(link);
		        
		        jobPost job = new jobPost();
		        job.setTitle(title);
		        job.setCompany(company);
		        job.setDate(date);
		        job.setDescription(description);
		        job.setLink(link);
		        job.setSource(name);
		        
		        //jobPost.add(job);
			        
	        }
	        
 			
	    } catch (Exception e) {
	    	System.out.println("Exception handling resquest from: " + name);
	    	System.out.print(e);
	    }
 		
	}
	
}