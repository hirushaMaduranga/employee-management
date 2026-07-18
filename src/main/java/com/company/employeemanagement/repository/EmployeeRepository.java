package com.company.employeemanagement.repository;

import com.company.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    long countByStatusIgnoreCase(String status);
}