package com.spring.tb.domain.services;

import com.spring.tb.domain.model.Endereco;
import com.spring.tb.domain.repository.EnderecoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco salvar(Endereco endereco){
        return enderecoRepository.save(endereco);
    }

    public Optional<Endereco> buscarPorClienteId(Long clienteId){
        return enderecoRepository.findByClienteId(clienteId);
    }

}
