package com.hkteam.ecommerce_platform.dto.response;

import java.util.Collections;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationResponse<T> {
    @Builder.Default
    private List<T> data = Collections.emptyList();

    int totalPages;
    int pageSize;
    long totalElements;
    int currentPage;
    boolean hasNext;
    boolean hasPrevious;
    Integer nextPage;
    Integer previousPage;
}