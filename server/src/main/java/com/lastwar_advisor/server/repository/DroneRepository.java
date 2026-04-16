package com.lastwar_advisor.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.Drone;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findByPlayerId(Long playerId);

}
