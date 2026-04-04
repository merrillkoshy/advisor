package com.lastwar_advisor.server.entity;

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

    @OneToMany(mappedBy = "gear")
    private java.util.List<GearStat> stats;

    @OneToMany(mappedBy = "gear")
    private java.util.List<GearLevel> levels;

}
