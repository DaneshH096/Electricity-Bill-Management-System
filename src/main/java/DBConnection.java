
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv("DB_PORT");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
                   + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        // Use env-based credentials
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
}


