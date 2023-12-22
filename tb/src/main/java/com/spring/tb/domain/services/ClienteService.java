package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.EntidadeNaoEncontradaException;
import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Cliente salvar(Cliente cliente){

        boolean emailEmUso = clienteRepository.findByEmail(cliente.getEmail())
                .stream()
                .anyMatch(clienteExistente -> !clienteExistente.equals(cliente));

        boolean cpfEmUso = clienteRepository.findByCpf(cliente.getCpf())
                .stream()
                .anyMatch(clienteExistente -> !clienteExistente.equals(cliente));

        if (emailEmUso) {
            throw new NegocioException("Email já cadastrado");
        }

        if (cpfEmUso) {
            throw new NegocioException("CPF já cadastrado");
        }

        String senhaCriptografada = bCryptPasswordEncoder.encode(cliente.getSenha());

        cliente.setSenha(senhaCriptografada);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {

        Optional<Cliente> clienteExistenteByEmail = clienteRepository.findByEmail(cliente.getEmail());

        /* Eu tenho consciência de que esse trecho de código é uma gambiarra,
        * mas vou concertá-lo futuramente. Foi o que consegui fazer por hora
        * para resolver  o problema da atualização de email duplicado. Abraço!
        */
        if(!clienteExistenteByEmail.isPresent() ){
            String senhaCriptografada = bCryptPasswordEncoder.encode(cliente.getSenha());
            cliente.setSenha(senhaCriptografada);

            return clienteRepository.save(cliente);
        }

        if (!clienteExistenteByEmail.get().getId().equals(cliente.getId())) {
            throw new NegocioException("Email já cadastrado");
        }

        Optional<Cliente> clienteExistenteByCpf = clienteRepository.findByCpf(cliente.getCpf());

        if (clienteExistenteByCpf.isPresent() && !clienteExistenteByCpf.get().getId().equals(cliente.getId())) {
            throw new NegocioException("CPF já cadastrado");
        }

        String senhaCriptografada = bCryptPasswordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);

        return clienteRepository.save(cliente);
    }

    public void deletarPorId(Long id){
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> buscarPorId(Long id){
        return clienteRepository.findById(id);
    }


    public Cliente verificaCadastroCliente(String email){

        Optional<Cliente> clienteEncontrado = clienteRepository.findByEmail(email);

        if(!clienteEncontrado.isPresent()){
            throw new EntidadeNaoEncontradaException("Você ainda não possui cadastro no sistema!");
        }

        return clienteEncontrado.get();
    }

    public Cliente verificaCadastroCliente(Long clienteId){

        Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteId);

        if(!clienteEncontrado.isPresent()){
            throw new EntidadeNaoEncontradaException("Você ainda não possui cadastro no sistema!");
        }

        return clienteEncontrado.get();
    }

}
