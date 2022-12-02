package menus;
import java.util.Scanner;
import java.sql.*;
import java.io.*;
import tools.*;
import interfaces.*;

public class AdministatorMenu {

    private Database db;
    private Scanner reader = new Scanner(System.in); 

    // Constructor
    public AdministatorMenu(Database db){
        this.db = db;
    }

    // Exception to check that all txt file exist (not required)
    public class NotAllFilesException extends Exception {  
        public NotAllFilesException(String errorMessage) {  
            super(errorMessage);  
        }  
    }  

    // Call Admin Menu
    public void callAdminMenu(){
        int choice = 0;
        try {
            System.out.println("-----Operations for administrator menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to the main menu");
            
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(reader.nextLine());

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
                case 5:
                    System.out.println();
                    break;
            }
        }
        catch (NumberFormatException e){
            System.err.println(e);
            // choice = 0;
            
        }

    }

    // 1. Create all tables
    private void createTables(){
        System.out.print("Processing...");

        try {
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Category (CatID INTEGER(1) PRIMARY KEY NOT NULL, CatName VARCHAR(20) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Manufacturer (ManuID INTEGER(2) PRIMARY KEY NOT NULL, ManuName VARCHAR(20) NOT NULL, ManuAddress VARCHAR(50) NOT NULL, ManuPhone INTEGER(8) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Part (PartID INTEGER(3) PRIMARY KEY NOT NULL, PartName VARCHAR(20) NOT NULL, PartPrice INTEGER(5) NOT NULL, ManuID INTEGER(2) NOT NULL, CatID INTEGER(1) NOT NULL, PartWarranty INTEGER(2) NOT NULL, PartAvailQuan INTEGER(2) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Salesperson (SalesID INTEGER(2) PRIMARY KEY NOT NULL, SalesName VARCHAR(20) NOT NULL, SalesAddress VARCHAR(50) NOT NULL, SalesPhone INTEGER(8) NOT NULL, SalesExperience INTEGER(1) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Transaction (TransID INTEGER(4) PRIMARY KEY NOT NULL, PartID INTEGER(3) NOT NULL, SalesID INTEGER(2) NOT NULL, TransDate DATE NOT NULL)");
            stmt.close();
        }
        catch (SQLException e){
            System.err.println(e);
        }

        System.out.println("Done! Database is initialized!\n");
    }

    // 2. Delete all tables
    private void deleteTables(){
        System.out.print("Processing...");

        try {
            Statement stmt = db.conn.createStatement();
            stmt.executeUpdate("DROP TABLE Category");
            stmt.executeUpdate("DROP TABLE Manufacturer");
            stmt.executeUpdate("DROP TABLE Part");
            stmt.executeUpdate("DROP TABLE Salesperson");
            stmt.executeUpdate("DROP TABLE Transaction");
            stmt.close();
        }
        catch(SQLException e){
            System.err.println(e);
        }

        System.out.println("Done! Database is removed!\n");
    }
    
    // 3. Load from datafile
    // TO DO: Absolute Path (?)
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
                    
                    if (!processSQLContent(file)){
                        return;
                    }    
                }

                System.out.println("Done! Data is inputted to the database!");
            }
            else {
                System.out.println("The folder does not exist");
            }
            
        }
        catch(NotAllFilesException e){
            System.err.println(e);
        }

        System.out.println();
    }

    // 4. Show content of a table
    private void showContent(){
        System.out.print("Which table would you like to show: ");
        String tableName = reader.nextLine();
        System.out.println("Content of table " + tableName + ":");

        switch (tableName){
            case "Category":
                CategoryInterface categoryInt = new CategoryInterface(db);
                categoryInt.showInterface();
                break;

            case "Manufacturer":
                ManufacturerInterface manufacturerInt = new ManufacturerInterface(db);
                manufacturerInt.showInterface();
                break;

            case "Part":
                PartInterface partInt = new PartInterface(db);
                partInt.showInterface();
                break;
            
            case "Salesperson":
                SalespersonInterface salesInt = new SalespersonInterface(db);
                salesInt.showInterface();
                break;
            
            case "Transaction":
                TransactionInterface transInt = new TransactionInterface(db);
                transInt.showInterface();
                break;

            default:
                System.out.println("No Such Table");
                break;
        }
        
        System.out.println();
    }

    // Helper function for (3) to process SQL Content 
    private boolean processSQLContent(File file){
        String fileName = file.getName();

        try {
            PreparedStatement pstmt = null;
            Scanner fileScanner = new Scanner(file);
            
            switch (fileName){
                case "category.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Category VALUES (?,?)");
                    if (!executeSQLUpdate(fileScanner, pstmt, "Category")) return false;
                    break;
                
                case "manufacturer.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Manufacturer VALUES (?,?,?,?)");
                    if (!executeSQLUpdate(fileScanner, pstmt, "Manufacturer")) return false;
                    break;
                
                case "part.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Part VALUES (?,?,?,?,?,?,?)");
                    if (!executeSQLUpdate(fileScanner, pstmt, "Part")) return false;
                    break;
                
                case "salesperson.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Salesperson VALUES (?,?,?,?,?)");
                    if (!executeSQLUpdate(fileScanner, pstmt, "Salesperson")) return false;
                    break;
                
                case "transaction.txt":
                    pstmt = db.conn.prepareStatement("INSERT INTO Transaction VALUES (?,?,?,?)");
                    if (!executeSQLUpdate(fileScanner, pstmt, "Transaction")) return false;
                    break;

                default:
                    System.out.println("There is no such table");
                    break;
            }

            fileScanner.close();
            pstmt.close();

            return true;

        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
        catch(FileNotFoundException e){
            System.err.println(e);
            return false;
        }

    }

    // Helper function for (3) to execute SQL Update
    private boolean executeSQLUpdate(Scanner fileScanner, PreparedStatement pstmt, String table){

        try {
            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                String[] inputs = line.split("\t");
                
                // If table == Transaction, change Date format
                if (table == "Transaction"){
                    String[] ddmmyyyy = inputs[3].split("/");
                    inputs[3] = ddmmyyyy[2] + "-" + ddmmyyyy[1] + "-" + ddmmyyyy[0];
                }

                for (int i = 0; i < inputs.length; i++){
                    pstmt.setString(i+1, inputs[i]);
                }
    
                pstmt.executeUpdate();
            }
            return true;

        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }

    }

}
