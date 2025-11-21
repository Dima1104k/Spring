package com.example.demo.controller;

import com.example.demo.models.Team;
import com.example.demo.service.TeamService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")

public class TeamRestController implements TeamRestControllerApi {
    private final TeamService teamService;

    public TeamRestController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.findAllTeams();
        return ResponseEntity.ok(teams);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Optional<Team> team = teamService.findTeamById(id);
        return ResponseEntity.of(team);
    }


    @Override
    @GetMapping("/search")
    public ResponseEntity<List<Team>> searchTeams(@RequestParam(required = false) String name) {
        List<Team> teams = teamService.findTeamsByName(name);
        return ResponseEntity.ok(teams);
    }

    @Override
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        if (team.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Team createdTeam = teamService.createTeam(team);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTeam.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTeam);
    }


    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        Optional<Team> existingGame = teamService.findTeamById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        if (team.getId() != null && !team.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Team> existingGame = teamService.findTeamById(id);
        if (existingGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        team.setId(id);
        teamService.updateTeam(team);
        return ResponseEntity.ok(team);
    }

    @Override
    @DeleteMapping("/{id}/with-games")
    public ResponseEntity<Void> deleteTeamWithGames(@PathVariable Long id) {
        Optional<Team> existingTeam = teamService.findTeamById(id);
        if (existingTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        teamService.deleteTeamWithGames(id);
        return ResponseEntity.noContent().build();
    }
}
