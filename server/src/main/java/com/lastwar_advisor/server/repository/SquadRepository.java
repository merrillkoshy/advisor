package com.lastwar_advisor.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lastwar_advisor.server.entity.Squad;
import com.lastwar_advisor.server.entity.Player.Player;

public interface SquadRepository extends JpaRepository<Squad, Long> {
    Optional<Squad> findByPlayerAndSquadNumber(Player player, Integer squadNumber);

    List<Squad> findByPlayer(Player player);

    @Query("SELECT s FROM Squad s LEFT JOIN FETCH s.slots sl LEFT JOIN FETCH sl.hero WHERE s.player = :player AND s.squadNumber = :squadNumber")
    Optional<Squad> findByPlayerAndSquadNumberWithHeroes(@Param("player") Player player,
            @Param("squadNumber") Integer squadNumber);

}
