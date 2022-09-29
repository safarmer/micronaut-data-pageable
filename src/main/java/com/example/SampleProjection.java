package com.example;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record SampleProjection(int id, String name, String owner, String token, boolean mine) {}
