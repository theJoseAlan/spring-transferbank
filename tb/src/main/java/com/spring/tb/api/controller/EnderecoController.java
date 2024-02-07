package com.spring.tb.api.controller;

import com.spring.tb.api.model.EnderecoRequest;
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

@RestController
@RequestMapping("/endereco")
@AllArgsConstructor
public class EnderecoController {

    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<Endereco> cadastrar(@Valid @RequestBody Endereco endereco,
                                              @RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        enderecoService.buscarPorClienteId(clienteId);

        endereco.setCliente(clienteEncontrado);

        Endereco enderecoSalvo = enderecoService.salvar(endereco);

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);

    }

    @PostMapping("/{cep}")
    public ResponseEntity<?> cadastrarEndereco(@RequestHeader String token,
                                                      @PathVariable String cep,
                                                      @RequestBody EnderecoRequest enderecoRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        enderecoService.buscarPorClienteId(clienteId);

        Endereco endereco = enderecoService.salvar(cep, enderecoRequest, clienteEncontrado);

        return ResponseEntity.status(HttpStatus.OK).body(endereco);

    }

    @PutMapping
    public ResponseEntity<String> atualizar(@Valid @RequestBody Endereco endereco,
                                       @RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        Endereco enderecoEncontrado = enderecoService.verificaEndereco(clienteId);

        endereco.setId(enderecoEncontrado.getId());

        endereco.setCliente(clienteEncontrado);

        enderecoService.salvar(endereco);

        return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado");

    }

    @GetMapping
    public ResponseEntity<Endereco> buscarPorClienteId(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        Endereco enderecoEncontrado = enderecoService.verificaEndereco(clienteId);

        return ResponseEntity.status(HttpStatus.OK).body(enderecoEncontrado);

    }

}
