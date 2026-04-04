package com.lastwar_advisor.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.StatKey;

public interface StatKeyRepository extends JpaRepository<StatKey, Long> {
    Optional<StatKey> findByKey(String key);

}