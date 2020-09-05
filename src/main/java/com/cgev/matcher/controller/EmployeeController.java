package com.cgev.matcher.controller;

import com.cgev.matcher.dto.Employee;
import com.cgev.matcher.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService service;

    EmployeeController(EmployeeService service) {
        this.service = service;
    }
    @PostMapping("/v1/process_employees")
    public List<Employee> processEmployees(@RequestParam("file") MultipartFile file){
        return service.processEmployees(file);
    }
}
