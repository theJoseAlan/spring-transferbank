package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.EntidadeNaoEncontradaException;
import com.spring.tb.domain.exception.NegocioException;
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

    public void deletarEnderecoExistente(Long clienteId){

        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByClienteId(clienteId);

        if(enderecoEncontrado.isPresent()){
            enderecoRepository.deleteById(enderecoEncontrado.get().getId());
        }

    }

    public void buscarPorClienteId(Long clienteId){

        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByClienteId(clienteId);

        if(!enderecoEncontrado.isEmpty()){
            throw new NegocioException("Você já possui um endereco cadastrado");
        }
    }

    public Endereco verificaEndereco(Long clienteId){

        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByClienteId(clienteId);

        if(!enderecoEncontrado.isPresent()){
            throw new EntidadeNaoEncontradaException("Você ainda não possui um endereco cadastrado!");
        }

        return enderecoEncontrado.get();
    }

}
