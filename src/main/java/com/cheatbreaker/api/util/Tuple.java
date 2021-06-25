package com.cheatbreaker.api.util;

import lombok.Data;

@Data
public class Tuple<T, A> {

    private final T key;
    private final A value;
}
