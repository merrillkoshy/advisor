package com.lastwar_advisor.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.repository.HeroRepository;

@Service
public class HeroService {
    private final HeroRepository repo;

    public HeroService(HeroRepository repo) {
        this.repo = repo;
    }

    public List<Hero> getAll() {
        return repo.findAll();
    }
}
