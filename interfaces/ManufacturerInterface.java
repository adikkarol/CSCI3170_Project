package interfaces;

import java.sql.*;
import tools.*;

public class ManufacturerInterface {
    private Database db;
    
    public ManufacturerInterface(Database db){
        this.db = db;
    }

    public void showInterface(){
        try {
            String manuID, manuName, manuAddress, manuPhone;
            
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM manufacturer;");

            System.out.print("| " + "m_id" + " | ");
            System.out.print("m_name" + " | ");
            System.out.print("m_address" + " | ");
            System.out.println("m_phone_number" + " |");

            while (rs.next()){
                manuID = rs.getString(1);
                manuName = rs.getString(2);
                manuAddress = rs.getString(3);
                manuPhone = rs.getString(4);
                
                System.out.print("| " + manuID + " | ");
                System.out.print(manuName + " | ");
                System.out.print(manuAddress + " | ");
                System.out.println(manuPhone + " |");
            }

            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }

    }
}
