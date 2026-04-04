package com.lastwar_advisor.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.service.HeroService;

@RestController
@RequestMapping("/heroes")
public class HeroController {

    private final HeroService service;

    public HeroController(HeroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Hero> getHeroes() {
        return service.getAll();
    }
}
