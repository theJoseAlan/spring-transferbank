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
    public ResponseEntity<Conta> abrirConta(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.verificaContaExistente(clienteId);

        Conta contaAberta = contaService.abrirConta(clienteEncontrado);

        return ResponseEntity.status(HttpStatus.OK).body(contaAberta);
    }

    @GetMapping
    public ResponseEntity<ContaDto> obterDadosDaConta(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.verificaConta(clienteId);

        Conta contaEncontrada = contaService.buscarPorClienteId(clienteId);

        ContaDto contaDto = modelMapper.map(contaEncontrada, ContaDto.class);

        return ResponseEntity.ok(contaDto);

    }

    @GetMapping("/saldo")
    public ResponseEntity<Float> consultarSaldo(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.verificaConta(clienteId);

        Float saldo = contaService.consultarSaldo(clienteId);

        return ResponseEntity.ok(saldo);

    }

    @PutMapping
    public ResponseEntity<String> depositar(@RequestHeader String token,
                                             @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.verificaConta(clienteId);

        contaService.depositar(clienteEncontrado, contaRequest.getNumero(), contaRequest.getValor());

        return ResponseEntity.ok().body("Depósito realizado com sucesso!");

    }

    @PutMapping("/depositar")
    public ResponseEntity<String> depositarNaPropriaConta(@RequestHeader String token,
                                            @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.verificaConta(clienteId);

        contaService.depositarNaPropriaConta(clienteEncontrado, contaRequest.getValor());

        return ResponseEntity.ok().body("Depósito realizado com sucesso!");

    }

    @PutMapping("/sacar")
    public ResponseEntity<String> sacar(@RequestHeader String token,
                                   @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.sacar(clienteId, contaRequest.getValor());

        return ResponseEntity.ok().body("Saque realizado com sucesso!");

    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestHeader String token,
                                        @RequestBody ContaRequest contaRequest){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        contaService.verificaConta(clienteId);

        contaService.transferir(clienteEncontrado, contaRequest.getNumero(), contaRequest.getValor());

        return ResponseEntity.ok("Transferência realizada com sucesso");

    }
}
