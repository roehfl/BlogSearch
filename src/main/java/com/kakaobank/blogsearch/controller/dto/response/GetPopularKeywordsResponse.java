package com.kakaobank.blogsearch.controller.dto.response;

import com.kakaobank.blogsearch.domain.model.PopularKeywords;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPopularKeywordsResponse {
    private List<PopularKeywords> popularKeywords;
}
