package com.spring.tb.api.controller;

import com.spring.tb.api.dto.ClienteDto;
import com.spring.tb.api.model.Login;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Endereco;
import com.spring.tb.domain.services.ClienteService;
import com.spring.tb.domain.services.EmailService;
import com.spring.tb.domain.services.EnderecoService;
import com.spring.tb.domain.services.JwtTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cliente")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

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

        return ResponseEntity.ok(tokenService.geraTokenLogin(login.getSenha(), clienteEncontrado.get()));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obterPorId(@PathVariable Long id, @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(id);

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ClienteDto clienteDto = modelMapper.map(clienteEncontrado.get(), ClienteDto.class);

        return ResponseEntity.ok(clienteDto);

    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody Cliente cliente,
                                             @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(id);

        if(clienteEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        cliente.setId(id);
        clienteService.atualizar(cliente);

        return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id,
                                        @RequestHeader String token){

        Optional<Cliente> clienteEncontrado = clienteService.buscarPorId(id);
        Optional<Endereco> enderecoEncontrado = enderecoService.buscarPorClienteId(id);

        if(clienteEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(!tokenService.verificaToken(clienteEncontrado, token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(enderecoEncontrado.isPresent()){
            enderecoService.deletarEndereco(enderecoEncontrado.get().getId());
        }

        clienteService.deletarPorId(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
