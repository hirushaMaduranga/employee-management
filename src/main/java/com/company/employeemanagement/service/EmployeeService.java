package com.company.employeemanagement.service;

import com.company.employeemanagement.dto.EmployeeRequest;
import com.company.employeemanagement.entity.Designation;
import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.exception.ResourceAlreadyExistsException;
import com.company.employeemanagement.exception.ResourceNotFoundException;
import com.company.employeemanagement.repository.DesignationRepository;
import com.company.employeemanagement.repository.EmployeeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ResourceAlreadyExistsException(
                    "Employee code already exists"
            );
        }

        if (employeeRepository.existsByNic(request.getNic())) {
            throw new ResourceAlreadyExistsException(
                    "NIC already exists"
            );
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already exists"
            );
        }

        Designation designation = designationRepository
                .findById(request.getDesignationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Designation not found"
                        )
                );

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
        employee.setStatus(request.getStatus().toUpperCase());
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeRequest request) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"
                        )
                );

        if (employeeRepository.existsByNicAndEmployeeIdNot(
                request.getNic(), id)) {

            throw new ResourceAlreadyExistsException(
                    "NIC already exists"
            );
        }

        if (employeeRepository.existsByEmailAndEmployeeIdNot(
                request.getEmail(), id)) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists"
            );
        }

        Designation designation = designationRepository
                .findById(request.getDesignationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Designation not found"
                        )
                );

        // These fields are intentionally not updated:
        // employeeCode
        // gender
        // dateOfBirth

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setAddress(request.getAddress());
        employee.setNic(request.getNic());
        employee.setMobileNo(request.getMobileNo());
        employee.setEmail(request.getEmail());
        employee.setDesignation(designation);
        employee.setProfileImagePath(request.getProfileImagePath());
        employee.setStatus(request.getStatus().toUpperCase());
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    public Employee updateEmployeeStatus(Long id, String status) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"
                        )
                );

        if (!status.equalsIgnoreCase("ACTIVE")
                && !status.equalsIgnoreCase("INACTIVE")) {

            throw new IllegalArgumentException(
                    "Status must be ACTIVE or INACTIVE"
            );
        }

        employee.setStatus(status.toUpperCase());
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    public Employee uploadProfileImage(
            Long id,
            MultipartFile file) throws IOException {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"
                        )
                );

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Profile image is required"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null
                || (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png"))) {

            throw new IllegalArgumentException(
                    "Only JPG and PNG images are allowed"
            );
        }

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null
                && originalFilename.contains(".")) {

            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        employee.setProfileImagePath(
                filePath.toString()
        );

        employee.setUpdatedAt(
                LocalDateTime.now()
        );

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Employee not found"
            );
        }

        employeeRepository.deleteById(id);
    }

    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }

    public long getActiveEmployeeCount() {
        return employeeRepository
                .countByStatusIgnoreCase("ACTIVE");
    }

    public long getInactiveEmployeeCount() {
        return employeeRepository
                .countByStatusIgnoreCase("INACTIVE");
    }

    public List<Employee> searchEmployees(
            String keyword,
            String status) {

        Specification<Employee> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates =
                            new ArrayList<>();

                    if (keyword != null
                            && !keyword.trim().isEmpty()) {

                        String searchValue =
                                "%" +
                                        keyword
                                                .toLowerCase()
                                                .trim()
                                        + "%";

                        predicates.add(
                                criteriaBuilder.or(
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(
                                                        root.get("employeeCode")
                                                ),
                                                searchValue
                                        ),
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(
                                                        root.get("nic")
                                                ),
                                                searchValue
                                        ),
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(
                                                        root.get("firstName")
                                                ),
                                                searchValue
                                        ),
                                        criteriaBuilder.like(
                                                criteriaBuilder.lower(
                                                        root.get("lastName")
                                                ),
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
                                        criteriaBuilder.upper(
                                                root.get("status")
                                        ),
                                        status.toUpperCase()
                                )
                        );
                    }

                    return criteriaBuilder.and(
                            predicates.toArray(
                                    new Predicate[0]
                            )
                    );
                };

        return employeeRepository.findAll(specification);
    }
}