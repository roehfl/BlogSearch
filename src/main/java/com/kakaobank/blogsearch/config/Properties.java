package com.kakaobank.blogsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "properties")
@Getter
@Setter
public class Properties {
    private List<Map<String, Object>> open_api;
}
