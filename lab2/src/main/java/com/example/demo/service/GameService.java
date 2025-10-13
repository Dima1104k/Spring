package com.example.demo.service;


import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import com.example.demo.models.Team;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;


    //ін'єкція через конструктор
    public GameService(GameRepository gameRepository, TeamRepository teamRepository) {
        this.gameRepository = gameRepository;
        System.out.println("ін'єкція через конструктор");
    }
    public List<Game> findGames(String teamName) {

        return gameRepository.findByTeamName(teamName);
    }
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }


    public Optional<Game> findGameById(Long id) {
        return gameRepository.findById(id);
    }


    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
    public void updateResult(Long gameId, GameResult result) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        gameOptional.ifPresentOrElse(game -> {
           game.setResult(result);
           gameRepository.save(game);
        },
          ()-> {
            throw new RuntimeException("Гра з id=" + gameId + " не знайдена!");
        }
        );
    }
}
