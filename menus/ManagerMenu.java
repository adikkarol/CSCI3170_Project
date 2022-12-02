package menus;
import java.util.Scanner;
import java.sql.*;
import tools.*;

public class ManagerMenu {

    private Database db;
    private Scanner reader = new Scanner(System.in); 

    // Constructor
    public ManagerMenu(Database db){
        this.db = db;
    }

    // Call Manager Menu
    public void callManagerMenu(){
        int choice = 0;
        try {
            System.out.println("-----Operations for manager menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. List all salespersons");
            System.out.println("2. Count the no. of sales record of each salesperson under a specific range on years of experience");
            System.out.println("3. Show the total sales value of each manufacturer");
            System.out.println("4. Show the N most popular part");
            System.out.println("5. Return to the main menu");
        
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(reader.nextLine());

            switch(choice){
                case 1:
                    listAllSalespersons();
                    break;
                case 2:
                    countTrans();
                    break;
                case 3:
                    showTotalSales();
                    break;
                case 4:
                    showNMostPop();
                    break;
                case 5:
                    System.out.println();
                    break;
            }  
        }
        catch(NumberFormatException e){
            System.err.println(e);
            // choice = -1;
        }


    }

    // 1. List all salespersons
    private void listAllSalespersons(){
        int choiceOrder = 0;
        String order = null;

        System.out.println("Choose ordering: ");
        System.out.println("1. By ascending order");
        System.out.println("2. By descending order");

        try {
            while (choiceOrder <= 0 || choiceOrder >= 3){
                System.out.print("Choose the list ordering: ");
                choiceOrder = Integer.parseInt(reader.nextLine());
            }
        }
        catch (NumberFormatException e){
            System.err.println(e);
            return;
        }

        switch(choiceOrder){
            case 1:
                order = "ASC";
                break;

            case 2:
                order = "DESC";
                break;

        }
        
        try {
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Salesperson ORDER BY SalesExperience " + order + ";");
            
            // Interface for the Query
            ////////////////////////////////////

            String salesID, salesName, salesAddress, salesPhone, salesExperience;
            System.out.print("| " + "salesID" + " | ");
            System.out.print("salesName" + " | ");
            System.out.print("salesAddress" + " | ");
            System.out.print("salesPhone" + " | ");
            System.out.println("salesExperience" + " |");
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

            ////////////////////////////////////
            stmt.close();
            rs.close();

            System.out.println("End of Query");
        }
        catch (SQLException e){
            System.err.println(e);
        }

        System.out.println();
    }

    // 2. Count the no. of sales record of each salesperson under a specific range on years of experience
    private void countTrans(){
        int lowerBound = 0;
        int upperBound = 0;

        try {
            System.out.print("Type in the lower bound for years of experience: ");
            lowerBound = Integer.parseInt(reader.nextLine());

            System.out.print("Type in the upper bound for years of experience: ");
            upperBound = Integer.parseInt(reader.nextLine());
        }
        catch(NumberFormatException e){
            System.err.println(e);
            return;
        }

        System.out.println("Transaction Record:");

        try {
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("CREATE VIEW Temp as " + 
            "SELECT T.SalesID, count(*) as N " +
            "FROM Transaction T " + 
            "WHERE T.SalesID in (SELECT SalesID FROM Salesperson WHERE SalesExperience >= " + lowerBound + " AND SalesExperience <= " + upperBound + ") " + 
            "GROUP BY T.SalesID;");

            ResultSet rs = stmt.executeQuery("SELECT S.SalesID, SalesName, SalesExperience, N " +
            "FROM Salesperson S, Temp T " +
            "WHERE S.SalesID = T.SalesID " + 
            "ORDER BY S.SalesID DESC;");
            
            // Interface for the Query
            ////////////////////////////////////

            String salesID, salesName, salesExperience, n;
            System.out.print("| " + "ID" + " | ");
            System.out.print("Name" + " | ");
            System.out.print("Years of Experience" + " | ");
            System.out.println("Number of Transaction" + " | ");

            while (rs.next()){
                salesID = rs.getString(1);
                salesName = rs.getString(2);
                salesExperience = rs.getString(3);
                n = rs.getString(4);
                System.out.print("| " + salesID + " | ");
                System.out.print(salesName + " | ");
                System.out.print(salesExperience + " | ");
                System.out.println(n + " | ");
            }

            ////////////////////////////////////
            stmt.executeUpdate("DROP VIEW Temp;");

            stmt.close();
            rs.close();

            System.out.println("End of Query");
            
        }
        catch (SQLException e){
            System.err.println(e);
        }

        System.out.println();
        
    }

