package com.kakaobank.blogsearch.controller.validation;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.exceptions.InvalidRequestException;
import org.springframework.stereotype.Component;

@Component
public class SearchBlogRequestValidator implements RequestValidator<SearchBlogRequest>{
    @Override
    public void validate(SearchBlogRequest request) {
        if (request.getQuery() == null || request.getQuery().isEmpty()) {
            throw new InvalidRequestException("Query is required.");
        }
        if (!(request.getSort() == null || request.getSort().isEmpty()) && !(request.getSort().equals("accuracy") || request.getSort().equals("recency"))) {
            throw new InvalidRequestException("Sort must be \"accuracy\" or \"recency\".");
        }
        if (request.getPage() != null && (request.getPage() < 1 || request.getPage() > 50)) {
            throw new InvalidRequestException("Page value must be between 1 and 50.");
        }
        if (request.getSize() != null && (request.getSize() < 1 || request.getSize() > 50)) {
            throw new InvalidRequestException("Size value must be between 1 and 50.");
        }
    }
}
