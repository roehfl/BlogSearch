package com.kakaobank.blogsearch.controller.dto.response;

import lombok.Setter;

import java.time.LocalDateTime;

@Setter
public class SearchBlogResponseDocumentsDto {
    private String title;
    private String contents;
    private String url;
    private String blogName;
    private String thumbnail;
    private LocalDateTime datetime;
}
