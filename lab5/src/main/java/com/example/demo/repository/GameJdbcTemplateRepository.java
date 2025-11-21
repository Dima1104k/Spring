package com.example.demo.repository;

import com.example.demo.models.Game;
import com.example.demo.models.GameResult;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class GameJdbcTemplateRepository implements GameRepositoryV2 {

    private final JdbcTemplate jdbcTemplate;

    private final TeamRepositoryV2 teamRepository;

    public GameJdbcTemplateRepository(JdbcTemplate jdbcTemplate, TeamRepositoryV2 teamRepository) {
        this.jdbcTemplate = jdbcTemplate;
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
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                INSERT_GAME,
                Types.BIGINT,
                Types.BIGINT,
                Types.TIMESTAMP,
                Types.INTEGER,
                Types.INTEGER
        );
        pscf.setGeneratedKeysColumnNames("id");
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(new Object[]{
                game.getHomeTeam().getId(),
                game.getAwayTeam().getId(),
                Timestamp.valueOf(game.getGameTime()),
                game.getResult() != null ? game.getResult().getHomeScore() : null,
                game.getResult() != null ? game.getResult().getAwayScore() : null

        });
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        Long newId = keyHolder.getKey().longValue();
        game.setId(newId);
        return game;
    }

    String FIND_BY_ID = "SELECT * FROM games WHERE id = ?";

    @Override
    public Optional<Game> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                    GAME_MAPPER(), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    String FIND_ALL_GAMES = "SELECT * FROM games";

    @Override
    public List<Game> findAll() {
        return jdbcTemplate.query(FIND_ALL_GAMES, GAME_MAPPER());
    }

    String UPDATE_GAME = "UPDATE games SET home_team_id = ?, away_team_id = ?, game_time = ?, home_score = ?, away_score = ? WHERE id = ?";

    @Override
    public int update(Game game) {
        return jdbcTemplate.update(UPDATE_GAME,
                game.getHomeTeam().getId(),
                game.getAwayTeam().getId(),
                Timestamp.valueOf(game.getGameTime()),
                game.getResult() != null ? game.getResult().getHomeScore() : null,
                game.getResult() != null ? game.getResult().getAwayScore() : null,
                game.getId()
        );
    }

    String DELETE_GAME = "DELETE FROM games WHERE id = ?";

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_GAME, id);
    }

    String FIND_BY_TEAM_ID = "SELECT * FROM games WHERE home_team_id = ? OR away_team_id = ?";

    @Override
    public List<Game> findByTeamId(Long teamId) {
        return jdbcTemplate.query(FIND_BY_TEAM_ID, GAME_MAPPER(), teamId, teamId);
    }

    String FIND_BY_TEAM_NAME = """
            SELECT g.* FROM games g
            JOIN teams t1 ON g.home_team_id = t1.id
            JOIN teams t2 ON g.away_team_id = t2.id
            WHERE LOWER(t1.name) LIKE ? OR LOWER(t2.name) LIKE ?
            """;
    @Override
    public List<Game> findByTeamName(String teamName) {
        String pattern = "%" + teamName.toLowerCase() + "%";
        return jdbcTemplate.query(FIND_BY_TEAM_NAME, GAME_MAPPER(), pattern, pattern);
    }
}
