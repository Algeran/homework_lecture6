package otus.springfreamwork.homework_lecture6.domain.repositories;

import otus.springfreamwork.homework_lecture6.domain.model.Genre;

import java.util.List;

public interface GenreDAO {

    void insert(Genre genre);

    Genre getById(int id);

    Genre getByName(String name);

    List<Genre> getAll();

    void deleteById(int id);

    void deleteByName(String name);

    int count();
}
