package com.cgev.matcher.controller;

import com.cgev.matcher.dto.Result;
import com.cgev.matcher.service.MatcherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MatcherController {

    private final MatcherService service;

    MatcherController(MatcherService service) {
        this.service = service;
    }

    /**
     * Matching employees with each other
     * @param file from where we could read input data of employees
     * @return the result of matching with the  best average score
     */
    @PostMapping("/v1/match/employees")
    public Result processEmployees(@RequestParam("file") MultipartFile file){
        return service.matchEmployees(file);
    }
}
