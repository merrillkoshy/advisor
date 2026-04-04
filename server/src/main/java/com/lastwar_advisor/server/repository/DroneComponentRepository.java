package com.lastwar_advisor.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.DroneComponent;

public interface DroneComponentRepository extends JpaRepository<DroneComponent, Long> {
    Optional<DroneComponent> findByName(String name);
}