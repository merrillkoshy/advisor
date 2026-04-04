package com.lastwar_advisor.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lastwar_advisor.server.entity.GearStat;

public interface GearStatRepository extends JpaRepository<GearStat, Long> {
    @Query("SELECT gs FROM GearStat gs JOIN FETCH gs.gear JOIN FETCH gs.statKey")
    java.util.List<GearStat> findAllWithRelations();

}
