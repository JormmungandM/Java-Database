package step.learning;

import com.google.inject.Guice;

public class Main {
    public static void main( String[] args ) {
        try( ConfigModule configModule = new ConfigModule() ) {
            Guice
                    .createInjector( configModule )
                    .getInstance( App.class )
                    .run() ;
        }
        catch( Exception ex ) {
            System.out.println( "Program terminated: " + ex.getMessage() ) ;
        }
    }
}