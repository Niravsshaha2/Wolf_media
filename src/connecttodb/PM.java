package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PM {
  public static Statement statement;
  public static String[] podcastepisode_list, podcast_list, podcasthost_list;

  public static void get_pm_menu(Connection connection) throws SQLException {
    int enteredValue = 0;

    do {
      Scanner sc = new Scanner(System.in);
      statement = connection.createStatement();
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. View All Podcasts");
      System.out.println("2. Add Podcast Info");
      System.out.println("3. Update Podcast Info");
      System.out.println("4. Delete Podcast Info");

      System.out.println("5. View All Podcast Hosts");
      System.out.println("6. Add Podcast Host Info");
      System.out.println("7. Update Podcast Host Info");
      System.out.println("8. Delete Podcast Host Info");

      System.out.println("9. View All Podcast Episodes");
      System.out.println("10. Add Podcast Episode Info");
      System.out.println("11. Update Podcast Episode Info");
      System.out.println("12. Delete Podcast Episode Info");

      System.out.println("13. Assign Podcast Episode to Podcast");
      System.out.println("14. Assign Podcast Host to Podcast Episode");
      System.out.println(
        "15. Assign Podcast Host to all Episodes in a Podcast"
      );
      System.out.println("0. Main Menu");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            podcast.view_all_podcasts(connection, "show");
            break;
          case 2:
            podcast.add_podcast_info(connection);
            break;
          case 3:
            podcast.update_podcast_info(connection, "update");
            break;
          case 4:
            podcast.update_podcast_info(connection, "delete");
            break;
          case 5:
            podcasthost.view_all_podcast_hosts(connection, "name");
            break;
          case 6:
            podcasthost.add_podcast_host_info(connection);
            break;
          case 7:
            podcasthost.update_podcast_host_info(connection, "update");
            break;
          case 8:
            podcasthost.update_podcast_host_info(connection, "delete");
            break;
          case 9:
            podcastepisode.view_all_podcast_episodes(connection);
            break;
          case 10:
            podcastepisode.add_podcast_episode_info(connection);
            break;
          case 11:
            podcastepisode.update_podcast_episode_info(connection, "update");
            break;
          case 12:
            podcastepisode.update_podcast_episode_info(connection, "delete");
            break;
          case 13:
            assign_podcast_episode_to_podcast(connection);
            break;
          case 14:
            assign_podcast_host_to_podcast_episode(connection);
            break;
          case 15:
            assign_podcast_host_to_podcast(connection);
            break;
          case 0:
            MainMenu.displayMenu(connection);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made a wrong choice. Please pick again.");
        get_pm_menu(connection);
        // } finally {
        //   closers(statement, sc);
      }
    } while (enteredValue != 0);
  }

  //	SWITCH CASE FOR PM MENU





  public static void assign_podcast_episode_to_podcast(Connection connection)
    throws SQLException {
    System.out.println("assign_podcast_episode_to_podcast");
  }

  public static void assign_podcast_host_to_podcast_episode(Connection connection)
    throws SQLException {
    System.out.println("assign_podcast_host_to_podcast_episode");
  }

  public static void assign_podcast_host_to_podcast(Connection connection)
    throws SQLException {
    System.out.println("assign_podcast_host_to_podcast");
  }

  //	GET PODACST LIST FOR PM TO FURTHER SELECT EPISODE FROM THE PODCAST
  public static void get_podcast(String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("*****");
    System.out.println("List of Podcast");

    statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    String query, podcastname;

    query = "SELECT p_name FROM Podcast";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
        podcastname = rs.getString("p_name");
        System.out.println("(" + row + ") " + podcastname + "");
        row++;
      }

      System.out.println("");
      System.out.println("Enter podcast id to select");
      System.out.println("");

      int selection = sc.nextInt();
      rs.absolute(selection - 1);
      if (rs.next()) {
        podcastname = rs.getString("p_name");
        System.out.println("Selected value: " + podcastname);
        get_podcast_episodes(podcastname, u_email_id, connection);
      } else {
        System.out.println("You can try again");
        get_podcast(u_email_id, connection);
      }
    } catch (SQLException e) {
      Helper.close(rs);
      System.out.println("Could not fetch podcast!");
      get_podcast(u_email_id, connection);
      // } finally {
      //   closers(statement, sc);
    }
  }

  //	GET PODACST EPISODE GIVEN PODCAST NAME
  public static void get_podcast_episodes(String podcastname, String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("*****");
    System.out.println("List of Podcast Episode for: " + podcastname);

    statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    String query, podcastepisodename, pe_title;

    query =
      "SELECT pe_title FROM PodcastEpisode where p_name ='" + podcastname + "'";

    ResultSet rs = null;
    try {
      int row = 1;
      rs = statement.executeQuery(query);
      while (rs.next()) {
        podcastepisodename = rs.getString("pe_title");
        System.out.println("(" + row + ") " + podcastname + " : " + podcastepisodename + "");
        row++;
      }
      while (true) {
        try {
          System.out.println("");
          System.out.println("Enter podcast episode title to select");
          System.out.println("");

          pe_title = sc.nextLine();

          query =
            "SELECT pe_title FROM PodcastEpisode where pe_title='" +
            pe_title +
            "' AND p_name='" +
            podcastname +
            "'";
          rs = statement.executeQuery(query);
          if (rs.next()) {
            podcast_episode_play_count(
              podcastname,
              pe_title,
              u_email_id,
              connection
            );
          } else {
            System.out.println("Try again");
            get_podcast_episodes(podcastname, u_email_id, connection);
          }
        } catch (SQLException e) {
          System.out.println("Could not fetch the episode, please try again!");
          get_podcast_episodes(podcastname, u_email_id, connection);
        }
      }
    } catch (SQLException e) {
      Helper.close(rs);
      e.printStackTrace();
      // } finally {
      //   closers(statement, sc);
    }
  }

  //	INCREMENT PODCAST EPISODE PLAY COUNT
  public static void podcast_episode_play_count(String podcastname, String pe_title, String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("Thank you for listening to Episode: " + pe_title + " from " + podcastname);
    System.out.println("");

    User.getusermenu(u_email_id, connection);
  }

  public static void closers(Statement statement, Scanner sc) {
    Helper.close(statement);
    Helper.close(sc);
  }
}
