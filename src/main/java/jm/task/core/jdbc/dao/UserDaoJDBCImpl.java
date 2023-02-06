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
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists task_jdbc.users (" +
                    "id int auto_increment primary key, " +
                    "name varchar(255) not null, " +
                    "last_name varchar(255) not null, " +
                    "age tinyint not null)");
        } catch (SQLException ignored) {
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("drop table if exists task_jdbc.users");
        } catch (SQLException ignored) {
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into task_jdbc.users " +
                     "(name, last_name, age) values (?, ?, ?)")) {
             statement.setString(1, name);
             statement.setString(2, lastName);
             statement.setByte(3, age);

            statement.execute();
            LOGGER.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
        } catch (SQLException ignored) {
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from task_jdbc.users where id = ?")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ignored) {
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from task_jdbc.users");

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
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("truncate task_jdbc.users");
        } catch (SQLException ignored) {
        }
    }
}
