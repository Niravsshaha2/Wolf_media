package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class artist {

  public static Statement statement;

  public static void delete_artist_info(String rl_name, String a_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();
    String sql;
    int rows;
    do {
      System.out.print("");
      System.out.println("Select from following:");
      System.out.println("1. Delete Artist country");
      System.out.println("0.  Go to previous menu");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            System.out.println("");
            sql =
              "update Artist set a_country = NULL WHERE a_email_id = '" +
              a_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Country deleted");
            break;

          case 0:
            RL.getRecordlabelMenu(rl_name, connection);
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

  public static void update_artist_info(String rl_name, String a_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();

    do {
      System.out.print("");
      System.out.println("Select from following:");
      System.out.println("1. Update Artist Country");
      System.out.println("0.  Go to previous menu");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            System.out.println("");
            System.out.println("Artist Country: ");
            String a_country = sc.next();
            String sql =
              "UPDATE Artist SET a_country='" +
              a_country +
              "' WHERE a_email_id = '" +
              a_email_id +
              "'";
            int rows = statement.executeUpdate(sql);
            System.out.println("Country updated");
            break;

          case 0:
            RL.getRecordlabelMenu(rl_name, connection);
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

  public static void viewartist(String rl_name, Connection connection)
    throws SQLException {
    String query;
    statement = connection.createStatement();
    ResultSet rs = null;

    System.out.println("");
    System.out.println("List of Artists");
    System.out.println("");
    //print all columns

    query =
      "SELECT a_name,a_email_id FROM Artist where rl_name ='" + rl_name + "'";

    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
        String title = rs.getString("a_name");
        System.out.println(row + " " + title + "");
        row++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void add_artist_info(String rl_name, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("Enter Artist details: ");

    Statement stmt = connection.createStatement();

    try {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter Artist Email ID:");
      String a_email_id = scanner.next();

      System.out.println("Enter Artist Name:");
      scanner.nextLine();
      String a_name = scanner.nextLine();

      System.out.println("Enter Country:");
      String a_country = scanner.next();

      System.out.println("Enter Primary Genre:");
      String a_primary_genre = scanner.next();
      String sql;

      sql =
        "INSERT INTO Artist (a_email_id, a_name, a_status, a_country, a_primary_genre, rl_name) VALUES ('" +
        a_email_id +
        "', '" +
        a_name +
        "', 'ACTIVE', '" +
        a_country +
        "', '" +
        a_primary_genre +
        "', '" +
        rl_name +
        "')";

      int rows = stmt.executeUpdate(sql);
      System.out.println(rows + " row(s) inserted.");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  //	  Get artist list
  public static void getartist(String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("List of Artists");
    System.out.println("");

    statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    String query, title, s_id = "";

    query = "SELECT a_name,a_email_id FROM Artist";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
        title = rs.getString("a_name");
        System.out.println(row + " " + title + "");
        row++;
      }

      System.out.println("");
      System.out.println("Enter Artist id to select");
      System.out.println("");

      int selection = sc.nextInt();
      rs.absolute(selection - 1);
      if (rs.next()) {
        String artist_name = rs.getString("a_email_id");
        // System.out.println("Selected value: " + artist_name);
        song.getsongs("Artist", artist_name, u_email_id, connection);
      } else {
        getartist(u_email_id, connection);
      }
    } catch (SQLException e) {
      Helper.close(rs);
      e.printStackTrace();
    }
  }
}