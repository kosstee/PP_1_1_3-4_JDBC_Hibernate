package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Objects;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction tx = null;
        try (var session = Util.getSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery("""
                    CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    lastName VARCHAR(255) NOT NULL,
                    age TINYINT UNSIGNED)""").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx = null;
        try (var session = Util.getSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction tx = null;
        try (var session = Util.getSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (var session = Util.getSession()) {
            tx = session.beginTransaction();
            session.remove(session.get(User.class, id));
            tx.commit();
        } catch (HibernateException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try (var session = Util.getSession()) {
            SelectionQuery<User> query = session.createQuery("from User", User.class);
            users = query.getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        try (var session = Util.getSession()) {
            tx = session.beginTransaction();
            getAllUsers().forEach(session::remove);
            tx.commit();
        } catch (HibernateException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
