package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.LoginNaoAutorizadoException;
import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

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

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        if (claims.getExpiration().before(new Date())) {
            return false;
        }

        if (!claims.getSubject().equals(email)) {
            return false;
        }

        return true;
    }

    public boolean verificaToken(Optional<Cliente> cliente, String token){

        try{
            if(cliente.isEmpty() || !validarToken(token, cliente.get().getEmail())){
                return false;
            }
        }catch (Exception ex){
            throw new NegocioException("Faça o login para obter o token de acesso");
        }

        return true;
    }

    public String geraTokenLogin(String senhaInput, Cliente cliente){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if(bCryptPasswordEncoder.matches(senhaInput, cliente.getSenha())){

            return geraToken(cliente);

        }else{
            throw new LoginNaoAutorizadoException("Senha ou email incorreto!");
        }
    }


}
