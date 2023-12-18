package com.spring.tb.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int agencia;

    private int numero;

    private Float saldo;

    @JsonIgnore
    @OneToOne
    private Cliente cliente;


    public Conta() {
    }
}
