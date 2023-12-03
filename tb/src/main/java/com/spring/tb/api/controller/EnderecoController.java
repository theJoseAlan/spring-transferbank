package com.spring.tb.api.controller;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Endereco;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.EnderecoService;
import com.spring.tb.domain.services.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/{clienteId}")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Endereco endereco,
                                              @PathVariable Long clienteId,
                                              @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

        if(clienteEncontrado.isEmpty() || !tokenService.validarToken(token, clienteEncontrado.get().getEmail())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!enderecoEncontrado.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esse cliente já possui um endereço cadastrado!");
        }

        endereco.setCliente(clienteEncontrado.get());
        Endereco enderecoSalvo = enderecoService.salvar(endereco);

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);

    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<?> atualizar(@Valid @RequestBody Endereco endereco,
                                       @PathVariable Long clienteId,
                                       @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

        if(clienteEncontrado.isEmpty() || !tokenService.validarToken(token, clienteEncontrado.get().getEmail())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(enderecoEncontrado.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Endereco não encontrado");
        }

        endereco.setId(enderecoEncontrado.get().getId());
        endereco.setCliente(clienteEncontrado.get());
        enderecoService.salvar(endereco);

        return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado");

    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Endereco> buscarPorClienteId(@PathVariable Long clienteId,
                                                       @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

        if(clienteEncontrado.isEmpty() || !tokenService.validarToken(token, clienteEncontrado.get().getEmail())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(enderecoEncontrado.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(enderecoEncontrado.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

}
