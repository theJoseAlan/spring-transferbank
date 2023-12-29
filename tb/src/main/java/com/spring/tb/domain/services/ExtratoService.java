package com.spring.tb.domain.services;

import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.repository.ExtratoRepository;
import jakarta.transaction.TransactionScoped;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ExtratoService {

    private ExtratoRepository extratoRepository;

    private ContaService contaService;

    public List<Extrato> listarPorCliente(Long clienteId){
        return extratoRepository.findAllByClienteId(clienteId);
    }

    @Transactional
    public void geraExtratoDeposito(Cliente cliente, int numeroConta, Float valor){

        Conta contaEncontrada = contaService.buscarPorNumero(numeroConta);

        Extrato extrato = new Extrato();
        extrato.setTipo("Deposito");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);

        if(contaEncontrada.getCliente().getId() != cliente.getId()){
            extrato.setNomeClienteDestino(contaEncontrada.getCliente().getNome());
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

        Conta contaOrigem = contaService.buscarPorNumero(nroContaOrigem);
        Conta contaDestino = contaService.buscarPorNumero(nroContaDestino);

        Extrato extrato = new Extrato();
        extrato.setTipo("Transferencia");
        extrato.setData(OffsetDateTime.now());
        extrato.setValor(valor);
        extrato.setNomeClienteDestino(contaDestino.getCliente().getNome());
        extrato.setCliente(contaOrigem.getCliente());

        extratoRepository.save(extrato);

    }

    public List<Extrato> listarPorTipo(String tipo, Long clienteId){
        return extratoRepository.findAllByTipoAndClienteId(tipo, clienteId);
    }

    public void deletarTodosPorClienteId(Long clienteId){
        extratoRepository.deletarTodosPorClienteId(clienteId);
    }

}
