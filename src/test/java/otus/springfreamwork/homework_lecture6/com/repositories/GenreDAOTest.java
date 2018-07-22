package otus.springfreamwork.homework_lecture6.com.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import otus.springfreamwork.homework_lecture6.domain.model.Genre;
import otus.springfreamwork.homework_lecture6.domain.repositories.GenreDAO;

import java.util.Collections;
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
public class GenreDAOTest {

    private GenreDAO genreDAO;

    @Mock
    private NamedParameterJdbcOperations jdbc;

    @Test
    public void genreDAOShouldCallInsertStatement() {
        genreDAO = new GenreDAOImpl(jdbc);
        Genre genre = new Genre("Fantasy");
        genreDAO.insert(genre);

        verify(jdbc, times(1)).update(anyString(), eq(Collections.singletonMap("name", genre.getName())));
    }

    @Test
    public void genreDAOShouldCallSelectStatementWithExpectedId() {
        genreDAO = new GenreDAOImpl(jdbc);
        Genre genre = new Genre(1, "Fantasy");
        final Map<String, Object> params = Collections.singletonMap("id", genre.getId());
        when(jdbc.queryForObject(anyString(), eq(params), any(RowMapper.class))).thenReturn(genre);
        Genre genreFromDao = genreDAO.getById(1);

        assertEquals(genre, genreFromDao);

        verify(jdbc, times(1)).queryForObject(anyString(), eq(params), any(RowMapper.class));
    }

    @Test
    public void genreDAOShouldCallSelectStatementWithExpectedName() {
        genreDAO = new GenreDAOImpl(jdbc);
        Genre genre = new Genre(1, "Fantasy");
        final Map<String, Object> params = Collections.singletonMap("name", genre.getName());
        when(jdbc.queryForObject(anyString(), eq(params), any(RowMapper.class))).thenReturn(genre);
        Genre genreFromDao = genreDAO.getByName(genre.getName());

        assertEquals(genre, genreFromDao);

        verify(jdbc, times(1)).queryForObject(anyString(), eq(params), any(RowMapper.class));
    }

    @Test
    public void genreDAOShouldCallSelectAllRowsStatement() {
        genreDAO = new GenreDAOImpl(jdbc);
        Genre genre = new Genre(1, "Fantasy");
        when(jdbc.query(anyString(), eq(Collections.emptyMap()), any(RowMapper.class))).thenReturn(Collections.singletonList(genre));
        List<Genre> genres = genreDAO.getAll();

        assertNotNull(genres);
        assertTrue(genres.contains(genre));

        verify(jdbc, times(1)).query(anyString(), eq(Collections.emptyMap()), any(RowMapper.class));
    }

    @Test
    public void genreDAOShouldCallSelectCountStatement() {
        genreDAO = new GenreDAOImpl(jdbc);
        when(jdbc.queryForObject(anyString(), eq(Collections.emptyMap()), eq(Integer.class))).thenReturn(1);
        int count = genreDAO.count();

        assertEquals(1, count);

        verify(jdbc, times(1)).queryForObject(anyString(), eq(Collections.emptyMap()), eq(Integer.class));
    }

    @Test
    public void genreDAOShouldCallDeleteStatementWithExpectedId() {
        genreDAO = new GenreDAOImpl(jdbc);
        genreDAO.deleteById(1);

        verify(jdbc, times(1)).update(anyString(), eq(Collections.singletonMap("id", 1)));
    }

    @Test
    public void genreDAOShouldCallDeleteStatementWithExpectedName() {
        genreDAO = new GenreDAOImpl(jdbc);
        String genreName = "Fantasy";
        genreDAO.deleteByName(genreName);

        verify(jdbc, times(1)).update(anyString(), eq(Collections.singletonMap("name", genreName)));
    }

}
