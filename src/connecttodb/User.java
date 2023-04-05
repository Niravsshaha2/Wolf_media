package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {

  public static Statement statement;

  public static void getuser(Connection connection)
	throws SQLException {
    String u_email_id;
    statement = connection.createStatement();
    do {
      Scanner sc = new Scanner(System.in);
      System.out.println("");
      System.out.println("User Login: Enter your Email: (press 0 to go back to main menu)");
      if (sc.next() != "0") {
	      try {
	        u_email_id = sc.next();
	        String x =
	          "select u_email_id AS u_email_id from User where u_email_id='" +
	          u_email_id +
	          "'";
	        ResultSet rs = statement.executeQuery(x);
	
	        if (rs.next()) {
	          System.out.println("User found");
	          getusermenu(u_email_id, connection);
	        } else {
	          System.out.println("Try again");
	          getuser(connection);
	        }
	      } catch (SQLException e) {
	        System.out.println("Could not fetch details");
	        getuser(connection);
//	      } finally {
//	      	exitProgram(connection, statement, sc);
		  }
      } else {
    	  MainMenu.displayMenu(connection);
      }
    } while (true);
  }

  public static void getusermenu(String u_email_id, Connection connection)
    throws SQLException {
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();

    do {
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. Song");
      System.out.println("2. Aritst");
      System.out.println("3. Album");
      System.out.println("4. Podcast");
      System.out.println("5. Update User Details");
      System.out.println("0. Exit");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            RL.getsongs(u_email_id, connection);
            break;
          case 2:
            RL.getartist(u_email_id, connection);
            break;
          case 3:
            RL.getalbum(u_email_id, connection);
            break;
          case 4:
            PM.get_podcast(u_email_id, connection);
            break;
          case 5:
            //
            break;
          case 0:
            MainMenu.displayMenu(connection);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made an invalid choice. Please pick again.");
        getusermenu(u_email_id, connection);
//      } finally {
//    	exitProgram(connection, statement, sc);
	  }
    } while (enteredValue != 0);
  }

  public static void exitProgram(Connection connection, Statement statement, Scanner sc) {
    Helper.close(connection);
    Helper.close(statement);
    Helper.close(sc);
    System.exit((0));
  }
}
