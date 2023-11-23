package com.spring.tb.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//Aqui cabe uma alteração: O cliente informa apenas o cep e eu dou um get no restante dos dados
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
