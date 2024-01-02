package com.spring.tb.api.controller;

import com.spring.tb.domain.exception.LoginNaoAutorizadoException;
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

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            enderecoService.buscarPorClienteId(clienteId);

            endereco.setCliente(clienteEncontrado);

            Endereco enderecoSalvo = enderecoService.salvar(endereco);

            return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao cadastrar endereço: "+e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@Valid @RequestBody Endereco endereco,
                                       @RequestHeader String token){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            Endereco enderecoEncontrado = enderecoService.verificaEndereco(clienteId);

            endereco.setId(enderecoEncontrado.getId());

            endereco.setCliente(clienteEncontrado);

            enderecoService.salvar(endereco);

            return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado");

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao atualizar endereço: "+e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<Endereco> buscarPorClienteId(@RequestHeader String token){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            Endereco enderecoEncontrado = enderecoService.verificaEndereco(clienteId);

            return ResponseEntity.status(HttpStatus.OK).body(enderecoEncontrado);

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao obter dados: "+e.getMessage());
        }

    }

}
