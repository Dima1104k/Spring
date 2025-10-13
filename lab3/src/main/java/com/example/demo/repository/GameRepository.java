package com.example.demo.repository;

import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import com.example.demo.models.Team;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Repository
public class GameRepository {
    private final Map<Long, Game> gameFake = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(10); // хай буде бо буде біда

    public void deleteById(Long id){
        gameFake.remove(id);
    }
    public List<Game> findAll(){
        return gameFake.values().stream().collect(Collectors.toList());
    }

    public GameRepository(TeamRepository teamRepository) {
        Team team1 = teamRepository.findById(1L).get();
        Team team2 = teamRepository.findById(2L).get();
        Team team3 = teamRepository.findById(3L).get();
        Team team4 = teamRepository.findById(4L).get();

        save(new Game(1L, team1, team2, LocalDateTime.now().plusDays(1).minusHours(1), new GameResult(2, 1)));
        save(new Game(2L, team3, team4, LocalDateTime.now().plusDays(4), new GameResult(4, 5)));
        save(new Game(3L, team3, team1, LocalDateTime.now().plusDays(6), new GameResult(2, 5)));
    }
    public Game save(Game game) {
        if (game.getId() == null) {
            game.setId(idCounter.incrementAndGet());
        }
        gameFake.put(game.getId(), game);
        return game;
    }
    public Optional <Game> findById(Long id){
        return Optional.ofNullable(gameFake.get(id));
    }
    public List<Game> findByTeamName(String team) {
        if (team == null || team.isBlank()) {
            return findAll();
        }
        return gameFake.values().stream()
                .filter(game -> game.getHomeTeam().getName().toLowerCase().contains(team.toLowerCase()) ||
                        game.getAwayTeam().getName().toLowerCase().contains(team.toLowerCase()))
                .toList();
    }
}
