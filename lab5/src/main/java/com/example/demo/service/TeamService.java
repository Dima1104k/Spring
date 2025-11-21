package com.example.demo.service;

import com.example.demo.models.Game;
import com.example.demo.models.Team;
import com.example.demo.repository.GameRepositoryV2;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamRepositoryV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private TeamRepositoryV2 teamRepository;
    private GameRepositoryV2 gameRepository;


 /*   //ін'єкція через сетер
    @Autowired
    public void setGameRepository(GameRepositoryV2 gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setTeamRepository(TeamRepositoryV2 teamRepository) {
        this.teamRepository = teamRepository;
    }*/
    //ін'єкція через конструктор


    public TeamService(TeamRepositoryV2 teamRepository, GameRepositoryV2 gameRepository) {
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Team createTeam(Team team) {
        return teamRepository.create(team);
    }

    public int updateTeam(Team team) {
        return teamRepository.update(team);
    }

    public Optional<Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public List<Team> findTeamsByName(String name) {
        return teamRepository.findByName(name);
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Transactional
    public void deleteTeamWithGames(Long teamId) {
        List<Game> games = gameRepository.findByTeamId(teamId);
        for (Game game : games) {
            gameRepository.deleteById(game.getId());
        }
 /*       if (games.size() >= 2) {
            gameRepository.deleteById(games.get(0).getId());
            gameRepository.deleteById(games.get(1).getId());

            System.out.println("ТЕСТ ROLLBACK:Симулюємо помилку");

            throw new RuntimeException("Тестова помилка для демонстрації rollback транзакції!");
        }*/
        teamRepository.deleteById(teamId);
    }
}