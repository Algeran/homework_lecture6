package otus.springfreamwork.homework_lecture6.domain.repositories;

import otus.springfreamwork.homework_lecture6.domain.model.Author;

import java.util.List;

public interface AuthorDAO {

    void insert(Author author);

    Author getById(int id);

    Author getByNameAndSurname(String name, String surname);

    List<Author> getAll();

    void deleteById(int id);

    void deleteByNameAndSurname(String name, String surname);

    int count();
}
