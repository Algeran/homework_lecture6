package otus.springfreamwork.homework_lecture6.com.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import otus.springfreamwork.homework_lecture6.domain.model.Genre;
import otus.springfreamwork.homework_lecture6.domain.repositories.GenreDAO;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDAOImpl implements GenreDAO {

    private NamedParameterJdbcOperations jdbc;

    @Autowired
    public GenreDAOImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(Genre genre) {
        final Map<String, Object> params = Collections.singletonMap("name", genre.getName());
        jdbc.update("INSERT INTO GENRES (NAME) VALUES (:name)", params);
    }

    @Override
    public Genre getById(int id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject(
                "SELECT * FROM GENRES WHERE ID = :id"
                , params
                , getRowMapper()
        );
    }

    @Override
    public Genre getByName(String name) {
        final Map<String, Object> params = Collections.singletonMap("name", name);
        return jdbc.queryForObject(
                "SELECT * FROM GENRES WHERE NAME = :name"
                , params
                , getRowMapper()
        );
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query(
                "SELECT * FROM GENRES"
                , Collections.emptyMap()
                , getRowMapper()
        );
    }

    @Override
    public int count() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM GENRES"
                , Collections.emptyMap()
                , Integer.class
        );
    }

    @Override
    public void deleteById(int id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("DELETE FROM GENRES WHERE ID = :id", params);
    }

    @Override
    public void deleteByName(String name) {
        final Map<String, Object> params = Collections.singletonMap("name", name);
        jdbc.update("DELETE FROM GENRES WHERE NAME = :name", params);
    }

    private RowMapper<Genre> getRowMapper() {
        return (ResultSet resultSet, int rowId) -> {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            return new Genre(id, name);
        };
    }
}
