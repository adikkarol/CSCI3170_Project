package tools;
import java.sql.*;

public class Database {
    
    public Connection conn = null;

    public static String address = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db23?autoReconnect=true&useSSL=false";
    private static String username = "Group23";
    private static String password = "CSCI3170";

    public void establishConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(address, username, password);
    }

}

