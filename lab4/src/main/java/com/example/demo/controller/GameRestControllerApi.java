package com.example.demo.controller;

import com.example.demo.models.Game;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Game API", description = "Операції для керування іграми")
public interface GameRestControllerApi {
    @Operation(summary = "Отримати список всіх ігор (з фільтрацією та пагінацією)",
            description = "Повертає сторінку зі списком ігор. Дозволяє фільтрувати за назвою команди " +
                    "(параметр 'teamName') та налаштовувати пагінацію (параметри 'page' та 'size')",
            parameters = {
                    @Parameter(name = "teamName", description = "Назва команди", example = "Динамо"),
                    @Parameter(name = "page", description = "Номер сторінки", example = "0"),
                    @Parameter(name = "size", description = "Кількість ігор на сторінці", example = "10")
            }
    )
    @ApiResponse(responseCode = "200", description = "Сторінка ігор успішно повернена", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping
    ResponseEntity<Page<Game>> getGamesByTeamName(
            @RequestParam(required = false) String teamName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size);

    @Operation(summary = "Отримати одну гру за її ID",
            description = "Повертає детальну інформацію про одну конкретну гру за її унікальним ідентифікатором." +
                    "Якщо гра з таким ID не знайдена, повертає статус 404 Not Found",
            parameters = {@Parameter(name = "id", description = "Унікальний ID гри", example = "1", required = true)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Гра успішно знайдена та повернена"),
            @ApiResponse(responseCode = "404", description = "Гра з вказаним ID не знайдена", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<Game> getGameById(
            @PathVariable Long id);

    @Operation(summary = "Створити нову гру",
            description = "Дозволяє створити нову гру, використовуючи дані з тіла запиту. ID для нової гри генерується автоматично. " +
                    "Повертає статус 201 Created, посилання на створений ресурс у заголовку 'Location' та сам створений об'єкт гри",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Дані для створення нової гри. Потрібно вказати ID існуючих команд.",
                    required = true,
                    content = @Content(mediaType = "application/json")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Гра успішно створена", content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для створення гри", content = @Content)
    })
    @PostMapping
    ResponseEntity<Game> createGame(@RequestBody Game gameDto);

    @Operation(summary = "Повністю оновити існуючу гру за ID",
            description = "Повністю замінює дані існуючої гри, знайденої за ID в URL, на дані, передані в тілі запиту." +
                    "Якщо гра з вказаним ID не існує, повертає статус 404 Not Found. Якщо ID в тілі запиту не збігається з ID в URL, " +
                    "повертає статус 400 Bad Request. У разі успішного оновлення повертає оновлений об'єкт гри зі статусом 200 OK",
            parameters = @Parameter(name = "id", description = "Унікальний ID гри для оновлення", example = "1", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Нові дані гри для повного оновлення",
                    required = true,
                    content = @Content(mediaType = "application/json"))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Гра успішно оновлена",
                    content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Некоректний запит (наприклад, ID в тілі не збігається з ID в URL)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Гру з вказаним ID не знайдено", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game gameDto);

    @Operation(summary = "Частково оновити існуючу гру за ID",
            description = "Дозволяє частково оновити дані існуючої гри, надсилаючи тільки ті поля, які потрібно змінити, у форматі JSON Merge Patch (RFC 7386)." +
                    "Необхідно вказати заголовок 'Content-Type: application/merge-patch+json'." +
                    "Якщо гра з вказаним ID не існує, повертає статус 404. Якщо гра не знайдена, повертає 404. У разі помилки формату патча повертає 400. " +
                    "В іншому випадку повертає оновлений об'єкт гри зі статусом 200 OK.",
            parameters = @Parameter(name = "id", description = "Унікальний ID гри для часткового оновлення", example = "1", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON Merge Patch з полями для оновлення",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json",
                            schema = @Schema(implementation = Game.class))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Гра успішно частково оновлена",
                    content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Некоректний запит (помилка формату патча)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Гру з вказаним ID не знайдено", content = @Content)
    })
    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    ResponseEntity<Game> patch(@PathVariable Long id, @RequestBody JsonMergePatch patch);

    @Operation(summary = "Видалити гру за її ID",
            description = "Дозволяє видалити існуючу гру за її унікальним ідентифікатором. " +
                    "Якщо гра з вказаним ID не існує, повертає статус 404 Not Found. У разі успішного видалення повертає статус 204 No Content",
            parameters = @Parameter(name = "id", description = "Унікальний ID гри для видалення", example = "1", required = true)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Гру успішно видалено", content = @Content),
            @ApiResponse(responseCode = "404", description = "Гру з вказаним ID не знайдено", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteGame(@PathVariable Long id);
}
