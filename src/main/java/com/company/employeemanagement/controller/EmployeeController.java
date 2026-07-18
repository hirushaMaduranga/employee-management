package com.company.employeemanagement.controller;

import com.company.employeemanagement.dto.EmployeeRequest;
import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        Employee createdEmployee =
                employeeService.createEmployee(request);

        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        Employee updatedEmployee =
                employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        if (employeeService.getEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}