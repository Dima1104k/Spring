package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class GameResult {
    private Integer homeScore;
    private Integer awayScore;

    public GameResult() {

    }

    public String getScore() {
        return homeScore + " : " + awayScore;
    }
}