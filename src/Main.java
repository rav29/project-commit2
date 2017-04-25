import Entities.User;
import Services.UserService;

/**
 * Created by Radion on 26.03.2017.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(UserService.getINSTANCE().getById(1L).toString());
    }
}
