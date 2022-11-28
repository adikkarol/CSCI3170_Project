package menuPackage;
import java.util.Scanner;
import java.sql.*;
import tools.*;
import java.io.*;

public class AdministatorMenu {

    private Database db;
    private Scanner reader = new Scanner(System.in); 

    public AdministatorMenu(Database db){
        this.db = db;
    }

    public class NotAllFilesException extends Exception {  
        public NotAllFilesException(String errorMessage) {  
            super(errorMessage);  
        }  
    }  

    public void callAdminMenu(){
        int choice = 0;
        
        while (choice != 5){
            System.out.println("-----Operations for administrator menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to the main menu");
            
            System.out.print("Enter your choice: ");
            
            try {
                choice = Integer.parseInt(reader.nextLine());
            }
            catch(NumberFormatException e){
                System.err.println(e);
            }

            System.out.println();
            switch(choice){
                case 1:
                    createTables();
                    break;
                case 2:
                    deleteTables();
                    break;
                case 3:
                    loadFromDatafile();
                    break;
                case 4:
                    showContent();
                    break;
            }
        }
        
        
    }

    private void createTables(){
        try {
            System.out.print("Processing...");
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Category (CatID INTEGER(1) PRIMARY KEY NOT NULL, CatName VARCHAR(20) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Manufacturer (ManuID INTEGER(2) PRIMARY KEY NOT NULL, ManuName VARCHAR(20) NOT NULL, ManuAddress VARCHAR(50) NOT NULL, ManuPhone INTEGER(8) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Part (PartID INTEGER(3) PRIMARY KEY NOT NULL, PartName VARCHAR(20) NOT NULL, PartPrice INTEGER(5) NOT NULL, ManuID INTEGER(2) NOT NULL, CatID INTEGER(1) NOT NULL, PartWarranty INTEGER(2) NOT NULL, PartAvailQuan INTEGER(2) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Salesperson (SalesID INTEGER(2) PRIMARY KEY NOT NULL, SalesName VARCHAR(20) NOT NULL, SalesAddress VARCHAR(50) NOT NULL, SalesPhone INTEGER(8) NOT NULL, SalesExperience INTEGER(1) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Transaction (TransID INTEGER(4) PRIMARY KEY NOT NULL, PartID INTEGER(3) NOT NULL, SalesID INTEGER(2) NOT NULL, TransDate DATE NOT NULL)");
            System.out.println("Done! Database is initialized!");
            
            stmt.close();
        }
        catch (SQLException e){
            System.err.println(e);
        }
        System.out.println();
    }

    private void deleteTables(){
        try {
            System.out.print("Processing...");
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("DROP TABLE Category");
            stmt.executeUpdate("DROP TABLE Manufacturer");
            stmt.executeUpdate("DROP TABLE Part");
            stmt.executeUpdate("DROP TABLE Salesperson");
            stmt.executeUpdate("DROP TABLE Transaction");
            System.out.println("Done! Database is removed!");
            stmt.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }
        System.out.println();
    }
    
    // Use For Loop
    private void processSQLContent(File file){
        try {
            String fileName = file.getName();
            // System.out.println(fileName);
            PreparedStatement pstmt = null;
            Scanner fileScanner = new Scanner(file);

            switch (fileName){
                case "category.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Category VALUES (?,?)");
                
                    while (fileScanner.hasNextLine()){
                        String line = fileScanner.nextLine();
                        String[] inputs = line.split("\t");
                        
                        pstmt.setString(1, inputs[0]);
                        pstmt.setString(2, inputs[1]);
                        pstmt.executeUpdate();
                    }

                    fileScanner.close();
                    pstmt.close();
                    break;
                
                case "manufacturer.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Manufacturer VALUES (?,?,?,?)");

                    while (fileScanner.hasNextLine()){
                        String line = fileScanner.nextLine();
                        String[] inputs = line.split("\t");

                        pstmt.setString(1, inputs[0]);
                        pstmt.setString(2, inputs[1]);
                        pstmt.setString(3, inputs[2]);
                        pstmt.setString(4, inputs[3]);
                        pstmt.executeUpdate();
                    }
                    
                    fileScanner.close();
                    pstmt.close();
                    break;
                
                case "part.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Part VALUES (?,?,?,?,?,?,?)");

                    while (fileScanner.hasNextLine()){
                        String line = fileScanner.nextLine();
                        String[] inputs = line.split("\t");
                        
                        pstmt.setString(1, inputs[0]);
                        pstmt.setString(2, inputs[1]);
                        pstmt.setString(3, inputs[2]);
                        pstmt.setString(4, inputs[3]);
                        pstmt.setString(5, inputs[4]);
                        pstmt.setString(6, inputs[5]);
                        pstmt.setString(7, inputs[6]);
                        pstmt.executeUpdate();
                    }
                    
                    fileScanner.close();
                    pstmt.close();
                    break;
                
                case "salesperson.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Salesperson VALUES (?,?,?,?,?)");

                    while (fileScanner.hasNextLine()){
                        String line = fileScanner.nextLine();
                        String[] inputs = line.split("\t");

                        pstmt.setString(1, inputs[0]);
                        pstmt.setString(2, inputs[1]);
                        pstmt.setString(3, inputs[2]);
                        pstmt.setString(4, inputs[3]);
                        pstmt.setString(5, inputs[4]);
                        pstmt.executeUpdate();
                    }
                    
                    fileScanner.close();
                    pstmt.close();
                    break;
                
                case "transaction.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Transaction VALUES (?,?,?,?)");

                    while (fileScanner.hasNextLine()){
                        String line = fileScanner.nextLine();
                        String[] inputs = line.split("\t");

                        String[] ddmmyyyy = inputs[3].split("/");
                        String yyyymmdd = ddmmyyyy[2] + "-" + ddmmyyyy[1] + "-" + ddmmyyyy[0];

                        pstmt.setString(1, inputs[0]);
                        pstmt.setString(2, inputs[1]);
                        pstmt.setString(3, inputs[2]);
                        pstmt.setString(4, yyyymmdd);
                        pstmt.executeUpdate();
                    }
                    
                    fileScanner.close();
                    pstmt.close();
                    break;

                default:
                    System.out.println("Default Table");
                    break;
            }

        }
        catch(SQLException e){
            System.err.println(e);
        }
        catch(FileNotFoundException e){
            System.err.println(e);
        }
    }

    private void showContent(){
        try {
            System.out.print("Which table would you like to show: ");
            String tableName = reader.nextLine();
            System.out.println("Content of table " + tableName + ":");
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            
            switch (tableName){
                case "Category":
                    String catID, catName;
                    System.out.print("| " + "catID" + " | ");
                    System.out.println("catName" + " |");
                    while (rs.next()){
                        catID = rs.getString(1);
                        catName = rs.getString(2);
                        System.out.print("| " + catID + " | ");
                        System.out.println(catName + " |");
                    }
                    break;

                case "Manufacturer":
                    String manuID, manuName, manuAddress, manuPhone;
                    System.out.print("| " + "manuID" + " | ");
                    System.out.print("manuName" + " | ");
                    System.out.print("manuAddress" + " | ");
                    System.out.println("manuPhone" + " |");
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
                    break;

                case "Part":
                    String partID, partName, partPrice, partWarranty, partAvilQuan;
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
                    break;
                
                case "Salesperson":
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
                    break;
                
                case "Transaction":
                    String transID, transDate;
                    String[] yyyymmdd;
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
                    break;

                default:
                    System.out.println("No Such Table");
            }

            stmt.close();
            rs.close();
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
        System.out.println();
    }

    // Redo Absolute Path
    private void loadFromDatafile(){
        try {
            System.out.print("Type in the Source Data Folder Path: ");
            String dataFolderName = reader.nextLine();
            System.out.print("Processing...");
            File dataFolder = new File("./" + dataFolderName);
            
            if (dataFolder.exists()){
                File[] allFiles = dataFolder.listFiles();
                
                for (File file : allFiles){
                    
                    if (!file.isFile()){
                        throw new NotAllFilesException("Not enough files to initialize the tables!");
                    }
                    
                    processSQLContent(file);
                }
            }
            else{
                System.out.println("Not exists");
            }


            System.out.println("Done! Data is inputted to the database!");
        }
        catch(NotAllFilesException e){
            System.err.println(e);
        }
        System.out.println();
    }

}
