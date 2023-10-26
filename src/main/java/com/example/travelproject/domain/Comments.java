package com.example.travelproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity(name = "comments")
public class Comments {
    @Id
    @SequenceGenerator(name = "seq_comments", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_comments", strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(name = "text")
    private String text;
    @ManyToOne
    private Users users;
}
