package com.searchengine.executables;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class webCrawlerObject extends JobSource implements Callable<ArrayList<jobPost>>{

	private String nameWebCrawler;
	private String thumbnail;
	
	private String searchWords[];

	private ArrayList<jobPost> jobPosts;
	
	// XML piece we need
	private Element xml_item;
	
	// Data for web Crawling request (from XML)
	private Elements web_urls;

	// Data triggered to the API
	String query_api;
	
	// Tags and data for API response (from XML)
	private String tag_container;
	private String tag_title;
	private String tag_description;
	private String tag_date;
	private String tag_link;
	private String tag_company;
	private String tag_date_format;
		
	public webCrawlerObject (Document xml, String name, String searchWords[]){
				
		this.xml_item = xml.getElementById(name);
		this.jobPosts = new ArrayList<jobPost>();
		this.searchWords = searchWords;
	}
	
	@Override
	public ArrayList<jobPost> call() throws Exception {
		
		getwebCrawlerDataRequest();
		return setwebCrawlerRequest();
	}

	public void getwebCrawlerDataRequest() {
		   
		//Element item = xml_item.getElementById("BerlinStartupJobs");
		   
		nameWebCrawler = xml_item.select("name").first().text();
		thumbnail = xml_item.select("thumbnail").first().text();
		
		    
		Element main_web_url = xml_item.getElementsByTag("url_main").first();
		web_urls = xml_item.getElementsByTag("url");
		   
		web_urls.add(main_web_url);
		   
		Elements tags = xml_item.getElementsByTag("post-tags");
		   
		this.tag_container = tags.select("post-container").first().text();
		this.tag_title = tags.select("post-title").first().text();
		this.tag_description = tags.select("post-description").first().text();
		this.tag_date = tags.select("post-date").first().text();
		this.tag_link = tags.select("post-link").first().text();
		this.tag_company = tags.select("post-company").first().text();
		this.tag_date_format= tags.select("post-date-format").first().text();
	}

	public Document getwebCrawlerResponse(Element web_url) {
		
		Document doc = null;
		try{
		   //GET RESPONSE
			System.out.println("Going to crawl next URL: " + web_url.text());
			
			doc = Jsoup.connect(web_url.text()).userAgent("Mozilla").get();

		} catch (Exception e) {
			   System.out.println("ERROR: A problem occur trying to crawl the web " + web_url.text() );
			   System.out.println(e);	
		}
		return doc;
	}

	public ArrayList<jobPost> setwebCrawlerRequest() {
		
		   try {
			   
			   for (Element web_url : web_urls ) {	   
				   
				   //GET RESPONSE 
			       Document doc = getwebCrawlerResponse(web_url);
			       
			       if( doc != null){
				       			       
				       if (nameWebCrawler.equalsIgnoreCase("Berlin Job")){
				    	   	jobPosts.addAll(getwebCrawlerDataResponseBerlinJob(doc));
				    
				       } else if ( nameWebCrawler.equalsIgnoreCase("Berlin Startup Jobs")){ 
				    	   jobPosts.addAll(getwebCrawlerDataResponseBerlinStartUpJob(doc));
				       }
			       }
			   }
		   } catch (Exception e) {
			   System.out.println("ERROR: A problem occur trying to get the data from the web");
			   System.out.println(e);			   
		   }
		   

	    System.out.println("Job posts found in " 
		   + this.nameWebCrawler 
		   + ": "
		   + jobPosts.size() );
	    
		return jobPosts;
	}
		
	public ArrayList<jobPost> getwebCrawlerDataResponseBerlinStartUpJob(Document doc) {
		
		// MANIPULATE RESPONE -- Parse the document	
		ArrayList<jobPost> jobsInWebPage = new ArrayList<jobPost>();
			
        // Get the main element for job-posts
        Element loop = doc.getElementById(tag_container);
               
        // Get all posts
        Elements posts = loop.children();

        try{ 
        	//For each post
	        for (Element post : posts) {
		        
		        // Get title AND Company (WARNING: it's an array)
		        Elements titlesCompanies = post.getElementsByTag(tag_title);
		        
		        if (titlesCompanies.size() != 1){
		        	//WARNING: There is more than one title
		        	// So far, just skip it
		        	continue;
		        }  
		        
		        Element titleCompanyElement = titlesCompanies.first();
		        String titleCompany[] = titleCompanyElement.text().split(" // ");
		        String title = titleCompany[0];
		        String company = titleCompany[1];
		        	             
		        // Get URl from the title
		        String linkTitle = titleCompanyElement.getElementsByTag(tag_link).first().attr("href");
		                	
		        // Get description
		        String description = post.getElementsByClass(tag_description).text();
		        
		        // Get date
		        String datePost = post.getElementsByClass(tag_date).text();
		        Date date = new SimpleDateFormat(tag_date_format, Locale.ENGLISH).parse(datePost);
		        
		        // Check if the post contains all search words in both 
		        // description and title 
		        //String[] texts = {description, title};
		        String[] texts = {title};
		        
		        if (checkMatch(texts, searchWords) && identifyLanguage(description)) {
			        
		        	// ADD RESULTS	        	
		        	
			        jobPost job = new jobPost();
			        job.setTitle(title);
			        job.setCompany(company);
			        job.setDate(date);
			        job.setDescription(description);
			        job.setLink(linkTitle);
			        job.setSource(this.nameWebCrawler);
			        job.setThumbnail(this.thumbnail);
			        				        
			        jobsInWebPage.add(job);
		        	
		        }          
		   }
		                
	    } catch (Exception e) {
	    	System.out.println("ERROR: Some unexpected occur trying to collecting the data.");
	    	System.out.println(e);
	    }
        
	   	return jobsInWebPage;
	}

		
	public ArrayList<jobPost> getwebCrawlerDataResponseBerlinJob(Document doc) {
		
		ArrayList<jobPost> jobsInWebPage = new ArrayList<jobPost>();
		
        try {
 		   
		        Elements loop = doc.select(tag_container);
	        
		        //For each group of jobs (sorted normally by company)
		        for (Element group_jobs : loop) {
		        		        	
			        String company = group_jobs.getElementsByTag(tag_company).first().text().trim();

			        // Empty company means no jobs there
			        if ( company.length() < 1){
			        	continue;
			        }
			        
			        Elements list_jobs = group_jobs.getElementsByTag(tag_title);		        
			        
			        for (Element link_job : list_jobs){
			        	
			        	String linkTitle = link_job.getElementsByTag(tag_link).first().attr("href").trim();
			        	String title = link_job.text();
			        	
				        String[] texts = {title};
				        if (checkMatch(texts, searchWords)) {
					        
					        // There is neither date nor description for these jobs posts
				        	// ADD RESULTS
					        jobPost job = new jobPost();
					        job.setTitle(title);
					        job.setCompany(company);
					        job.setDate(null);
					        job.setDescription(null);
					        job.setLink(linkTitle);
					        job.setSource(nameWebCrawler);
					        job.setThumbnail(this.thumbnail);
					        
					        jobsInWebPage.add(job);
				        	
				        }
			        	
			        }
			        		        
		        }
		                
	    } catch (Exception e) {
	    	System.out.println("ERROR: Some unexpected occur trying to collecting the data.");
	    	System.out.println(e);
	    }
	   
	   return jobsInWebPage;
	}
	



}


