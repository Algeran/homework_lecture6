package otus.springfreamwork.homework_lecture6.domain.repositories;

import otus.springfreamwork.homework_lecture6.domain.model.Book;

import java.util.List;

public interface BookDAO {

    void insert(Book book);

    Book getById(int id);

    Book getByName(String name);

    List<Book> getAll();

    int count();

    void deleteById(int id);

    void deleteByName(String name);

    List<Book> getByAuthorId(int authorId);

    List<Book> getByGenreId(int genreId);
}
