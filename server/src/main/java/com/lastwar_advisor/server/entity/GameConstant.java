package com.lastwar_advisor.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "game_constants")
public class GameConstant {
    @Id
    private String key;
    private Double value;
    private String description;
}
