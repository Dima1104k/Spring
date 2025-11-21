package com.example.demo.controller;

import com.example.demo.models.Team;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Team API", description = "Операції для керування командами")
public interface TeamRestControllerApi {
    @GetMapping
    @Operation(summary = "Отримати всі команди",
            description = "Повертає список всіх футбольних команд")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команди успішно отримані",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class)))
    })
    ResponseEntity<List<Team>> getAllTeams();

    @GetMapping("/{id}")
    @Operation(summary = "Отримати одну команду за її ID",
            description = "Повертає детальну інформацію про одну конкретну команду за її унікальним ідентифікатором." +
                    "Якщо команда з таким ID не знайдена, повертає статус 404 Not Found",
            parameters = {@Parameter(name = "id", description = "Унікальний ID гри", example = "1", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команда успішно знайдена та повернена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class))),
            @ApiResponse(responseCode = "404", description = "Команда з вказаним ID не знайдена", content = @Content)
    })
    ResponseEntity<Team> getTeamById(@PathVariable Long id);

    @GetMapping("/search")
    @Operation(summary = "Пошук команд за назвою",
            description = "Повертає список команд, які відповідають заданій назві",
            parameters = {@Parameter(name = "name", description = "Назва команди для пошуку", example = "Динамо", required = false)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команди успішно знайдені",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Team.class))))
    })
    ResponseEntity<List<Team>> searchTeams(@RequestParam(required = false) String name);

    @PostMapping
    @Operation(summary = "Створити нову команду",
            description = "Дозволяє створити нову команду, використовуючи дані з тіла запиту. ID для нової команди генерується автоматично. " +
                    "Повертає статус 201 Created, посилання на створений ресурс у заголовку 'Location' та сам створений об'єкт команди",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для створення нової команди.",
                    required = true,
                    content = @Content(mediaType = "application/json")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Команда успішно створена", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Team.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для створення команди", content = @Content)
    })
    ResponseEntity<Team> createTeam(@RequestBody Team team);

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити команду за її ID",
            description = "Видаляє команду, знайдену за її унікальним ідентифікатором. " +
                    "Якщо команда з вказаним ID не існує, повертає статус 404 Not Found. " +
                    "У разі успішного видалення повертає статус 204 No Content",
            parameters = {@Parameter(name = "id", description = "Унікальний ID команди для видалення", example = "1", required = true)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Команда успішно видалена", content = @Content),
            @ApiResponse(responseCode = "404", description = "Команду з вказаним ID не знайдено", content = @Content)
    })
    ResponseEntity<Void> deleteTeam(@PathVariable Long id);

    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити існуючу команду за ID",
            description = "Повністю замінює дані існуючої команди, знайденої за ID в URL, на дані, передані в тілі запиту." +
                    "Якщо команда з вказаним ID не існує, повертає статус 404 Not Found. Якщо ID в тілі запиту не збігається з ID в URL, " +
                    "повертає статус 400 Bad Request. У разі успішного оновлення повертає оновлений об'єкт команди зі статусом 200 OK",
            parameters = @Parameter(name = "id", description = "Унікальний ID команди для оновлення", example = "1", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Нові дані команди для повного оновлення",
                    required = true,
                    content = @Content(mediaType = "application/json"))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команда успішно оновлена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class))),
            @ApiResponse(responseCode = "400", description = "Некоректний запит (наприклад, ID в тілі не збігається з ID в URL)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Команду з вказаним ID не знайдено", content = @Content)
    })
    ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team);

    @DeleteMapping("/{id}/with-games")
    @Operation(summary = "Видалити команду разом з усіма її іграми за її ID",
            description = "Видаляє команду та всі пов'язані з нею ігри, знайдені за її унікальним ідентифікатором. " +
                    "Якщо команда з вказаним ID не існує, повертає статус 404 Not Found. " +
                    "У разі успішного видалення повертає статус 204 No Content",
            parameters = {@Parameter(name = "id", description = "Унікальний ID команди для видалення разом з іграми", example = "1", required = true)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Команда та її ігри успішно видалені", content = @Content),
            @ApiResponse(responseCode = "404", description = "Команду з вказаним ID не знайдено", content = @Content)
    })
    ResponseEntity<Void> deleteTeamWithGames(@PathVariable Long id);
}
