package com.kakaobank.blogsearch.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class SearchBlogResponse {
    private Integer total;
    private Integer display;
    private Boolean isEnd;
    private List<Document> documents;

    @JsonProperty("meta")
    private void unpackNestedAttribute(Map<String, Object> meta) {
        this.total = (Integer) meta.get("total_count");
        this.isEnd = (Boolean) meta.get("is_end");
    }

    @JsonProperty("items")
    private void setItems(List items) {
        this.documents = items;
    }

    @JsonProperty("start")
    private void setDisplayValue(Integer start) {
        this.display = start;
        this.isEnd = false;
    }

    @JsonProperty("display")
    private void setDisplayAndCurrentPageAndIsEnd(Integer display) {
        if (this.display + display >= this.total) {
            this.isEnd = true;
        }
        this.display = display;
    }
}


