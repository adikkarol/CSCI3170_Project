package menuPackage;
import java.util.Scanner;
import java.sql.*;
import tools.*;
import java.time.*;

public class SalespersonMenu {
    private Database db;
    private Scanner reader = new Scanner(System.in); 

    public SalespersonMenu(Database db){
        this.db = db;
    }

    public void callSalesMenu(){
        int choice = 0;
        
        while (choice != 3){
            System.out.println("-----Operations for salesperson menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Search for parts");
            System.out.println("2. Sell a part");
            System.out.println("3. Return to the main menu");
            
            System.out.print("Enter your choice: ");
            
            try {
                choice = Integer.parseInt(reader.nextLine());
            }
            catch(NumberFormatException e){
                System.err.println(e);
            }

            switch(choice){
                case 1:
                    searchParts();
                    break;
                case 2:
                    performTrans();
                    break;
            }
        }
        
    }

    private void searchParts(){
        try {
            int searchCriterion = 0;
            int sortCriterion = 0;

            System.out.println("Choose the Search criterion: ");
            System.out.println("1. Part Name");
            System.out.println("2. Manufacturer Name");
            
            while (searchCriterion <= 0 || searchCriterion >= 3){
                System.out.print("Choose the search criterion: ");
                searchCriterion = Integer.parseInt(reader.nextLine());
            }

            System.out.print("Type in the Search Keyword: ");
            String searchKeyword = null;
            searchKeyword = reader.nextLine();

            while (sortCriterion <= 0 || sortCriterion >= 3){
                System.out.println("Choose Ordering: ");
                System.out.println("1. By price, ascending order");
                System.out.println("2. By price, descending order");
                System.out.print("Choose the search criterion: ");
                sortCriterion = Integer.parseInt(reader.nextLine());
            }

            String searchQuery = null;
            if (searchCriterion == 1){
                searchQuery = "P.PartName";
            }
            else {
                searchQuery = "M.ManuName";
            }

            String sortQuery = null;
            if (sortCriterion == 1){
                sortQuery = "ASC";
            }
            else {
                sortQuery = "DESC";
            }

            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT P.PartID, P.PartName, M.ManuName, C.CatName, P.PartAvailQuan, P.PartWarranty, P.PartPrice FROM Part P, Manufacturer M, Category C WHERE P.ManuID=M.ManuID AND " + searchQuery + " LIKE '%" + searchKeyword + "%' AND P.CatID=C.CatID ORDER BY P.PartPrice " + sortQuery + ";");

            String partID, partName, manuName, catName, partAvailQuan, partWarranty, partPrice;
            System.out.print("| " + "ID" + " | ");
            System.out.print("Name" + " | ");
            System.out.print("Manufacturer" + " | ");
            System.out.print("Category" + " | ");
            System.out.print("Quantity" + " | ");
            System.out.print("Warranty" + " | ");
            System.out.println("Price" + " | ");

            while (rs.next()){
                partID = rs.getString(1);
                partName = rs.getString(2);
                manuName = rs.getString(3);
                catName = rs.getString(4);
                partAvailQuan = rs.getString(5);
                partWarranty = rs.getString(6);
                partPrice = rs.getString(7);

                System.out.print("| " + partID + " | ");
                System.out.print(partName + " | ");
                System.out.print(manuName + " | ");
                System.out.print(catName + " | ");
                System.out.print(partAvailQuan + " | ");
                System.out.print(partWarranty + " | ");
                System.out.println(partPrice + " | ");
            }

            stmt.close();
            rs.close();

            System.out.println("End of Query");
            System.out.println();

        }
        catch(SQLException e){
            System.err.println(e);
        }
        catch(NumberFormatException e){
            System.err.println(e);
        }

    }
    
    // check salesperson exists
    private void performTrans(){
        
        try{
            int partChoice = 0;
            int salesChoice = 0;
            System.out.print("Enter The Part ID: ");
            partChoice = Integer.parseInt(reader.nextLine());

            System.out.print("Enter The Salesperson ID: ");
            salesChoice = Integer.parseInt(reader.nextLine());

            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT P.PartID, P.PartName, P.PartAvailQuan FROM Part P WHERE P.PartID = " + partChoice + ";");
            
            String partID = null;
            String partName = null;
            int partAvailQuan = 0;

            while (rs.next()){
                partID = rs.getString(1);
                partName = rs.getString(2);
                partAvailQuan = Integer.valueOf(rs.getString(3));
            }

            if (partAvailQuan <= 0){
                System.out.println("There are no parts left with PartID = " + partChoice + "\n");
                return;
            }
            stmt.executeUpdate("UPDATE Part SET PartAvailQuan = PartAvailQuan - 1 WHERE PartID = " + partChoice + ";");

            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM Transaction;");

            int lastTransID = 0;

            while (rs1.next()){
                lastTransID = Integer.valueOf(rs1.getString(1));
            }

            PreparedStatement pstmt = db.conn.prepareStatement("INSERT INTO Transaction VALUES (?,?,?,?)");
            pstmt.setString(1, String.valueOf(lastTransID+1));
            pstmt.setString(2, String.valueOf(partChoice));
            pstmt.setString(3, String.valueOf(salesChoice));

            LocalDate todayLocal = LocalDate.now(); 
            Date today = Date.valueOf(todayLocal);
            pstmt.setDate(4, today);
            pstmt.executeUpdate();

            System.out.println("Product: " + partName + "(id: " + partID + ") Remaining Quantity: " + String.valueOf(partAvailQuan - 1));

            stmt.close();
            rs.close();
            pstmt.close();

            System.out.println("End of Query");
            System.out.println();

        }
        catch(SQLException e){
            System.err.println(e);
        }
        catch(NumberFormatException e){
            System.err.println(e);
        }
    }
}
