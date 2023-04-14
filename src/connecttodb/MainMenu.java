package connecttodb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainMenu {

  public static Statement statement;
//   Display Main menu for End user
  public static void displayMenu(Connection connection) {
    Scanner sc = new Scanner(System.in);

    int enteredValue = 0;

    do {
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. User");
      System.out.println("2. Record Label Manager");
      System.out.println("3. Podcast Manager");
      System.out.println("4. Admin");
      System.out.println("0. Exit");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            //Makes users inactive if date not in current month
        	User.makeinactive(connection);
            //goes to User menu
            User.getuser(connection);
            break;
          case 2:
            //goes to Record Label menu
            RL.rl_page(connection);
            break;
          case 3:
            //goes to Podcast Manager menu
            PM.get_pm_menu(connection);
            break;
          case 4:
            //goes to Admin menu
            admin.getAdminMenu(connection);
            break;
          case 0:
            System.out.print("Thank you for using our streaming service");
            exitProgram(connection);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made an invalid choice. Please pick again.");
        displayMenu(connection);
      }
    } while (enteredValue != 0);
  }
//Helper function called for closing the connection
  public static void exitProgram(Connection connection) throws SQLException {
    // Statement statement = null;
    statement = connection.createStatement();

    Helper.close(connection);
    Helper.close(statement);
    System.exit((0));
  }
}
