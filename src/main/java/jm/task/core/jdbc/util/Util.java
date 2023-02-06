package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Util {
    private static Util instance;
    private final String user;
    private final String password;
    private final String url;

    private Util() {
        final ResourceBundle bundle = ResourceBundle.getBundle("database");
        user = bundle.getString("db.user");
        password = bundle.getString("db.password");
        url = bundle.getString("db.url");
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new Util();
        }
        return DriverManager.getConnection(instance.url, instance.user, instance.password);
    }
}
