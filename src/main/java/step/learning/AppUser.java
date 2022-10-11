package step.learning;

import step.learning.dao.UserDAO;
import step.learning.entities.User;
import step.learning.services.hash.HashService;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class AppUser {
    private final Connection connection;   // injected field
    private final HashService hashService;
    private final UserDAO userDAO;

    @Inject
    public AppUser(Connection connection, HashService hashService, UserDAO userDAO) {
        this.connection = connection;
        this.hashService = hashService;
        this.userDAO = userDAO;
    }

    public void run() {
        String sql = "CREATE TABLE  IF NOT EXISTS  Users (" +
                "    `id`    CHAR(36)     NOT NULL   COMMENT 'UUID'," +
                "    `login` VARCHAR(32)  NOT NULL," +
                "    `pass`  CHAR(40)     NOT NULL   COMMENT 'SHA-160 hash'," +
                "    `name`  TINYTEXT     NOT NULL," +
                "    PRIMARY KEY(id)" +
                " ) Engine=InnoDB  DEFAULT CHARSET = UTF8" ;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println(sql);
            return;
        }

        Scanner kbScanner = new Scanner(System.in);
        System.out.print("1 - Registration\n2 - Log in\nEnter choice: ");
        int userChoice = kbScanner.nextInt();

        if (userChoice == 1) {
            if(regUser()) {
                System.out.println("User created");
            } else System.out.println("User creation error");
        }

    }

    private boolean regUser() {

        Scanner kbScanner = new Scanner( System.in );
        String login, password;

        // region Enter login
        while (true) {

            System.out.print("Enter login: ");
            login = kbScanner.nextLine();
            if (login.equals("")) {                     //checking for an empty string
                System.out.println("Login count not be empty");
                continue;
            }
            if (userDAO.isLoginUsed(login)) {           //check if login is free
                System.out.println("Login is use");
                continue;
            }
            break;
        }
        // endregion

        // region Enter password
        while (true) {

            System.out.print("Enter password: ");
            password = kbScanner.nextLine();
            if (password.equals("")) {                          //checking for an empty string
                System.out.println("Password required! ");
                continue;
            }

            //  Repeat password
            System.out.print("Repeat password: ");
            if (!kbScanner.nextLine().equals(password)) {
                System.out.println("Password mismatch! ");
                continue;
            }
            break;
        }
        // endregion

        //region Enter user name
        System.out.print( "Enter real name: ");
        String name = kbScanner.nextLine();
        kbScanner.close();
        //endregion

        //region Create new User
        User user = new User();
        user.setLogin( login );         // adding login
        user.setPass( password );       // adding password
        user.setName( name );           // adding name
        //endregion

        //region Adding User to db
        String id = userDAO.add( user );
        if ( id == null ) {
            System.out.println( "Registration error" );
            return false;
        }
        else {
            System.out.println( "OK, id; " + id );
            return true;
        }
        //endregion
    }
}