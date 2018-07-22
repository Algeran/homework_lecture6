package otus.springfreamwork.homework_lecture6.com.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import otus.springfreamwork.homework_lecture6.domain.model.Author;
import otus.springfreamwork.homework_lecture6.domain.model.Book;
import otus.springfreamwork.homework_lecture6.domain.model.Genre;
import otus.springfreamwork.homework_lecture6.domain.repositories.AuthorDAO;
import otus.springfreamwork.homework_lecture6.domain.repositories.BookDAO;
import otus.springfreamwork.homework_lecture6.domain.repositories.GenreDAO;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookDAOImpl implements BookDAO {

    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;

    @Autowired
    public BookDAOImpl(
            NamedParameterJdbcOperations jdbc,
            AuthorDAO authorDAO,
            GenreDAO genreDAO
    ) {
        this.jdbc = jdbc;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
    }

    @Override
    public void insert(Book book) {
        Genre genre = book.getGenre();
        Genre genreFromDB = genreDAO.getByName(genre.getName());
        Author author = book.getAuthor();
        Author authorFromDB = authorDAO.getByNameAndSurname(author.getName(), author.getSurname());

        final Map<String, Object> params = new HashMap<>();
        params.put("name", book.getName());
        params.put("genre_id", genreFromDB.getId());
        params.put("author_id", authorFromDB.getId());
        jdbc.update("INSERT INTO BOOKS (NAME, AUTHOR_ID, GENRE_ID) VALUES (:name, :author_id, :genre_id)", params);
    }

    @Override
    public Book getById(int id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject("SELECT * FROM BOOKS WHERE ID = :id", params, getRowMapper());
    }

    @Override
    public Book getByName(String name) {
        final Map<String, Object> params = Collections.singletonMap("name", name);
        return jdbc.queryForObject("SELECT * FROM BOOKS WHERE NAME = :name", params, getRowMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("SELECT * FROM BOOKS", Collections.emptyMap(), getRowMapper());
    }

    @Override
    public int count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM BOOKS", Collections.emptyMap(), Integer.class);
    }

    @Override
    public void deleteById(int id) {
        jdbc.update("DELETE FROM BOOKS WHERE ID = :id", Collections.singletonMap("id", id));
    }

    @Override
    public void deleteByName(String name) {
        jdbc.update("DELETE FROM BOOKS WHERE NAME = :name", Collections.singletonMap("name", name));
    }

    @Override
    public List<Book> getByAuthorId(int authorId) {
        return jdbc.query("SELECT * FROM BOOKS WHERE AUTHOR_ID = :author_id", Collections.singletonMap("author_id", authorId), getRowMapper());
    }

    @Override
    public List<Book> getByGenreId(int genreId) {
        return jdbc.query("SELECT * FROM BOOKS WHERE GENRE_ID = :genre_id", Collections.singletonMap("genre_id", genreId), getRowMapper());
    }

    private RowMapper<Book> getRowMapper() {
        return (ResultSet resultSet, int rowId) -> {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            int genre_id = resultSet.getInt("GENRE_ID");
            int author_id = resultSet.getInt("AUTHOR_ID");
            Genre genre = genreDAO.getById(genre_id);
            Author author = authorDAO.getById(author_id);
            return new Book(id, name, author, genre);
        };
    }
}
