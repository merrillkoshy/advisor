package com.lastwar_advisor.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lastwar_advisor.server.entity.GameConstant;
import com.lastwar_advisor.server.repository.GameConstantRepository;

import jakarta.transaction.Transactional;

@Service
public class GameConstantService {
    private final GameConstantRepository gameConstantRepository;

    public GameConstantService(GameConstantRepository gameConstantRepository) {
        this.gameConstantRepository = gameConstantRepository;
    }

    @Transactional
    public List<GameConstant> getAll() {
        return gameConstantRepository.findAll();
    }

}
