package com.kakaobank.blogsearch.controller.validation;

public interface RequestValidator<T> {
    void validate(T request);
}
