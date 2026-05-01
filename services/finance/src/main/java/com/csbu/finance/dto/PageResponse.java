package com.csbu.finance.dto;


import java.util.List;

public record PageResponse<T>(
        Integer currentPage,
        Integer totalPages,
        Integer pageSize,
        Integer totalElement,
        List<T> data
) {
}
