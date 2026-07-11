package com.lastwar_advisor.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.GameConstant;

public interface GameConstantRepository extends JpaRepository<GameConstant, String> {
    Optional<GameConstant> findByKey(String key);

    List<GameConstant> findAll();

}
