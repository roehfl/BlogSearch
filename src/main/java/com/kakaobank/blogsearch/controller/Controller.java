package com.kakaobank.blogsearch.controller;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.GetPopularKeywordsResponse;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;
import com.kakaobank.blogsearch.controller.validation.SearchBlogRequestValidator;
import com.kakaobank.blogsearch.exceptions.GetPopularKeywordsException;
import com.kakaobank.blogsearch.exceptions.InvalidRequestException;
import com.kakaobank.blogsearch.exceptions.SearchBlogException;
import com.kakaobank.blogsearch.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final BlogService blogService;
    private final SearchBlogRequestValidator searchBlogRequestValidator;

    public Controller(BlogService blogService, SearchBlogRequestValidator searchBlogRequestValidator) {
        this.blogService = blogService;
        this.searchBlogRequestValidator = searchBlogRequestValidator;
    }

    @PostMapping("/searchBlog")
    public ResponseEntity<SearchBlogResponse> searchBlog(@RequestBody SearchBlogRequest searchBlogRequest) {
        try {
            searchBlogRequestValidator.validate(searchBlogRequest);
            SearchBlogResponse searchBlogResponse = blogService.searchBlog(searchBlogRequest);
            blogService.insertOrUpdatePopularKeywords(searchBlogRequest.getQuery());
            return new ResponseEntity<>(searchBlogResponse, HttpStatus.OK);
        } catch (SearchBlogException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPopularKeywords")
    public ResponseEntity<GetPopularKeywordsResponse> getPopularKeywords() {
        try {
            GetPopularKeywordsResponse getPopularKeywordsResponse = blogService.getPopularKeywords();
            return new ResponseEntity<>(getPopularKeywordsResponse, HttpStatus.OK);
        } catch (GetPopularKeywordsException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
