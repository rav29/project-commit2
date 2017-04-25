package Entities;

import java.time.LocalDate;
import java.util.Set;


/**
 * Created by Radion on 26.03.2017.
 */
public class Film {
    private String title;
    private Set<MemberOfFilmCrew> actors;
    private MemberOfFilmCrew director;
    private LocalDate dateOfProduction;
    private String country;
    private Long filmId;
    private Genre genre;

    public Film(String title, Set<MemberOfFilmCrew> actors, MemberOfFilmCrew director, Genre genre, LocalDate dateOfProduction, String country) {
        this.title = title;
        this.actors = actors;
        this.director = director;
        this.genre = genre;
        this.dateOfProduction = dateOfProduction;
        this.country = country;
    }

    public Genre getGenre() {

        return genre;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + title.length();
        result = prime * result + dateOfProduction.getDayOfYear() + dateOfProduction.getYear()
                + dateOfProduction.getDayOfMonth();
        result = result + 5 * Math.toIntExact(filmId);
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getTitle().equals(film.getTitle()) && getDateOfProduction().equals(film.getDateOfProduction())
                && getCountry().equals(film.getCountry()) && getFilmId().equals(film.getFilmId());
    }

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Long getFilmId() {

        return filmId;
    }

    public String getTitle() {
        return title;
    }

    public Set<MemberOfFilmCrew> getActors() {
        return actors;
    }

    public MemberOfFilmCrew getDirector() {
        return director;
    }

    public LocalDate getDateOfProduction() {
        return dateOfProduction;
    }

    public String getCountry() {
        return country;
    }
}
