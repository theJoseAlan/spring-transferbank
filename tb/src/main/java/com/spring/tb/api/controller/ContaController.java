package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ContaDto;
import com.spring.tb.api.model.ContaRequest;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.ContaService;
import com.spring.tb.domain.services.JwtTokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/conta")
@AllArgsConstructor
public class ContaController {

    private ContaService contaService;

    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);
        Optional<Conta> contaEncontrada = contaService.buscarPorClienteId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado.get(), token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!contaEncontrada.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esse cliente já possui uma conta aberta!");
        }

        Conta contaAberta = contaService.abrirConta(clienteEncontrado.get());

        return ResponseEntity.status(HttpStatus.OK).body(contaAberta);
    }

    @GetMapping
    public ResponseEntity<?> obterDadosDaConta(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado.get(), token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Conta> contaEncontrada = contaService.buscarPorClienteId(clienteId);

        contaService.verificaConta(clienteId);

        ContaDto contaDto = modelMapper.map(contaEncontrada.get(), ContaDto.class);

        return ResponseEntity.ok(contaDto);

    }

    @GetMapping("/saldo")
    public ResponseEntity<?> consultarSaldo(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado.get(), token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        contaService.verificaConta(clienteId);

        Float saldo =  contaService.consultarSaldo(clienteId);

        return ResponseEntity.ok(saldo);

    }

    @PutMapping
    public ResponseEntity<?> depositar(@RequestHeader String token,
                                             @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado.get(), token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        contaService.verificaConta(clienteId);

        contaService.depositar(clienteEncontrado.get(), contaRequest.getNumero(), contaRequest.getValor());

        return ResponseEntity.ok().body("Depósito realizado com sucesso!");
    }

    @PutMapping("/sacar")
    public ResponseEntity<?> sacar(@RequestHeader String token,
                                   @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        contaService.sacar(clienteId, contaRequest.getValor());

        return ResponseEntity.ok().body("Saque realizado com sucesso!");

    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestHeader String token,
                                        @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado.get(), token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        contaService.verificaConta(clienteId);

        contaService.transferir(clienteEncontrado.get(), contaRequest.getNumero(), contaRequest.getValor());

        return ResponseEntity.ok("Transferência realizada com sucesso");

    }
}
