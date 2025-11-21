package com.example.demo.repository;

import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class GameJdbcClientRepository implements GameRepositoryV2 {
    private final JdbcClient jdbcClient;

    private final TeamRepositoryV2 teamRepository;

    public GameJdbcClientRepository(JdbcClient jdbcClient, TeamRepositoryV2 teamRepository) {
        this.jdbcClient = jdbcClient;
        this.teamRepository = teamRepository;
    }

    private RowMapper<Game> GAME_MAPPER() {
        return (rs, rowNum) -> {
            Game game = new Game();
            game.setId(rs.getLong("id"));
            Long homeTeamId = rs.getLong("home_team_id");
            Long awayTeamId = rs.getLong("away_team_id");
            game.setHomeTeam(teamRepository.findById(homeTeamId).orElse(null));
            game.setAwayTeam(teamRepository.findById(awayTeamId).orElse(null));
            game.setGameTime(rs.getTimestamp("game_time").toLocalDateTime());
            Integer homeScore = rs.getObject("home_score", Integer.class);
            Integer awayScore = rs.getObject("away_score", Integer.class);
            if (homeScore != null && awayScore != null) {
                game.setResult(new GameResult(homeScore, awayScore));
            } else {
                game.setResult(null);
            }
            return game;
        };
    }

    String INSERT_GAME = "INSERT INTO games (home_team_id, away_team_id, game_time, home_score, away_score) VALUES (?, ?, ?, ?, ?)";

    @Override
    public Game create(Game game) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(INSERT_GAME)
                .param(game.getHomeTeam().getId())
                .param(game.getAwayTeam().getId())
                .param(Timestamp.valueOf(game.getGameTime()))
                .param(game.getResult() != null ? game.getResult().getHomeScore() : null)
                .param(game.getResult() != null ? game.getResult().getAwayScore() : null)
                .update(keyHolder, "id");
        Long newId = keyHolder.getKey().longValue();
        game.setId(newId);
        return game;
    }

    String FIND_BY_ID = "SELECT * FROM games WHERE id = ?";

    @Override
    public Optional<Game> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID)
                .param(id)
                .query(GAME_MAPPER())
                .optional();
    }

    String FIND_ALL_GAMES = "SELECT * FROM games";

    @Override
    public List<Game> findAll() {
        return jdbcClient.sql(FIND_ALL_GAMES)
                .query(GAME_MAPPER())
                .list();
    }

    String UPDATE_GAME = "UPDATE games SET home_team_id = ?, away_team_id = ?, game_time = ?, home_score = ?, away_score = ? WHERE id = ?";

    @Override
    public int update(Game game) {
        return jdbcClient.sql(UPDATE_GAME)
                .param(game.getHomeTeam().getId())
                .param(game.getAwayTeam().getId())
                .param(Timestamp.valueOf(game.getGameTime()))
                .param(game.getResult() != null ? game.getResult().getHomeScore() : null)
                .param(game.getResult() != null ? game.getResult().getAwayScore() : null)
                .param(game.getId())
                .update();
    }

    String DELETE_GAME = "DELETE FROM games WHERE id = ?";

    @Override
    public void deleteById(Long id) {
        jdbcClient.sql(DELETE_GAME)
                .param(id)
                .update();
    }

    String FIND_BY_TEAM_ID = "SELECT * FROM games WHERE home_team_id = ? OR away_team_id = ?";

    @Override
    public List<Game> findByTeamId(Long teamId) {
        return jdbcClient.sql(FIND_BY_TEAM_ID)
                .param(teamId)
                .param(teamId)
                .query(GAME_MAPPER())
                .list();
    }

    String FIND_BY_TEAM_NAME = """
            SELECT g.* FROM games g
            JOIN teams t1 ON g.home_team_id = t1.id
            JOIN teams t2 ON g.away_team_id = t2.id
            WHERE LOWER(t1.name) LIKE ? OR LOWER(t2.name) LIKE ?
            """;

    @Override
    public List<Game> findByTeamName(String teamName) {
        return jdbcClient.sql(FIND_BY_TEAM_NAME)
                .param("%" + teamName.toLowerCase() + "%")
                .param("%" + teamName.toLowerCase() + "%")
                .query(GAME_MAPPER())
                .list();
    }
}
