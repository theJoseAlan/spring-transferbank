package com.spring.tb.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContaRequest {

    private int agencia;

    private int numero;

    private Float valor;
}
