package com.lastwar_advisor.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.SlotPosition;
import com.lastwar_advisor.server.entity.Squad;
import com.lastwar_advisor.server.entity.SquadSlot;

public interface SquadSlotRepository extends JpaRepository<SquadSlot, Long> {
    Optional<SquadSlot> findBySquadAndPositionAndSlotIndex(Squad squad, SlotPosition position, Integer slotIndex);
}
