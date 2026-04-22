package com.lastwar_advisor.server.entity.Opponent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.SlotPosition;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class OpponentSquadSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer slotIndex;

    private Integer gunStars;
    private Integer armorStars;
    private Integer chipStars;
    private Integer radarStars;

    @Enumerated(EnumType.STRING)
    private SlotPosition position;

    @JsonBackReference("opponent-squad-slots")
    @ManyToOne
    @JoinColumn(name = "squad_id")
    private OpponentSquad squad;

    @ManyToOne
    @JoinColumn(name = "hero_id")
    @JsonIgnoreProperties("slots")
    private Hero hero;
}
