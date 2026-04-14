package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private DatabaseConnection() {}

    private static final String URL = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
    private static final String USER = "heikki";
    private static final String PASS = System.getenv("DB_PASS");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}