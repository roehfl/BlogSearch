package com.kakaobank.blogsearch.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Meta {
    private Integer total_count;
    private Integer pageable_count;
    private Boolean is_end;
}
