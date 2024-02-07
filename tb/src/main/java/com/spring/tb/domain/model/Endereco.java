package com.spring.tb.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotNull
    private String logradouro;

    @NotEmpty
    @NotNull
    private String numero;

    private String complemento;

    @NotEmpty
    @NotNull
    private String bairro;

    @NotEmpty
    @NotNull
    private String cidade;

    @NotEmpty
    @NotNull
    private String estado;

    @JsonIgnore
    @OneToOne
    private Cliente cliente;

}
