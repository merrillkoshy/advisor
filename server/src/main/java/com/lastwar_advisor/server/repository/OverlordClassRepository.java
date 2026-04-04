package com.lastwar_advisor.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.OverlordClass;

public interface OverlordClassRepository extends JpaRepository<OverlordClass, Long> {
    Optional<OverlordClass> findByLevel(Integer level);
}