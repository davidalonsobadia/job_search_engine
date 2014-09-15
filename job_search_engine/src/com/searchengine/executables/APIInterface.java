package com.searchengine.executables;

import java.util.ArrayList;

public interface APIInterface {

	void getAPIDataRequest();
	
	void setAPIRequest(String search);
	
	void getAPIResponse();
	
	void getAPIDataResponse();
	
	ArrayList<jobPost> setAPIResponse();
	
}
