package com.lastwar_advisor.server.repository.Opponent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.SlotPosition;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquad;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquadSlot;

public interface OpponentSquadSlotRepository extends JpaRepository<OpponentSquadSlot, Long> {
    Optional<OpponentSquadSlot> findBySquadAndPositionAndSlotIndex(OpponentSquad squad, SlotPosition position,
            Integer slotIndex);
}
