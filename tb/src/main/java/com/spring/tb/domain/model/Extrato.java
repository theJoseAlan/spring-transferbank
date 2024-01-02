package com.spring.tb.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
public class Extrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private Float valor;

    private OffsetDateTime dataHora;

    private String nomeClienteDestino;

    @ManyToOne
    private Cliente cliente;

}
