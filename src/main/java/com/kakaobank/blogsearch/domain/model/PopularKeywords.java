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
public class PopularKeywords {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
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
