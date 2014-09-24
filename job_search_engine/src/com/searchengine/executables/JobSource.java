package com.searchengine.executables;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.searchengine.executables.StringSearch;


public abstract class JobSource {
	
	public boolean checkMatch(String[] texts, String[] searchWords){
		
		String pattern_search = "^";
		String all_texts = "";

		for ( String single_search : searchWords)	
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
	
	public static boolean detectLanguage(String text){
		
		return false;
	}
}

