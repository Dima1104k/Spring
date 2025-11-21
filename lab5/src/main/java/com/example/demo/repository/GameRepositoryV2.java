package com.example.demo.repository;

import com.example.demo.models.Game;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface GameRepositoryV2 {
    Game create(Game game);

    Optional<Game> findById(Long id);

    List<Game> findAll();

    int update(Game game);

    void deleteById(Long id);

    List<Game> findByTeamId(Long teamId);
    List<Game> findByTeamName(String teamName);
}
