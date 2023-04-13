package connecttodb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainMenu {

  public static Statement statement;

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
            User.getuser(connection);
            break;
          case 2:
            RL.rl_page(connection);
            break;
          case 3:
            PM.get_pm_menu(connection);
            break;
          case 4:
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

  public static void exitProgram(Connection connection) throws SQLException {
    // Statement statement = null;
    statement = connection.createStatement();

    Helper.close(connection);
    Helper.close(statement);
    System.exit((0));
  }
}
