package com.lastwar_advisor.server.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Gear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer basePower;
    private String baseName;
    private String mythicName;
    private String type;

    @JsonManagedReference("gear-stats")
    @OneToMany(mappedBy = "gear")
    private Set<GearStat> stats;

    @JsonManagedReference("gear-levels")
    @OneToMany(mappedBy = "gear")
    private Set<GearLevel> levels;

}
