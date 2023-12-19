package com.spring.tb.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtratoDto {

    private String tipo;

    private Float valor;

    private OffsetDateTime data;

    private String nomeClienteDestino;

    private String nomeCliente;

}
