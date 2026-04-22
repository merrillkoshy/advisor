package com.lastwar_advisor.server.entity.Opponent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "drones")
public class OpponentDrone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "opponent_id", unique = true)
    @JsonBackReference("opponent-drone")
    private Opponent opponent;

    private Integer level;

    @JsonProperty("starTier")
    public Integer getStarTier() {
        if (level < 30)
            return 0;
        if (level < 50)
            return 1;
        if (level < 70)
            return 2;
        if (level < 90)
            return 3;
        if (level < 110)
            return 4;
        return 5;
    }

    @JsonProperty("title")
    public String getTitle() {
        return switch (getStarTier()) {
            case 0 -> "TD-1 Pathfinder";
            case 1 -> "TD-2 Blaster";
            case 2 -> "TD-3 Silver Knight";
            case 3 -> "TD-4 Phantom";
            case 4 -> "TD-5 Destroyer";
            default -> "TD-6 Colossus";
        };
    }
}