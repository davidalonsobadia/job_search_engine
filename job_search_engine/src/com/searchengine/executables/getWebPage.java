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
	
	private static StringSearch search;
	private String config_url = "com/searchengine/resources/webpages_config.xml";
	
	public getWebPage(StringSearch search){
		this.search = search;
	}
	

	public static org.w3c.dom.Document CreateBuilderFactory() throws Exception{
		

		org.w3c.dom.Document XMLDocument = null;

		InputStream webpage_config = getWebPage.class
		           .getClassLoader()
		           .getResourceAsStream("com/searchengine/resources/webpages_config.xml");
		
		
		XMLDocument = Utils.newDocumentFromInputStream(webpage_config);
		
		//String s = Utils.getStringFromInputStream(webpage_config);
		//XMLDocument = Utils.getw3cDocumentfronString(s);
		
		//System.out.println(xmlDocument);

		return XMLDocument;
	}
	
	public static String getStringFromXMLConfig() throws Exception{
		

		//String XMLDocument = null;

		InputStream webpage_config = getWebPage.class
		           .getClassLoader()
		           .getResourceAsStream("com/searchengine/resources/webpages_config.xml");
		
		
		//XMLDocument = Utils.newDocumentFromInputStream(webpage_config);
		
		String s = Utils.getStringFromInputStream(webpage_config);
		//XMLDocument = Utils.getw3cDocumentfronString(s);
		
		//System.out.println(xmlDocument);

		return s;
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


	public static boolean checkMatch(String[] texts){
		
		String pattern_search = "^";
		String all_texts = "";

		//^(?=.*?(single_search))(?=.*?(single_search)).*$
		
		for ( String single_search : search.getArrayWords())	
			pattern_search = pattern_search 
				 + "(?=.*?(\\b" + single_search + "\\b))";
		
		pattern_search += ".*$";
			
		for ( String text : texts)
			all_texts = all_texts + " " + text;
		
        Pattern pattern = Pattern.compile(pattern_search, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(all_texts);
        
        //Test the search
        
        if (matcher.find()){
        	System.out.println("Match");
        	System.out.println(matcher.group(1));
        	System.out.println(matcher.group(0));
            return true;
        }
		return false;
	}
	
	public ArrayList<jobPost> BerlinStartupJobs() throws Exception{
		
		
        // CLass joBposts where we will store each post for the page
        ArrayList<jobPost> jobPosts = new ArrayList<>();
        
        /**
        org.w3c.dom.Document XML_config = CreateBuilderFactory();

        XPath xPath =  XPathFactory.newInstance().newXPath();
        
        String expr_getItem = "/web_pages/web_page[name='Berlin Startup Jobs']/post-tags";
        org.w3c.dom.Node node = (org.w3c.dom.Node) xPath.compile(expr_getItem).evaluate(XML_config, XPathConstants.NODE);
        **/
        
        /**
        String xml_config = getStringFromXMLConfig();
        Document xml_doc = Jsoup.parse(xml_config, "", Parser.xmlParser());
        **/
        
        // GET THE DATA FROM THE XML FILE
        
        //String internalUrl = "com/searchengine/resources/webpages_config.xml";
        
        Document xml_config = getXMLDocumentFromInternalFile(config_url);
       
        
		/**All this is for Berlin Start-up Job. 
		 * We should have to do a kind of list of webpages I want to visit,
		 * and also then a class-master class batracting all methods we can reuse.
		 */
    
	   Element item = xml_config.getElementById("BerlinStartupJobs");
	   
       String name = item.select("name").first().text();
	    
	   Element main_web_url = item.getElementsByTag("url_main").first();
	   Elements web_urls = item.getElementsByTag("url");
	   
	   web_urls.add(main_web_url);
	   
	   Elements tags = item.getElementsByTag("post-tags");
	   
	   String tag_container = tags.select("post-container").first().text();
	   String tag_title = tags.select("post-title").first().text();
	   String tag_description = tags.select("post-description").first().text();
	   String tag_date = tags.select("post-date").first().text();
	   String tag_link = tags.select("post-link").first().text();
        
	   try {
		   
		   
		   for (Element web_url : web_urls ) {
		   
			   
			   //GET RESPONSE 
			   
		       Document doc = Jsoup.connect(web_url.text()).userAgent("Mozilla").get();
		        
		       
		       // MANIPULATE RESPONE -- Parse the document		       

		        // Get the main element for job-posts
		        Element loop = doc.getElementById(tag_container);
		        
		        // Get all posts
		        Elements posts = loop.children();
		        
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
			        Date date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).parse(datePost);
			        
			        // Check if the post contains all search words in both 
			        // description and title 
			        //String[] texts = {description, title};
			        String[] texts = {title};
			        
			        if (checkMatch(texts)) {
				        
			        	// ADD RESULTS
			        	
				        jobPost job = new jobPost();
				        job.setTitle(title);
				        job.setCompany(company);
				        job.setDate(date);
				        job.setDescription(description);
				        job.setLink(linkTitle);
				        job.setSource(name);
				        
				        jobPosts.add(job);
			        	
			        }


		        }
		          
		        /**
		         *  For next page, look for <div class="pagination">
		         *  <a href="http://berlinstartupjobs.com/page/2/" class="nextpostslink">Older</a></div>
		         *  
		         */
		   }
		                
	    } catch (Exception e) {}
	
	   System.out.println("Job posts founded in " 
			   + item.getElementsByTag("name").first().text() 
			   + ": "
			   + jobPosts.size() );
	   
	   return jobPosts;
	}

	public ArrayList<jobPost> BerlinJob() throws Exception{
		
		
        // CLass joBposts where we will store each post for the page
        ArrayList<jobPost> jobPosts = new ArrayList<>();
        
        
        // GET THE DATA FROM THE XML FILE       
        Document xml_config = getXMLDocumentFromInternalFile(config_url);
       
           
        Element item = xml_config.getElementById("BerlinJob");
	   
        String name = item.select("name").first().text();
	    
        Element main_web_url = item.getElementsByTag("url_main").first();
        Elements web_urls = item.getElementsByTag("url");
	   
        web_urls.add(main_web_url);
	   
        Elements tags = item.getElementsByTag("post-tags");
	   
        String tag_container = tags.select("post-container").first().text();
        String tag_title = tags.select("post-title").first().text();
        String tag_description = tags.select("post-description").first().text();
        String tag_date = tags.select("post-date").first().text();
        String tag_link = tags.select("post-link").first().text();
        String tag_company = tags.select("post-company").first().text();
	   
        
        try {
		   
		   
		   //for (Element web_url : web_urls ) {
		   	for(int t = 0 ; t==0; t++){
			   
			   //GET RESPONSE 
			   
		       //Document doc = Jsoup.connect(web_url.text()).userAgent("Mozilla").get();
			   Document doc = Jsoup.connect("http://berlinjob.net/startup-jobs-berlin/index.php").userAgent("Mozilla").get();
			   
		       // MANIPULATE REPSONE 
		       
		        // Parse the document
		        // Get the main element for job-posts
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
				        if (checkMatch(texts)) {
					        
					        // There is neither date nor description for these jobs posts
				        	// ADD RESULTS
				        	
					        jobPost job = new jobPost();
					        job.setTitle(title);
					        job.setCompany(company);
					        job.setDate(null);
					        job.setDescription(null);
					        job.setLink(linkTitle);
					        job.setSource(name);
					        
					        jobPosts.add(job);
				        	
				        }
			        	
			        }
			        		        
		        }

		   }
		                
	    } catch (Exception e) {}
	
	   System.out.println("Job posts founded in " 
			   + item.getElementsByTag("name").first().text() 
			   + ": "
			   + jobPosts.size() );
	   
	   return jobPosts;
	}

	public ArrayList<jobPost> IndeedJobs() throws Exception{
		
		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		Document xml_config = getXMLDocumentFromInternalFile(config_url);
		
		String name = "Indeed";
		
		APIObject api = new APIObject(xml_config, name);
		
		
                         		
 		try {
 			
 			String search_adapted = URLEncoder.encode(search.getCompleteSearch().replace(" ", "+"), "UTF-8");
 			String user_agent_adapted = URLEncoder.encode(api.user_agent, "UTF-8");
 					
 			String query_api =  api.root_url + "&" + api.publisher_id + "&" + api.query 
 					+ search_adapted + "&" + api.location + "&" + api.country 
 					+ "&" + user_agent_adapted + "&" + api.api_version  + "&" + api.limit ;
 			
	        System.out.println(query_api);

 			
 			// MAKE THE QUERY
 			
 			Document doc = Jsoup.connect(query_api).userAgent("Mozilla").get();
 			
	        //System.out.println(doc);	

 			// GET RESPONSE AND MANIPULATE IT
 			
 			Elements tags = api.item.getElementsByTag("post-tags");
 			
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

	public ArrayList<jobPost> LinkedinJobs() throws Exception{

		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		return jobPosts;
	}
}