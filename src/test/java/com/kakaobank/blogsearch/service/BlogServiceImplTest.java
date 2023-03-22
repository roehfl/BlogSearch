package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.config.Properties;
import com.kakaobank.blogsearch.controller.dto.response.GetPopularKeywordsResponse;
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
        given.put("url", "https://dapi.kakao.com/v2/search/blog");
        given.put("Authorization", "KakaoAK f4f86203522b4f1aa7764ac1ec110397");
        //when
        Map when = properties.getKakao();
        //that
        assertThat(when).isEqualTo(given);
    }

    @Test
    void popularKeywordsTest() {

        //given
        for (int i = 0; i < 20; i++) {
            blogService.insertOrUpdatePopularKeywords("테스트 키워드_" + i);
        }

        for (int i = 0; i < 5; i++) {
            blogService.insertOrUpdatePopularKeywords("테스트 키워드_1");
        }

        for (int i = 0; i < 3; i++) {
            blogService.insertOrUpdatePopularKeywords("테스트 키워드_2");
        }

        //when
        GetPopularKeywordsResponse getPopularKeywordsResponse = blogService.getPopularKeywords();

        //then
        assertThat(getPopularKeywordsResponse.getPopularKeywords()).hasSize(10);
        assertThat(getPopularKeywordsResponse.getPopularKeywords().get(0).getKeyword()).isEqualTo("테스트 키워드_1");
        assertThat(getPopularKeywordsResponse.getPopularKeywords().get(1).getKeyword()).isEqualTo("테스트 키워드_2");
    }
}