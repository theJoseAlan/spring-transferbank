package com.spring.tb.domain.exception;

public class LoginNaoAutorizadoException extends NegocioException{

    private static final long serialVersionUID = 1L;

    public LoginNaoAutorizadoException(String mensagem){
        super(mensagem);
    }
}
