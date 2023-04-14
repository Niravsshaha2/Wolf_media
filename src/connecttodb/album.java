package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class album {

  public static Statement statement;
  // Album information deletion
  public static void delete_album_info(String rl_name, String l_name, Connection connection)
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
      System.out.println("1. Delete Album Edition");
      System.out.println("2. Delete Album Release Year");
      System.out.println("0.  Go to previous menu");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            System.out.println("");
            sql = "update Album set l_edition = NULL WHERE l_name = '" + l_name + "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Album edition deleted");
            break;
            
          case 2:
              System.out.println("");
              sql = "update Album set l_release_year = NULL WHERE l_name = '" + l_name + "'";
              rows = statement.executeUpdate(sql);
              System.out.println("Album Release Year deleted");
              break;
          
          case 0:
            RL.getRecordlabelMenu(rl_name, connection);
            break;

          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    } while (enteredValue != 0);
  }
  // Album information Updation
  public static void update_album_info(String rl_name, String l_name, Connection connection)
    throws SQLException {
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();

    do {
      System.out.print("");
      System.out.println("Select what you want to update:");
      System.out.println("1. Update Album Edition");
      System.out.println("2. Update Album Release Year");
      System.out.println("0.  Go to previous menu");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            System.out.println("");
            System.out.println("Album Edition from (Special, Limited, Collector): ");
            String l_edition = sc.next();
            String sql =
              "UPDATE Album SET l_edition='" +
              l_edition +
              "' WHERE l_name = '" +
              l_name +
              "'";
            int rows = statement.executeUpdate(sql);
            System.out.println("Album Edition Updated");
            // song.update_song_title(s_id, connection);
            break;
            
          case 2:
              System.out.println("");
              System.out.println("Album Release Year(YYY-MM-DD): ");
              String l_release_year = sc.next();
              sql =
                "UPDATE Album SET l_release_year='" +
                l_release_year +
                "' WHERE l_name = '" +
                l_name +
                "'";
              rows = statement.executeUpdate(sql);
              System.out.println("Album Release year Updated");
              // song.update_song_title(s_id, connection);
              break;

          case 0:
            RL.getRecordlabelMenu(rl_name, connection);
            break;

          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    } while (enteredValue != 0);
  }
  // List of All Album
  public static void viewalbum(String rl_name, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("List of Albums");
    System.out.println("");

    String query;
    statement = connection.createStatement();
    ResultSet rs = null;

    query = "SELECT l_name FROM Album";

    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
        String title = rs.getString("l_name");
        System.out.println(row + " " + title + "");
        row++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  // Adding Album information
  public static void add_album_info(String rl_name, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("Enter Album details: ");

    Statement stmt = connection.createStatement();

    try {
      Scanner scanner = new Scanner(System.in);

      System.out.println("Enter Album Name:");
      String l_name = scanner.nextLine();

      System.out.println("Enter Release date (yyyy-dd-mm):");
      String l_release_year_str = scanner.nextLine();
      java.sql.Date l_release_date = java.sql.Date.valueOf(l_release_year_str);

      // System.out.print(l_release_date);
      System.out.println("Enter Edition(Collector, Special, Limited):");
      String l_edition = scanner.nextLine();

      String sql =
        "INSERT INTO Album (l_name, l_release_year, l_edition) VALUES ('" +
        l_name +
        "', '" +
        l_release_year_str +
        "', '" +
        l_edition +
        "')";
      int rows = stmt.executeUpdate(sql);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  //	  Get album list
  public static void getalbum(String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("List of Albums");
    System.out.println("");

    statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    String query, title, s_id = "";

    query = "SELECT l_name FROM Album";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
        title = rs.getString("l_name");
        System.out.println(row + " " + title + "");
        row++;
      }

      System.out.println("");
      System.out.println("Enter Album id to select");
      System.out.println("");

      int selection = sc.nextInt();
      rs.absolute(selection - 1);
      if (rs.next()) {
        String album_name = rs.getString("l_name");
        // System.out.println("Selected value: " + album_name);
        song.getsongs("Album", album_name, u_email_id, connection);
      } else {
        getalbum(u_email_id, connection);
      }
    } catch (SQLException e) {
      Helper.close(rs);
      e.printStackTrace();
    }
  }
}
