package com.kakaobank.blogsearch.controller.dto.response;

import lombok.Setter;

@Setter
public class SearchBlogResponseMetaDto {
    private int total_count;
    private int pageable_count;
    private boolean is_end;
}
