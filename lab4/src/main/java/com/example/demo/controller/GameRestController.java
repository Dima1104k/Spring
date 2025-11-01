package com.example.demo.controller;

import com.example.demo.models.Game;
import com.example.demo.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.*;
 import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")

public class GameRestController implements GameRestControllerApi {
    private GameService gameService;

    private ObjectMapper objectMapper;

    public GameRestController(GameService gameService, ObjectMapper objectMapper) {
        this.gameService = gameService;
        this.objectMapper = objectMapper;
    }

    /*    @GetMapping
        public ResponseEntity<List<Game>> getAllGames() {
            List<Game> games = gameService.getAllGames();
            return ResponseEntity.ok(games);
        }*/
  /*  @GetMapping("/search")
    public ResponseEntity<List<Game>> getGamesByTeamName(@RequestParam(required = false) String teamName) {
        List<Game> games = gameService.findGames(teamName);
        return ResponseEntity.ok(games);
    }*/
    @Override
    @GetMapping
    public ResponseEntity<Page<Game>> getGamesByTeamName(String teamName, int page, int size) {
        List<Game> games = gameService.findGames(teamName);
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), games.size());
        List<Game> pagedGames;
        if (start > games.size()) {
            pagedGames = List.of();
        } else
            pagedGames = games.subList(start, end);

        PageImpl<Game> games1 = new PageImpl<>(pagedGames, pageable, games.size());
        return ResponseEntity.ok(games1);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(Long id) {
        Optional<Game> game = gameService.findGameById(id);
        return ResponseEntity.of(game);
    }

    @Override
    @PostMapping
    public ResponseEntity<Game> createGame(Game gameDto) {
        if (gameDto.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        Game savegame = gameService.saveGame(gameDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savegame.getId())
                .toUri();
        return ResponseEntity.created(location).body(savegame);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(Long id, Game gameDto) {
        if (gameDto.getId() != null && !gameDto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Game> existingGame = gameService.findGameById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        gameDto.setId(id);
        Game updatedGame = gameService.saveGame(gameDto);
        return ResponseEntity.ok(updatedGame);
    }

    @Override
    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Game> patch(Long id, JsonMergePatch patch) {
        Optional<Game> existingGame = gameService.findGameById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Game existingGame1 = existingGame.get();
        try {
            JsonNode patched = objectMapper.convertValue(existingGame1, JsonNode.class);
            patched = patch.apply(patched);
            existingGame1 = objectMapper.treeToValue(patched, Game.class);
            existingGame1 = gameService.saveGame(existingGame1);
            return ResponseEntity.ok(existingGame1);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(Long id) {
        Optional<Game> existingGame = gameService.findGameById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
