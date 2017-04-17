package com.lucjross.java8lombokstreamsexample;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = {"name"})
class Company {

    private final @NonNull String name;

    private @Setter String description;
}
