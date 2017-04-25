package Services;

import Dao.MemberOfFilmCrewDao;
import Entities.MemberOfFilmCrew;

/**
 * Created by Radion on 24.04.2017.
 */
public class MemberOfFilmCrewService {
    private static final Object LOCK = new Object();
    private static MemberOfFilmCrewService INSTANCE = null;

    public static MemberOfFilmCrewService getINSTANCE () {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new MemberOfFilmCrewService();
                }
            }
        }
        return INSTANCE;
    }
    public void createMemberOfFilmCrew (MemberOfFilmCrew memberOfFilmCrew) {
        MemberOfFilmCrewDao.getINSTANCE().save(memberOfFilmCrew);
    }

    public MemberOfFilmCrew getActorById (Long id) {
        return MemberOfFilmCrewDao.getINSTANCE().getActorById(id);
    }

    public MemberOfFilmCrew getDirectorById (Long id) {
        return MemberOfFilmCrewDao.getINSTANCE().getDirectorById(id);
    }
}
