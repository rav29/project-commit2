package Entities;

/**
 * Created by Radion on 26.03.2017.
 */
public class Review {
    private Film film;
    private User user;
    private String text;
    private int mark;
    private Long reviewId;

    public Review(Film film, User user, String text, int mark) {
        this.film = film;
        this.user = user;
        this.text = text;
        this.mark = mark;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getReviewId() {

        return reviewId;
    }

    public Film getFilm() {
        return film;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public int getMark() {
        return mark;
    }
}
