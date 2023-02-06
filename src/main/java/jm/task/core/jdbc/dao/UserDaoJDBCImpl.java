package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery query = session.createSQLQuery("create table if not exists task_jdbc.users (" +
                    "id int auto_increment primary key," +
                    "name varchar(255) not null," +
                    "last_name varchar(255) not null," +
                    "age tinyint not null);");
            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }

    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery query = session.createSQLQuery("drop table if exists task_jdbc.users");
            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery query = session.createSQLQuery("insert into task_jdbc.users (name, last_name, age) " +
                    "values (:name, :lastName, :age)");
            query
                    .setParameter("name", name)
                    .setParameter("lastName", lastName)
                    .setParameter("age", age);

            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            LOGGER.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
            transaction.commit();
        }
    }

    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery query = session.createSQLQuery("delete from task_jdbc.users where id = :id");
            query.setParameter("id", id);
            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }

    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery<User> nativeQuery = session.createNativeQuery("select * from task_jdbc.users", User.class);
            return nativeQuery.getResultList();
        }
    }

    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery query = session.createSQLQuery("truncate task_jdbc.users");
            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }
}
