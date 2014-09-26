package com.searchengine.executables;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class InitSearch
 * 
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

		System.out.println("\n========================================================================");
		System.out.println("\t\tNEW SEARCH DONE IN OUR WEB PAGE");
		System.out.println("========================================================================\n");
		
		String search = request.getParameter("Search").trim();
		System.out.println("Servlet-Post Method");

		
		StringSearch sSearch = new StringSearch();
		sSearch.setCompleteSearch(search);
		
		BuilderSearcher builderSearch = new BuilderSearcher(sSearch);
		
		ArrayList<jobPost> jobs = new ArrayList<>();
		
		
		
		try {
			
			jobs.addAll(builderSearch.setSearches());
			//jobs.addAll(web_pages.BerlinStartupJobs());
			//jobs.addAll(web_pages.IndeedJobs());
			//jobs.addAll(web_pages.BerlinJob());
			//jobs.addAll(web_pages.CareerBuilderJobs());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		request.setAttribute("JobsList", jobs);
		request.setAttribute("search", search);
		request.getRequestDispatcher("searchSuccess.jsp").forward(request, response);
			
		
		
	}

}
