package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ContaDto;
import com.spring.tb.api.model.ContaRequest;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.ContaService;
import com.spring.tb.domain.services.JwtTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/{clienteId}")
    public ResponseEntity<?> cadastrar(@PathVariable Long clienteId, @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);
        Optional<Conta> contaEncontrada = contaService.buscarPorClienteId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!contaEncontrada.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esse cliente já possui uma conta aberta!");
        }

        Conta contaAberta = contaService.abrirConta(clienteEncontrado.get());

        return ResponseEntity.status(HttpStatus.OK).body(contaAberta);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<?> obterDadosDaConta(@PathVariable Long clienteId,
                                                   @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);
        Optional<Conta> contaEncontrada = contaService.buscarPorClienteId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(contaEncontrada.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esse cliente não possui uma conta aberta!");
        }

        ContaDto contaDto = modelMapper.map(contaEncontrada.get(), ContaDto.class);

        return ResponseEntity.status(HttpStatus.OK).body(contaDto);

    }

    @GetMapping("/saldo/{clienteId}")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long clienteId,
                                                 @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);
        Optional<Conta> contaEncontrada = contaService.buscarPorClienteId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(contaEncontrada.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esse cliente não possui uma conta aberta!");
        }

        Float saldo =  contaService.consultarSaldo(clienteId);

        return ResponseEntity.ok(saldo);

    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<?> depositar(@PathVariable Long clienteId,
                                             @RequestHeader String token,
                                             @RequestBody ContaRequest contaRequest){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!contaService.existeContaPorNumero(contaRequest.getNroconta())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Conta não encontrada. Verifique os dados e tente novamente!");
        }

        contaService.depositar(clienteEncontrado.get(), contaRequest.getNroconta(), contaRequest.getValor());

        return ResponseEntity.ok().body("Depósito realizado com sucesso!");
    }

    @PutMapping("/sacar/{clienteId}")
    public ResponseEntity<?> sacar(@PathVariable Long clienteId,
                                   @RequestHeader String token,
                                   @RequestBody ContaRequest contaRequest){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        contaService.sacar(clienteId, contaRequest.getValor());

        return ResponseEntity.ok().body("Saque realizado com sucesso!");

    }
}
