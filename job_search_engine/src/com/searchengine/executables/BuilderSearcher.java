package com.searchengine.executables;


import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class BuilderSearcher {
	
	private StringSearch search;
	private Document xml_config;
	private String config_url = "com/searchengine/resources/webpages_config.xml";
	
	private ArrayList<jobPost> jobPosts;
	
	private String searchNames[] = 
		{"BerlinStartupJobs",
			 "BerlinJob",
			 "Indeed",
			 "CareerBuilder"};
	
	public BuilderSearcher(StringSearch search){
		
		try {
			
			URL url = JobSource.class.getClassLoader().getResource("com/searchengine/languageprofiles/");			
			DetectorFactory.loadProfile(url.getPath());
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception loading profiles for Language detection library");
			e.printStackTrace();
		}
		
		this.search = search;
		this.jobPosts = new ArrayList<jobPost>();
		
	}
	
	public ArrayList<jobPost> setSearches() throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		
		this.xml_config = getXMLDocumentFromInternalFile(config_url);
		
				
		ExecutorService executorService = Executors.newFixedThreadPool(searchNames.length);
		
		List<Callable<ArrayList<jobPost>>> lst = new ArrayList<Callable<ArrayList<jobPost>>>();
		
		for (int i=0 ; i < searchNames.length ; i++ ){
			
			String xml_item = xml_config.getElementById(searchNames[i]).attr("type");
			
			if (xml_item.equalsIgnoreCase("api")) {
				lst.add( new APIObject(xml_config, searchNames[i], search.getCompleteSearch()));
			} else if (xml_item.equalsIgnoreCase("web_page")){
				lst.add( new webCrawlerObject(xml_config, searchNames[i], search.getArrayWords() ));
			}

		}
		
		List<Future<ArrayList<jobPost>>> futureList = executorService.invokeAll(lst);
		
		 
        for(Future<ArrayList<jobPost>> future : futureList)
        {
        	for(jobPost final_job : future.get()){
        		jobPosts.add(final_job);
        	}
        }
        
        System.out.println("TOTAL Number of results: " + jobPosts.size());
        
        return jobPosts;
		
	}

	public static Document getXMLDocumentFromInternalFile (String internalUrl) throws IOException, URISyntaxException{
		
		InputStream webpage_config = BuilderSearcher.class
			           .getClassLoader()
			           .getResourceAsStream(internalUrl);
	          
	     URL url_web_page = BuilderSearcher.class.
	     		getClassLoader().
	     		getResource(internalUrl);
	     
	     String urlString = url_web_page.toURI().toString();
	     
	     Document xml_doc= Jsoup.parse(webpage_config,
	             "UTF-8",
	             urlString,
	             Parser.xmlParser());
	     	     
	     return xml_doc;
		
	}


	public ArrayList<jobPost> LinkedinJobs() throws Exception{

		ArrayList<jobPost> jobPosts = new ArrayList<>();
		
		return jobPosts;
	}
}