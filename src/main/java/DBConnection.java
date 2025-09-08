
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://yamanote.proxy.rlwy.net:58736/electricity_db", "root", "RpZLDRFfrjUKgxYfUIwfTASbiVvUAnEA");
    }
}

