package com.kakaobank.blogsearch.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SearchBlogResponse {
    private Meta meta;
    @JsonAlias("items")
    private List<Document> documents;

    public void setTotal(Integer total) {
        if (meta == null) {
            meta = new Meta();
        }
        meta.setTotal_count(total);
    }

    public void setDisplay(Integer display) {
        if (meta == null) {
            meta = new Meta();
        }
        meta.setPageable_count(display);
    }

    public void setStart(Integer start) {
        if (meta == null) {
            meta = new Meta();
        }
        meta.setIs_end(false);
        if (meta.getTotal_count() != null && meta.getTotal_count().equals(start)) {
            meta.setIs_end(true);
        }
    }
}


