package com.lastwar_advisor.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.DroneMilestone;

public interface DroneMilestoneRepository extends JpaRepository<DroneMilestone, Long> {
    List<DroneMilestone> findByUnlockLevelLessThanEqual(Integer level);
}
