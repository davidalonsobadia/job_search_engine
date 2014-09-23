package com.searchengine.executables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*******************************************************************************
 * Copyright (C) 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

/**
 * Various XML utilities.
 * 
 * @author simonjsmith, ksim
 * @version 1.1 - ksim - March 6th, 2007 - Added functions regarding streaming
 * @version 1.2 - ksim - March 10th, 2007 - Added functions regarding DOM
 *          manipulation
 */
public class Utils {
	
  public static Document newDocumentFromInputStream(InputStream in) {

    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;
    Document ret = null;

    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }

    try {
      ret = builder.parse(new InputSource(in));
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ret;
  }

  public static String getStringFromInputStream(InputStream is) {
	  
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}


  public static Document getw3cDocumentfronString(String s) throws ParserConfigurationException, SAXException, IOException{
	  java.io.InputStream sbis = new java.io.StringBufferInputStream(s);
	   
	  javax.xml.parsers.DocumentBuilderFactory b = javax.xml.parsers.DocumentBuilderFactory.newInstance();
	  b.setNamespaceAware(false);
	  org.w3c.dom.Document doc = null;    
	  javax.xml.parsers.DocumentBuilder db = null;
	   
	  db = b.newDocumentBuilder();
	  doc = db.parse(sbis);
	  
	  return doc;
  }

	public static boolean checkMatch(String[] texts, StringSearch search){
		
		String pattern_search = "^";
		String all_texts = "";

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
}