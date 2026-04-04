package com.lastwar_advisor.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GearLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer level;
    private Double value;

    @ManyToOne
    @JoinColumn(name = "stat_key_id")
    private StatKey statKey;

    @ManyToOne
    @JoinColumn(name = "gear_id")
    private Gear gear;

}
