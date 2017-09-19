package db;

import db.mongodb.MongoDBConnection;
import db.mysql.MySQLDBConnection;

public class DBConnectionFactory {

    // This should change based on the pipeline
    private static final String DEFAULT_DB = "mysql";
    // private static final String DEFAULT_DB = "mongodb";

    // Create a DBConnection based on given db type
    public static DBConnection getDBConnection(String db) {
        switch (db) {
        case "mysql":
            return MySQLDBConnection.getInstance();
        case "mongodb":
            return MongoDBConnection.getInstance();
        default:
            throw new IllegalArgumentException("Invalid db " + db);
        }
    }

    // Overloading
    public static DBConnection getDBConnection() {
        return getDBConnection(DEFAULT_DB);
    }
}