package com.spring.tb.api.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ExtratoInput {
    private String tipo;
    private LocalDate data;
    private LocalTime hora;
}
