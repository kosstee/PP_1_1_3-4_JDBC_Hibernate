package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        StringBuilder query = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS users (")
                .append("id SERIAL PRIMARY KEY,")
                .append("name VARCHAR(255) NOT NULL,")
                .append("lastName VARCHAR(255) NOT NULL,")
                .append("age TINYINT UNSIGNED);");
        try (var connection = Util.getConnection()) {
            connection.prepareStatement(query.toString()).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS users;";
        try (var connection = Util.getConnection()) {
            connection.prepareStatement(query).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (var connection = Util.getConnection()) {
            var prepState = connection.prepareStatement(query);
            prepState.setString(1, name);
            prepState.setString(2, lastName);
            prepState.setByte(3, age);
            prepState.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (var connection = Util.getConnection()) {
            var prepState = connection.prepareStatement(query);
            prepState.setLong(1, id);
            prepState.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users;";
        List<User> users = new ArrayList<>();
        try (var connection = Util.getConnection()) {
            var resultSet = connection.prepareStatement(query).executeQuery();
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

    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE users;";
        try (var connection = Util.getConnection()) {
            connection.prepareStatement(query).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
