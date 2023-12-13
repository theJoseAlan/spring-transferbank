package com.spring.tb.domain.services;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;


    Random random = new Random();

    @Transactional
    public Conta abrirConta(Cliente cliente){



        Conta conta = new Conta();

        conta.setAgencia(123);
        conta.setNumero(random.nextInt(100, 999));
        conta.setSaldo(0.0);
        conta.setCliente(cliente);

        contaRepository.save(conta);

        return conta;
    }

    public Optional<Conta> buscarPorClienteId(Long clienteId){
        return contaRepository.findByClienteId(clienteId);
    }

}
