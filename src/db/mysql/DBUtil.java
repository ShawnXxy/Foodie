package db.mysql;

import java.util.Properties;

public class DBUtil {
//    private static final String HOSTNAME = "localhost";
////    private static final String HOSTNAME = "dockertest-mysql";
//    private static final String PORT_NUM = "3306";
//    public static final String DB_NAME = "foodie";
//    private static final String USERNAME = "root";
//    private static final String PASSWORD = "root";
//    public static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME + "?user=" + USERNAME + "&password=" + PASSWORD + "&autoreconnect=true";
    
    // Azure Database for MySQL
    private static final String HOSTNAME = "az-foodie-db.mysql.database.azure.com"; // az-foodie-db.mysql.database.azure.com 52.175.33.150
    private static final String PORT_NUM = "3306";
    public static final String DB_NAME = "foodie";
    static final String USERNAME = "x_xiao2_bnu@az-foodie-db";
    static final String PASSWORD = "XXy@4592995";
    public static final String URL = String.format("jdbc:mysql://%s/%s", HOSTNAME, DB_NAME);
    

}
