package Services;

import Dao.UserDao;
import Entities.User;

/**
 * Created by Radion on 24.04.2017.
 */
public class UserService {
    private static final Object LOCK = new Object();
    private static UserService INSTANCE = null;

    public static UserService getINSTANCE () {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE==null) {
                    INSTANCE = new UserService();
                }
            }
        }
        return INSTANCE;
    }
    public void createUser (User user) {
        UserDao.getINSTANCE().save(user);
    }

    public User getById (Long userId) {
        return UserDao.getINSTANCE().getUserById(userId);
    }
}
