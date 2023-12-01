package com.spring.tb.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {

    @Email
    private String email;

    @NotBlank
    @NotEmpty
    private String senha;

}
