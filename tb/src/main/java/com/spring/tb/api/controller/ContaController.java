package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ContaDto;
import com.spring.tb.api.model.ContaRequest;
import com.spring.tb.domain.exception.LoginNaoAutorizadoException;
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
    public ResponseEntity<?> abrirConta(@RequestHeader String token){

        try{
                Long clienteId = tokenService.obterIdPorToken(token);

                Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

                tokenService.verificaToken(clienteEncontrado, token);

                contaService.verificaContaExistente(clienteId);

                Conta contaAberta = contaService.abrirConta(clienteEncontrado);

                return ResponseEntity.status(HttpStatus.OK).body(contaAberta);

            }catch (Exception e){
                throw new LoginNaoAutorizadoException("Erro ao abrir conta: "+e.getMessage());
            }
    }

    @GetMapping
    public ResponseEntity<?> obterDadosDaConta(@RequestHeader String token){

        try {
            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            contaService.verificaConta(clienteId);

            Conta contaEncontrada = contaService.buscarPorClienteId(clienteId);

            ContaDto contaDto = modelMapper.map(contaEncontrada, ContaDto.class);

            return ResponseEntity.ok(contaDto);

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao obter dados da conta: "+e.getMessage());
        }


    }

    @GetMapping("/saldo")
    public ResponseEntity<?> consultarSaldo(@RequestHeader String token){

        try {
            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            contaService.verificaConta(clienteId);

            Float saldo =  contaService.consultarSaldo(clienteId);

            return ResponseEntity.ok(saldo);

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao consultar saldo: "+e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> depositar(@RequestHeader String token,
                                             @RequestBody ContaRequest contaRequest){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            contaService.verificaConta(clienteId);

            contaService.depositar(clienteEncontrado, contaRequest.getNumero(), contaRequest.getValor());

            return ResponseEntity.ok().body("Depósito realizado com sucesso!");

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao depositar: "+e.getMessage());
        }
    }

    @PutMapping("/sacar")
    public ResponseEntity<?> sacar(@RequestHeader String token,
                                   @RequestBody ContaRequest contaRequest){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            contaService.sacar(clienteId, contaRequest.getValor());

            return ResponseEntity.ok().body("Saque realizado com sucesso!");

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao sacar: "+e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestHeader String token,
                                        @RequestBody ContaRequest contaRequest){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            tokenService.verificaToken(clienteEncontrado, token);

            contaService.verificaConta(clienteId);

            contaService.transferir(clienteEncontrado, contaRequest.getNumero(), contaRequest.getValor());

            return ResponseEntity.ok("Transferência realizada com sucesso");

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Erro ao transferir: "+e.getMessage());
        }
    }
}
