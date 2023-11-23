package com.spring.tb.api.controller;

import com.spring.tb.api.model.Login;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody Cliente cliente){

        clienteService.cadastrar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login){

        Cliente clienteEncontrado = clienteService.buscarPoremail(login.getEmail());

        if(clienteEncontrado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        if(bCryptPasswordEncoder.matches(login.getSenha(), clienteEncontrado.getSenha())){

            String token = tokenService.geraToken(clienteEncontrado);

            return ResponseEntity.ok(token);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorreto");

    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obter(@PathVariable Long id, @RequestHeader String token){

        Cliente clienteEncontrado = clienteService.buscarPorId(id);

        // Verifica se o token é válido
        if (!tokenService.validarToken(token, clienteEncontrado.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        return ResponseEntity.ok(clienteEncontrado);

    }

}
