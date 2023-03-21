package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.config.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchServiceImplTest {
    @Autowired
    Properties properties;


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
    void searchBlog() {
    }
}