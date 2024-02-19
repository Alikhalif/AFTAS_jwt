package com.youcode.myaftas.repositories;

import com.youcode.myaftas.entities.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    @Query("SELECT COALESCE(MAX(l.point), 0) FROM Level l")
    Integer findMaxPoints();
}




