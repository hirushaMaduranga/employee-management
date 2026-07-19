package com.company.employeemanagement.controller;

import com.company.employeemanagement.entity.Employee;
import com.company.employeemanagement.exception.ResourceNotFoundException;
import com.company.employeemanagement.service.EmployeeService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    private final EmployeeService employeeService;

    // =========================================================
    // ALL EMPLOYEES - CSV
    // =========================================================

    @GetMapping("/employees/csv")
    public ResponseEntity<byte[]> exportEmployeesCsv() {

        List<Employee> employees =
                employeeService.getAllEmployees();

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "Employee Code,First Name,Last Name,Address,NIC,Mobile,Gender,Email,Designation,Date of Birth,Status\n"
        );

        for (Employee employee : employees) {

            csv.append(safe(employee.getEmployeeCode())).append(",");
            csv.append(safe(employee.getFirstName())).append(",");
            csv.append(safe(employee.getLastName())).append(",");
            csv.append(safe(employee.getAddress())).append(",");
            csv.append(safe(employee.getNic())).append(",");
            csv.append(safe(employee.getMobileNo())).append(",");
            csv.append(safe(employee.getGender())).append(",");
            csv.append(safe(employee.getEmail())).append(",");

            csv.append(
                    employee.getDesignation() != null
                            ? safe(
                            employee
                                    .getDesignation()
                                    .getDesignationName()
                    )
                            : ""
            );

            csv.append(",");

            csv.append(
                    employee.getDateOfBirth() != null
                            ? employee
                            .getDateOfBirth()
                            .toString()
                            : ""
            );

            csv.append(",");

            csv.append(
                    safe(
                            employee.getStatus()
                    )
            );

            csv.append("\n");
        }

        byte[] data =
                csv.toString()
                        .getBytes(
                                StandardCharsets.UTF_8
                        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee-report.csv"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "text/csv"
                        )
                )
                .body(data);
    }

    // =========================================================
    // ALL EMPLOYEES - PDF
    // =========================================================

    @GetMapping("/employees/pdf")
    public ResponseEntity<byte[]> exportEmployeesPdf() {

        List<Employee> employees =
                employeeService.getAllEmployees();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document();

        PdfWriter.getInstance(
                document,
                outputStream
        );

        document.open();

        document.add(
                new Paragraph(
                        "Employee Report"
                )
        );

        document.add(
                new Paragraph(" ")
        );

        PdfPTable table =
                new PdfPTable(9);

        table.addCell("Employee Code");
        table.addCell("Name");
        table.addCell("Address");
        table.addCell("NIC");
        table.addCell("Mobile");
        table.addCell("Email");
        table.addCell("Designation");
        table.addCell("Date of Birth");
        table.addCell("Status");

        for (Employee employee : employees) {

            table.addCell(
                    safe(
                            employee.getEmployeeCode()
                    )
            );

            table.addCell(
                    safe(employee.getFirstName())
                            + " "
                            + safe(employee.getLastName())
            );

            table.addCell(
                    safe(
                            employee.getAddress()
                    )
            );

            table.addCell(
                    safe(
                            employee.getNic()
                    )
            );

            table.addCell(
                    safe(
                            employee.getMobileNo()
                    )
            );

            table.addCell(
                    safe(
                            employee.getEmail()
                    )
            );

            table.addCell(
                    employee.getDesignation() != null
                            ? safe(
                            employee
                                    .getDesignation()
                                    .getDesignationName()
                    )
                            : ""
            );

            table.addCell(
                    employee.getDateOfBirth() != null
                            ? employee
                            .getDateOfBirth()
                            .toString()
                            : ""
            );

            table.addCell(
                    safe(
                            employee.getStatus()
                    )
            );
        }

        document.add(table);

        document.close();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee-report.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(
                        outputStream.toByteArray()
                );
    }

    // =========================================================
    // SINGLE EMPLOYEE - PDF
    // =========================================================

    @GetMapping("/employees/{id}/pdf")
    public ResponseEntity<byte[]> exportEmployeePdf(
            @PathVariable Long id
    ) {

        Employee employee =
                employeeService
                        .getEmployeeById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"
                                )
                        );

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document();

        PdfWriter.getInstance(
                document,
                outputStream
        );

        document.open();

        document.add(
                new Paragraph(
                        "Employee Detail Report"
                )
        );

        document.add(
                new Paragraph(" ")
        );

        PdfPTable table =
                new PdfPTable(2);

        table.addCell("Employee Code");
        table.addCell(
                safe(
                        employee.getEmployeeCode()
                )
        );

        table.addCell("Full Name");
        table.addCell(
                safe(employee.getFirstName())
                        + " "
                        + safe(employee.getLastName())
        );

        table.addCell("Address");
        table.addCell(
                safe(
                        employee.getAddress()
                )
        );

        table.addCell("NIC");
        table.addCell(
                safe(
                        employee.getNic()
                )
        );

        table.addCell("Mobile No");
        table.addCell(
                safe(
                        employee.getMobileNo()
                )
        );

        table.addCell("Gender");
        table.addCell(
                safe(
                        employee.getGender()
                )
        );

        table.addCell("Email");
        table.addCell(
                safe(
                        employee.getEmail()
                )
        );

        table.addCell("Designation");
        table.addCell(
                employee.getDesignation() != null
                        ? safe(
                        employee
                                .getDesignation()
                                .getDesignationName()
                )
                        : ""
        );

        table.addCell("Date of Birth");
        table.addCell(
                employee.getDateOfBirth() != null
                        ? employee
                        .getDateOfBirth()
                        .toString()
                        : ""
        );

        table.addCell("Status");
        table.addCell(
                safe(
                        employee.getStatus()
                )
        );

        document.add(table);

        document.close();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee-"
                                + safe(employee.getEmployeeCode())
                                + ".pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(
                        outputStream.toByteArray()
                );
    }

    // =========================================================
    // SINGLE EMPLOYEE - EXCEL STYLE CSV
    // =========================================================

    @GetMapping("/employees/{id}/excel")
    public ResponseEntity<byte[]> exportEmployeeExcel(
            @PathVariable Long id
    ) {

        Employee employee =
                employeeService
                        .getEmployeeById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"
                                )
                        );

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "Field,Value\n"
        );

        csv.append(
                "Employee Code,"
        ).append(
                safe(
                        employee.getEmployeeCode()
                )
        ).append("\n");

        csv.append(
                "Full Name,"
        ).append(
                safe(employee.getFirstName())
                        + " "
                        + safe(employee.getLastName())
        ).append("\n");

        csv.append(
                "Address,"
        ).append(
                safe(
                        employee.getAddress()
                )
        ).append("\n");

        csv.append(
                "NIC,"
        ).append(
                safe(
                        employee.getNic()
                )
        ).append("\n");

        csv.append(
                "Mobile No,"
        ).append(
                safe(
                        employee.getMobileNo()
                )
        ).append("\n");

        csv.append(
                "Gender,"
        ).append(
                safe(
                        employee.getGender()
                )
        ).append("\n");

        csv.append(
                "Email,"
        ).append(
                safe(
                        employee.getEmail()
                )
        ).append("\n");

        csv.append(
                "Designation,"
        ).append(
                employee.getDesignation() != null
                        ? safe(
                        employee
                                .getDesignation()
                                .getDesignationName()
                )
                        : ""
        ).append("\n");

        csv.append(
                "Date of Birth,"
        ).append(
                employee.getDateOfBirth() != null
                        ? employee
                        .getDateOfBirth()
                        .toString()
                        : ""
        ).append("\n");

        csv.append(
                "Status,"
        ).append(
                safe(
                        employee.getStatus()
                )
        ).append("\n");

        byte[] data =
                csv.toString()
                        .getBytes(
                                StandardCharsets.UTF_8
                        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee-"
                                + safe(employee.getEmployeeCode())
                                + ".csv"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "text/csv"
                        )
                )
                .body(data);
    }

    private String safe(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace(",", " ")
                .replace("\n", " ")
                .replace("\r", " ");
    }
}