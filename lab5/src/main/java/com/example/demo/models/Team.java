package com.example.demo.models;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Schema(description = "Модель футбольної команди")
public class Team {
    @Schema(
            description = "ID команди",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;
    @Schema(description = "Назва команди",
            example = "Шахтар",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;


}