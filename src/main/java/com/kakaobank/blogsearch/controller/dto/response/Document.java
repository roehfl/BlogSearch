package com.kakaobank.blogsearch.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Document {
    private String title;
    @JsonAlias("description")
    private String contents;
    @JsonAlias("link")
    private String url;
    @JsonAlias("bloggername")
    private String blogname;
    private String thumbnail;
    @JsonAlias("postdate")
    private String datetime;
}
