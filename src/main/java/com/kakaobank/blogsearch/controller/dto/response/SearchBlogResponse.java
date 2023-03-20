package com.kakaobank.blogsearch.controller.dto.response;

import lombok.Setter;

@Setter
public class SearchBlogResponse {
    private SearchBlogResponseDocumentsDto[] documents;
    private SearchBlogResponseMetaDto meta;
}
