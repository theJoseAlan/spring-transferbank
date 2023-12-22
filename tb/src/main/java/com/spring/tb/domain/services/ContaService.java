package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.EntidadeNaoEncontradaException;
import com.spring.tb.domain.exception.NegocioException;
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

    @Autowired
    private ExtratoService extratoService;

    @Autowired
    private EnderecoService enderecoService;

    @Transactional
    public Conta abrirConta(Cliente cliente){

        enderecoService.verificaEndereco(cliente.getId());

        Conta conta = new Conta();
        Random random = new Random();

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

        if(!contaEncontrada.isPresent()){
            throw new NegocioException("Você não possui uma conta aberta!");
        }

        return contaEncontrada.get().getSaldo();

    }

    public void depositar(Cliente cliente, int numeroConta, Float valor){

        Optional<Conta> contaEncontrada = contaRepository.findByNumero(numeroConta);

        if(!contaEncontrada.isPresent()){
            throw new EntidadeNaoEncontradaException("Conta não encontrada");
        }

        Float valorDeposito = contaEncontrada.get().getSaldo() + valor;

        contaEncontrada.get().setSaldo(valorDeposito);

        contaRepository.save(contaEncontrada.get());

        extratoService.geraExtratoDeposito(cliente, numeroConta, valor);

    }

    public void sacar(Long clienteId, Float valor){

        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(!contaEncontrada.isPresent()){
            throw new NegocioException("Você não possui uma conta aberta!");
        }

        if(valor > contaEncontrada.get().getSaldo()){
            throw new NegocioException("Saldo insuficiente");
        }

        Float valorSaque = contaEncontrada.get().getSaldo() - valor;

        contaEncontrada.get().setSaldo(valorSaque);

        contaRepository.save(contaEncontrada.get());

        extratoService.geraExtratoSaque(contaEncontrada.get().getCliente(), valor);

    }

    public void transferir(Cliente cliente, int nroContaDestino, Float valor){

        Optional<Conta> contaOrigem = contaRepository.findByClienteId(cliente.getId());

        Optional<Conta> contaDestino = contaRepository.findByNumero(nroContaDestino);

        if(contaOrigem == contaDestino){
            throw new NegocioException("Não é possível transferir para sua própria conta!");
        }

        if(!contaOrigem.isPresent() || !contaDestino.isPresent()){
            throw new EntidadeNaoEncontradaException("Sua conta ou destinatário não foi encontrado!");
        }

        if(valor > contaOrigem.get().getSaldo()){
            throw new NegocioException("Saldo insuficiente");
        }

        Float saldoContaOrigem = contaOrigem.get().getSaldo() - valor;
        Float saldoContaDestino = contaDestino.get().getSaldo() + valor;

        contaOrigem.get().setSaldo(saldoContaOrigem);
        contaDestino.get().setSaldo(saldoContaDestino);

        contaRepository.save(contaOrigem.get());
        contaRepository.save(contaDestino.get());

        extratoService.geraExtratoTransferencia(contaOrigem.get().getNumero(), nroContaDestino, valor);

    }

    public void verificaConta(Long clienteId){
        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(contaEncontrada.isEmpty()){
            throw new NegocioException("Você não possui uma conta aberta!");
        }
    }
}
