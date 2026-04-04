package com.lastwar_advisor.server.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    private String description;
    private Integer maxLevel;

    @ManyToMany
    @JoinTable(name = "drone_component_stat_key", joinColumns = @JoinColumn(name = "drone_component_id"), inverseJoinColumns = @JoinColumn(name = "stat_key_id"))
    private List<StatKey> statKeys;
}