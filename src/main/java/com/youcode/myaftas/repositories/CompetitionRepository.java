package com.youcode.myaftas.repositories;

import com.youcode.myaftas.entities.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, String> {
    Optional<Competition> findByCode(String code);
}
