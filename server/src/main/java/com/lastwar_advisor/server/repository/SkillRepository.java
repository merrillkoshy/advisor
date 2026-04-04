package com.lastwar_advisor.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lastwar_advisor.server.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByHeroIdAndName(Long heroId, String name);

    @Query("SELECT s FROM Skill s JOIN FETCH s.hero")
    List<Skill> findAllWithEffects();
}