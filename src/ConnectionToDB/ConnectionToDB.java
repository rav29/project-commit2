package ConnectionToDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Radion on 06.04.2017.
 */
public class ConnectionToDB {
    private static final String URL = "jdbc:mysql://localhost:3306/music_store";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD)){
            return connection;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
