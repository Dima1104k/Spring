package com.example.demo.service;

import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import com.example.demo.repository.GameRepositoryV2;
import com.example.demo.repository.TeamRepositoryV2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepositoryV2 gameRepository;

    public GameService(GameRepositoryV2 gameRepository) {
        this.gameRepository = gameRepository;
        System.out.println("ін'єкція через конструктор");
    }

    public List<Game> findGames(String teamName) {
        return gameRepository.findByTeamName(teamName);
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public int updateGame(Game game) {
        if (game.getHomeTeam().getId().equals(game.getAwayTeam().getId())) {
            throw new RuntimeException("Команда не може грати сама з собою!");
        }
        return gameRepository.update(game);
    }

    public Optional<Game> findGameById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> findGamesByTeamId(Long id) {
        return gameRepository.findByTeamId(id);
    }

    public Game createGame(Game game) {
        if (game.getHomeTeam().getId().equals(game.getAwayTeam().getId())) {
            throw new RuntimeException("Команда не може грати сама з собою!");
        }
        return gameRepository.create(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    @Transactional
    public Game updateResult(Long gameId, Integer homeScore, Integer awayScore) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Гра з id=" + gameId + " не знайдена!"));
        if (game.getGameTime().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Неможливо оновити результат до початку гри!");
        }
        game.setResult(new GameResult(homeScore, awayScore));
        gameRepository.update(game);
        return game;
    }
}