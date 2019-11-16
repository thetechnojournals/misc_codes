package com.ttj.webscraper.service;

/**
 * @author ashok
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ttj.webscraper.domain.Article;
import com.ttj.webscraper.util.WebScraperHelper;

@Service
public class WebScraperServiceImpl implements WebScraperService{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private List<Article> articles = new ArrayList<>();
	
	@Value("${newspaper.thehindu.url}")
	private String newspaperUrl;
	@Value("${newspaper.thehindu.parse.timeout.ms}")
	Integer parseTimeoutMillis;
	@Value("${newspaper.thehindu.article.authortag}")
	String authorTagName;
	@Value("${newspaper.thehindu.article.titletag}")
	String titleTagName;
	@Value("${newspaper.thehindu.article.desctag}")
	String descTagName;

	@Value("#{'${newspaper.thehindu.article.searchtags}'.split(',')}")
	List<String> articleLinksSearchTags;
	
	public WebScraperServiceImpl() {
	}
	
	@PostConstruct
	@Override
	public void loadContents() throws IOException {
		LOGGER.info("loadContents()...start");
		articles.clear();
		List<String> articleDetailsSearchTags = Arrays.asList(authorTagName, titleTagName, descTagName);
		WebScraperHelper scraperHelper = new WebScraperHelper(newspaperUrl, parseTimeoutMillis, articleDetailsSearchTags, articleLinksSearchTags);

		LOGGER.info("Extracting article details...start");
						
		scraperHelper.fetchAllLinkMetaDetailsFromPage()
		.thenAccept(list->{
			list.stream().filter(map->map.get(authorTagName)!=null && map.get(authorTagName).length()>0)
			.forEach(map->{
				articles.add(new Article(map.get(titleTagName), map.get(descTagName), map.get(authorTagName)));
			});
		});
		
		LOGGER.info("loadContents()...completed");
	}
	
	@Override
	public List<String> listAuthors() {
		return articles.stream().map(a->a.getAuthorName())
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	public List<Article> searchArticlesByAuthor(String authorName) {
		return articles.stream().filter(a->a.getAuthorName().equalsIgnoreCase(authorName))
				.collect(Collectors.toList());
	}

	@Override
	public List<Article> searchArticleByTitle(String title) {
		return articles.stream().filter(a->a.getTitle().startsWith(title))
				.collect(Collectors.toList());
	}

	@Override
	public List<Article> searchArticleByDescription(String desc) {
		return articles.stream().filter(a->a.getDescription().startsWith(desc))
				.collect(Collectors.toList());
	}
}
