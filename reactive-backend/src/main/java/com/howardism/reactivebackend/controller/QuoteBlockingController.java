package com.howardism.reactivebackend.controller;

import com.howardism.reactivebackend.domain.Quote;
import com.howardism.reactivebackend.repository.QuoteMongoBlockingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class QuoteBlockingController {

    private static final int DELAY_PER_ITEM_MS = 100;

    private QuoteMongoBlockingRepository quoteMongoBlockingRepository;

    @GetMapping("/quotes-blocking")
    public Iterable<Quote> getQuotesBlocking() throws InterruptedException {
        Thread.sleep((DELAY_PER_ITEM_MS * quoteMongoBlockingRepository.count()));
        return quoteMongoBlockingRepository.findAll();
    }

    @GetMapping("/quotes-blocking-paged")
    public Iterable<Quote> getQuotesBlocking(final @RequestParam(name = "page") int page,
            final @RequestParam(name = "size") int size) throws InterruptedException {
        Thread.sleep((DELAY_PER_ITEM_MS * size));
        return quoteMongoBlockingRepository
                .findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size));
    }
}
