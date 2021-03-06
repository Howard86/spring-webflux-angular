package com.howardism.reactivebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Quote {

    private String id;
    private String book;
    private String content;

}
