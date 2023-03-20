package com.kakaobank.blogsearch.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import com.kakaobank.blogsearch.config.Properties;
import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service("searchService")
public class SearchServiceImpl implements SearchService{
    private final Properties properties;

    public SearchServiceImpl(Properties properties) {
        this.properties = properties;
    }

//    @CircuitBreaker(name = "searchBlog", fallbackMethod = "searchBlogFallBack")
    @Override
    public SearchBlogResponse searchBlog(SearchBlogRequest searchBlogRequest){
        String apiKey = Optional.ofNullable(properties.getOpen_api().get(0).get("apiKey")).orElse("").toString();
        String apiUrl = Optional.ofNullable(properties.getOpen_api().get(0).get("url")).orElse("").toString();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl).queryParam("query", searchBlogRequest.getQuery());

        Optional<String> sort = Optional.ofNullable(searchBlogRequest.getSort());
        Optional<Integer> page = Optional.ofNullable(searchBlogRequest.getPage());
        Optional<Integer> size = Optional.ofNullable(searchBlogRequest.getSize());

        sort.ifPresent(value -> builder.queryParam("sort", value));
        page.ifPresent(value -> builder.queryParam("page", value));
        size.ifPresent(value -> builder.queryParam("size", value));

        apiUrl = builder.toUriString();

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "KakaoAK " + apiKey)
                .build();

        SearchBlogResponse searchBlogResponse = webClient.get().retrieve().bodyToMono(SearchBlogResponse.class).block();

        return searchBlogResponse;
    }

    private SearchBlogResponse searchBlogFallBack(SearchBlogRequest searchBlogRequest) {
        String apiClientId = Optional.ofNullable(properties.getOpen_api().get(1).get("clientId")).orElse("").toString();
        String apiClientSecret = Optional.ofNullable(properties.getOpen_api().get(1).get("clientSecret")).orElse("").toString();
        String apiUrl = Optional.ofNullable(properties.getOpen_api().get(1).get("client")).orElse("").toString();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl).queryParam("query", searchBlogRequest.getQuery());

        Optional<String> sort = Optional.ofNullable(searchBlogRequest.getSort());
        Optional<Integer> page = Optional.ofNullable(searchBlogRequest.getPage());
        Optional<Integer> size = Optional.ofNullable(searchBlogRequest.getSize());

        sort.ifPresent(value -> builder.queryParam("sort", value.replace("accuracy", "sim").replace("recency", "date")));
        page.ifPresent(value -> builder.queryParam("start", value));
        size.ifPresent(value -> builder.queryParam("display", value));

        apiUrl = builder.toUriString();

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Naver-Client-Id", apiClientId)
                .defaultHeader("X-Naver-Client-Secret", apiClientSecret)
                .build();

        SearchBlogResponse searchBlogResponse = webClient.get().retrieve().bodyToMono(SearchBlogResponse.class).block();

        return searchBlogResponse;
    }
}
