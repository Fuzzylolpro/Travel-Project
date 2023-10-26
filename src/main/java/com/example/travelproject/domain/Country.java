package com.example.travelproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity(name = "country")
public class Country {
    @Id
    @SequenceGenerator(name = "seq_country", sequenceName = "country_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_country", strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(name = "country_name")
    private String countryName;
}

