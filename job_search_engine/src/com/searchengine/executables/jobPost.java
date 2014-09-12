// Object Bean

package com.searchengine.executables;
import java.util.Date;


public class jobPost {

	private String title;
	private Date date;
	private String link;
	private String company;
	private String description;
	private String source;
	
	public jobPost(){
	}
	
	// Set Methods
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setCompany(String company){
		this.company = company;
	}
	
	public void setLink(String link){
		this.link = link;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	public void setDate(Date date){
		this.date = date;
	}
	
	public void setSource(String Source){
		this.source = source;
	}
	
	// Get Methods
	public String getTitle(){
		return this.title;
	}
	
	public String getCompany(){
		return this.company;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public String getSource(){
		return this.source;
	}
}
