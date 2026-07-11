package com.lastwar_advisor.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.DroneSkillTier;

public interface DroneSkillTierRepository extends JpaRepository<DroneSkillTier, Long> {
    Optional<DroneSkillTier> findByStarTier(Integer starTier);

}
