package com.company.employeemanagement.repository;

import com.company.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    long countByStatusIgnoreCase(String status);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByNic(String nic);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCodeAndEmployeeIdNot(
            String employeeCode,
            Long employeeId
    );

    boolean existsByNicAndEmployeeIdNot(
            String nic,
            Long employeeId
    );

    boolean existsByEmailAndEmployeeIdNot(
            String email,
            Long employeeId
    );
}