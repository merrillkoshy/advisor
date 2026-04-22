package com.lastwar_advisor.server.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lastwar_advisor.server.entity.Drone;
import com.lastwar_advisor.server.entity.Squad;

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
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<Squad> squads;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonManagedReference("player-drone")
    private Drone drone;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonManagedReference("player-drone-components")
    private List<PlayerDroneComponent> droneComponents = new ArrayList<>();
}
