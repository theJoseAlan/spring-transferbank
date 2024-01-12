package com.spring.tb.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Extrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private Float valor;

    private LocalDate data;

    private LocalTime hora;

    private String nomeClienteDestino;

    @ManyToOne
    private Cliente cliente;

}
