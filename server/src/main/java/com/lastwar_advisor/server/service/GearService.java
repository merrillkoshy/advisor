package com.lastwar_advisor.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lastwar_advisor.server.entity.Gear;
import com.lastwar_advisor.server.repository.GearRepository;

@Service
public class GearService {
    private final GearRepository repo;

    public GearService(GearRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public List<Gear> getAll() {
        return repo.findAllWithStatsAndLevels();
    }
}
