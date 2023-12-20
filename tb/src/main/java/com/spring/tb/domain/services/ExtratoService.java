package com.spring.tb.domain.services;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.repository.ContaRepository;
import com.spring.tb.domain.repository.ExtratoRepository;
import jakarta.transaction.TransactionScoped;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExtratoService {

    @Autowired
    private ExtratoRepository extratoRepository;

    @Autowired
    private ContaRepository contaRepository;

    public List<Extrato> listarPorCliente(Long clienteId){
        return extratoRepository.findAllByClienteId(clienteId);
    }

    @Transactional
    public void geraExtratoDeposito(Cliente cliente, int numeroConta, Float valor){

        Optional<Conta> contaEncontrada = contaRepository.findByNumero(numeroConta);

        Extrato extrato = new Extrato();
        extrato.setTipo("Deposito");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);

        if(contaEncontrada.get().getCliente().getId() != cliente.getId()){
            extrato.setNomeClienteDestino(contaEncontrada.get().getCliente().getNome());
        }

        extrato.setCliente(cliente);

        extratoRepository.save(extrato);
    }

    @Transactional
    public void geraExtratoSaque(Cliente cliente, Float valor){

        Extrato extrato = new Extrato();
        extrato.setTipo("Saque");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);
        extrato.setCliente(cliente);

        extratoRepository.save(extrato);
    }

    @TransactionScoped
    public void geraExtratoTransferencia(int nroContaOrigem, int nroContaDestino, Float valor){

        Optional<Conta> contaOrigem = contaRepository.findByNumero(nroContaOrigem);
        Optional<Conta> contaDestino = contaRepository.findByNumero(nroContaDestino);

        Extrato extrato = new Extrato();
        extrato.setTipo("Transferência");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);
        extrato.setNomeClienteDestino(contaDestino.get().getCliente().getNome());
        extrato.setCliente(contaOrigem.get().getCliente());

        extratoRepository.save(extrato);

    }

    public List<Extrato> listarPorTipo(String tipo, Long clienteId){
        return extratoRepository.findAllByTipoAndClienteId(tipo, clienteId);
    }

}
