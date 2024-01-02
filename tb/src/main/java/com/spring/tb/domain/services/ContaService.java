package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.EntidadeNaoEncontradaException;
import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ContaService {

    private ContaRepository contaRepository;

    private EnderecoService enderecoService;

    private ExtratoService extratoService;

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

    public Conta buscarPorClienteId(Long clienteId){
        return contaRepository.findByClienteId(clienteId).get();
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

        if(valor <= 0){
            throw new NegocioException("Insira um valor válido para o depósito");
        }

        Float valorDeposito = contaEncontrada.get().getSaldo() + valor;

        contaEncontrada.get().setSaldo(valorDeposito);

        contaRepository.save(contaEncontrada.get());

        extratoService.geraExtratoDeposito(valor, cliente, contaEncontrada.get());

    }

    public void sacar(Long clienteId, Float valor){

        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(!contaEncontrada.isPresent()){
            throw new NegocioException("Você não possui uma conta aberta!");
        }

        if(valor > contaEncontrada.get().getSaldo()){
            throw new NegocioException("Saldo insuficiente");
        }

        if(valor <= 0){
            throw new NegocioException("Insira um valor válido para o depósito");
        }

        Float valorSaque = contaEncontrada.get().getSaldo() - valor;

        contaEncontrada.get().setSaldo(valorSaque);

        contaRepository.save(contaEncontrada.get());

        extratoService.geraExtratoSaque(valor, contaEncontrada.get().getCliente());

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

        if(valor <= 0){
            throw new NegocioException("Insira um valor válido para o depósito");
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

        extratoService.geraExtratoTransferencia(valor,
                contaDestino.get().getCliente(),
                contaOrigem.get().getCliente());

    }

    public void verificaConta(Long clienteId){
        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(contaEncontrada.isEmpty()){
            throw new NegocioException("Você não possui uma conta aberta!");
        }
    }


    public void verificaContaExistente(Long clienteId){
        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(!contaEncontrada.isEmpty()){
            throw new NegocioException("Você já possui uma conta aberta!");
        }
    }

    public void deletarConta(Long clienteId){

        Optional<Conta> contaEncontrada = contaRepository.findByClienteId(clienteId);

        if(contaEncontrada.isPresent()){

            if(contaEncontrada.get().getSaldo() > 0){
                throw new NegocioException("Você ainda possui saldo na conta");
            }

            //extratoService.deletarTodosPorClienteId(clienteId);
            contaRepository.deleteById(contaEncontrada.get().getId());
        }

    }

    public Conta buscarPorNumero(int numeroConta){
        return contaRepository.findByNumero(numeroConta).get();
    }
}
