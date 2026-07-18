package com.company.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "designations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designation_id")
    private Long designationId;

    @Column(name = "designation_name", nullable = false, unique = true, length = 100)
    private String designationName;

    @Column(nullable = false)
    private Boolean active = true;
}