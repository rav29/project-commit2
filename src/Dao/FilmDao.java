package Dao;

import ConnectionToDB.ConnectionToDB;
import Entities.Film;
import Entities.Genre;
import Entities.MemberOfFilmCrew;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Radion on 16.04.2017.
 */
public class FilmDao {
    private static final Object LOCK = new Object();
    private static FilmDao INSTANCE = null;

    public static FilmDao getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new FilmDao();
                }
            }
        }
        return INSTANCE;
    }

    public Optional<Film> save(Film film) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO films (film_name, director_id, release_date, country, genre)" +
                            "VALUES(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, film.getTitle());
                preparedStatement.setLong(2, film.getDirector().getId());
                preparedStatement.setDate(3, Date.valueOf(film.getDateOfProduction()));
                preparedStatement.setString(4, film.getCountry());
                preparedStatement.setObject(5, film.getGenre().toString());
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    film.setFilmId(generatedKeys.getLong(1));
                    return Optional.of(film);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Film getFilmById(Long id) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM films WHERE films.id = ?;")) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Genre genre = Genre.valueOf(resultSet.getString("genre"));          //варианты?
                    LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
                    return new Film(resultSet.getString("film_title"), getSetOfActors(id),
                            getDirectorByFilmId(id), genre, releaseDate,
                            resultSet.getString("country"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<MemberOfFilmCrew> getSetOfActors(Long filmId) {
        Set<MemberOfFilmCrew> setOfActors = new HashSet<>();
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT a.id, a.actor_name, a.birth_date   FROM actors AS a" +
                            "JOIN films_actors AS fa ON a.id = fa.actor_id" +
                            "JOIN films AS f ON fa.film_id = f.id WHERE f.id=?;")) {
                preparedStatement.setLong(1, filmId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    LocalDate dateOfBirth = resultSet.getDate("birth_date").toLocalDate();
                    setOfActors.add(new MemberOfFilmCrew(resultSet.getString("actor_name"),
                            dateOfBirth, resultSet.getLong("id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return setOfActors;
    }

    public MemberOfFilmCrew getDirectorByFilmId(Long filmId) {                                                  //кандидат на удаление за ненадобностью
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT d.id, d.director_name, d.birth_date   FROM directors AS d" +
                            "JOIN films AS f ON f.director_id = d.id WHERE f.id=?;")) {
                preparedStatement.setLong(1, filmId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate("birth_date").toLocalDate();
                    return new MemberOfFilmCrew(resultSet.getString("director_name"), birthDate,
                            resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<Film> getFilmsByYear (LocalDate dateOfRelease ){
        Set<Film> filmSet = new HashSet<>();
        try (Connection connection = ConnectionToDB.createConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM films WHERE year(films.release_date)  = ?;")){
                preparedStatement.setDate(1, Date.valueOf(dateOfRelease));
                getSetOfFilms(preparedStatement, filmSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  filmSet;
    }

    public Set<Film> getFilmsByActor (Long actorId){
        Set<Film> filmSet = new HashSet<>();
        try (Connection connection = ConnectionToDB.createConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT f.film_title, f.director_id, f.release_date, f.country, f.genre FROM films AS f" +
                            "JOIN films_actors AS fa ON fa.film_id = f.id" +
                            "JOIN actors AS a ON fa.actor_id =a.id  WHERE a.id = ?;")){
                preparedStatement.setLong(1, actorId);
                getSetOfFilms(preparedStatement, filmSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  filmSet;
    }

    private Set<Film> getSetOfFilms (PreparedStatement preparedStatement, Set<Film> filmSet) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Genre genre = Genre.valueOf(resultSet.getString("genre"));
            LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
            filmSet.add(new Film(resultSet.getString("film_title"),
                    getSetOfActors(resultSet.getLong("id")),
                    getDirectorByFilmId(resultSet.getLong("id")), genre, releaseDate,
                    resultSet.getString("country")));
        }
        return filmSet;
    }
}
