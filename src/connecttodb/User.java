package connecttodb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {

  public static Statement statement;

  public static void delete_user_info(String u_email_id, Connection connection)
    throws SQLException {
    String sql;
    int rows;
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();

    do {
      System.out.print("");
      System.out.println("Select what you want to delete:");
      System.out.println("1. User Last Name");
      System.out.println("2. User Phone Number");
      System.out.println("0. Go to previous menu");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            System.out.println("");
            //                    String u_last_name = sc.next();
            sql =
              "UPDATE User SET u_last_name=null WHERE u_email_id = '" +
              u_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Last Name Deleted ");

            break;
          case 2:
            System.out.println("");
            //                    String u_phone = sc.next();
            sql =
              "UPDATE User SET u_phone=null WHERE u_email_id = '" +
              u_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Phone Number Deleted ");

            break;
          case 0:
            MainMenu.displayMenu(connection);
            break;
          default:
            System.out.println(
              "You have made an invalid choice. Please pick again."
            );
        }
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    } while (enteredValue != 0);
  }

  public static void update_user_info(String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();
    String sql;
    int rows;
    do {
      System.out.print("");
      System.out.println("Select what you want to update:");
      System.out.println("1. User First Name");
      System.out.println("2. User Last Name");
      System.out.println("3. User Phone Number");
      System.out.println("0. Go to previous menu");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            System.out.println("");
            System.out.println("First Name: ");
            String u_first_name = sc.next();
            sql =
              "UPDATE User SET u_first_name='" +
              u_first_name +
              "' WHERE u_email_id = '" +
              u_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            break;
          case 2:
            System.out.println("");
            System.out.println("Last Name: ");
            String u_last_name = sc.next();
            sql =
              "UPDATE User SET u_last_name='" +
              u_last_name +
              "' WHERE u_email_id = '" +
              u_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            break;
          case 3:
            System.out.println("");
            System.out.println("Phone Number: ");
            String u_phone = sc.next();
            sql =
              "UPDATE User SET u_phone='" +
              u_phone +
              "' WHERE u_email_id = '" +
              u_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            break;
          case 4:
            MainMenu.displayMenu(connection);
            break;
          default:
            System.out.println(
              "You have made an invalid choice. Please pick again."
            );
        }
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    } while (enteredValue != 0);
  }

  public static void activateuser(Connection connection) throws SQLException {
    System.out.println("");
    System.out.println("Enter User details: ");
    Scanner scanner = new Scanner(System.in);
    String query;
    ResultSet rs = null;
    String sql;
    statement = connection.createStatement();
    try {
      System.out.println("Enter User Email ID:");
      String u_email_id = scanner.next();
      query = "SELECT * FROM User WHERE u_email_id = '" + u_email_id + "'";
      rs = statement.executeQuery(query);

      if (rs.next()) {
        String isActive = rs.getString("u_subscription_status");
        if (isActive.equals("ACTIVE")) {
          // User is already active
          System.out.println("User is already active.");
        } else {
          // User is inactive
          System.out.println("User is not activated.");

          sql =
            "UPDATE User SET u_subscription_status = 'ACTIVE' WHERE u_email_id = '" +
            u_email_id +
            "'";
          statement.executeUpdate(sql);

          sql =
            "UPDATE BillingService SET bs_revenue = bs_revenue + 100 WHERE bs_date = DATE_FORMAT(CURRENT_DATE, '%Y-%m-01')";
          statement.executeUpdate(sql);

          sql =
            "INSERT INTO pays_to (up_fee_for_subscription, up_date, bs_date, u_email_id) " +
            "VALUES (100,  DATE_FORMAT(CURRENT_DATE, '%Y-%m-01') , DATE_FORMAT(CURRENT_DATE, '%Y-%m-01'), '" +
            u_email_id +
            "')";
          statement.executeUpdate(sql);
        }
      } else {
        // User does not exist in table
        System.out.println("User not present.");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  public static void addusers(Connection connection) throws SQLException {
    System.out.println("");
    System.out.println("Enter User details: ");

    Statement stmt = connection.createStatement();
    try {
      Scanner scanner = new Scanner(System.in);

      System.out.println("Enter User Email ID:");
      String u_email_id = scanner.next();

      System.out.println("Enter User First Name:");
      String u_first_name = scanner.next();

      System.out.println("Enter User Last Name:");
      String u_last_name = scanner.next();

      System.out.println("Enter Registration Date(yyyy-dd-mm):");
      String u_reg_date = scanner.next();

      u_reg_date = u_reg_date.substring(0, u_reg_date.length() - 2) + "" + "01";

      System.out.println("Enter Phone Number:");
      BigDecimal u_phone = new BigDecimal(scanner.next());

      String sql =
        "INSERT INTO User(u_email_id, u_first_name, u_last_name, u_reg_date, u_subscription_status, u_phone) " +
        "VALUES ('" +
        u_email_id +
        "', '" +
        u_first_name +
        "', '" +
        u_last_name +
        "', '" +
        u_reg_date +
        "','ACTIVE', '" +
        u_phone +
        "')";
      int rows = stmt.executeUpdate(sql);

      sql =
        "INSERT INTO pays_to (up_fee_for_subscription, up_date, bs_date, u_email_id) " +
        "VALUES (100, '" +
        u_reg_date +
        "', '" +
        u_reg_date +
        "', '" +
        u_email_id +
        "')";
      rows = stmt.executeUpdate(sql);

      System.out.println("User added");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  public static void viewusers(Connection connection) throws SQLException {
    System.out.println("");
    System.out.println("List of Users");
    System.out.println("");
    statement = connection.createStatement();

    String query;

    query = "select * from User";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);

      while (rs.next()) {
        //print all columns
        String u_email_id = rs.getString("u_email_id");
        String u_first_name = rs.getString("u_first_name");
        System.out.println(u_email_id + " " + u_first_name + "");
      }
    } catch (SQLException e) {
      System.out.println(e);
      Helper.close(rs);
      e.printStackTrace();
    }
  }

  public static void getuser(Connection connection) throws SQLException {
    Scanner sc = new Scanner(System.in);
    String u_email_id;
    statement = connection.createStatement();
    do {
      System.out.println("");
      System.out.println("User Login: Enter your Email");
      try {
        u_email_id = sc.next();
        u_email_id = "user4@example.com";
        String x =
          "select u_email_id AS u_email_id from User where u_email_id='" +
          u_email_id +
          "'";
        ResultSet rs4 = statement.executeQuery(x);

        if (rs4.next()) {
          getusermenu(u_email_id, connection);
        } else {
          System.out.println("Try again");
          getuser(connection);
        }
      } catch (SQLException e) {
        System.out.println("Could not fetch details");
        getuser(connection);
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
      System.out.println("0. Go to previous menu");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            song.getsongs(u_email_id, connection);
            break;
          case 2:
            artist.getartist(u_email_id, connection);
            break;
          case 3:
            album.getalbum(u_email_id, connection);
            break;
          case 4:
            PM.get_podcast(u_email_id, connection);
            break;
          case 0:
            MainMenu.displayMenu(connection);
            break;
          default:
            System.out.println(
              "You have made an invalid choice. Please pick again."
            );
        }
      } catch (Exception e) {
        System.out.println("You have made an invalid choice. Please pick again.");
        getusermenu(u_email_id, connection);
      }
    } while (enteredValue != 0);
  }

  public static void exitProgram(Connection connection) {
    Helper.close(connection);
    Helper.close(statement);
    System.exit((0));
  }
}
