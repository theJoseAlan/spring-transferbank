package com.spring.tb.api.controller;

import com.spring.tb.api.model.Login;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.repository.ClienteRepository;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private ClienteRepository clienteRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody Cliente cliente){

        clienteService.salvar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login login){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPoremail(login.getEmail());

        if(clienteEncontrado.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        if(bCryptPasswordEncoder.matches(login.getSenha(), clienteEncontrado.get().getSenha())){

            String token = tokenService.geraToken(clienteEncontrado.get());

            return ResponseEntity.ok(token);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorreto");

    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterPorId(@PathVariable Long id, @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(id);

        if(clienteEncontrado.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!tokenService.validarToken(token, clienteEncontrado.get().getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(clienteEncontrado.get());

    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable  Long id,
                                             @Valid @RequestBody Cliente cliente,
                                             @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(id);

        if(clienteEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if (!tokenService.validarToken(token, clienteEncontrado.get().getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        cliente.setId(id);
        clienteService.atualizar(cliente);

        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

}
