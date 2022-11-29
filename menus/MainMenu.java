package menus;
import java.util.Scanner;
import tools.*;

public class MainMenu {

    private Database db;
    private Scanner reader = new Scanner(System.in);

    public MainMenu(Database db){
        this.db = db;
    }

    public void callMainMenu(){
        int choice = 0;
        
        System.out.println("Welcome to sales system!\n");

        while (choice != 4){
            
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operations for salesperson");
            System.out.println("3. Operations for manager");
            System.out.println("4. Exit the program");
            
            System.out.print("Enter your choice: ");
            choice = reader.nextInt();
            System.out.println();

            switch(choice){
                case 1:
                    AdministatorMenu adminMenu = new AdministatorMenu(db);
                    adminMenu.callAdminMenu();
                    break;
                
                case 2:
                    SalespersonMenu salesMenu = new SalespersonMenu(db);
                    salesMenu.callSalesMenu();
                    break;

                case 3:
                    ManagerMenu managerMenu = new ManagerMenu(db);
                    managerMenu.callManagerMenu();
                    break;
            }

        }

    }

}
