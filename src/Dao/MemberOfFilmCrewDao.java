package Dao;

import ConnectionToDB.ConnectionToDB;
import Entities.MemberOfFilmCrew;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Radion on 16.04.2017.
 */
public class MemberOfFilmCrewDao {
    private static final Object LOCK = new Object();
    private static MemberOfFilmCrewDao INSTANCE = null;

    public static MemberOfFilmCrewDao getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new MemberOfFilmCrewDao();
                }
            }
        }
        return INSTANCE;
    }


    public Optional<MemberOfFilmCrew> save(MemberOfFilmCrew memberOfFilmCrew) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            String sqlStatement;
            if (memberOfFilmCrew.isDirector()) {
                sqlStatement = "INSERT INTO directors (director_name, birth_date) VALUES (?,?)";
            } else {
                sqlStatement = "INSERT INTO actors (actor_name, birth_date) VALUES (?,?)";
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, memberOfFilmCrew.getFullName());
                preparedStatement.setDate(2, Date.valueOf(memberOfFilmCrew.getDateOfBirth()));
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    memberOfFilmCrew.setId(generatedKeys.getLong(1));
                    return Optional.of(memberOfFilmCrew);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public MemberOfFilmCrew getActorById(Long id) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM actors WHERE id = ?")) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate("birth_date").toLocalDate();
                    return new MemberOfFilmCrew(resultSet.getString("actor_name"), birthDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MemberOfFilmCrew getDirectorById(Long id) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM directors WHERE id = ?")) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate("birth_date").toLocalDate();
                    return new MemberOfFilmCrew(resultSet.getString("director_name"), birthDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
