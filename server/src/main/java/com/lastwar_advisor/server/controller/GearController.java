package com.lastwar_advisor.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.entity.Gear;
import com.lastwar_advisor.server.service.GearService;

@RestController
@RequestMapping("/gears")
public class GearController {
    private final GearService service;

    public GearController(GearService service) {
        this.service = service;
    }

    @GetMapping
    public List<Gear> getGears() {
        return service.getAll();
    }

}
