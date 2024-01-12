package com.spring.tb.domain.services;

import com.spring.tb.domain.exception.NegocioException;
import com.spring.tb.domain.model.Cliente;
import com.spring.tb.domain.model.Conta;
import com.spring.tb.domain.model.Extrato;
import com.spring.tb.domain.repository.ExtratoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ExtratoService {

    private ExtratoRepository extratoRepository;

    @Transactional
    public Extrato geraExtratoTransferencia(Float valor, Cliente contaClienteDestino,
                                         Cliente contaClienteOrigem){

        Extrato extrato = new Extrato();

        if(contaClienteOrigem.equals(contaClienteDestino)){
            throw new NegocioException("Não é possível transferir para sua própria conta.");
        }

        extrato.setTipo("Transferencia");
        extrato.setValor(valor);

        // Formatando a data no formato desejado (dd/MM/yy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String dataFormatada = LocalDate.now().format(formatter);

        // Convertendo a string formatada para LocalDate
        extrato.setData(LocalDate.parse(dataFormatada, formatter));

        extrato.setHora(LocalTime.now());

        extrato.setNomeClienteDestino(contaClienteDestino.getNome());
        extrato.setCliente(contaClienteOrigem);

        return extratoRepository.save(extrato);
    }

    public Extrato geraExtratoDeposito(Float valor, Cliente cliente, Conta conta){

        Extrato extrato = new Extrato();

        extrato.setTipo("Deposito");
        extrato.setValor(valor);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String dataFormatada = LocalDate.now().format(formatter);
        extrato.setData(LocalDate.parse(dataFormatada, formatter));

        extrato.setHora(LocalTime.now());

        if(!cliente.equals(conta.getCliente())){
            extrato.setNomeClienteDestino(conta.getCliente().getNome());
        }

        extrato.setCliente(cliente);

        return extratoRepository.save(extrato);
    }

    public Extrato geraExtratoSaque(Float valor, Cliente cliente){

        Extrato extrato = new Extrato();

        extrato.setTipo("Saque");
        extrato.setValor(valor);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String dataFormatada = LocalDate.now().format(formatter);
        extrato.setData(LocalDate.parse(dataFormatada, formatter));

        extrato.setHora(LocalTime.now());

        extrato.setCliente(cliente);

        return extratoRepository.save(extrato);
    }

    public void deletarTodosPorClienteId(Long clienteId){
        extratoRepository.deletarTodosPorClienteId(clienteId);
    }

    public List<Extrato> listarPorData(LocalDate data){
        return extratoRepository.findAllByData(data);
    }

    public List<Extrato> listarPorDataEHora(LocalDate data, LocalTime hora){
        return extratoRepository.findAllByDataAndHora(data, hora);
    }

    public List<Extrato> listarPorClienteId(Long clienteId){
        return extratoRepository.findAllByClienteId(clienteId);
    }

    public List<Extrato> listarPorTipo(String tipo, Long clienteId){
        return extratoRepository.findAllByTipoAndClienteId(tipo, clienteId);
    }

}
