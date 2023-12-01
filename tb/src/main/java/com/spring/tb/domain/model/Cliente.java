package com.spring.tb.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Getter
@Setter
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String nome;

    @NotBlank
    @NotNull
    @Email
    private String email;

    private String telefone;

    @NotBlank
    @NotNull
    @CPF
    private String cpf;

    @NotBlank
    @NotNull
    private String senha;

//    @OneToOne
//    private Endereco endereco;

    public Cliente() {
    }
}
