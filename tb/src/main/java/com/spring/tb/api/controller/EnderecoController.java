package com.spring.tb.api.controller;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Endereco;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.EnderecoService;
import com.spring.tb.domain.services.JwtTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/endereco")
@AllArgsConstructor
public class EnderecoController {

    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Endereco endereco,
                                              @RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

        if(!enderecoEncontrado.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esse cliente já possui um endereço cadastrado!");
        }

        endereco.setCliente(clienteEncontrado.get());
        Endereco enderecoSalvo = enderecoService.salvar(endereco);

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);

    }

    @PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody Endereco endereco,
                                       @RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

        enderecoService.verificaEndereco(clienteId);

        endereco.setId(enderecoEncontrado.get().getId());

        endereco.setCliente(clienteEncontrado.get());

        enderecoService.salvar(endereco);

        return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado");

    }

    @GetMapping
    public ResponseEntity<Endereco> buscarPorClienteId(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);


        tokenService.verificaToken(clienteEncontrado, token);

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

        if(enderecoEncontrado.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(enderecoEncontrado.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

}
