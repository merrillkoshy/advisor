package com.lastwar_advisor.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.DroneMilestone;
import com.lastwar_advisor.server.entity.DroneSkillTier;
import com.lastwar_advisor.server.service.DroneService;

@RestController
@RequestMapping("/drone")
public class DroneController {

    public final DroneService service;

    public DroneController(DroneService service) {
        this.service = service;
    }

    @GetMapping("/components")
    public List<DroneComponent> getDroneComponents() {
        return service.getAllComponents();
    }

    @GetMapping("/milestones")
    public List<DroneMilestone> getDroneMilestones() {
        return service.getAllMilestones();
    }

    @GetMapping("/skill-tiers")
    public List<DroneSkillTier> getDroneSkillTiers() {
        return service.getAllSkillTiers();
    }

}
