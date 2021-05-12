package com.howardism.reactivebackend.configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Supplier;
import com.howardism.reactivebackend.domain.Quote;
import com.howardism.reactivebackend.repository.QuoteMongoReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class QuoteDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger((QuoteDataLoader.class));

    private final QuoteMongoReactiveRepository quoteMongoReactiveRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (quoteMongoReactiveRepository.count().block() == 0l) {
            Supplier<String> idSupplier = getIdSequenceSupplier();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("pg2000.txt")));

            Flux.fromStream(bufferedReader.lines().filter(l -> !l.trim().isEmpty())
                    .map(l -> quoteMongoReactiveRepository
                            .save(new Quote(idSupplier.get(), "Test Quote", l))))
                    .subscribe(m -> log.info("New quote loaded: {}", m.block()));
            log.info("Repository contains now {} entries.",
                    quoteMongoReactiveRepository.count().block());
        }
    }

    private Supplier<String> getIdSequenceSupplier() {
        return new Supplier<String>() {
            Long l = 0l;

            @Override
            public String get() {
                // add zero padding
                return String.format("%05d", l++);
            }
        };
    }
}
