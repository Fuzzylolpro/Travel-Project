package com.example.travelproject.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Component
@Entity(name = "users")
public class Users {
    @Id
    @SequenceGenerator(name = "seq_users", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seq_users", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(min = 2, max = 20)
    @NotNull
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Max(value = 120)
    @Column(name = "age")
    private Integer age;
    @Column(name = "is_married")
    private Boolean isMarried;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    @CreationTimestamp
    private Timestamp created;
    @ManyToMany
    @JsonBackReference
    @JoinTable(name = "l_user_attraction",joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name = "attractions_id"))
    private Set<Attractions> favoriteAttractions;
}

