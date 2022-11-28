import java.util.Scanner;
import menuPackage.*;
import tools.Database;

import java.sql.*;

public class Main {
    static Scanner reader = new Scanner(System.in);

    public static void main(String args[]) {
        // Establish connection to the DB
        Database db = new Database();
        try {
            db.establishConnection();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
        
        MainMenu mainMenu = new MainMenu(db);
        mainMenu.callMainMenu();
        
        try {
            db.conn.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
    }
}