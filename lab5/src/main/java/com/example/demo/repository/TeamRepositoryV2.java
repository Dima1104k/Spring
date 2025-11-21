package com.example.demo.repository;

import com.example.demo.models.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepositoryV2 {
    Team create(Team team);

    Optional<Team> findById(Long id);

    List<Team> findAll();

    int update(Team team);

    void deleteById(Long id);

    List<Team> findByName(String name);

}
