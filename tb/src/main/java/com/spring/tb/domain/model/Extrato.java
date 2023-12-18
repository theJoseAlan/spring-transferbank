package com.spring.tb.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;



@Entity
@Getter
@Setter
@AllArgsConstructor
public class Extrato {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private Float valor;

    private OffsetDateTime data;

    @OneToOne
    private Cliente cliente;

    public Extrato() {
    }
}
