package connecttodb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainMenu {
	
    public static Statement statement;

	  
	  
	  public static void displayMenu(Connection connection) {
	        Scanner sc = new Scanner(System.in);

	        int enteredValue=0;

	        do {
	            System.out.println("Select from the options below");
	            System.out.println("1. User");
	            System.out.println("2. Record Label Manager");
	            System.out.println("3. Podcast Manager");
	            System.out.println("4. Admin");
	            System.out.println("5. Exit");
	            System.out.print("Enter your option:");

	            try {
	                enteredValue = sc.nextInt();

	                switch (enteredValue) {
	                    case 1:
	                        User.getuser(connection);
	                        break;
	                    case 2:
//	                        Signup.signUpPage();
	                        break;
	                    case 3:
//	                        ShowQuery.showQueryPage();
	                        break;
	                    case 4:
//	                    	
	                    case 5:
	                        System.out.print("Exit");
	                        exitProgram(connection);
	                        break;
	                    default:
	                        System.out.println("You have made an invalid choice. Please pick again.");
	                }
	            } catch (Exception e) {
	                System.out.println("You have made an invalid choice. Please pick again.");
	                displayMenu(connection);
	            }
	        } while (enteredValue != 4);
	    }

	    public static void exitProgram(Connection connection) throws SQLException {
	    	
//	    	Statement statement = null;
	        statement = connection.createStatement();

	        Helper.close(connection);
	        Helper.close(statement);
	        System.exit((0));
	    }
	}


