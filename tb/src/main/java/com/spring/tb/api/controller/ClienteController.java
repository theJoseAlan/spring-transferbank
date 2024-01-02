package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ClienteDto;
import com.spring.tb.api.model.Login;
import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.services.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

    @Autowired
    private JwtTokenService tokenService;

    private ModelMapper modelMapper;

    private EmailService emailService;

    private EnderecoService enderecoService;

    private ContaService contaService;

    @PostMapping
    public ResponseEntity<ClienteDto> cadastrar(@Valid @RequestBody Cliente cliente){

        clienteService.salvar(cliente);

//        emailService.sendEmail(cliente.getEmail(), "Cadastro TransferBank",
//                cliente.getNome()+", seu cadastro foi criado com suceso");

        ClienteDto clienteDto = modelMapper.map(cliente, ClienteDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody Login login){

        Cliente clienteEncontrado = clienteService.verificaCadastroCliente(login.getEmail());

        String token = tokenService.geraTokenLogin(login.getSenha(), clienteEncontrado);

        return ResponseEntity.ok(token);

    }

    @GetMapping
    public ResponseEntity<ClienteDto> obterPorId(@RequestHeader String token){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            if(!tokenService.verificaToken(clienteEncontrado, token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            tokenService.obterIdPorToken(token);

            ClienteDto clienteDto = modelMapper.map(clienteEncontrado, ClienteDto.class);

            return ResponseEntity.ok(clienteDto);

        }catch (Exception e){
            throw new NegocioException("Erro ao obter dados cliente: "+e.getMessage());
        }

    }

    @PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody Cliente cliente,
                                             @RequestHeader String token){
        try {
            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            if(!tokenService.verificaToken(clienteEncontrado, token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            cliente.setId(clienteId);
            clienteService.atualizar(cliente);

            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado!");
        }catch (Exception e){
            throw new NegocioException("Erro ao atualizar cliente "+e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletar(@RequestHeader String token){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Cliente clienteEncontrado = clienteService.verificaCadastroCliente(clienteId);

            if(!tokenService.verificaToken(clienteEncontrado, token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            contaService.deletarConta(clienteId);

            enderecoService.deletarEnderecoExistente(clienteId);

            clienteService.deletarPorId(clienteId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }catch (Exception e){
            throw new NegocioException("Erro ao deletar cliente: "+e.getMessage());

        }
    }

}
