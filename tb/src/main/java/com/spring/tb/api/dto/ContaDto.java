package com.spring.tb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ContaDto {

    private String nomeCliente;
    private int agencia;
    private int numero;

    public ContaDto() {
    }
}
