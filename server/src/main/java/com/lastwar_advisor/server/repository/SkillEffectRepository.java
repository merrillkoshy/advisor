package com.lastwar_advisor.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lastwar_advisor.server.entity.SkillEffect;

public interface SkillEffectRepository extends JpaRepository<SkillEffect, Long> {
    Optional<SkillEffect> findBySkillIdAndStatKeyIdAndLevel(Long skillId, Long statKeyId, Integer level);

    @Query("SELECT se FROM SkillEffect se JOIN FETCH se.skill s JOIN FETCH s.hero JOIN FETCH se.statKey")
    List<SkillEffect> findAllWithRelations();
}