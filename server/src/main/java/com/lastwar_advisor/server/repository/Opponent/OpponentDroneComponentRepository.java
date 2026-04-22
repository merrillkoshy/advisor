package com.lastwar_advisor.server.repository.Opponent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.Opponent.Opponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentDroneComponent;

public interface OpponentDroneComponentRepository extends JpaRepository<OpponentDroneComponent, Long> {
    List<OpponentDroneComponent> findByOpponent(Opponent opponent);

    Optional<OpponentDroneComponent> findByOpponentAndDroneComponent(Opponent opponent, DroneComponent droneComponent);

    @Query("SELECT odc FROM OpponentDroneComponent odc JOIN FETCH odc.droneComponent dc JOIN FETCH dc.stats WHERE odc.opponent = :opponent")
    List<OpponentDroneComponent> findByOpponentWithComponents(@Param("opponent") Opponent opponent);

}
