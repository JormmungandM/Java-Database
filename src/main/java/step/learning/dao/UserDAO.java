package step.learning.dao;

import step.learning.entities.User;
import step.learning.services.hash.HashService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;

@Singleton
public class UserDAO {
    private final Connection connection ;
    private final HashService hashService;

    @Inject
    public UserDAO( Connection connection,HashService hashService ) {
        this.connection = connection ;
        this.hashService = hashService;
    }

    /**
     * Inserts user in DB `Users` table
     * @param user data to insert
     * @return `id` of new record or null if fails
     */
    public String add( User user ) {
        // generate id for new post
        String id = UUID.randomUUID().toString() ;
        // generate salt
        String salt = hashService.hash( UUID.randomUUID().toString() );
        // generate hash password
        String passHash = this.hashPassword( user.getPass(), salt );
        // preparing a request (substituting the entered data)
        String sql = "INSERT INTO Users( id, login, pass, name, salt ) VALUES(?,?,?,?,?)" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            prep.setString( 2, user.getLogin() ) ;
            prep.setString( 3, passHash  ) ;
            prep.setString( 4, user.getName()  ) ;
            prep.setString( 5, salt  ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }
        return id ;
    }

    /**
     * Checks User table for login given
     * @param login value to look for
     * @return  true if login is in table
     */
    public boolean isLoginUsed(String login){
        String sql = "SELECT COUNT(u.id) FROM users u WHERE u.login=?";

        try ( PreparedStatement prep = connection.prepareStatement(sql))
        {
            prep.setString(1, login );
            ResultSet res = prep.executeQuery();
            res.next();
            return  res.getInt(1) > 0;

        } catch (SQLException ex) {
            System.out.println("DB connection error! "+ ex.getMessage());
            System.out.println(sql);
            return true;
        }
    }

    /**
     * Calculates hash (optionally salted) from password
     * @param password Open password string
     * @return  hash for DB table
     */
    public String hashPassword(String password, String salt){
        return hashService.hash( salt + password + salt  );
    }

    public User getUserByCredentials(String login, String password) {
        String sql = "SELECT u.* FROM users u WHERE u.login=?";                 // creating request

        try ( PreparedStatement prep = connection.prepareStatement( sql ) ) {

            prep.setString(1, login );
            ResultSet res = prep.executeQuery();

            if(res.next()) {
                User user = new User(res);
                // password - open password, user.pass - hash(password,user.salt)
                String expectedHash = this.hashPassword( password,user.getSalt());
                if( expectedHash.equals(user.getPass())){   // checking if password is created with salt
                    return user;
                }

                String simpleHash = hashService.hash(password);
                if(simpleHash.equals(user.getPass())){  // checking if password is created without salt
                    return user;
                }
            }

        }
        catch (SQLException ex) {
            System.out.println("DB connection error! " + ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }


    public User getUserByCredentialsOld(String login, String password) {
        String sql = "SELECT u.* FROM users u WHERE u.login=? AND u.pass=?";

        try ( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString(1, login );
            prep.setString(2,  password );
            ResultSet res = prep.executeQuery();
            if(res.next()) return new User(res);

        }
        catch (SQLException ex) {
            System.out.println("DB connection error! " + ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
}