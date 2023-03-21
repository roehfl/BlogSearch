package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.GetPopularKeywordsResponse;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;

public interface BlogService {
    SearchBlogResponse searchBlog(SearchBlogRequest searchBlogRequest);
    void insertOrUpdatePopularKeywords(String keyword);
    GetPopularKeywordsResponse getPopularKeywords();
}
