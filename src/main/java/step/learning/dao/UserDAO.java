package step.learning.dao;

import step.learning.entities.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Singleton
public class UserDAO {
    private final Connection connection ;
    @Inject
    public UserDAO( Connection connection ) {
        this.connection = connection ;
    }

    /**
     * Inserts user in DB `Users` table
     * @param user data to insert
     * @return `id` of new record or null if fails
     */
    public String add( User user ) {
        // generate id for new post
        String id = UUID.randomUUID().toString() ;
        // preparing a request (substituting the entered data)
        String sql = "INSERT INTO Users(`id`,`login`,`pass`,`name`) VALUES(?,?,?,?)" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            prep.setString( 2, user.getLogin() ) ;
            prep.setString( 3, user.getPass()  ) ;
            prep.setString( 4, user.getName()  ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }
        return id ;
    }
}