package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.config.Properties;
import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;
import com.kakaobank.blogsearch.domain.repository.PopularKeywordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BlogServiceImplTest {
    @Autowired
    Properties properties;
    @Autowired
    PopularKeywordsRepository popularKeywordsRepository;

    private BlogService blogService;

    @Mock
    private BlogServiceImpl blogServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        blogService = new BlogServiceImpl(properties, popularKeywordsRepository);
    }

    @Test
    void getPropertiesTest(){
        //given
        Map<String, String> given = new HashMap<>();
        //when
        given.put("name", "kakao");
        given.put("url", "https://dapi.kakao.com/v2/search/blog");
        given.put("apiKey", "f4f86203522b4f1aa7764ac1ec110397");
        //that
        assertThat(properties.getKakao()).isEqualTo(given);
    }

    @Test
    void searchBlogFallBackTest() {
        SearchBlogRequest searchBlogRequest = new SearchBlogRequest();
        searchBlogRequest.setQuery("fallback");

        SearchBlogResponse actualResponse = blogService.searchBlog(searchBlogRequest);

        SearchBlogResponse expectedResponse = new SearchBlogResponse();

        when(blogServiceImpl.searchBlogFallBack(any(), any())).thenReturn(expectedResponse);



        assertThat(expectedResponse).isNotEqualTo(actualResponse);
    }
}