package otus.springfreamwork.homework_lecture6.com.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import otus.springfreamwork.homework_lecture6.domain.model.Author;
import otus.springfreamwork.homework_lecture6.domain.model.Book;
import otus.springfreamwork.homework_lecture6.domain.model.Genre;
import otus.springfreamwork.homework_lecture6.domain.repositories.AuthorDAO;
import otus.springfreamwork.homework_lecture6.domain.repositories.BookDAO;
import otus.springfreamwork.homework_lecture6.domain.repositories.GenreDAO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookDAOTest {

    private BookDAO bookDAO;

    @Mock
    private NamedParameterJdbcOperations jdbc;
    @Mock
    private GenreDAO genreDAO;
    @Mock
    private AuthorDAO authorDAO;

    @Test
    public void bookDAOShouldCallInsertStatementWithExpectedParams() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        Author author = new Author(1, "Leo", "Tolstoy");
        Genre genre = new Genre(1, "Novel");
        Book book = new Book(1, "War and Piece", author, genre);
        when(genreDAO.getByName(eq(genre.getName()))).thenReturn(genre);
        when(authorDAO.getByNameAndSurname(eq(author.getName()), eq(author.getSurname()))).thenReturn(author);

        bookDAO.insert(book);

        final Map<String, Object> params = new HashMap<>();
        params.put("name", book.getName());
        params.put("genre_id", genre.getId());
        params.put("author_id", author.getId());
        verify(genreDAO, times(1)).getByName(eq(genre.getName()));
        verify(authorDAO, times(1)).getByNameAndSurname(eq(author.getName()), eq(author.getSurname()));
        verify(jdbc, times(1)).update(anyString(), eq(params));
    }

    @Test
    public void bookDAOShouldCallSelectStatementWithExpectedId() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        Author author = new Author(1, "Leo", "Tolstoy");
        Genre genre = new Genre(1, "Novel");
        Book book = new Book(1, "War and Piece", author, genre);
        when(jdbc.queryForObject(anyString(), eq(Collections.singletonMap("id", book.getId())), any(RowMapper.class))).thenReturn(book);

        Book bookFromDAO = bookDAO.getById(1);

        assertEquals(book, bookFromDAO);
        verify(jdbc, times(1)).queryForObject(anyString(), eq(Collections.singletonMap("id", book.getId())), any(RowMapper.class));
    }

    @Test
    public void bookDAOShouldCallSelectStatementWithExpectedName() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        Author author = new Author(1, "Leo", "Tolstoy");
        Genre genre = new Genre(1, "Novel");
        Book book = new Book(1, "War and Piece", author, genre);
        when(jdbc.queryForObject(anyString(), eq(Collections.singletonMap("name", book.getName())), any(RowMapper.class))).thenReturn(book);

        Book bookFromDAO = bookDAO.getByName(book.getName());

        assertEquals(book, bookFromDAO);
        verify(jdbc, times(1)).queryForObject(anyString(), eq(Collections.singletonMap("name", book.getName())), any(RowMapper.class));
    }

    @Test
    public void bookDAOShouldCallSelectAllRows() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        Author author = new Author(1, "Leo", "Tolstoy");
        Genre genre = new Genre(1, "Novel");
        Book book = new Book(1, "War and Piece", author, genre);
        when(jdbc.query(anyString(), eq(Collections.emptyMap()), any(RowMapper.class))).thenReturn(Collections.singletonList(book));

        List<Book> books = bookDAO.getAll();

        assertNotNull(books);
        assertTrue(books.contains(book));
        verify(jdbc, times(1)).query(anyString(), eq(Collections.emptyMap()), any(RowMapper.class));
    }

    @Test
    public void bookDAOShouldCallSelectCount() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        when(jdbc.queryForObject(anyString(), eq(Collections.emptyMap()), eq(Integer.class))).thenReturn(1);

        int count = bookDAO.count();

        assertEquals(1, count);
        verify(jdbc, times(1)).queryForObject(anyString(), eq(Collections.emptyMap()), eq(Integer.class));
    }

    @Test
    public void bookDAOShouldCallSelectStatementWithExpectedAuthorId() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        Author author = new Author(1, "Leo", "Tolstoy");
        Genre genre = new Genre(1, "Novel");
        Book book = new Book(1, "War and Piece", author, genre);
        when(jdbc.query(anyString(), eq(Collections.singletonMap("author_id", author.getId())), any(RowMapper.class))).thenReturn(Collections.singletonList(book));

        List<Book> books = bookDAO.getByAuthorId(author.getId());

        assertNotNull(books);
        assertTrue(books.contains(book));

        verify(jdbc, times(1)).query(anyString(), eq(Collections.singletonMap("author_id", author.getId())), any(RowMapper.class));
    }

    @Test
    public void bookDAOShouldCallSelectStatementWithExpectedGenreId() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        Author author = new Author(1, "Leo", "Tolstoy");
        Genre genre = new Genre(1, "Novel");
        Book book = new Book(1, "War and Piece", author, genre);
        when(jdbc.query(anyString(), eq(Collections.singletonMap("genre_id", author.getId())), any(RowMapper.class))).thenReturn(Collections.singletonList(book));

        List<Book> books = bookDAO.getByGenreId(genre.getId());

        assertNotNull(books);
        assertTrue(books.contains(book));

        verify(jdbc, times(1)).query(anyString(), eq(Collections.singletonMap("genre_id", author.getId())), any(RowMapper.class));
    }

    @Test
    public void bookDAOShouldCallDeleteStatementWithExpectedId() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        bookDAO.deleteById(1);

        verify(jdbc, times(1)).update(anyString(), eq(Collections.singletonMap("id", 1)));
    }

    @Test
    public void bookDAOShouldCallDeleteStatementWithExpectedName() {
        bookDAO = new BookDAOImpl(jdbc, authorDAO, genreDAO);
        bookDAO.deleteByName("War and Piece");

        verify(jdbc, times(1)).update(anyString(), eq(Collections.singletonMap("name", "War and Piece")));
    }
}
