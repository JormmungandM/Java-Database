package step.learning;

import com.mysql.cj.jdbc.Driver;

import java.math.BigInteger;
import java.sql.*;
import java.util.Random;

public class App {
        public void run() {
            // Registering a driver for MySQL database
            Driver mysqlDriver ;
            try {
                mysqlDriver = new Driver() ;
                DriverManager.registerDriver( mysqlDriver ) ;
            }
            catch( SQLException ex ) {
                System.out.println( "Driver ini error: " + ex.getMessage() ) ;
                return ;
            }
            // connection to СУБД->БД
            String connectionString = "jdbc:mysql://localhost:3306/java191" +  // location
                    "?useUnicode=true&characterEncoding=UTF-8" ;               // encoding
            Connection connection ;
            try {
                connection = DriverManager.getConnection( connectionString, "user191", "pass191" ) ;
            }
            catch( SQLException ex ) {
                System.out.println( "DB Connection error: " + ex.getMessage() ) ;
                return ;
            }

            // region Home INSERT
            Random rnd = new Random() ;
            int rndID = rnd.nextInt(555) ;
            int rndNum = rnd.nextInt(777) ;
            String rndStr = String.valueOf(rnd.nextInt(999));

            String sql = "CREATE TABLE IF NOT EXISTS randoms ( " +
                    "id BIGINT PRIMARY KEY," +
                    "num INT NOT NULL," +
                    "str VARCHAR(64) NULL" +
                    ") Engine=InnoDB DEFAULT CHARSET = UTF8";
            String insertSql ="INSERT INTO randoms VALUES ( "
                    + rndID + ","
                    + rndNum + ","
                    + rndStr
                    + " )";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);  // вариант без возврата данных
                statement.executeUpdate(insertSql);
                System.out.println("Query OK");
            } catch (SQLException e) {
                System.out.println("DB connection error! "+ e.getMessage());
                return;
            }
            // endregion

            // Command Execution: Separate Methods for Return and Non-Return Commands
            // region CREATE TABLE
//            String sql = "CREATE TABLE  IF NOT EXISTS  randoms ( " +
//                    "id BIGINT PRIMARY KEY," +
//                    "num INT NOT NULL," +
//                    "str VARCHAR(64) NULL" +
//                    ") Engine=InnoDB  DEFAULT CHARSET = UTF8" ;
//            try {
//                Statement statement = connection.createStatement() ;
//                statement.executeUpdate( sql ) ;   // вариант без возврата данных
//                System.out.println( "Query OK" ) ;
//            }
//            catch( SQLException ex ) {
//                System.out.println( "Query error: " + ex.getMessage() ) ;
//                return ;
//            }
            // endregion

            // Executing queries
            // region INSERT
//            Random rnd = new Random() ;
//            int rndInt = rnd.nextInt() ;
//            String rndStr = "Str " + rnd.nextInt() ;
//
//            sql = String.format(
//                    "INSERT INTO randoms VALUES ( UUID_SHORT(), %d, '%s' )",
//                    rndInt, rndStr ) ;
//            try( Statement statement = connection.createStatement() ) {
//                statement.executeUpdate( sql ) ;
//                System.out.println( "Insert OK" ) ;
//            }
//            catch( SQLException ex ) {
//                System.out.println( "Query error: " + ex.getMessage() ) ;
//                System.out.println( sql ) ;
//                return ;
//            }
            // endregion

            // region SELECT
//            sql = "SELECT r.id, r.num, r.str FROM randoms AS r" ;
//            try( Statement statement = connection.createStatement() ) {
//                ResultSet res =   // ADO ~ DataReader
//                        statement.executeQuery( sql ) ;
//                while( res.next() ) {  // ADO ~ .Read()  -- построчное считывание
//                    System.out.printf( "%d  %d  %s %n",
//                            res.getLong( 1 ),       // getT - получить значение из "строки" таблицы
//                            res.getInt( "num" ),    // !! JDBC - номерация начинается с 1 (ADO - c 0)
//                            res.getString( 3 )      // можно обращаться по имени поля
//                    ) ;
//                }
//                res.close() ;
//            }
//            catch( SQLException ex ) {
//                System.out.println( "Query error: " + ex.getMessage() ) ;
//                System.out.println( sql ) ;
//                return ;
//            }
            //endregion

            // region Prepared queries
//            sql = "INSERT INTO randoms VALUES ( UUID_SHORT(), ?, ? )" ;
//            try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
//                for( int i = 100500; i < 100503; ++i ) {
//                    rndStr = "Prep " + rnd.nextInt();
//                    prep.setInt(1, i ) ;
//                    prep.setString(2, rndStr ) ;
//                    prep.executeUpdate();
//                }
//                System.out.println( "Prep OK" ) ;
//            }
//            catch( SQLException ex ) {
//                System.out.println( "Query error: " + ex.getMessage() ) ;
//                System.out.println( sql ) ;
//                return ;
//            }

            // endregion

            // region ORDER BY
//            System.out.println( "------------------------------------------------" ) ;
//            sql = "SELECT * FROM randoms ORDER BY num DESC " ;
//            try( Statement statement = connection.createStatement() ;
//                 ResultSet res = statement.executeQuery( sql ) ) {
//                while( res.next() ) {
//                    System.out.printf( "%d  %d  %s %n",
//                            res.getLong( 1 ), res.getInt( "num" ), res.getString( 3 ) ) ;
//                }
//            }
//            catch( SQLException ex ) {
//                System.out.println( "Query error: " + ex.getMessage() ) ;
//                System.out.println( sql ) ;
//                return ;
//            }
            // endregion


            try {
                connection.close() ;
                DriverManager.deregisterDriver( mysqlDriver ) ;
            } catch( SQLException ignored ) {}
        }
}
