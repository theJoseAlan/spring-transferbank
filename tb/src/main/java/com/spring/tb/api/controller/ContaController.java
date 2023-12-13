package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ContaDto;
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

}
