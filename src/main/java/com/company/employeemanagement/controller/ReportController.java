package com.company.employeemanagement.controller;

import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    private final EmployeeService employeeService;

    @GetMapping("/employees/csv")
    public ResponseEntity<byte[]> exportEmployeesCsv() {

        List<Employee> employees =
                employeeService.getAllEmployees();

        StringBuilder csv = new StringBuilder();

        csv.append(
                "Employee Code,First Name,Last Name,NIC,Mobile,Gender,Email,Designation,Date of Birth,Status\n"
        );

        for (Employee employee : employees) {

            csv.append(employee.getEmployeeCode()).append(",");
            csv.append(employee.getFirstName()).append(",");
            csv.append(employee.getLastName()).append(",");
            csv.append(employee.getNic()).append(",");
            csv.append(employee.getMobileNo()).append(",");
            csv.append(employee.getGender()).append(",");
            csv.append(employee.getEmail()).append(",");

            if (employee.getDesignation() != null) {
                csv.append(
                        employee.getDesignation()
                                .getDesignationName()
                );
            }

            csv.append(",");
            csv.append(employee.getDateOfBirth()).append(",");
            csv.append(employee.getStatus()).append("\n");
        }

        byte[] data = csv
                .toString()
                .getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee-report.csv"
                )
                .contentType(
                        MediaType.parseMediaType("text/csv")
                )
                .body(data);
    }
}