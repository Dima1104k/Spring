package com.example.demo;

import com.example.demo.repository.GameRepository;
import com.example.demo.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {


    @Autowired
    private GameRepository gameRepository;


    @Autowired
    private TeamRepository teamRepository;

    public ReportService() {
        System.out.println("ін'єкція в поля");
    }

    public String generateSimpleReport() {
        int gamesCount = gameRepository.findAll().size();
        int teamsCount = teamRepository.findAll().size();
        return "всього ігор у системі - " + gamesCount + ", Всього команд - " + teamsCount + ".";
    }

    @PostConstruct
    public void init() {
        System.out.println("   " + generateSimpleReport());
    }
}