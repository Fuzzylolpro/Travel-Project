package com.example.travelproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@Entity(name = "attractions")
public class Attractions {
    @Id
    @SequenceGenerator(name = "seq_attractions", sequenceName = "attractions_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_attractions", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(min = 2, max = 40)
    @NotNull
    @Column(name = "attractions_name")
    private String attractionsName;
    @Column(name = "attraction_info")
    private String attractionsInfo;
    @ManyToOne
    private City cityName;
    @OneToMany(mappedBy = "attractions")
    private List<Comments> comments = new ArrayList<>();

}
