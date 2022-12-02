package interfaces;

import java.sql.*;
import tools.*;

public class SalespersonInterface {
    private Database db;
    
    public SalespersonInterface(Database db){
        this.db = db;
    }

    public void showInterface(){
        try {
            String salesID, salesName, salesAddress, salesPhone, salesExperience;
            
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM salesperson;");

            System.out.print("| " + "s_id" + " | ");
            System.out.print("s_name" + " | ");
            System.out.print("s_address" + " | ");
            System.out.print("s_phone_number" + " | ");
            System.out.println("s_experience" + " |");

            while (rs.next()){
                salesID = rs.getString(1);
                salesName = rs.getString(2);
                salesAddress = rs.getString(3);
                salesPhone = rs.getString(4);
                salesExperience = rs.getString(5);

                System.out.print("| " + salesID + " | ");
                System.out.print(salesName + " | ");
                System.out.print(salesAddress + " | ");
                System.out.print(salesPhone + " | ");
                System.out.println(salesExperience + " |");
            }

            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }

    }

}
