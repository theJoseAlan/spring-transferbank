package com.spring.tb.domain.repository;

import com.spring.tb.domain.model.Extrato;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Long> {


    List<Extrato> findAllByClienteId(Long clienteId);

    List<Extrato> findAllByTipoAndClienteId(String tipo, Long clienteId);

    List<Extrato> findAllByData(LocalDate data);

    List<Extrato> findAllByDataAndHora(LocalDate data, LocalTime hora);

    @Modifying
    @Transactional
    @Query("DELETE FROM Extrato e WHERE e.cliente.id = :clienteId")
    void deletarTodosPorClienteId(@Param("clienteId") Long clienteId);

}
