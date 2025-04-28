
import java.sql.*;
import java.util.*;


public class DatabaseHandler {

    public static Connection connection;

    public static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb",
                        "root", "abcd1234");

    return connection;
    }
}
