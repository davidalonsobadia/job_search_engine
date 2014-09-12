package com.searchengine.executables;

public class StringSearch {
	
	private String CompleteSearch;
	private String[] ArrayWords;
	
	public StringSearch() {	
	}
	
	public void setCompleteSearch(String CompleteSearch){
		this.CompleteSearch = CompleteSearch;
		this.ArrayWords = CompleteSearch.split("[\\W]");
	}
	
	public void setArrayWords(String CompleteSearch) {
		this.ArrayWords = CompleteSearch.split("[\\W]");
	}
	
	public String[] getArrayWords(){
		return this.ArrayWords;
	}
	
	public String getCompleteSearch(){
		return this.CompleteSearch;
	}
	
}
