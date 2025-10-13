package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class Game {
    private Long id;
    private Team homeTeam;
    private Team awayTeam;
    private LocalDateTime gameTime;
    private GameResult result;

    public Game(Long id, Team homeTeam, Team awayTeam, LocalDateTime gameTime) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameTime = gameTime;
        this.result = null;
    }


}