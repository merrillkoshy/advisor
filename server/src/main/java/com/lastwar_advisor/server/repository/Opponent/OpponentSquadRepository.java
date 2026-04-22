package com.lastwar_advisor.server.repository.Opponent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lastwar_advisor.server.entity.Opponent.Opponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquad;

public interface OpponentSquadRepository extends JpaRepository<OpponentSquad, Long> {
    Optional<OpponentSquad> findByOpponentAndSquadNumber(Opponent opponent, Integer squadNumber);

    List<OpponentSquad> findByOpponent(Opponent opponent);

    @Query("SELECT s FROM OpponentSquad s LEFT JOIN FETCH s.slots sl LEFT JOIN FETCH sl.hero WHERE s.opponent = :opponent AND s.squadNumber = :squadNumber")
    Optional<OpponentSquad> findByOpponentAndSquadNumberWithHeroes(@Param("opponent") Opponent opponent,
            @Param("squadNumber") Integer squadNumber);

}
