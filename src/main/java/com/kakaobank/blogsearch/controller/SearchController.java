package com.kakaobank.blogsearch.controller;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;
import com.kakaobank.blogsearch.controller.validation.SearchBlogRequestValidator;
import com.kakaobank.blogsearch.exceptions.InvalidRequestException;
import com.kakaobank.blogsearch.exceptions.SearchBlogException;
import com.kakaobank.blogsearch.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    private final SearchService searchService;
    private final SearchBlogRequestValidator searchBlogRequestValidator;

    public SearchController(SearchService searchService, SearchBlogRequestValidator searchBlogRequestValidator) {
        this.searchService = searchService;
        this.searchBlogRequestValidator = searchBlogRequestValidator;
    }

    @PostMapping("/searchBlog")
    public ResponseEntity<SearchBlogResponse> searchBlog(@RequestBody SearchBlogRequest searchBlogRequest) {
        try {
            searchBlogRequestValidator.validate(searchBlogRequest);
            SearchBlogResponse searchBlogResponse = searchService.searchBlog(searchBlogRequest);
            searchBlogResponse.setPopularKeywords(searchService.getPopularKeywordsAndAddCount(searchBlogRequest.getQuery()).getContent());
            return new ResponseEntity<>(searchBlogResponse, HttpStatus.OK);
        } catch (SearchBlogException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
