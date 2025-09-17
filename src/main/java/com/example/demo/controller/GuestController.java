package com.example.demo.controller;

import com.example.demo.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuestController {

    private final GameService gameService;

    public GuestController(GameService gameService) {
        this.gameService = gameService;
    }
    @GetMapping({ "/games"})
    public String showSchedule(@RequestParam(required = false) String teamName, Model model) {

        model.addAttribute("games", gameService.findGames(teamName));
        model.addAttribute("teamName", teamName);

        return "schedule";
    }
}
