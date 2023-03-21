package com.kakaobank.blogsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "properties.openapi")
@Getter
@Setter
public class Properties {
    private Map<String, String> kakao;
    private Map<String, String> naver;
}
