package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;

public class podcasthost {

  public static String[] podcasthost_list;
  public static Statement statement;
  Scanner sc = new Scanner(System.in);

  public static String[] view_all_podcast_hosts(
    Connection connection,
    String typ
  ) throws SQLException {
    System.out.println("*****");
    if (typ.equals("name")) {
      System.out.println("List of Podcast Hosts (with podcast and episode being hosted)");
    } else if (typ.equals("email")) {
      System.out.println("List of Podcast Hosts (email id)");
    }

    statement = connection.createStatement();
    String query, podcasthostfirstname, podcasthostlastname, podcasthostemail;

    query = "SELECT ph_email_id, ph_first_name, ph_last_name FROM PodcastHost";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;
      int podcasthost_count = 0;
      if (rs.last()) {
        podcasthost_count = rs.getRow();
        rs.beforeFirst();
      }
      podcasthost_list = new String[podcasthost_count];

      while (rs.next()) {
        podcasthostfirstname = rs.getString("ph_first_name");
        podcasthostlastname = rs.getString("ph_last_name");
        podcasthostemail = rs.getString("ph_email_id");
        podcasthost_list[row - 1] = podcasthostemail;
        if (typ.equals("name")) {
          System.out.println(row + "] " + podcasthostfirstname + " " + podcasthostlastname);
        } else if (typ.equals("email")) {
          System.out.println(row + "] " + podcasthostemail);
        }
        row++;
      }
    } catch (SQLException e) {
      System.out.println("Unfortunately, we don't have any podcast hosts yet!");
      PM.get_pm_menu(connection);
    }
    return podcasthost_list;
  }

  public static void add_podcast_host_info(Connection connection)
    throws SQLException {
    // ph_email_id, ph_first_name, ph_last_name, ph_phone, ph_city
    System.out.println("***** Enter Podcast Host Details *****");
    Statement statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    try {
      System.out.println("Enter Podcast Host Email:");
      String podcasthostemail = sc.nextLine();

      System.out.println("Enter Podcast Host First Name:");
      String podcasthostfirstname = sc.nextLine();

      System.out.println("Enter Podcast Host Last Name:");
      String podcasthostlastname = sc.nextLine();

      System.out.println("Enter Podcast Host City:");
      String podcasthostcity = sc.nextLine();

      System.out.println("Enter Podcast Host Phone number:");
      long podcasthostphone = sc.nextLong();

      String sql =
        "INSERT INTO PodcastHost (ph_email_id, ph_first_name, ph_last_name, ph_phone, ph_city) VALUES ('" +
        podcasthostemail +
        "', '" +
        podcasthostfirstname +
        "', '" +
        podcasthostlastname +
        "', '" +
        podcasthostphone +
        "', '" +
        podcasthostcity +
        "')";
      int rows = statement.executeUpdate(sql);
      System.out.println(rows + " row(s) inserted.");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      // } finally {
      //   closers(statement, sc);
    }
  }

  public static void update_podcast_host_info(Connection connection, String typ)
    throws SQLException {
    String podcasthost_continue_choice = "n";
    String podcasthostname_choice = "";
    Scanner sc = new Scanner(System.in);
    view_all_podcast_hosts(connection, "email");
    do {
      System.out.println("Enter podcast host email:");
      try {
        podcasthostname_choice = sc.nextLine();
        if (!Arrays.asList(podcasthost_list).contains(podcasthostname_choice)) {
          System.out.println("Podcast host does not exist. Please try again from below list of podcast hosts!");
          update_podcast_host_info(connection, typ);
        }
        update_podcast_host_choice_function(
          connection,
          podcasthostname_choice,
          typ
        );
        System.out.println("Do you want to " + typ + " in another podcast host? (y/n)");
        podcasthost_continue_choice = sc.next();
      } catch (Exception e) {
        System.out.println("Podcast host does not exist. Please try again!");
        update_podcast_host_info(connection, typ);
      }
    } while (podcasthost_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_host_choice_function(Connection connection, String podcasthostemail_choice, String typ) throws SQLException {
    String query = "", new_str_val = "";
    long new_num_val_phone = 0;
    int rows = 0, update_choice = 0;
    String update_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      System.out.println("Which field to " + typ + "?:");
      System.out.println("1. First Name");
      System.out.println("2. Last Name");
      System.out.println("3. Phone");
      System.out.println("4. City");
      System.out.println("0. Go back to choosing podcast host name");
      System.out.println("Enter your option:");
      System.out.println("");
      try {
        update_choice = sc.nextInt();
        switch (update_choice) {
          case 1:
            if (typ.equals("update")) {
              System.out.println("Enter new value for first name:");
              new_str_val = sc.next();
              query =
                "UPDATE PodcastHost SET ph_first_name='" +
                new_str_val +
                "' WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastHost SET ph_first_name=" +
                null +
                " WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;
          case 2:
            if (typ.equals("update")) {
              System.out.println("Enter new value for last name:");
              new_str_val = sc.next();
              query =
                "UPDATE PodcastHost SET ph_last_name='" +
                new_str_val +
                "' WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastHost SET ph_last_name=" +
                null +
                " WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;
          case 3:
            if (typ.equals("update")) {
              System.out.println("Enter new value for phone:");
              new_num_val_phone = sc.nextLong();
              query =
                "UPDATE PodcastHost SET ph_phone=" +
                new_num_val_phone +
                " WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastHost SET ph_phone=" +
                null +
                " WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            }
            new_num_val_phone = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;
          case 4:
            if (typ.equals("update")) {
              System.out.println("Enter new value for city:");
              new_str_val = sc.next();
              query =
                "UPDATE PodcastHost SET ph_city='" +
                new_str_val +
                "' WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastHost SET ph_city=" +
                null +
                " WHERE ph_email_id='" +
                podcasthostemail_choice +
                "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;
          case 0:
            update_podcast_host_info(connection, typ);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made a wrong choice. Please choose again:");
        update_podcast_host_choice_function(
          connection,
          podcasthostemail_choice,
          typ
        );
      }
      System.out.println("Do you want to " + typ + " more fields for the podcast? (y/n)");
      update_continue_choice = sc.next();
    } while (update_continue_choice.toLowerCase().equals("y"));
  }
}
