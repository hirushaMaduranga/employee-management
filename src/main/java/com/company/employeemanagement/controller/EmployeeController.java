package com.company.employeemanagement.controller;

import com.company.employeemanagement.dto.EmployeeRequest;
import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.exception.ResourceNotFoundException;
import com.company.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String status) {

        return employeeService.searchEmployees(keyword, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.createEmployee(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Employee> updateEmployeeStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                employeeService.updateEmployeeStatus(id, status)
        );
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Employee> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return ResponseEntity.ok(
                employeeService.uploadProfileImage(id, file)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}