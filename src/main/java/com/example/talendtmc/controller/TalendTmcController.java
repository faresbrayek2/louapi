package com.example.talendtmc.controller;

import com.example.talendtmc.service.TalendTmcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/talend")
public class TalendTmcController {

    @Autowired
    private TalendTmcService talendTmcService;

    @GetMapping("/plans")
    public List<Map<String, Object>> listPlans() {
        return talendTmcService.listPlans();
    }

    @PostMapping("/plans/{planId}/execute")
    public String executePlan(@PathVariable String planId) {
        return talendTmcService.executePlan(planId);
    }

    @GetMapping("/executions/{executionId}/status")
    public Map<String, Object> getJobStatus(@PathVariable String executionId) {
        return talendTmcService.getJobStatus(executionId);
    }
}
