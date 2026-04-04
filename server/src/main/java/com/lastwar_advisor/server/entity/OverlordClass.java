package com.lastwar_advisor.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OverlordClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Integer level;

    private String className;
    private Integer overlordAtk;
    private Integer overlordDef;
    private Integer overlordHp;
    private Double overlordHpBoost;
    private Double overlordAtkBoost;
    private Double overlordDefBoost;
}