package com.lastwar_advisor.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.engine.scenario.engine.ScenarioEngine;
import com.lastwar_advisor.server.engine.scenario.model.ScenarioRequest;
import com.lastwar_advisor.server.engine.scenario.model.ScenarioResult;

@RestController
@RequestMapping("/advisor")
public class AdvisorController {

    private final ScenarioEngine scenarioEngine;

    public AdvisorController(ScenarioEngine scenarioEngine) {
        this.scenarioEngine = scenarioEngine;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ScenarioResult> analyze(@RequestBody ScenarioRequest request) {
        ScenarioResult result = scenarioEngine.run(request);
        return ResponseEntity.ok(result);
    }
}
