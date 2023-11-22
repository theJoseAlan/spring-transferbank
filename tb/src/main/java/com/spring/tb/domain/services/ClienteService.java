package com.spring.tb.domain.services;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public Cliente cadastrar(Cliente cliente){



        boolean emailExistente = clienteRepository.findByEmail(cliente.getEmail())
                .stream()
                .anyMatch(clienteExistente -> !clienteExistente.equals(cliente));

        boolean cpfExistente = clienteRepository.findByEmail(cliente.getCpf())
                .stream()
                .anyMatch(clienteExistente -> !clienteExistente.equals(cliente));

        if(emailExistente){
            throw new RuntimeException("Email já cadastrado");
        }

        if(cpfExistente){
            throw new RuntimeException("Cpf já cadastrado");
        }

        String senhaCriptografada = bCryptPasswordEncoder.encode(cliente.getSenha());

        cliente.setSenha(senhaCriptografada);

        return clienteRepository.save(cliente);
    }

}
