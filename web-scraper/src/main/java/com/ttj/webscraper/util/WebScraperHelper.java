package com.ttj.webscraper.util;

/**
 * @author ashok
 */
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebScraperHelper {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private String pageUrl;
	private Integer pageParseTimeoutMillis;
	private List<String> detailsSearchTag;
	private List<String> linksSearchTags;
	
	public WebScraperHelper(String pageUrl, Integer pageParseTimeoutMillis, List<String> detailsSearchTag,
			List<String> linksSearchTags) {
		super();
		this.pageUrl = pageUrl;
		this.pageParseTimeoutMillis = pageParseTimeoutMillis;
		this.detailsSearchTag = detailsSearchTag;
		this.linksSearchTags = linksSearchTags;
	}
	/**
	 * This method uses main page url supplied in constructor and retrieves all the links from that page
	 * which are coming under the tags expression supplied as links search tags and then fetches all the meta details for those pages
	 * @return : returns a list of all articles with the details fetched using the links search tag supplied in constructor.
	 */
	public CompletableFuture<List<Map<String, String>>> fetchAllLinkMetaDetailsFromPage(){
		List<Map<String, String>> metaDetailsList = new ArrayList<>();
		return CompletableFuture.supplyAsync(()->{
			try {
				Set<String> links = getAllLinksFromPage();
				return links;
			} catch (IOException e) {
				LOGGER.error("Error in getting links.", e);
			}
			return null;
		}).thenApplyAsync(links->{
			links.forEach(lnk->{
				CompletableFuture<Map<String, String>> detailsFuture = CompletableFuture.supplyAsync(()->{
					try {
						return getLinkDetails(lnk);
					} catch (IOException e) {
						LOGGER.error("Error in getting link details.", e);
					}
					return null;
				});
				try {
					metaDetailsList.add(detailsFuture.get());
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error("Error in extracting results after task completion.", e);
				}
			});
			return metaDetailsList;
		}).toCompletableFuture();
	}
	/**
	 * Extracts article details from meta tag using the detailsSearchTag supplied in constructor.
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Map<String, String> getLinkDetails(String url) throws IOException{
		Document doc = Jsoup.parse(new URL(url), pageParseTimeoutMillis);
		Map<String, String> tagDetails = new HashMap<>();
		detailsSearchTag.forEach(tag->{
			tagDetails.put(tag, doc.select(tag).get(0).attr("content"));
		});
		return tagDetails;
	}
	/**
	 * Fetches all the links from the page which matches the criteria for linksSearchTags supplied in constructor
	 * @return
	 * @throws IOException
	 */
	private Set<String> getAllLinksFromPage() throws IOException {
		Document doc = Jsoup.parse(new URL(pageUrl), pageParseTimeoutMillis);
		return searchLinkTags(doc, linksSearchTags);
	}
	
	/**
	 * Extracts the actual link from a tag
	 * @param doc
	 * @param searchTags
	 * @return
	 */
	private Set<String> searchLinkTags(Document doc, List<String> searchTags){
		Set<String> links = new HashSet<>();
		searchTags.forEach(tag->{
			Elements elems = doc.select(tag);
			elems.forEach(e->{
				links.add(e.select("a[href]").attr("href"));
			});
		});
		return links;
	}
}
