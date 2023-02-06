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
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery nativeQuery = session.createNativeQuery("create table if not exists task_jdbc.users (" +
                    "id int auto_increment primary key," +
                    "name varchar(255) not null," +
                    "last_name varchar(255) not null," +
                    "age tinyint not null);");

            Transaction transaction = session.getTransaction();
            transaction.begin();
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }

    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery<User> nativeQuery = session.createNativeQuery("drop table if exists task_jdbc.users", User.class);

            Transaction transaction = session.getTransaction();
            transaction.begin();
            nativeQuery.executeUpdate();
            transaction.commit();
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
            }

            if (transaction.getStatus() == COMMITTED) {
                LOGGER.log(Level.INFO, "User с именем – {0} добавлен в базу данных", name);
            }
        }
    }

    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.createQuery("delete from User where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }

    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            NativeQuery<User> nativeQuery = session.createNativeQuery("truncate task_jdbc.users", User.class);
            Transaction transaction = session.getTransaction();
            transaction.begin();
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }
}
