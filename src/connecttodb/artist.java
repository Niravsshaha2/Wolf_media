package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class artist {

  public static Statement statement;

  public static String[] artist_list;

  public static void show_artist_genre(Connection connection)
    throws SQLException {
    System.out.println("List of Genres:");
    Scanner sc = new Scanner(System.in);
    ResultSet rs = null;
    try {
      statement = connection.createStatement();
      String title2;

      String query = "SELECT genre from Genre";

      rs = statement.executeQuery(query);
      while (rs.next()) {
        title2 = rs.getString("genre");
        System.out.println(title2 + "");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

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
      System.out.println("1. Delete Artist Country");
      System.out.println("2. Delete Artist Name");
      //System.out.println("3. Delete Artist Status");// update ACTIVE <-> INACTIVE
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

          case 2:
            System.out.println("");
            sql =
              "update Artist set a_name = NULL WHERE a_email_id = '" +
              a_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Name deleted");
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

  public static void update_artist_info(String rl_name, String a_email_id, Connection connection) throws SQLException {
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();

    do {
      System.out.print("");
      System.out.println("Select from following:");
      System.out.println("1. Update Artist Country");
      System.out.println("2. Update Artist Name");
      System.out.println("3. Update Artist Status");
      System.out.println("4. Update Artist Monthly Listeners");
      System.out.println("0. Go to previous menu");
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
          case 2:
            System.out.println("");
            System.out.println("Artist Name: ");
            String a_name = sc.next();
            sql =
              "UPDATE Artist SET a_name='" +
              a_name +
              "' WHERE a_email_id = '" +
              a_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Name updated");

            break;

          case 3:
            System.out.println("");
            System.out.println("Artist Status ( active / retired ): ");
            String a_status = sc.next();
            sql =
              "UPDATE Artist SET a_status='" +
              a_status +
              "' WHERE a_email_id = '" +
              a_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            System.out.println("Status updated");

            break;

          case 4:
            System.out.println("");
            System.out.println("Artist Monthly Listeners: ");
            int a_monthly_listeners = sc.nextInt();
            sql =
              "UPDATE Artist SET a_monthly_listeners='" +
              a_monthly_listeners +
              "' WHERE a_email_id = '" +
              a_email_id +
              "'";
            rows = statement.executeUpdate(sql);
            sc.nextLine();
            System.out.println("Monthly Listeners updated");

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

  public static String[] view_all_artists(
    Connection connection,
    String show_type
  ) throws SQLException {
    String query;
    statement = connection.createStatement();
    ResultSet rs = null;

    System.out.println("");
    if (show_type.equals("show")) {
      System.out.println("~ List of Artists ~");
      System.out.println("");
    }
    //print all columns

    query = "SELECT a_email_id FROM Artist";

    try {
      rs = statement.executeQuery(query);
      int row = 1;
      int artist_count = 0;
      if (rs.last()) {
        artist_count = rs.getRow();
        rs.beforeFirst();
      }
      artist_list = new String[artist_count];

      while (rs.next()) {
        String artistemail = rs.getString("a_email_id");
        artist_list[row - 1] = artistemail;
        if (show_type.equals("show")) {
          System.out.println(row + ". " + artistemail + "");
        }
        row++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return artist_list;
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

      show_artist_genre(connection);
      System.out.println("Enter Primary Genre:");
      String a_primary_genre = scanner.next();
      String sql;

      sql =
        "INSERT INTO Artist (a_email_id, a_name, a_status, a_country, ag_genre, rl_name) VALUES ('" +
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
    System.out.println("~ List of Artists ~");
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
      System.out.println("Enter Artist id to select: ");
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
