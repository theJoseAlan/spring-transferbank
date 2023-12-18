package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.repository.ContaRepository;
import com.spring.tb.domain.repository.ExtratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ExtratoRepository extratoRepository;

    Random random = new Random();

    @Transactional
    public Conta abrirConta(Cliente cliente){

        Conta conta = new Conta();

        conta.setAgencia(123);
        conta.setNumero(random.nextInt(100, 999));
        conta.setSaldo((float) 0);
        conta.setCliente(cliente);

        contaRepository.save(conta);

        return conta;
    }

    public Optional<Conta> buscarPorClienteId(Long clienteId){
        return contaRepository.findByClienteId(clienteId);
    }

    public Float consultarSaldo(Long clienteId){

        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        return contaEncontrada.get().getSaldo();

    }

    public void depositar(int numeroConta, Float valor){

        Optional<Conta> contaEncontrada = contaRepository.findByNumero(numeroConta);

        if(!contaEncontrada.isPresent()){
            throw new NegocioException("Conta não encontrada");
        }

        Float valorDeposito = contaEncontrada.get().getSaldo() + valor;

        contaEncontrada.get().setSaldo(valorDeposito);

        contaRepository.save(contaEncontrada.get());

        Extrato extrato = new Extrato();
        extrato.setTipo("Deposito");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);

        extratoRepository.save(extrato);

    }

    public boolean existeContaPorNumero(int numero){

        if(contaRepository.findByNumero(numero).isPresent()){
            return true;
        }

        return false;

    }

    public void sacar(Long clienteId, Float valor){

        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(!contaEncontrada.isPresent()){
            throw new NegocioException("Conta não encontrada");
        }

        if(valor > contaEncontrada.get().getSaldo()){
            throw new NegocioException("Saldo insuficiente");
        }

        Float valorSaque = contaEncontrada.get().getSaldo() - valor;

        contaEncontrada.get().setSaldo(valorSaque);

        contaRepository.save(contaEncontrada.get());

        Extrato extrato = new Extrato();
        extrato.setTipo("Saque");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);

        extratoRepository.save(extrato);

    }

}
