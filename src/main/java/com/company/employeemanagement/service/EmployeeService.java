package com.company.employeemanagement.service;

import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.repository.EmployeeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Employee> searchEmployees(String keyword, String status) {

        Specification<Employee> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {

                String searchValue = "%" + keyword.toLowerCase().trim() + "%";

                Predicate employeeCodeMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("employeeCode")),
                        searchValue
                );

                Predicate nicMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nic")),
                        searchValue
                );

                Predicate firstNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        searchValue
                );

                Predicate lastNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        searchValue
                );

                predicates.add(
                        criteriaBuilder.or(
                                employeeCodeMatch,
                                nicMatch,
                                firstNameMatch,
                                lastNameMatch
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