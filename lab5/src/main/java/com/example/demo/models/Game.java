package com.example.demo.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель футбольної гри")
public class Game {
    @Schema(description = "Унікальний ідентифікатор гри",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;
    @Schema(description = "Домашня команда",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Team homeTeam;
    @Schema(description = "Гостьова команда",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Team awayTeam;
    @Schema(description = "Дата та час проведення гри",
            example = "2025-01-15T18:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime gameTime;
    @Schema(description = "Результат гри")
    private GameResult result;

    public Game(Long id, Team homeTeam, Team awayTeam, LocalDateTime gameTime) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameTime = gameTime;
        this.result = null;
    }


}