package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;

public interface SearchService {
    SearchBlogResponse searchBlog(SearchBlogRequest searchBlogRequest);
}
