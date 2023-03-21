package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.domain.model.PopularKeywords;
import com.kakaobank.blogsearch.domain.repository.PopularKeywordsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import com.kakaobank.blogsearch.config.Properties;
import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("searchService")
public class SearchServiceImpl implements SearchService{
    private final Properties properties;
    private final PopularKeywordsRepository popularKeywordsRepository;

    public SearchServiceImpl(Properties properties, PopularKeywordsRepository popularKeywordsRepository) {
        this.properties = properties;
        this.popularKeywordsRepository = popularKeywordsRepository;
    }

    @Override
    @Cacheable(value = "kakaoCache")
    @CircuitBreaker(name = "searchBlog", fallbackMethod = "searchBlogFallBack")
    public SearchBlogResponse searchBlog(SearchBlogRequest searchBlogRequest) {
        String apiKey = Optional.ofNullable(properties.getKakao().get("Authorization")).orElse("");
        String apiUrl = Optional.ofNullable(properties.getKakao().get("url")).orElse("");
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
                .defaultHeader("Authorization", apiKey)
                .build();

        SearchBlogResponse searchBlogResponse = webClient.get().retrieve().bodyToMono(SearchBlogResponse.class).block();

        return searchBlogResponse;
    }

    @Cacheable(value = "naverCache")
    public SearchBlogResponse searchBlogFallBack(SearchBlogRequest searchBlogRequest, Throwable t) {
        String apiClientId = Optional.ofNullable(properties.getNaver().get("X-Naver-Client-Id")).orElse("");
        String apiClientSecret = Optional.ofNullable(properties.getNaver().get("X-Naver-Client-Secret")).orElse("");
        String apiUrl = Optional.ofNullable(properties.getNaver().get("url")).orElse("");

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

    @Transactional
    @Override
    public Page<PopularKeywords> getPopularKeywordsAndAddCount(String keyword ) {
        PopularKeywords popularKeywords = popularKeywordsRepository.findPopularKeywordsByKeyword(keyword).orElse(new PopularKeywords());
        if (popularKeywords.getKeyword() == null || popularKeywords.getKeyword().isEmpty()) {
            popularKeywords.setKeyword(keyword);
            popularKeywords.setSearchCount(0);
        }
        popularKeywords.setSearchCount(popularKeywords.getSearchCount() + 1);
        popularKeywordsRepository.save(popularKeywords);
        PageRequest pageRequest = PageRequest.of(0, 10);
        return popularKeywordsRepository.findAllByOrderBySearchCountDesc(pageRequest);
    }

}
