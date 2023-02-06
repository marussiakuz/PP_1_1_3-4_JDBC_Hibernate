package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Util {
    private static Util instance;
    private final SessionFactory sessionFactory;
    private final String user;
    private final String password;
    private final String url;

    private Util() {
        final ResourceBundle bundle = ResourceBundle.getBundle("database");
        user = bundle.getString("db.user");
        password = bundle.getString("db.password");
        url = bundle.getString("db.url");

        Properties properties = new Properties();
        properties.put(Environment.DRIVER, bundle.getString("db.driver"));
        properties.put(Environment.URL, url);
        properties.put(Environment.DIALECT, bundle.getString("db.dialect"));
        properties.put(Environment.HBM2DDL_AUTO, bundle.getString("db.hbm2ddl"));
        properties.put(Environment.USER, user);
        properties.put(Environment.PASS, password);

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (instance == null) {
            instance = new Util();
        }
        return instance.sessionFactory;
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new Util();
        }
        return DriverManager.getConnection(instance.url, instance.user, instance.password);
    }
}
