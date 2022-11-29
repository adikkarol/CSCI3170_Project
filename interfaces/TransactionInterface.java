package interfaces;

import java.sql.*;
import tools.*;

public class TransactionInterface {
    private Database db;
    
    public TransactionInterface(Database db){
        this.db = db;
    }

    public void showInterface(){
        try {
            String transID, transDate, partID, salesID;
            String[] yyyymmdd;

            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Transaction;");

            System.out.print("| " + "transID" + " | ");
            System.out.print("partID" + " | ");
            System.out.print("salesID" + " | ");
            System.out.println("transDate" + " | ");

            while (rs.next()){
                transID = rs.getString(1);
                partID = rs.getString(2);
                salesID = rs.getString(3);
                yyyymmdd = rs.getString(4).split("-");
                transDate = yyyymmdd[2] + "/" + yyyymmdd[1] + "/" + yyyymmdd[0];
                
                System.out.print("| " + transID + " | ");
                System.out.print(partID + " | ");
                System.out.print(salesID + " | ");
                System.out.println(transDate + " | ");
            }

            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }

    }
}
