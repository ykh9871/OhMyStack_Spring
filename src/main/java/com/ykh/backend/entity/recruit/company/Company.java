package com.ykh.backend.entity.recruit.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_company_id", nullable = false)
    private MainCompany mainCompany;

    private String innerCompany;
    private String address;
    private Double latitude;
    private Double longitude;
}