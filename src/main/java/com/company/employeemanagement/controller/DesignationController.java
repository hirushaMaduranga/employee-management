package com.company.employeemanagement.controller;

import com.company.employeemanagement.entity.Designation;
import com.company.employeemanagement.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DesignationController {

    private final DesignationService designationService;

    @GetMapping
    public List<Designation> getAllDesignations() {
        return designationService.getAllDesignations();
    }
}