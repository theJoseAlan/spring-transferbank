package com.spring.tb.api.controller;

import com.spring.tb.api.assembler.ExtratoAssembler;
import com.spring.tb.api.dto.ExtratoDto;
import com.spring.tb.api.model.ExtratoInput;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.ExtratoService;
import com.spring.tb.domain.services.JwtTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/extrato")
@AllArgsConstructor
public class ExtratoController {

    @Autowired
    private JwtTokenService tokenService;

    private ExtratoService extratoService;

    private ExtratoAssembler extratoAssembler;

    private ClienteService clienteService;

    @GetMapping("/tipo")
    public ResponseEntity<List<ExtratoDto>> listarPorTipo(@RequestHeader String token,
                                                    @RequestBody ExtratoInput extratoInput){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        List<Extrato> listaDeExtratosPorTipo = extratoService.listarPorTipo(extratoInput.getTipo(),
                clienteEncontrado.getId());

        List<ExtratoDto> extratos = extratoAssembler.toList(listaDeExtratosPorTipo);

        return ResponseEntity.ok(extratos);

    }

    @GetMapping
    public ResponseEntity<List<ExtratoDto>> listarTodos(@RequestHeader String token){

        Long clienteId = tokenService.obterIdPorToken(token);

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

        tokenService.verificaToken(clienteEncontrado, token);

        List<Extrato> listaDeExtratosPorTipo = extratoService.listarPorClienteId(clienteId);

        List<ExtratoDto> extratos = extratoAssembler.toList(listaDeExtratosPorTipo);

        return ResponseEntity.ok(extratos);

    }
}
