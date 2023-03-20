package com.kakaobank.blogsearch.controller.validation;

import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SearchBlogRequestValidatorTest {
    @Autowired
    SearchBlogRequestValidator searchBlogRequestValidator;

    @Test
    void invalidRequestTestForRequiredValue() {
        //given
        SearchBlogRequest searchBlogRequest = new SearchBlogRequest();
        //when
        searchBlogRequest.setQuery(null);
        //then
        assertThatThrownBy(() -> searchBlogRequestValidator.validate(searchBlogRequest)).isInstanceOf(InvalidRequestException.class)
                .hasMessage("Query is required.");
    }

    @Test
    void invalidRequestTestForSortValue() {
        //given
        String sort = "test";
        String query = "not null";
        SearchBlogRequest searchBlogRequest = new SearchBlogRequest();

        //when
        searchBlogRequest.setQuery(query);
        searchBlogRequest.setSort(sort);

        //then
        assertThatThrownBy(() -> searchBlogRequestValidator.validate(searchBlogRequest)).isInstanceOf(InvalidRequestException.class)
                .hasMessage("Sort must be \"accuracy\" or \"recency\".");
    }

    @Test
    void invalidRequestTestForPageValue() {
        //given
        int page = 0;
        String query = "not null";
        SearchBlogRequest searchBlogRequest = new SearchBlogRequest();

        //when
        searchBlogRequest.setQuery(query);
        searchBlogRequest.setPage(page);

        //then
        assertThatThrownBy(() -> searchBlogRequestValidator.validate(searchBlogRequest)).isInstanceOf(InvalidRequestException.class)
                .hasMessage("Page value must be between 1 and 50.");
    }

    @Test
    void invalidRequestTestForSizeValue() {
        //given
        int size = 0;
        String query = "not null";
        SearchBlogRequest searchBlogRequest = new SearchBlogRequest();

        //when
        searchBlogRequest.setQuery(query);
        searchBlogRequest.setSize(size);

        //then
        assertThatThrownBy(() -> searchBlogRequestValidator.validate(searchBlogRequest)).isInstanceOf(InvalidRequestException.class)
                .hasMessage("Size value must be between 1 and 50.");
    }
}