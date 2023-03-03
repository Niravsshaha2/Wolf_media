package main.java.wolfmedia;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

public class WolfmediaApp {

    static final String DEFAULT_CONFIG = "config.properties";

    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();

        String configFileName = DEFAULT_CONFIG;

        if (args.length > 0 && args[0] != null) {
            configFileName = args[0];
        }

        ClassLoader classLoader = WolfmediaApp.class.getClassLoader();

        URL res = Objects.requireNonNull(classLoader.getResource(configFileName),
                "Can't find configuration file " + configFileName);

        InputStream is = new FileInputStream(res.getFile());

        prop.load(is);

        String driver = prop.getProperty("driver");
        String jdbcURL = prop.getProperty("jdbcURL");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");

        System.out.println("Driver: " + driver);
        System.out.println("JDBC URL: " + jdbcURL);
        System.out.println("User: " + user);

        Connection connection = null;

        try {
            Class.forName(driver);

            connection = DriverManager.getConnection(jdbcURL, user, password);

            DatabaseOperations.clearDatabase(connection);
            DatabaseOperations.createTables(connection);

        } catch(Throwable oops) {
            oops.printStackTrace();
        } finally {
            close(connection);
        }
    }

    static void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch(Throwable whatever) {}
        }
    }
}