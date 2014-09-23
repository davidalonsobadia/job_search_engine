package com.searchengine.executables;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;


/**
 * Servlet implementation class InitSearch
 */

@WebServlet("/InitSearch")
public class InitSearch extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       

    public InitSearch() {
        super();
        // TODO Auto-generated constructor stub
    }


    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String search = request.getParameter("Search").trim();
		System.out.println("Servlet-Post Method");

		
		StringSearch sSearch = new StringSearch();
		sSearch.setCompleteSearch(search);
		
		getWebPage web_pages = new getWebPage(sSearch);
		
		ArrayList<jobPost> jobs = null;
		
		try {
			//jobs = web_pages.BerlinStartupJobs();
			//jobs = web_pages.IndeedJobs();
			jobs = web_pages.BerlinJob();
			//jobs = web_pages.CareerBuilderJobs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		request.setAttribute("JobsList", jobs);
		request.setAttribute("search", search);
		request.getRequestDispatcher("searchSuccess.jsp").forward(request, response);
			
		
		
	}

}
