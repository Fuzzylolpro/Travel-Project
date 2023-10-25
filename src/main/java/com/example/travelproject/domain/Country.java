package com.example.travelproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
@Entity(name = "country")
public class Country {
    @Id
    @SequenceGenerator(name = "seq_country", sequenceName = "country_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_country", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(min = 2, max = 20)
    @NotNull
    @Column(name = "country_name")
    private String CountryName;
}

