package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "last_name VARCHAR(255) NOT NULL, " +
                "age TINYINT NOT NULL)";

        try (Connection connection = Util.getConnection();) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "failed to create a table users due to an error - {0}", e.getMessage());
            }

        } catch (SQLException ignore) {
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "failed to drop table users due to an error - {0}", e.getMessage());
            }

        } catch (SQLException ignore) {
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);
                statement.execute();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "failed to save user with name - {0} due to an error - {1}",
                        new String[] {name, e.getMessage()});
            }

            LOGGER.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
        } catch (SQLException ignore) {
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.execute();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "failed to remove user with id - {0} due to an error - {1}",
                        new String[] {String.valueOf(id), e.getMessage()});
            }

        } catch (SQLException ignore) {
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException ignored) {
        }

        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE users";

        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "failed to clear table users due to an error - {0}", e.getMessage());
            }

        } catch (SQLException ignore) {
        }
    }
}
