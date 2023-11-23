package com.spring.tb.domain.services;

import com.spring.tb.domain.model.Cliente;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;

@Service
public class JwtTokenService {

    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String geraToken(Cliente cliente){

        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[256];
        random.nextBytes(keyBytes);

        Claims claims = Jwts.claims()
                .setSubject(cliente.getEmail())
                .setId(cliente.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600000L));

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();

    }

    public boolean validarToken(String token, String email) {
        // Extrai as reivindicações do token
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // Valida a expiração e o assunto do token
        if (claims.getExpiration().before(new Date())) {
            return false;
        }

        if (!claims.getSubject().equals(email)) {
            return false;
        }

        // Se o token for válido, retorne true
        return true;
    }

}
