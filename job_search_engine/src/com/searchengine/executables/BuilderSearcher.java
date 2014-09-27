package com.searchengine.executables;


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
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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