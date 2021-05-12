package com.howardism.reactivebackend.repository;

import java.util.List;
import com.howardism.reactivebackend.domain.Quote;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuoteMongoBlockingRepository extends PagingAndSortingRepository<Quote, String> {

    List<Quote> findAllByIdNotNullOrderByIdAsc(final Pageable page);
}
