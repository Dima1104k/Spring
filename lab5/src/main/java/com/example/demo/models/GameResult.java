package com.example.demo.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Результат футбольної гри")
public class GameResult {
    @Schema(description = "Рахунок домашньої команди", example = "2")
    private Integer homeScore;
    @Schema(description = "Рахунок гостьової команди", example = "1")
    private Integer awayScore;

    public GameResult() {

    }
    @Schema(
            description = "Загальний рахунок у форматі рядка",
            example = "2 : 1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getScore() {
        return homeScore + " : " + awayScore;
    }
    public void setScore(String score) {
        String[] parts = score.split(" : ");
        if (parts.length == 2) {
            this.homeScore = Integer.parseInt(parts[0].trim());
            this.awayScore = Integer.parseInt(parts[1].trim());
        } else {
            throw new IllegalArgumentException("Невірний формат рахунку: " + score);
        }
    }
}