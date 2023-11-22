package com.spring.tb.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    //@NotBlank
    private String logradouro;

    //@NotBlank
    private String numero;

    //@NotBlank
    private String complemento;

    //@NotBlank
    private String bairro;

    private String cidade;

    private String estado;

}
