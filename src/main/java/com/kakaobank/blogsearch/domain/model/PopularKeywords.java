package com.kakaobank.blogsearch.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "POPULAR_KEYWORDS_SEQ_GENERATOR",
        sequenceName = "POPULAR_KEYWORDS_SEQ",
        allocationSize = 1)
public class PopularKeywords {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POPULAR_KEYWORDS_SEQ_GENERATOR")
    private long id;
    private String keyword;
    private long searchCount;
    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdDateTime = LocalDateTime.now();
    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
}
