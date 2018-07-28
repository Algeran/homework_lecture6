package otus.springfreamwork.homework_lecture6.com.shell;

//import org.h2.tools.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import otus.springfreamwork.homework_lecture6.domain.model.Author;
import otus.springfreamwork.homework_lecture6.domain.model.Book;
import otus.springfreamwork.homework_lecture6.domain.model.Genre;
import otus.springfreamwork.homework_lecture6.domain.repositories.AuthorDAO;
import otus.springfreamwork.homework_lecture6.domain.repositories.BookDAO;
import otus.springfreamwork.homework_lecture6.domain.repositories.GenreDAO;

import java.util.stream.Collectors;

@ShellComponent
public class ShellCommands {

    private final GenreDAO genreDAO;
    private final AuthorDAO authorDAO;
    private final BookDAO bookDAO;

    @Autowired
    public ShellCommands(
            GenreDAO genreDAO,
            AuthorDAO authorDAO,
            BookDAO bookDAO
    ) {
        this.genreDAO = genreDAO;
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    @ShellMethod("listBooks")
    public String listBooks() {
        return bookDAO.getAll().stream().map(Book::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod("listAuthors")
    public String listAuthors() {
        return authorDAO.getAll().stream().map(Author::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod("listGenres")
    public String listGenres() {
        return genreDAO.getAll().stream().map(Genre::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod("countBooks")
    public int countBooks() {
        return bookDAO.count();
    }

    @ShellMethod("countAuthors")
    public int countAuthors() {
        return authorDAO.count();
    }

    @ShellMethod("countGenres")
    public int countGenres() {
        return genreDAO.count();
    }

    @ShellMethod("getBook")
    public String getBook(@ShellOption String name) {
        try {
            Book book = bookDAO.getByName(name);
            return book.toString();
        } catch (Exception e) {
            return "Книги с названием [" + name + "] не найдено";
        }
    }

    @ShellMethod("getAuthor")
    public String getAuthor(
            @ShellOption String name,
            @ShellOption String surname
    )
    {
        try {
            Author author = authorDAO.getByNameAndSurname(name, surname);
            return author.toString();
        } catch (Exception e) {
            return "Автора с именем [" + name + " " + surname + "] не найдено";
        }
    }

    @ShellMethod("getGenre")
    public String getGenre(@ShellOption String name) {
        try {
            Genre genre = genreDAO.getByName(name);
            return genre.toString();
        } catch (Exception e) {
            return "Жанра с названием [" + name + "] не найдено";
        }
    }

    @ShellMethod("createAuthor")
    public String createAuthor(
            @ShellOption String name,
            @ShellOption String surname
    ) {
        Author author = new Author(name, surname);
        authorDAO.insert(author);
        return "Автор сохранен";
    }

    @ShellMethod("createGenre")
    public String createGenre(@ShellOption String name) {
        Genre genre = new Genre(name);
        genreDAO.insert(genre);
        return "Жанр сохранен";
    }

    @ShellMethod("createBook")
    public String createBook(
            @ShellOption String name,
            @ShellOption String authorName,
            @ShellOption String authorSurname,
            @ShellOption String genreName
    ) {
        Author author;
        try {
            author = authorDAO.getByNameAndSurname(authorName, authorSurname);
        } catch (Exception e) {
            author = new Author(authorName, authorSurname);
            authorDAO.insert(author);
            author = authorDAO.getByNameAndSurname(author.getName(), author.getSurname());
            System.out.println("Автор не найден в базе, записали");
        }

        Genre genre;
        try {
            genre = genreDAO.getByName(genreName);
        } catch (Exception e) {
            genre = new Genre(genreName);
            genreDAO.insert(genre);
            genre = genreDAO.getByName(genre.getName());
            System.out.println("Жанр не найден в базе, записали");
        }

        Book book = new Book(name, author, genre);
        bookDAO.insert(book);
        return "Книга сохранена";
    }

    @ShellMethod("deleteAuthor")
    public String deleteAuthor(
            @ShellOption String name,
            @ShellOption String surname
    ) {
        authorDAO.deleteByNameAndSurname(name, surname);
        return "Автор удален";
    }

    @ShellMethod("deleteGenre")
    public String deleteGenre(
            @ShellOption String name
    ) {
        genreDAO.deleteByName(name);
        return "Жанр удален";
    }

    @ShellMethod("bookDelete")
    public String bookDelete(
            @ShellOption String name
    ) {
        bookDAO.deleteByName(name);
        return "Книга удалена";
    }

//    @ShellMethod("console")
//    public void console() throws SQLException {
//        Console.main(new String[]{});
//    }

}
