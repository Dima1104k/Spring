package com.example.demo.repository;

import com.example.demo.models.Team;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// @Primary
public class TeamJdbcClientRepository implements TeamRepositoryV2 {

    private final JdbcClient jdbcClient;

    public TeamJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    String INSERT_TEAM = "INSERT INTO teams (name) VALUES (:name)";

    @Override
    public Team create(Team team) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(INSERT_TEAM)
                .paramSource(team)
                .update(keyHolder, "id");
        Long newId = keyHolder.getKey().longValue();
        team.setId(newId);
        return team;
    }

    String FIND_BY_ID = "SELECT * FROM teams WHERE id = ?";

    @Override
    public Optional<Team> findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID)
                .param(id)
                .query(Team.class)
                .optional();
    }

    String SELECT_ALL_TEAMS = "SELECT * FROM teams";

    @Override
    public List<Team> findAll() {
        return jdbcClient.sql(SELECT_ALL_TEAMS)
                .query(Team.class)
                .list();
    }

    String UPDATE_TEAM = "UPDATE teams SET name = :name WHERE id = :id";

    @Override
    public int update(Team team) {
        return jdbcClient.sql(UPDATE_TEAM)
                .paramSource(team)
                .update();
    }

    String DELETE_BY_ID = "DELETE FROM teams WHERE id = ?";

    @Override
    public void deleteById(Long id) {
        jdbcClient.sql(DELETE_BY_ID)
                .param(id)
                .update();
    }

    String FIND_BY_NAME = "SELECT * FROM teams WHERE LOWER(name) LIKE ?";

    @Override
    public List<Team> findByName(String name) {
        return jdbcClient.sql(FIND_BY_NAME)
                .param("%" + name.toLowerCase() + "%")
                .query(Team.class)
                .list();
    }
}
