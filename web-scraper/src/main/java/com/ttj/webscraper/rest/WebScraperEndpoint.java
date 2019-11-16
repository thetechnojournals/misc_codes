package com.ttj.webscraper.rest;

/**
 * @author ashok
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ttj.webscraper.domain.Article;
import com.ttj.webscraper.service.WebScraperService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/articles")
public class WebScraperEndpoint {
	
	@Autowired
	WebScraperService scraperService;
	
	@ApiOperation(value = "Search articles by author name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response"),
            @ApiResponse(code = 401, message = "Resource not authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Resource not found")
    }
    )
	@RequestMapping(value="/by-author/{authorName}", method = RequestMethod.GET, produces = "application/json")
	public List<Article> searchArticlesByAuthor(@PathVariable("authorName") String authorName) {
		return scraperService.searchArticlesByAuthor(authorName);
	}

	@RequestMapping(value="/authors", method = RequestMethod.GET, produces = "application/json")
	public List<String> listAuthors() {
		return scraperService.listAuthors();
	}

	@RequestMapping(value="/by-title/{title}", method = RequestMethod.GET, produces = "application/json")
	public List<Article> searchArticleByTitle(@PathVariable("title") String title) {
		return scraperService.searchArticleByTitle(title);
	}

	@RequestMapping(value="/by-desc/{desc}", method = RequestMethod.GET, produces = "application/json")
	public List<Article> searchArticleByDescription(@PathVariable("desc") String desc) {
		return scraperService.searchArticleByDescription(desc);
	}
}
