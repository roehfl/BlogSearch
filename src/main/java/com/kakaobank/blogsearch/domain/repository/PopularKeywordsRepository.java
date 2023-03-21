package com.kakaobank.blogsearch.domain.repository;

import com.kakaobank.blogsearch.domain.model.PopularKeywords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopularKeywordsRepository extends JpaRepository<PopularKeywords, Long> {
    Optional<PopularKeywords> findPopularKeywordsByKeyword(String keyword);
    Page<PopularKeywords> findAllByOrderBySearchCountDesc(PageRequest pageRequest);
}
