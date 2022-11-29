package interfaces;

import java.sql.*;
import tools.*;

public class PartInterface {
    private Database db;
    
    public PartInterface(Database db){
        this.db = db;
    }

    public void showInterface(){
        try {
            String partID, partName, partPrice, partWarranty, partAvilQuan, manuID, catID;

            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Part;");

            System.out.print("| " + "partID" + " | ");
            System.out.print("partName" + " | ");
            System.out.print("partPrice" + " | ");
            System.out.print("manuID" + " | ");
            System.out.print("catID" + " | ");
            System.out.print("partWarranty" + " | ");
            System.out.println("partAvilQuan" + " |");

            while (rs.next()){
                partID = rs.getString(1);
                partName = rs.getString(2);
                partPrice = rs.getString(3);
                manuID = rs.getString(4);
                catID = rs.getString(5);
                partWarranty = rs.getString(6);
                partAvilQuan = rs.getString(7);
                
                System.out.print("| " + partID + " | ");
                System.out.print(partName + " | ");
                System.out.print(partPrice + " | ");
                System.out.print(manuID + " | ");
                System.out.print(catID + " | ");
                System.out.print(partWarranty + " | ");
                System.out.println(partAvilQuan + " |");
            }

            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }

    }
}
