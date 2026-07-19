package com.company.employeemanagement.repository;

import com.company.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByNic(String nic);

    boolean existsByEmail(String email);

    boolean existsByNicAndEmployeeIdNot(
            String nic,
            Long employeeId
    );

    boolean existsByEmailAndEmployeeIdNot(
            String email,
            Long employeeId
    );

    long countByStatusIgnoreCase(
            String status
    );

    Optional<Employee> findByEmployeeCode(
            String employeeCode
    );
}