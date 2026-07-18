package com.company.employeemanagement.repository;

import com.company.employeemanagement.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
}