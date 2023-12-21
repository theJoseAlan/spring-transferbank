package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ClienteDto;
import com.spring.tb.api.model.Login;
import com.spring.tb.domain.exception.EntidadeNaoEncontradaException;
import com.spring.tb.domain.exception.LoginNaoAutorizadoException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Endereco;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.EmailService;
import com.spring.tb.domain.services.EnderecoService;
import com.spring.tb.domain.services.JwtTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @PostMapping
    public ResponseEntity<ClienteDto> cadastrar(@Valid @RequestBody Cliente cliente){

        clienteService.salvar(cliente);

//        emailService.sendEmail(cliente.getEmail(), "Cadastro TransferBank",
//                cliente.getNome()+", seu cadastro foi criado com suceso");

        ClienteDto clienteDto = modelMapper.map(cliente, ClienteDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login login){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPoremail(login.getEmail());

        if(!clienteEncontrado.isPresent()){
            throw new EntidadeNaoEncontradaException("Você ainda não possui cadastro no sistema!");
        }

        return ResponseEntity.ok(tokenService.geraTokenLogin(login.getSenha(), clienteEncontrado.get()));

    }

    @GetMapping
    public ResponseEntity<ClienteDto> obterPorId(@RequestHeader String token){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

            if(!clienteEncontrado.isPresent()){
                throw new EntidadeNaoEncontradaException("Você ainda não possui cadastro no sistema!");
            }

            if(!tokenService.verificaToken(clienteEncontrado, token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            tokenService.obterIdPorToken(token);

            ClienteDto clienteDto = modelMapper.map(clienteEncontrado.get(), ClienteDto.class);

            return ResponseEntity.ok(clienteDto);

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Faça o login para obter o token de acesso");
        }

    }

    @PutMapping
    public ResponseEntity<?> atualizar(@Valid @RequestBody Cliente cliente,
                                             @RequestHeader String token){
        try {
            Long clienteId = tokenService.obterIdPorToken(token);

            Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

            if(!clienteEncontrado.isPresent()){
                throw new EntidadeNaoEncontradaException("Você ainda não possui cadastro no sistema!");
            }

            if(clienteEncontrado.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            if(!tokenService.verificaToken(clienteEncontrado, token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            cliente.setId(clienteId);
            clienteService.atualizar(cliente);

            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado!");
        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Faça o login para obter o token de acesso");
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestHeader String token){

        try {

            Long clienteId = tokenService.obterIdPorToken(token);

            Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(clienteId);

            if(!clienteEncontrado.isPresent()){
                throw new EntidadeNaoEncontradaException("Você ainda não possui cadastro no sistema!");
            }

            if(!tokenService.verificaToken(clienteEncontrado, token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(clienteId);

            if(enderecoEncontrado.isPresent()){
                enderecoService.deletarEndereco(enderecoEncontrado.get().getId());
            }

            clienteService.deletarPorId(clienteId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }catch (Exception e){
            throw new LoginNaoAutorizadoException("Faça o login para obter o token de acesso");

        }
    }

}
