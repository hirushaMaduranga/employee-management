package com.company.employeemanagement.service;

import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }

    public long getActiveEmployeeCount() {
        return employeeRepository.countByStatusIgnoreCase("ACTIVE");
    }

    public long getInactiveEmployeeCount() {
        return employeeRepository.countByStatusIgnoreCase("INACTIVE");
    }
}