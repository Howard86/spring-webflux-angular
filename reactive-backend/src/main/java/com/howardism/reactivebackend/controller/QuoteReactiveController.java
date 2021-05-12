package com.howardism.reactivebackend.controller;

import java.time.Duration;

import com.howardism.reactivebackend.domain.Quote;
import com.howardism.reactivebackend.repository.QuoteMongoReactiveRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class QuoteReactiveController {

    private static final int DELAY_PER_ITEM_MS = 100;

    private final QuoteMongoReactiveRepository quoteMongoReactiveRepository;

    @GetMapping("/quote-reactive")
    public Flux<Quote> getQuoteFlux() {
        return this.quoteMongoReactiveRepository.findAll()
                .delayElements(Duration.ofMillis(DELAY_PER_ITEM_MS));
    }

    @GetMapping("/quote-reactive-paged")
    public Flux<Quote> getQuoteFlux(final @RequestParam(name = "page") int page,
            final @RequestParam(name = "size") int size) {
        return this.quoteMongoReactiveRepository
                .findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size))
                .delayElements(Duration.ofMillis(DELAY_PER_ITEM_MS));
    }
}
