package Dao;

import ConnectionToDB.ConnectionToDB;
import Entities.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Radion on 16.04.2017.
 */
public class ReviewDao {
    private static final Object LOCK = new Object();
    private static ReviewDao INSTANCE = null;

    public static ReviewDao getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ReviewDao();
                }
            }
        }
        return INSTANCE;
    }

    public Optional<Review> save(Review review) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO reviews (film_id, user_id, text, mark)VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, review.getFilm().getFilmId());
                preparedStatement.setLong(2, review.getUser().getUserId());
                preparedStatement.setString(3, review.getText());
                preparedStatement.setInt(4, review.getMark());
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    review.setReviewId(generatedKeys.getLong(1));
                    return Optional.of(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Review> getReviewsByFilmId(Long filmId) {
        String request = "SELECT * FROM reviews WHERE reviews.film_id=?;";
        return getListOfReviews(filmId, request);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        String request = "SELECT * FROM reviews WHERE reviews.user_id=?;";
        return getListOfReviews(userId, request);
    }

    public Review getReviewById(Long reviewId) {
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM reviews WHERE reviews.id = ?;")) {
                preparedStatement.setLong(1, reviewId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    return new Review(FilmDao.getINSTANCE().getFilmById(resultSet.getLong("film_id")),
                            UserDao.getINSTANCE().getUserById(resultSet.getLong("user_id")),
                            resultSet.getString("text"), resultSet.getInt("mark"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Review> getListOfReviews (Long id, String searchByIdSQLRequest) {
        List<Review> resultList = new ArrayList<>();
        try (Connection connection = ConnectionToDB.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(searchByIdSQLRequest)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                int index = 0;
                while (resultSet.next()) {
                    resultList.add(index, new Review(FilmDao.getINSTANCE().getFilmById(resultSet.getLong("film_id")),
                            UserDao.getINSTANCE().getUserById(resultSet.getLong("user_id")),
                            resultSet.getString("text"), resultSet.getInt("mark")));
                    index++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
