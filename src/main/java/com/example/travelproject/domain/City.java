package com.example.travelproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;
@Data
@Component
@Entity(name = "cities")
public class City {
        @Id
        @SequenceGenerator(name = "seq_city", sequenceName = "city_id_seq", allocationSize = 1)
        @GeneratedValue(generator = "seq_city", strategy = GenerationType.SEQUENCE)
        private Long id;
        @ManyToOne
        private Country CountryName;
}
