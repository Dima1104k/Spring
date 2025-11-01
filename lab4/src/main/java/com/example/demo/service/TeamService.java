package com.example.demo.service;

import com.example.demo.models.Team;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private TeamRepository teamRepository;


    //ін'єкція через сетер
    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        System.out.println("ін'єкція через сетер");
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }
}