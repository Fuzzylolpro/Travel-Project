package com.example.travelproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity(name = "cities")
public class City {
    @Id
    @SequenceGenerator(name = "seq_cities", sequenceName = "cities_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_cities", strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(name = "cities_name")
    private String name;
    @ManyToOne
    private Country countryName;
}
