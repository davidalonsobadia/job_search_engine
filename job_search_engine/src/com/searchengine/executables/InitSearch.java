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
	
	private int pageSize = 4;
	private int currentPage = 1;
	private int pageWindow = 2;
	
	private ArrayList<jobPost> jobs;
	
	
	
    public InitSearch() {
        super();
        // TODO Auto-generated constructor stub
    }


    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("\n========================================================================");
		System.out.println("\t\tNEW SEARCH DONE IN OUR WEB PAGE");
		System.out.println("========================================================================\n");
		
		this.currentPage = 1;
		
		String search = request.getParameter("Search").trim();
		System.out.println("Servlet-Post Method");

		jobs = new ArrayList<>();
		
		StringSearch sSearch = new StringSearch();
		sSearch.setCompleteSearch(search);
		
		BuilderSearcher builderSearch = new BuilderSearcher(sSearch);

		
		try {
			jobs.addAll(builderSearch.setSearches());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Unexpected exti from Servlet ( Controller)");
			System.out.println(e);
		}
		
		Pageable pageable = new Pageable(jobs);
		
		pageable.setPageSize(pageSize);
		pageable.setPage(currentPage);
		pageable.setPageWindow(pageWindow);
		
		ArrayList<jobPost> chunkJobs = new ArrayList<jobPost> (pageable.getListForPage());
				
		request.setAttribute("JobsList", chunkJobs);
		request.setAttribute("search", search);
		
		request.setAttribute("maxPageRange", pageable.getMaxPageRange());
		request.setAttribute("minPageRange", pageable.getMinPageRange());
        request.setAttribute("currentPage", pageable.getPage());
        
		
		request.getRequestDispatcher("searchSuccess.jsp").forward(request, response);
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
		if(request.getParameter("page") != null)
            currentPage = Integer.parseInt(request.getParameter("page"));
		
		Pageable pageable = new Pageable(jobs);
		
		pageable.setPageSize(pageSize);
		pageable.setPage(currentPage);
		pageable.setPageWindow(pageWindow);
				
		request.setAttribute("maxPageRange", pageable.getMaxPageRange());
		request.setAttribute("minPageRange", pageable.getMinPageRange());
		request.setAttribute("currentPage", pageable.getPage());
        
        ArrayList<jobPost> chunkJobs = new ArrayList<jobPost> (pageable.getListForPage());
		
		request.setAttribute("JobsList", chunkJobs);
		
		request.getRequestDispatcher("searchSuccess.jsp").forward(request, response);
		
	}
	
}
