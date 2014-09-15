package com.searchengine.executables;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class APIObject implements APIInterface{

	private String nameAPI;
	
	// XML piece we need
	private Element xml_item;
	
	// Data for API request (from XML)

	private String root_url;
	private String publisher_id;
	private String query;
	private String location;
	private String country;
	private String user_agent;
	private String api_version;
	private String limit;

	// Data triggered to the API
	String query_api;
	
	// Tags and data for API response (from XML)
	private String tag_container;
	private String tag_title;
	private String tag_description;
	private String tag_date;
	private String tag_link;
	private String tag_company; 
		
	
	// Data retrieved from API
	private Document APIResponse; 
	
	
	public APIObject (Document xml, String name){
		xml_item = xml.getElementById(name);
	}
	
	public void getAPIDataRequest(){		
		        
        Element api = xml_item.getElementsByTag("api").first();
        
        this.nameAPI = xml_item.select("name").first().text();
		
		this.root_url = api.select("root_url").first().text();
 	   	this.publisher_id = api.select("publisher_id").first().text();
 	   	this.query = api.select("query").first().text();
 	   	this.location = api.select("location").first().text();
 	   	this.country = api.select("country").first().text();
 	   	this.user_agent = api.select("user_agent").first().text();
 	   	this.api_version = api.select("api_version").first().text();
 		this.limit = api.select("limit").first().text();
		
	}
	
	public void setAPIRequest(String search){
		
		String search_adapted = "";
		String user_agent_adapted = "";
		
		try {
			search_adapted = URLEncoder.encode(search.replace(" ", "+"), "UTF-8");	
			user_agent_adapted = URLEncoder.encode(user_agent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error encoding and handling the String search: "+ search);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		String query_api =  root_url + "&" + publisher_id + "&" +query 
				+ search_adapted + "&" + location + "&" + country 
				+ "&" + user_agent_adapted + "&" + api_version  + "&" + limit ;
			
        System.out.println("Query launched: " + query_api);
	}
	
	public void getAPIResponse(){
		// MAKE THE QUERY
		try {
			APIResponse = Jsoup.connect(query_api).userAgent("Mozilla").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error getting API Data from Jsoup");
			System.out.println("API call: "+ query_api);
			e.printStackTrace();
		}
	}
	
	public void getAPIDataResponse(){
		
		Elements tags = xml_item.getElementsByTag("post-tags");
		
		this.tag_container = tags.select("post-container").first().text();
		this.tag_title = tags.select("post-title").first().text();
		this.tag_description = tags.select("post-description").first().text();
		this.tag_date = tags.select("post-date").first().text();
		this.tag_link = tags.select("post-link").first().text();
		this.tag_company = tags.select("post-company").first().text(); 
			
		
	}
	
	public void setAPIResponse(){
		
 		Element loop = APIResponse.getElementsByTag(tag_container).first();
		
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
	        try {
				Date date = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz", Locale.ENGLISH).parse(datePost);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Ecpetion produced handling Date formats for " + nameAPI);
				e.printStackTrace();
			}
	        
	        //link
	        String link = post.getElementsByTag(tag_link).text();
	        
	        // company
	        String company = post.getElementsByTag(tag_company).text();
	        
	        System.out.println(company);
	        
        }
		
	}
	
	/**
	public JobPost getResponse(){
		
		
	}
	**/
	
	/**
	public void copyAnswer(){
		
        jobPost job = new jobPost();
        job.setTitle(title);
        job.setCompany(company);
        job.setDate(date);
        job.setDescription(description);
        job.setLink(link);
        job.setSource(name);
	}
	**/

	/**
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
 			String user_agent_adapted = URLEncoder.encode(user_agent, "UTF-8");
 					
 			String query_api =  root_url + "&" + publisher_id + "&" +query 
 					+ search_adapted + "&" + location + "&" + country 
 					+ "&" + user_agent_adapted + "&" + api_version  + "&" + limit ;
 			
	        System.out.println(query_api);

 			
 			// MAKE THE QUERY
 			
 			Document doc = Jsoup.connect(query_api).userAgent("Mozilla").get();
 			
 			// GET RESPONSE AND MANIPULATE IT
 			
 			Elements tags = item.getElementsByTag("post-tags");
 			
 			this.tag_container = tags.select("post-container").first().text();
 			this.tag_title = tags.select("post-title").first().text();
 			this.tag_description = tags.select("post-description").first().text();
 			this.tag_date = tags.select("post-date").first().text();
 			this.tag_link = tags.select("post-link").first().text();
 			this.tag_company = tags.select("post-company").first().text(); 
 			
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
	**/
}