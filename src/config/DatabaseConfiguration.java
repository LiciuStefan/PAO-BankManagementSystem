package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {

    private static DatabaseConfiguration instance;
    private final Connection databaseConnection;

    private DatabaseConfiguration(){
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/sys";
            String username = "root";
            String pass = "root1234";
            databaseConnection = DriverManager.getConnection(url, username, pass);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConfiguration getInstance(){
        if(instance == null)
            return new DatabaseConfiguration();
        return instance;
    }
    public Connection getConnection(){
        return databaseConnection;
    }


}