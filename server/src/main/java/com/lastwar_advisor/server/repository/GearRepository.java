package com.lastwar_advisor.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lastwar_advisor.server.entity.Gear;

public interface GearRepository extends JpaRepository<Gear, Long> {
    @Query("SELECT DISTINCT g FROM Gear g LEFT JOIN FETCH g.stats LEFT JOIN FETCH g.levels")
    List<Gear> findAllWithStatsAndLevels();

}
