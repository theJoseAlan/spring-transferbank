package com.spring.tb.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositoRequest {

    private int agencia;

    private int nroconta;

    private Float valor;
}
