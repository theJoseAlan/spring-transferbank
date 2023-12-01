package com.spring.tb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class ClienteDto {

    private Long id;
    private String nome;
    private String email;
    private String telefone;

    public ClienteDto(){

    }

}
