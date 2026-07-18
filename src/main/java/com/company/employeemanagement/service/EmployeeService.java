package com.company.employeemanagement.service;

import com.company.employeemanagement.dto.EmployeeRequest;
import com.company.employeemanagement.entity.Designation;
import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.repository.DesignationRepository;
import com.company.employeemanagement.repository.EmployeeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(EmployeeRequest request) {

        Designation designation = designationRepository
                .findById(request.getDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setAddress(request.getAddress());
        employee.setNic(request.getNic());
        employee.setMobileNo(request.getMobileNo());
        employee.setGender(request.getGender());
        employee.setEmail(request.getEmail());
        employee.setDesignation(designation);
        employee.setProfileImagePath(request.getProfileImagePath());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setStatus(request.getStatus());
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeRequest request) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Designation designation = designationRepository
                .findById(request.getDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setAddress(request.getAddress());
        employee.setNic(request.getNic());
        employee.setMobileNo(request.getMobileNo());
        employee.setGender(request.getGender());
        employee.setEmail(request.getEmail());
        employee.setDesignation(designation);
        employee.setProfileImagePath(request.getProfileImagePath());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setStatus(request.getStatus());
        employee.setUpdatedAt(LocalDateTime.now());

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

    public List<Employee> searchEmployees(String keyword, String status) {

        Specification<Employee> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    if (keyword != null && !keyword.trim().isEmpty()) {

                        String searchValue =
                                "%" + keyword.toLowerCase().trim() + "%";

                        predicates.add(
                                criteriaBuilder.or(
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(root.get("employeeCode")),
                                                searchValue
                                        ),
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(root.get("nic")),
                                                searchValue
                                        ),
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(root.get("firstName")),
                                                searchValue
                                        ),
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(root.get("lastName")),
                                                searchValue
                                        )
                                )
                        );
                    }

                    if (status != null
                            && !status.isBlank()
                            && !status.equalsIgnoreCase("ALL")) {

                        predicates.add(
                                criteriaBuilder.equal(
                                        criteriaBuilder.upper(root.get("status")),
                                        status.toUpperCase()
                                )
                        );
                    }

                    return criteriaBuilder.and(
                            predicates.toArray(new Predicate[0])
                    );
                };

        return employeeRepository.findAll(specification);
    }
}