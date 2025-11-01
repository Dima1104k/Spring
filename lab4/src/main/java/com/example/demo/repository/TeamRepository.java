package com.example.demo.repository;

import com.example.demo.models.Team;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TeamRepository {

    private final Map<Long, Team> teamFake = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(5);

    public Team save(Team team) {
        if (team.getId() == null) {
            team.setId(idCounter.incrementAndGet());
        }
        teamFake.put(team.getId(), team);
        return team;
    }
    public TeamRepository() {
        save(new Team(1L, "Динамо"));
        save(new Team(2L, "Шахтар"));
        save(new Team(3L, "Зоря"));
        save(new Team(4L, "Металіст"));

    }

    public Optional<Team> findById(Long id){
        return Optional.ofNullable(teamFake.get(id));
    }
    public List<Team> findAll(){
        return teamFake.values().stream().collect(Collectors.toList());
    }
}
