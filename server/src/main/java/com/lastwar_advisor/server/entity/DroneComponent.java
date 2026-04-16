package com.lastwar_advisor.server.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DroneComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String imageUrl;
    private String description;
    private Integer maxLevel;

    @OneToMany(mappedBy = "droneComponent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("component-stats")
    private List<DroneComponentStat> stats = new ArrayList<>();
}