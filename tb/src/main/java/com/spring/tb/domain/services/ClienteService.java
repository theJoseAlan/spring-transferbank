package com.spring.tb.domain.services;

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
            throw new NegocioException("Email já cadastrado");
        }

        String senhaCriptografada = bCryptPasswordEncoder.encode(cliente.getSenha());

        cliente.setSenha(senhaCriptografada);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {

        Optional<Cliente> clienteExistenteByEmail = clienteRepository.findByEmail(cliente.getEmail());

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

    public Optional<Cliente> buscarPorId(Long id){
        return clienteRepository.findById(id);
    }


    public Optional<Cliente> buscarPoremail(String email){

        return clienteRepository.findByEmail(email);
    }

}
