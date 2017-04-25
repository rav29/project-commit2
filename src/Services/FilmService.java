package Services;

import Dao.FilmDao;
import Entities.Film;
import Entities.MemberOfFilmCrew;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by Radion on 24.04.2017.
 */
public class FilmService {
    private static final Object LOCK = new Object();
    private static FilmService INSTANCE = null;

    public static FilmService getINSTANCE () {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new FilmService();
                }
            }
        }
        return INSTANCE;
    }

    public void saveFilm (Film film) {
        FilmDao.getINSTANCE().save(film);
    }

    public Film getFilmById (Long id) {
        return FilmDao.getINSTANCE().getFilmById(id);
    }

    public Set<MemberOfFilmCrew> getActorsByFilmId (Long id) {
        return FilmDao.getINSTANCE().getSetOfActors(id);
    }

    public MemberOfFilmCrew getFilmDirectorByFilmId (Long id) {
        return FilmDao.getINSTANCE().getDirectorByFilmId(id);
    }

    public Set<Film> getFilmByYear (LocalDate dateOfRelease) {
        return FilmDao.getINSTANCE().getFilmsByYear(dateOfRelease);
    }

    public Set<Film> getFilmByActrorId (Long id) {
        return FilmDao.getINSTANCE().getFilmsByActor(id);
    }


}
