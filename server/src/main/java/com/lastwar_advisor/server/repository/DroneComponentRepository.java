package com.lastwar_advisor.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.DroneComponentStat;

public interface DroneComponentRepository extends JpaRepository<DroneComponent, Long> {
    Optional<DroneComponent> findByName(String name);

    @Query("SELECT d FROM DroneComponent d LEFT JOIN FETCH d.stats st WHERE st.id = :componentId")
    List<DroneComponentStat> findComponentStats(@Param("componentId") Long componentId);

}