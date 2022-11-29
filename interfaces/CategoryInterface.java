package interfaces;

import java.sql.*;
import tools.*;

public class CategoryInterface {
    private Database db;
    
    public CategoryInterface(Database db){
        this.db = db;
    }

    public void showInterface(){
        try {
            String catID, catName;

            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Category;");

            System.out.print("| " + "catID" + " | ");
            System.out.println("catName" + " |");

            while (rs.next()){
                catID = rs.getString(1);
                catName = rs.getString(2);
                
                System.out.print("| " + catID + " | ");
                System.out.println(catName + " |");
            }

            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }

    }
}
