package com.example.demo.repository;


import com.example.demo.models.Team;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TeamJdbcTemplateRepository implements TeamRepositoryV2 {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TeamJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("teams")
                .usingGeneratedKeyColumns("id");
        simpleJdbcInsert.compile();
    }

    /* private final RowMapper<Team> teamRowMapper = (rs, rowNum) -> new Team(
                    rs.getLong("id"),
                    rs.getString("name")
            );*/
    private final RowMapper<Team> TEAM_MAPPER =
            BeanPropertyRowMapper.newInstance(Team.class);

    String INSERT_TEAM = "INSERT INTO teams (name) VALUES (?)";

    /*  @Override
      public Team create(Team team) {
          PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                  INSERT_TEAM,
                  Types.VARCHAR
          );
          pscf.setGeneratedKeysColumnNames("id");
          PreparedStatementCreator psc = pscf.newPreparedStatementCreator(new Object[]{
                  team.getName()
          });
          KeyHolder keyHolder = new GeneratedKeyHolder();
          jdbcTemplate.update(psc, keyHolder);
          Long newId = keyHolder.getKey().longValue();
          team.setId(newId);
          return team;
      }*/
    @Override
    public Team create(Team team) {
        Long newId = simpleJdbcInsert.executeAndReturnKey(
                        new BeanPropertySqlParameterSource(team))
                .longValue();
        team.setId(newId);
        return team;
    }

    String FIND_BY_ID = "SELECT * FROM teams WHERE id = ?";

    @Override
    public Optional<Team> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                    TEAM_MAPPER, id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    String SELECT_ALL_TEAMS = "SELECT * FROM teams";

    @Override
    public List<Team> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TEAMS, TEAM_MAPPER);
    }

    String UPDATE_TEAM = "UPDATE teams SET name = ? WHERE id = ?";

    @Override
    public int update(Team team) {
        return jdbcTemplate.update(UPDATE_TEAM, team.getName(), team.getId());
    }

    String DELETE_BY_ID = "DELETE FROM teams WHERE id = ?";

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    String FIND_BY_NAME = "SELECT * FROM teams WHERE LOWER(name) LIKE ?";

    @Override
    public List<Team> findByName(String name) {
        return jdbcTemplate.query(FIND_BY_NAME, TEAM_MAPPER, "%" + name.toLowerCase() + "%");
    }
}
