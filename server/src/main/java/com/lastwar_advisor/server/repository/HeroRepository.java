package com.lastwar_advisor.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lastwar_advisor.server.entity.Hero;

public interface HeroRepository extends JpaRepository<Hero, Long> {

}
