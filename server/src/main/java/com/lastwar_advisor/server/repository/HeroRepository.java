package com.lastwar_advisor.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lastwar_advisor.server.entity.Hero;

public interface HeroRepository extends JpaRepository<Hero, Long> {
    @Query("SELECT DISTINCT h FROM Hero h LEFT JOIN FETCH h.skills s LEFT JOIN FETCH s.effects")
    List<Hero> findAllWithSkillsAndEffects();
}
