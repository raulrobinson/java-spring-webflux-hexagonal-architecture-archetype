package com.raulbolivarnavas.ms.usecase.helpers;

import com.raulbolivarnavas.ms.usecase.dto.PageResponse;
import com.raulbolivarnavas.ms.usecase.dto.PagedResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PaginationHelper {

    public <T, R> PageResponse<R> toPageResponse(PagedResult<T> result, Function<T, R> mapper) {
        List<R> mappedContent = result.content().stream()
                .map(mapper)
                .toList();

        return new PageResponse<>(
                mappedContent,
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.first(),
                result.last()
        );
    }
}
