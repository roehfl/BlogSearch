package com.kakaobank.blogsearch.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBlogRequest {
    private String query;
    private String sort;
    private Integer page;
    private Integer size;
}
