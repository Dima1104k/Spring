package com.example.demo.controller;

import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import com.example.demo.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameRestController implements GameRestControllerApi {
    private final GameService gameService;


    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.findAllGames();
        return ResponseEntity.ok(games);
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGames(@RequestParam(required = false) String teamName) {
        return ResponseEntity.ok(gameService.findGames(teamName));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Optional<Game> game = gameService.findGameById(id);
        return ResponseEntity.of(game);
    }

    @Override
    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        if (game.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        Game savegame = gameService.createGame(game);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savegame.getId())
                .toUri();
        return ResponseEntity.created(location).body(savegame);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game gameDto) {
        if (gameDto.getId() != null && !gameDto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Game> existingGame = gameService.findGameById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        gameDto.setId(id);
        gameService.updateGame(gameDto);
        return ResponseEntity.ok(gameDto);
    }

    @Override
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Game>> getGamesByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(gameService.findGamesByTeamId(teamId));
    }

    @Override
    @PatchMapping("/{id}/result")
    public ResponseEntity<Game> updateGameResult(@PathVariable Long id,
                                                 @RequestBody GameResult result) {
        Game updated = gameService.updateResult(id, result.getHomeScore(), result.getAwayScore());
        return ResponseEntity.ok(updated);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        Optional<Game> existingGame = gameService.findGameById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