    // 3. Show the total sales value of each manufacturer
    private void showTotalSales(){
        try{
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("CREATE VIEW Temp AS " + 
            "SELECT PartID, Count(*) AS N " + 
            "FROM Transaction T " + 
            "GROUP BY PartID;");

            stmt.executeUpdate("CREATE VIEW Temp2 AS " + 
            "SELECT P.PartID, P.PartName, P.PartPrice*N AS totalPart, P.ManuID " + 
            "FROM Part P, Temp T " + 
            "WHERE P.PartID=T.PartID;");

            ResultSet rs = stmt.executeQuery("SELECT T.ManuID, M.ManuName, Sum(T.totalPart) AS S "+ 
            "FROM Temp2 T, Manufacturer M " + 
            "WHERE M.ManuID=T.ManuID " +
            "GROUP BY T.ManuID " + 
            "ORDER BY S DESC;");
            
            // Interface for the Query
            ////////////////////////////////////
            
            String manuID, manuName, totalSales;
            System.out.print("| " + "Manufacturer ID" + " | ");
            System.out.print("Manufacturer Name" + " | ");
            System.out.println("Total Sales Value" + " | ");

            while (rs.next()){
                manuID = rs.getString(1);
                manuName = rs.getString(2);
                totalSales = rs.getString(3);

                System.out.print("| " + manuID + " | ");
                System.out.print(manuName + " | ");
                System.out.println(totalSales + " | ");
            }

            ////////////////////////////////////

            stmt.executeUpdate("DROP VIEW Temp2;");
            stmt.executeUpdate("DROP VIEW Temp;");

            stmt.close();
            rs.close();

            System.out.println("End of Query");
            
        }
        catch(SQLException e){
            System.err.println(e);
        }

        System.out.println();
    }

    // 4. Show the N most popular part
    private void showNMostPop(){
        int n = 0;

        try {
            System.out.print("Type in the number of parts: ");
            n = Integer.parseInt(reader.nextLine());
        }
        catch(NumberFormatException e){
            System.err.println(e);
            return;
        }

        try {
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("CREATE VIEW Temp AS " +
            "SELECT PartID, Count(*) AS N " + 
            "FROM Transaction T " + 
            "GROUP BY PartID;");

            ResultSet rs = stmt.executeQuery("SELECT P.PartID, P.PartName, T.N " + 
            "FROM Part P, Temp T " + 
            "WHERE P.PartID = T.PartID " + 
            "ORDER BY T.N DESC LIMIT " + n + ";");

            // Interface for the Query
            ////////////////////////////////////

            String partID, partName, numOfTrans;
            System.out.print("| " + "Part ID" + " | ");
            System.out.print("Part Name" + " | ");
            System.out.println("No. of Transaction" + " | ");

            while (rs.next()){
                partID = rs.getString(1);
                partName = rs.getString(2);
                numOfTrans = rs.getString(3);

                System.out.print("| " + partID + " | ");
                System.out.print(partName + " | ");
                System.out.println(numOfTrans + " | ");
            }

            ////////////////////////////////////

            stmt.executeUpdate("DROP VIEW Temp;");
            
            stmt.close();
            rs.close();

            System.out.println("End of Query");

        }
        catch(SQLException e){
            System.err.println(e);
        }

        System.out.println();
    }

}