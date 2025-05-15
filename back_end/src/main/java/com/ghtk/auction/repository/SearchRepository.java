package com.ghtk.auction.repository;

import com.ghtk.auction.dto.model.SearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchRepository {
    Page<?> search(SearchRequest request, Class<?> clazz);

    List<Long> summary(List<SearchRequest> requestList, Class<?> clazz);
}
