package com.example.demo.controller;

import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import com.example.demo.models.Team;
import com.example.demo.service.GameService;
import com.example.demo.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final GameService gameService;
    private final TeamService teamService;

    public AdminController(GameService gameService, TeamService teamService) {
        this.gameService = gameService;
        this.teamService = teamService;
    }

    @GetMapping("/games")
    public String listGames(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "admin/list";
    }

    @GetMapping("/games/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        return "admin/game-form";
    }

    @PostMapping("/games/save")
    public String saveGame(@RequestParam Long homeTeamId, @RequestParam Long awayTeamId, @RequestParam LocalDateTime gameTime) {
        Team homeTeam = teamService.findTeamById(homeTeamId).orElseThrow();
        Team awayTeam = teamService.findTeamById(awayTeamId).orElseThrow();

        Game game = new Game(null, homeTeam, awayTeam, gameTime);
        gameService.saveGame(game);

        return "redirect:/admin/games";
    }

    @GetMapping("/games/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return "redirect:/admin/games";
    }

    @GetMapping("/games/result/{id}")
    public String showResultForm(@PathVariable Long id, Model model) {
        Game game = gameService.findGameById(id).orElseThrow();
        model.addAttribute("game", game);
        if (game.getResult() != null) {
            model.addAttribute("result", game.getResult());
        }
        else {
            model.addAttribute("result", new GameResult());
        }
        return "admin/result-form";
    }


    @PostMapping("/games/result/{id}")
    public String saveResult(@PathVariable Long id, @ModelAttribute GameResult result) {
        gameService.updateResult(id, result);
        return "redirect:/admin/games";
    }
}