package com.spring.tb.domain.repository;

import com.spring.tb.domain.model.Extrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Long> {
}
