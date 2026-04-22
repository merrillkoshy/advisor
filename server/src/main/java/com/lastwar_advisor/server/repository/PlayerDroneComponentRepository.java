package com.lastwar_advisor.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.Player.Player;
import com.lastwar_advisor.server.entity.Player.PlayerDroneComponent;

public interface PlayerDroneComponentRepository extends JpaRepository<PlayerDroneComponent, Long> {
    List<PlayerDroneComponent> findByPlayer(Player player);

    Optional<PlayerDroneComponent> findByPlayerAndDroneComponent(Player player, DroneComponent droneComponent);

    @Query("SELECT pdc FROM PlayerDroneComponent pdc JOIN FETCH pdc.droneComponent dc JOIN FETCH dc.stats WHERE pdc.player = :player")
    List<PlayerDroneComponent> findByPlayerWithComponents(@Param("player") Player player);

}
