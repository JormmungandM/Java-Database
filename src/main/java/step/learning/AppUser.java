package step.learning;

import step.learning.dao.UserDAO;
import step.learning.entities.User;
import step.learning.services.hash.HashService;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AppUser {
    private final Connection connection ;   // injected field
    private final HashService hashService ;
    private final UserDAO userDAO ;

    @Inject
    public AppUser( Connection connection, HashService hashService, UserDAO userDAO ) {
        this.connection = connection ;
        this.hashService = hashService ;
        this.userDAO = userDAO ;
    }

    public void run() {
        String sql = "CREATE TABLE  IF NOT EXISTS  Users (" +
                "    `id`    CHAR(36)     NOT NULL   COMMENT 'UUID'," +
                "    `login` VARCHAR(32)  NOT NULL," +
                "    `pass`  CHAR(40)     NOT NULL   COMMENT 'SHA-160 hash'," +
                "    `name`  TINYTEXT     NOT NULL," +
                "    PRIMARY KEY(id)" +
                " ) Engine=InnoDB  DEFAULT CHARSET = UTF8" ;
        try( Statement statement = connection.createStatement() ) {
            statement.executeUpdate( sql ) ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
            return ;
        }
        System.out.print( "1 - Registration\n2 - Log in\nEnter choice: " ) ;
        Scanner kbScanner = new Scanner( System.in ) ;
        int userChoice = kbScanner.nextInt() ;
        if( userChoice == 1 ) {
            User user = new User() ;
            user.setLogin( "user" ) ;
            user.setPass( hashService.hash( "123" ) ) ;
            user.setName( "Super User" ) ;
            String id = userDAO.add( user ) ;
            if( id == null ) {
                System.out.println( "Registration error" ) ;
            }
            else {
                System.out.println( "OK, id: " + id ) ;
            }
        }
    }
}