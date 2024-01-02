package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.repository.ExtratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ExtratoService {

    @Autowired
    private ExtratoRepository extratoRepository;

    @Transactional
    public void geraExtratoTransferencia(Float valor, Cliente contaClienteDestino,
                                         Cliente contaClienteOrigem){

        Extrato extrato = new Extrato();

        if(contaClienteOrigem.equals(contaClienteDestino)){
            throw new NegocioException("Não é possível transferir para sua própria conta.");
        }

        extrato.setTipo("Transferencia");
        extrato.setValor(valor);
        extrato.setDataHora(OffsetDateTime.now());
        extrato.setNomeClienteDestino(contaClienteDestino.getNome());
        extrato.setCliente(contaClienteOrigem);

        extratoRepository.save(extrato);
    }

    public void geraExtratoDeposito(Float valor, Cliente cliente, Conta conta){

        Extrato extrato = new Extrato();

        extrato.setTipo("Deposito");
        extrato.setValor(valor);
        extrato.setDataHora(OffsetDateTime.now());

        if(!cliente.equals(conta.getCliente())){
            extrato.setNomeClienteDestino(conta.getCliente().getNome());
        }

        extrato.setCliente(cliente);

        extratoRepository.save(extrato);
    }

    public void geraExtratoSaque(Float valor, Cliente cliente){

        Extrato extrato = new Extrato();

        extrato.setTipo("Saque");
        extrato.setValor(valor);
        extrato.setDataHora(OffsetDateTime.now());

        extrato.setCliente(cliente);

        extratoRepository.save(extrato);
    }

    public List<Extrato> listarPorClienteId(Long clienteId){
        return extratoRepository.findAllByClienteId(clienteId);
    }

    public List<Extrato> listarPorTipo(String tipo, Long clienteId){
        return extratoRepository.findAllByTipoAndClienteId(tipo, clienteId);
    }

}
