package com.searchengine.executables;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.jsoup.parser.Parser;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class getWebPage {
	
	private StringSearch search;
	private String config_url = "com/searchengine/resources/webpages_config.xml";
	
	public getWebPage(StringSearch search){
		this.search = search;
	}
	

	public static Document getXMLDocumentFromInternalFile(String internalUrl) throws IOException, URISyntaxException{
		
		InputStream webpage_config = getWebPage.class
			           .getClassLoader()
			           .getResourceAsStream(internalUrl);
	          
	     URL url_web_page = getWebPage.class.
	     		getClassLoader().
	     		getResource(internalUrl);
	     
	     String urlString = url_web_page.toURI().toString();
	     
	     Document xml_doc= Jsoup.parse(webpage_config,
	             "UTF-8",
	             urlString,
	             Parser.xmlParser());
	     	     
	     return xml_doc;
		
	}

	
	public ArrayList<jobPost> BerlinStartupJobs() throws Exception{
		
		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		Document xml_config = getXMLDocumentFromInternalFile(config_url);
		
		String name = "BerlinStartupJobs";
		
		webCrawlerObject webObject = new webCrawlerObject(xml_config, name, search.getArrayWords());
		
		webObject.getwebCrawlerDataRequest();
			
		jobPosts = webObject.setwebCrawlerRequest();
			
		return jobPosts;
		
	}

	
	public ArrayList<jobPost> BerlinJob() throws Exception{
		
		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		Document xml_config = getXMLDocumentFromInternalFile(config_url);
		
		String name = "BerlinJob";
		
		webCrawlerObject webObject = new webCrawlerObject(xml_config, name, search.getArrayWords());
		
		webObject.getwebCrawlerDataRequest();
			
		jobPosts = webObject.setwebCrawlerRequest();
			
		return jobPosts;
		
	}
	
	public ArrayList<jobPost> IndeedJobs() throws Exception{
		
		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		Document xml_config = getXMLDocumentFromInternalFile(config_url);
		
		String name = "Indeed";
		
		APIObject api = new APIObject(xml_config, name);
		
		api.getAPIDataRequest();
		
		api.setAPIRequest(search.getCompleteSearch());
		
		api.getAPIResponse();
	
		api.getAPIDataResponse();
			
		jobPosts = api.setAPIResponse(); 
		        
        return jobPosts;
		
	}
	
	public ArrayList<jobPost> CareerBuilderJobs() throws Exception{
		
		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		Document xml_config = getXMLDocumentFromInternalFile(config_url);
		
		String name = "CareerBuilder";
		
		APIObject api = new APIObject(xml_config, name);
		
		api.getAPIDataRequest();
		
		api.setAPIRequest(search.getCompleteSearch());
		
		api.getAPIResponse();
	
		api.getAPIDataResponse();
			
		jobPosts = api.setAPIResponse(); 
		        
        return jobPosts;
		
	}
	
	/**
	public ArrayList<jobPost> CareerBuilderJobs() throws Exception{
		 
		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
        Document xml_config = getXMLDocumentFromInternalFile(config_url);
		
        Element item = xml_config.getElementById("CareerBuilder");
        
        Element api = item.getElementsByTag("api").first();
        
        String name = item.select("name").first().text();
 	    
        String root_url = api.select("root_url").first().text();
 	   	String publisher_id = api.select("publisher_id").first().text();
 	   	String query = api.select("query").first().text();
 	   	String location = api.select("location").first().text();
 	   	String user_agent = api.select("user_agent").first().text();
 	   	
 	   try {
			
			String search_adapted = URLEncoder.encode(search.getCompleteSearch().replace(" ", "+"), "UTF-8");
			String user_agent_adapted = URLEncoder.encode(user_agent, "UTF-8");
					
			String query_api =  root_url + "&" + publisher_id + "&" + query 
					+ search_adapted + "&" + location;
			
			
			// MAKE THE QUERY
			
			Document doc = Jsoup.connect(query_api).userAgent("Mozilla").get();
		
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
		        
	 			System.out.println(post);
	        	
		        // Get title 
	        	String title = post.getElementsByTag(tag_title).text();
		               
		        //description
		        String description = post.getElementsByTag(tag_description).text();

		        // Get date
		        String datePost = post.getElementsByTag(tag_date).text();
		        Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", Locale.ENGLISH).parse(datePost);
		        
		        //link
		        String link = post.getElementsByTag(tag_link).text();
		        
		        // company
		        String company = post.getElementsByTag(tag_company).text();
		        
		        System.out.println(link);
		        
		        
		        // ADD INTERESTING RESULTS
		        
		        jobPost job = new jobPost();
		        job.setTitle(title);
		        job.setCompany(company);
		        job.setDate(date);
		        job.setDescription(description);
		        job.setLink(link);
		        job.setSource(name);
		        
		        jobPosts.add(job);
			        
	        }
		
	    } catch (Exception e) {
	    	System.out.println("Exception handling resquest from: " + name);
	    	System.out.print(e);
	    }
 	   
 	  return jobPosts;
	}
	
	**/

	public ArrayList<jobPost> LinkedinJobs() throws Exception{

		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		return jobPosts;
	}
}