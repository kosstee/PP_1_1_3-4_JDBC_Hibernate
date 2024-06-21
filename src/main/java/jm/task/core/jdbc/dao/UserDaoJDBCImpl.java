package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                lastName VARCHAR(255) NOT NULL,
                age TINYINT UNSIGNED)""";
        try (var connection = Util.getConnection();
             var prepStmt = connection.prepareStatement(query)) {
            prepStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS users";
        try (var connection = Util.getConnection();
             var prepStmt = connection.prepareStatement(query)) {
            prepStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (var connection = Util.getConnection();
             var prepStmt = connection.prepareStatement(query)) {
            prepStmt.setString(1, name);
            prepStmt.setString(2, lastName);
            prepStmt.setByte(3, age);
            prepStmt.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (var connection = Util.getConnection();
             var prepStmt = connection.prepareStatement(query)) {
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (var connection = Util.getConnection();
             var prepStmt = connection.prepareStatement(query);
             var resultSet = prepStmt.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE users";
        try (var connection = Util.getConnection();
             var prepStmt = connection.prepareStatement(query)) {
            prepStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
