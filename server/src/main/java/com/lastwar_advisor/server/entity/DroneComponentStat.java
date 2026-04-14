package com.lastwar_advisor.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "drone_component_stats")
public class DroneComponentStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drone_component_id")
    @JsonBackReference("component-stats")
    private DroneComponent droneComponent;

    @ManyToOne
    @JoinColumn(name = "stat_key_id")
    private StatKey statKey;

    private Integer unlockLevel;
    private Double baseValue;
    private Double increment;

}