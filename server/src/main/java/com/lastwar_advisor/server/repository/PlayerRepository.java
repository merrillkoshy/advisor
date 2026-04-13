package com.lastwar_advisor.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
