package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("One", "First", (byte) 20);
        userService.saveUser("Two", "Second", (byte) 25);
        userService.saveUser("Three", "Third", (byte) 30);
        userService.saveUser("Four", "Fourth", (byte) 50);

        userService.getAllUsers().forEach(System.out::println);

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
