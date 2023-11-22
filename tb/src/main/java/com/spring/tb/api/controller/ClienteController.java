package com.spring.tb.api.controller;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.repository.ClienteRepository;
import com.spring.tb.domain.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody Cliente cliente){

        clienteService.cadastrar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
}
