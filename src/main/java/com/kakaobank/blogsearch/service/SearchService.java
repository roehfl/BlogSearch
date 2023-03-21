package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;
import com.kakaobank.blogsearch.domain.model.PopularKeywords;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {
    SearchBlogResponse searchBlog(SearchBlogRequest searchBlogRequest);
    SearchBlogResponse searchBlogFallBack(SearchBlogRequest searchBlogRequest, Throwable t);

    Page<PopularKeywords> getPopularKeywordsAndAddCount(String keyword);
}
