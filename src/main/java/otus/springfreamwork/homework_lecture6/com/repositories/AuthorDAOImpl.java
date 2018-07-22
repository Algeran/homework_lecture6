package otus.springfreamwork.homework_lecture6.com.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import otus.springfreamwork.homework_lecture6.domain.model.Author;
import otus.springfreamwork.homework_lecture6.domain.repositories.AuthorDAO;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDAOImpl implements AuthorDAO {

    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public AuthorDAOImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(Author author) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("name", author.getName());
        params.put("surname", author.getSurname());
        jdbc.update("INSERT INTO AUTHORS (NAME,SURNAME) VALUES (:name, :surname)", params);
    }

    @Override
    public Author getById(int id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject(
                "SELECT * FROM AUTHORS WHERE ID = :id"
                , params
                , getRowMapper()
                );
    }

    @Override
    public void deleteById(int id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("DELETE FROM AUTHORS WHERE ID = :id", params);
    }

    @Override
    public Author getByNameAndSurname(String name, String surname) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("name", name);
        params.put("surname", surname);
        return jdbc.queryForObject(
                "SELECT * FROM AUTHORS WHERE NAME = :name AND SURNAME = :surname"
                , params
                , getRowMapper()
        );
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query(
                "SELECT * FROM AUTHORS"
                , Collections.emptyMap()
                , getRowMapper()
        );
    }

    @Override
    public void deleteByNameAndSurname(String name, String surname) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("name", name);
        params.put("surname", surname);
        jdbc.update("DELETE FROM AUTHORS WHERE NAME = :name AND SURNAME = :surname", params);
    }

    @Override
    public int count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM AUTHORS", Collections.emptyMap(), Integer.class);
    }

    private RowMapper<Author> getRowMapper() {
        return (ResultSet resultSet, int rowId) -> {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            String surname = resultSet.getString("SURNAME");
            return new Author(id, name, surname);
        };
    }
}
