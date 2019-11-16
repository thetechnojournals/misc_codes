package com.ttj.webscraper.domain;

/**
 * @author ashok
 */
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Article", description="News article")
public class Article implements Serializable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("Title for article")
	private String title;
	@ApiModelProperty("Article description")
	private String description;
	@ApiModelProperty("Author name")
	private String authorName;
	
	public Article() {
		
	}
	public Article(String title, String description, String authorName) {
		super();
		this.title = title;
		this.description = description;
		this.authorName = authorName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
}
