package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hibernate.resource.transaction.spi.TransactionStatus.*;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "last_name VARCHAR(255) NOT NULL, " +
                "age TINYINT NOT NULL)";

        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery<User> nativeQuery = session.createNativeQuery(sql, User.class);

            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                nativeQuery.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.getStatus() == ACTIVE || transaction.getStatus() == MARKED_ROLLBACK) {
                    transaction.rollback();
                }
                LOGGER.log(Level.SEVERE, "failed to create a table users due to an error - {0}", e.getMessage());
            }
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery<User> nativeQuery = session.createNativeQuery(sql, User.class);

            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                nativeQuery.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.getStatus() == ACTIVE || transaction.getStatus() == MARKED_ROLLBACK) {
                    transaction.rollback();
                }
                LOGGER.log(Level.SEVERE, "failed to drop table users due to an error - {0}", e.getMessage());
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);

            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                session.save(user);
                transaction.commit();
            }
            catch (Exception e) {
                if (transaction.getStatus() == ACTIVE || transaction.getStatus() == MARKED_ROLLBACK) {
                    transaction.rollback();
                }
                LOGGER.log(Level.SEVERE, "failed to save user with name - {0} due to an error - {1}",
                        new String[] {name, e.getMessage()});
            }

            if (transaction.getStatus() == COMMITTED) {
                LOGGER.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
            }
        }
    }

    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {

            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                session.createQuery("DELETE FROM User WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.getStatus() == ACTIVE || transaction.getStatus() == MARKED_ROLLBACK) {
                    transaction.rollback();
                }
                LOGGER.log(Level.SEVERE, "failed to remove user with id - {0} due to an error - {1}",
                        new String[] {String.valueOf(id), e.getMessage()});
            }
        }
    }

    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        }
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE users";

        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery<User> nativeQuery = session.createNativeQuery(sql, User.class);

            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                nativeQuery.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.getStatus() == ACTIVE || transaction.getStatus() == MARKED_ROLLBACK) {
                    transaction.rollback();
                }
                LOGGER.log(Level.SEVERE, "failed to clear table users due to an error - {0}", e.getMessage());
            }
        }
    }
}
