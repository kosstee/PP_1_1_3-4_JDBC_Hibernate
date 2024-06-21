package jm.task.core.jdbc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) {

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);

        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Matthew", "McConaughey", (byte) 54);
        userService.saveUser("Charlie", "Hunnam", (byte) 44);
        userService.saveUser("Colin", "Farrell", (byte) 48);
        userService.saveUser("Michelle", "Dockery", (byte) 42);
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();

        Util.shutdown();
    }
}
