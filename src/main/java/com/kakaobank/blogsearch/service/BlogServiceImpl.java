package com.kakaobank.blogsearch.service;

import com.kakaobank.blogsearch.controller.dto.response.GetPopularKeywordsResponse;
import com.kakaobank.blogsearch.domain.model.PopularKeywords;
import com.kakaobank.blogsearch.domain.repository.PopularKeywordsRepository;
import com.kakaobank.blogsearch.exceptions.GetPopularKeywordsException;
import com.kakaobank.blogsearch.exceptions.InvalidRequestException;
import com.kakaobank.blogsearch.exceptions.SearchBlogException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.kakaobank.blogsearch.config.Properties;
import com.kakaobank.blogsearch.controller.dto.request.SearchBlogRequest;
import com.kakaobank.blogsearch.controller.dto.response.SearchBlogResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import java.util.Optional;

@Service("searchService")
public class BlogServiceImpl implements BlogService {
    private final Properties properties;
    private final PopularKeywordsRepository popularKeywordsRepository;

    public BlogServiceImpl(Properties properties, PopularKeywordsRepository popularKeywordsRepository) {
        this.properties = properties;
        this.popularKeywordsRepository = popularKeywordsRepository;
    }

    @Override
    @CircuitBreaker(name = "searchBlog", fallbackMethod = "searchBlogFallBack")
    public SearchBlogResponse searchBlog(SearchBlogRequest searchBlogRequest) {
        String apiKey = Optional.ofNullable(properties.getKakao().get("Authorization")).orElse("");
        String apiUrl = Optional.ofNullable(properties.getKakao().get("url")).orElse("");

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        Optional<String> sort = Optional.ofNullable(searchBlogRequest.getSort());
        Optional<Integer> page = Optional.ofNullable(searchBlogRequest.getPage());
        Optional<Integer> size = Optional.ofNullable(searchBlogRequest.getSize());

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        SearchBlogResponse searchBlogResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("query", searchBlogRequest.getQuery())
                    .queryParamIfPresent("sort", sort)
                    .queryParamIfPresent("page", page)
                    .queryParamIfPresent("size", size)
                    .build())
                .header("Authorization", apiKey)
                .retrieve().bodyToMono(SearchBlogResponse.class).block();
        searchBlogResponse.setDisplay(size.orElse(10));

        return searchBlogResponse;
    }

    public SearchBlogResponse searchBlogFallBack(SearchBlogRequest searchBlogRequest, Throwable t) {
        String apiClientId = Optional.ofNullable(properties.getNaver().get("X-Naver-Client-Id")).orElse("");
        String apiClientSecret = Optional.ofNullable(properties.getNaver().get("X-Naver-Client-Secret")).orElse("");
        String apiUrl = Optional.ofNullable(properties.getNaver().get("url")).orElse("");

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        Optional<String> sort = Optional.ofNullable(searchBlogRequest.getSort());
        Optional<Integer> page = Optional.ofNullable(searchBlogRequest.getPage());
        Optional<Integer> size = Optional.ofNullable(searchBlogRequest.getSize());
        int start = 1;

        if (page.isPresent()) {
            start = (page.get() - 1) * size.orElse(10) + 1;
        }
        if (start >= 1000) {
            throw new InvalidRequestException("Value of page times size cannot be greater or same than 1000");
        }


        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Integer finalStart = start;
        SearchBlogResponse searchBlogResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", searchBlogRequest.getQuery())
                        .queryParamIfPresent("sort", sort)
                        .queryParam("start", finalStart)
                        .queryParamIfPresent("display", size)
                        .build())
                .header("X-Naver-Client-Id", apiClientId)
                .header("X-Naver-Client-Secret", apiClientSecret)
                .retrieve().bodyToMono(SearchBlogResponse.class).block();

        return searchBlogResponse;
    }

    @Override
    @Transactional
    @CacheEvict(value = "Top10", allEntries = true)
    public void insertOrUpdatePopularKeywords(String keyword) {
        try {
            PopularKeywords popularKeywords = popularKeywordsRepository.findPopularKeywordsByKeyword(keyword).orElse(new PopularKeywords());
            if (popularKeywords.getKeyword() == null || popularKeywords.getKeyword().isEmpty()) {
                popularKeywords.setKeyword(keyword);
                popularKeywords.setSearchCount(1L);
            } else {
                popularKeywords.setSearchCount(popularKeywords.getSearchCount() + 1L);
            }
            popularKeywordsRepository.save(popularKeywords);
        } catch (DataAccessException e) {
            throw new SearchBlogException("Insert or update PopularKeywords failed.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Top10")
    public GetPopularKeywordsResponse getPopularKeywords() {
        try {
            PageRequest pageRequest = PageRequest.of(0, 10);
            return new GetPopularKeywordsResponse(popularKeywordsRepository.findAllByOrderBySearchCountDesc(pageRequest).getContent());
        } catch (DataAccessException e) {
            throw new GetPopularKeywordsException("Get PopularKeywords failed.");
        }

    }


}
