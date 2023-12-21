package com.spring.tb.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ObjetoToken {
    private String sub;
    private Long jti;
    private BigInteger iat;
    private BigInteger exp;

}
