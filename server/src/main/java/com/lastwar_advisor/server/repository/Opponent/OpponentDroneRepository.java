package com.lastwar_advisor.server.repository.Opponent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.Opponent.OpponentDrone;

public interface OpponentDroneRepository extends JpaRepository<OpponentDrone, Long> {
    Optional<OpponentDrone> findByOpponentId(Long opponentId);
}
