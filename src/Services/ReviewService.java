package Services;

import Dao.ReviewDao;
import Entities.Review;

import java.util.List;

/**
 * Created by Radion on 24.04.2017.
 */
public class ReviewService {
    private static final Object LOCK = new Object();
    private static ReviewService INSTANCE = null;

    public static ReviewService getINSTANCE () {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ReviewService();
                }
            }
        }
        return INSTANCE;
    }

    public void saveReview (Review review) {
        ReviewDao.getINSTANCE().save(review);
    }


    public List<Review> getReviewsByFilmId (Long id) {
        return ReviewDao.getINSTANCE().getReviewsByFilmId(id);
    }

    public List<Review> getReviewsByUserId (Long id) {
        return ReviewDao.getINSTANCE().getReviewsByUserId(id);
    }

    public Review getReviewById (Long id) {
        return ReviewDao.getINSTANCE().getReviewById(id);
    }
}
