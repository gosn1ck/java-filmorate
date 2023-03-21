package ru.yandex.practicum.filmorate.service;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.friend.InMemoryFriendManager;
import ru.yandex.practicum.filmorate.service.like.InMemoryLikeManager;
import ru.yandex.practicum.filmorate.service.like.LikeManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private static final String DESCRIPTION = "Chronicles the experiences of a formerly successful banker as  "
            + "a prisoner in the gloomy jailhouse of Shawshank after being found guilty of a crime"
            + "he did not commit.";

    public static final LocalDate RELEASE_DATE = LocalDate.of(1967, 3, 25);
    public static final int DURATION = 100;
    public static final String NAME = "The Shawshank Redemption";

    private FilmService filmService;
    private UserService userService;

    @BeforeEach
    @Test
    void beforeEach() {
        UserRepository userRepository = new InMemoryUserRepository();
        this.userService = new UserService(userRepository, new InMemoryFriendManager(userRepository));

        FilmRepository filmRepository = new InMemoryFilmRepository();
        LikeManager likeManager = new InMemoryLikeManager(filmRepository, userRepository);
        this.filmService = new FilmService(filmRepository, likeManager);
    }

    @DisplayName("Фильм добавлен в сервис")
    @Test
    void shouldAddFilm() {
        val film = new Film();
        film.setName(NAME);
        film.setDescription(DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);

        val savedFilm = filmService.addFilm(film);
        assertEquals(1, savedFilm.getId());
        assertEquals(film.getName(), savedFilm.getName());
        assertEquals(film.getDescription(), savedFilm.getDescription());
        assertEquals(film.getDuration(), savedFilm.getDuration());
        assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate());

        val films = filmService.getFilms();
        assertEquals(1, films.size());

        val savedFilmList = films.get(0);
        assertEquals(1, savedFilmList.getId());
        assertEquals(film.getName(), savedFilmList.getName());
        assertEquals(film.getDescription(), savedFilmList.getDescription());
        assertEquals(film.getDuration(), savedFilmList.getDuration());
        assertEquals(film.getReleaseDate(), savedFilmList.getReleaseDate());

        val optionalFilm = filmService.getFilm(1);
        val savedByIdFilm = optionalFilm.get();
        assertEquals(film.getName(), savedByIdFilm.getName());
        assertEquals(film.getDescription(), savedByIdFilm.getDescription());
        assertEquals(film.getDuration(), savedByIdFilm.getDuration());
        assertEquals(film.getReleaseDate(), savedByIdFilm.getReleaseDate());

    }

    @DisplayName("Фильм обновлен в сервисе если он там есть")
    @Test
    void shouldUpdateFilm() {
        val film = new Film();
        film.setName(NAME);
        film.setDescription(DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);

        filmService.addFilm(film);

        var newName = "new name";
        film.setName(newName);
        var newDescription = "new description";
        film.setDescription(newDescription);

        val optFilm = filmService.updateFilm(film);
        val updatedFilm = optFilm.get();
        assertEquals(film.getId(), updatedFilm.getId());
        assertEquals(film.getName(), updatedFilm.getName());
        assertEquals(film.getDescription(), updatedFilm.getDescription());

        val notExistingFilm = new Film();
        notExistingFilm.setId(9999);
        notExistingFilm.setName(NAME);
        notExistingFilm.setDescription(DESCRIPTION);
        notExistingFilm.setReleaseDate(RELEASE_DATE);
        notExistingFilm.setDuration(DURATION);

        val optNotExistingFilm = filmService.updateFilm(notExistingFilm);
        assertTrue(optNotExistingFilm.isEmpty());

    }

    @DisplayName("Фильма нет по несуществующему идентификатору")
    @Test
    void shouldReturnEmptyFilm() {

        val optionalFilm = filmService.getFilm(9999);
        assertTrue(optionalFilm.isEmpty());

    }

    @DisplayName("Пользователь добавил лайк фильму")
    @Test
    void shouldAddLike() {
        val film = new Film();
        film.setName(NAME);
        film.setDescription(DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        filmService.addFilm(film);

        val user = new User();
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user);

        filmService.addLike(film.getId(), user.getId());
        val films = filmService.popularFilms(1);
        assertEquals(1, films.size());
        assertEquals(film.getId(), films.get(0).getId());

    }

    @DisplayName("Пользователь не добавил лайк к фильму")
    @Test
    void shouldNotAddLike() {
        val film = new Film();
        film.setName(NAME);
        film.setDescription(DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        filmService.addFilm(film);

        val user = new User();
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user);

        val exceptionLikeFilm = assertThrows(NotFoundException.class, () ->
                filmService.addLike(film.getId(), 9999));
        assertEquals("user with id 9999 not found", exceptionLikeFilm.getMessage());

        val exceptionLikeUser = assertThrows(NotFoundException.class, () ->
                filmService.addLike(9999, user.getId()));
        assertEquals("film with id 9999 not found", exceptionLikeUser.getMessage());

    }

    @DisplayName("Пользователь удалил лайк фильма")
    @Test
    void shouldRemoveLike() {
        val film = new Film();
        film.setName(NAME);
        film.setDescription(DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        filmService.addFilm(film);

        val user = new User();
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user);

        filmService.addLike(film.getId(), user.getId());
        assertEquals(1, film.getLikes().size());

        filmService.removeLike(user.getId(), film.getId());
        assertEquals(0, film.getLikes().size());
    }

    @DisplayName("Пользователь не удалил лайк фильма")
    @Test
    void shouldNotRemoveLike() {
        val film = new Film();
        film.setName(NAME);
        film.setDescription(DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        filmService.addFilm(film);

        val user = new User();
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user);

        val exceptionLikeFilm = assertThrows(NotFoundException.class, () ->
                filmService.removeLike(film.getId(), 9999));
        assertEquals("user with id 9999 not found", exceptionLikeFilm.getMessage());

        val exceptionLikeUser = assertThrows(NotFoundException.class, () ->
                filmService.removeLike(9999, user.getId()));
        assertEquals("film with id 9999 not found", exceptionLikeUser.getMessage());

    }

    @DisplayName("Пользователь добавил лайк фильму")
    @Test
    void shouldGetPopularFilm() {
        val film1 = new Film();
        film1.setName(NAME);
        film1.setDescription(DESCRIPTION);
        film1.setReleaseDate(RELEASE_DATE);
        film1.setDuration(DURATION);
        filmService.addFilm(film1);

        val film2 = new Film();
        film2.setName(NAME);
        film2.setDescription(DESCRIPTION);
        film2.setReleaseDate(RELEASE_DATE);
        film2.setDuration(DURATION);
        filmService.addFilm(film2);

        val film3 = new Film();
        film3.setName(NAME);
        film3.setDescription(DESCRIPTION);
        film3.setReleaseDate(RELEASE_DATE);
        film3.setDuration(DURATION);
        filmService.addFilm(film3);

        val user1 = new User();
        user1.setName("Nick Name");
        user1.setLogin("dolore");
        user1.setEmail("mail@mail.ru");
        user1.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user1);

        val user2 = new User();
        user2.setName("Nick Name");
        user2.setLogin("dolore");
        user2.setEmail("mail@mail.ru");
        user2.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user2);

        val user3 = new User();
        user3.setName("Nick Name");
        user3.setLogin("dolore");
        user3.setEmail("mail@mail.ru");
        user3.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user3);

        filmService.addLike(film1.getId(), user1.getId());
        var films = filmService.popularFilms(1);
        assertEquals(1, films.size());
        assertEquals(film1.getId(), films.get(0).getId());

        filmService.addLike(film2.getId(), user1.getId());
        filmService.addLike(film2.getId(), user2.getId());
        films = filmService.popularFilms(2);
        assertEquals(2, films.size());
        assertEquals(film2.getId(), films.get(0).getId());

        filmService.addLike(film3.getId(), user1.getId());
        filmService.addLike(film3.getId(), user2.getId());
        filmService.addLike(film3.getId(), user3.getId());
        films = filmService.popularFilms(3);
        assertEquals(3, films.size());
        assertEquals(film3.getId(), films.get(0).getId());

        val user4 = new User();
        user4.setName("Nick Name");
        user4.setLogin("dolore");
        user4.setEmail("mail@mail.ru");
        user4.setBirthday(LocalDate.of(1999, 10, 12));
        userService.addUser(user4);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user2.getId());
        filmService.addLike(film1.getId(), user3.getId());
        filmService.addLike(film1.getId(), user4.getId());
        films = filmService.popularFilms(1);
        assertEquals(1, films.size());
        assertEquals(film1.getId(), films.get(0).getId());

    }

}
