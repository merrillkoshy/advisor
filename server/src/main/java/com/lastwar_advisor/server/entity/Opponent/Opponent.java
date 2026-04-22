package com.lastwar_advisor.server.entity.Opponent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Opponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonManagedReference("opponent-squads")
    @OneToMany(mappedBy = "opponent", cascade = CascadeType.ALL)
    private Set<OpponentSquad> squads;

    @OneToOne(mappedBy = "opponent", cascade = CascadeType.ALL)
    @JsonManagedReference("opponent-drone")
    private OpponentDrone drone;

    @OneToMany(mappedBy = "opponent", cascade = CascadeType.ALL)
    @JsonManagedReference("opponent-drone-components")
    private List<OpponentDroneComponent> droneComponents = new ArrayList<>();
}
