package com.lastwar_advisor.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lastwar_advisor.server.entity.GearLevel;

public interface GearLevelRepository extends JpaRepository<GearLevel, Long> {
    @Query("SELECT gl FROM GearLevel gl JOIN FETCH gl.gear")
    java.util.List<GearLevel> findAllWithRelations();
}
