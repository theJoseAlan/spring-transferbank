package com.spring.tb.api.controller;

import com.spring.tb.api.assembler.ExtratoAssembler;
import com.spring.tb.api.model.ExtratoInput;
import com.spring.tb.api.model.ExtratoResponse;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.ExtratoService;
import com.spring.tb.domain.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/extrato")
public class ExtratoController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private ExtratoService extratoService;

    @Autowired
    private ExtratoAssembler extratoAssembler;

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<ExtratoResponse>> listar(@PathVariable Long clienteId,
                                        @RequestHeader String token){

        List<Extrato> listaDeExtratosPorCliente = extratoService.listarPorCliente(clienteId);

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ExtratoResponse> extratos = extratoAssembler.toList(listaDeExtratosPorCliente);

        return ResponseEntity.ok(extratos);

    }

    @GetMapping("/tipo/{clienteId}")
    public ResponseEntity<List<Extrato>> listarPorTipo(@PathVariable Long clienteId,
                                                 @RequestHeader String token,
                                                 @RequestBody ExtratoInput extratoInput){

        List<Extrato> extratos = extratoService.listarPorTipo(extratoInput.getTipo());

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //ExtratoResponse extratos = extratoAssembler.toModel(listaDeExtratosPorTipo.get());

        return ResponseEntity.ok(extratos);

    }


}