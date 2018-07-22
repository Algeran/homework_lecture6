package otus.springfreamwork.homework_lecture6.com.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import otus.springfreamwork.homework_lecture6.domain.model.Author;
import otus.springfreamwork.homework_lecture6.domain.repositories.AuthorDAO;

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
public class AuthorDAOTest {

    private AuthorDAO authorDAO;

    @Mock
    private NamedParameterJdbcOperations jdbc;

    @Test
    public void authorDAOShouldCallInsertStatementWithExpectedParams() {
        authorDAO = new AuthorDAOImpl(jdbc);
        Author author = new Author("Leo", "Tolstoy");
        authorDAO.insert(author);

        final Map<String, Object> expectedAttrMap = new HashMap<>();
        expectedAttrMap.put("name", author.getName());
        expectedAttrMap.put("surname", author.getSurname());
        verify(jdbc, times(1)).update(anyString(), eq(expectedAttrMap));
    }

    @Test
    public void authorDAOShouldCallSelectStatementWithExpectedId() {
        authorDAO = new AuthorDAOImpl(jdbc);
        Author author = new Author(1, "Leo", "Tolstoy");
        when(jdbc.queryForObject(anyString(), eq(Collections.singletonMap("id", 1)), any(RowMapper.class))).thenReturn(author);

        Author authorFromDAO = authorDAO.getById(1);

        assertEquals(author, authorFromDAO);
        verify(jdbc, times(1)).queryForObject(anyString(), eq(Collections.singletonMap("id", 1)), any(RowMapper.class));
    }

    @Test
    public void authorDAOShouldCallSelectStatementWithExpectedNameAndSurname() {
        authorDAO = new AuthorDAOImpl(jdbc);
        Author author = new Author(1, "Leo", "Tolstoy");
        final Map<String, Object> params = new HashMap<>();
        params.put("name", author.getName());
        params.put("surname", author.getSurname());
        when(jdbc.queryForObject(anyString(), eq(params), any(RowMapper.class))).thenReturn(author);

        Author authorFromDAO = authorDAO.getByNameAndSurname(author.getName(), author.getSurname());

        assertEquals(author, authorFromDAO);
        verify(jdbc, times(1)).queryForObject(anyString(), eq(params), any(RowMapper.class));
    }

    @Test
    public void authorDAOShouldCallSelectAllRows() {
        authorDAO = new AuthorDAOImpl(jdbc);
        Author author = new Author(1, "Leo", "Tolstoy");
        when(jdbc.query(anyString(), eq(Collections.emptyMap()), any(RowMapper.class))).thenReturn(Collections.singletonList(author));

        List<Author> authors = authorDAO.getAll();

        assertNotNull(authors);
        assertTrue(authors.contains(author));

        verify(jdbc, times(1)).query(anyString(), eq(Collections.emptyMap()), any(RowMapper.class));
    }

    @Test
    public void authorsDAOShouldCallSelectCountStatement() {
        authorDAO = new AuthorDAOImpl(jdbc);
        when(jdbc.queryForObject(anyString(), eq(Collections.emptyMap()), eq(Integer.class))).thenReturn(1);
        int count = authorDAO.count();

        assertEquals(1, count);

        verify(jdbc, times(1)).queryForObject(anyString(), eq(Collections.emptyMap()), eq(Integer.class));
    }

    @Test
    public void authorDAOShouldCallDeleteStatementWithExpectedId() {
        authorDAO = new AuthorDAOImpl(jdbc);
        authorDAO.deleteById(1);

        verify(jdbc, times(1)).update(anyString(), eq(Collections.singletonMap("id", 1)));
    }

    @Test
    public void authorDAOShouldCallDeleteStatementWithExpectedNameAndSurname() {
        authorDAO = new AuthorDAOImpl(jdbc);
        authorDAO.deleteByNameAndSurname("Leo", "Tolstoy");
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "Leo");
        params.put("surname", "Tolstoy");

        verify(jdbc, times(1)).update(anyString(), eq(params));
    }
}
