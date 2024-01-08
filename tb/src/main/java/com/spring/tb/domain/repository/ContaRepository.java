package com.spring.tb.domain.repository;

import com.spring.tb.domain.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumero(int numero);

    Optional<Conta> findByClienteId(Long clienteId);

}
