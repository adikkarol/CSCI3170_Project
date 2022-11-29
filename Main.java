import java.util.Scanner;

import menus.*;
import tools.Database;

import java.sql.*;

public class Main {
    Scanner reader = new Scanner(System.in);

    public static void main(String args[]) {
        // Establish connection to the DB
        Database db = new Database();
        try {
            db.establishConnection();
        }
        catch(SQLException e){
            System.err.println(e);
        }
        catch(ClassNotFoundException e){
            System.err.println(e);
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