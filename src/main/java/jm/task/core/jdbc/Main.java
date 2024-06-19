package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Matthew", "McConaughey", (byte) 54);
        userService.saveUser("Charlie", "Hunnam", (byte) 44);
        userService.saveUser("Colin", "Farrell", (byte) 48);
        userService.saveUser("Michelle", "Dockery", (byte) 42);
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
