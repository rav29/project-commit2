package Entities;

/**
 * Created by Radion on 26.03.2017.
 */
public class User {
    private String fullName;
    private String password;
    private String email;
    private Long userId;

    public User(String fullName, String password, String email) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
    }

    public User(String fullName, String password, String email, Long userId) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public Long getUserId() {

        return userId;
    }
}
