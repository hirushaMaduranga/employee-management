package com.company.employeemanagement.controller;

import com.company.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final EmployeeService employeeService;

    @GetMapping("/summary")
    public Map<String, Long> getDashboardSummary() {

        return Map.of(
                "totalEmployees", employeeService.getTotalEmployeeCount(),
                "activeEmployees", employeeService.getActiveEmployeeCount(),
                "inactiveEmployees", employeeService.getInactiveEmployeeCount()
        );
    }
}