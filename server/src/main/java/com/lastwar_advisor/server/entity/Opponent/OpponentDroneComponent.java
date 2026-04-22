package com.lastwar_advisor.server.entity.Opponent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lastwar_advisor.server.entity.DroneComponent;

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
@Table(name = "opponent_drone_components")
public class OpponentDroneComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "opponent_id")
    @JsonBackReference("opponent-drone-components")
    private Opponent opponent;

    @ManyToOne
    @JoinColumn(name = "drone_component_id")
    private DroneComponent droneComponent;

    private Integer level;
}
