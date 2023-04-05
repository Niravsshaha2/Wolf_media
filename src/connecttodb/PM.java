package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PM {

  public static void get_pm_menu(Connection connection)
    throws SQLException {
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();

    do {
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. Add Podcast Info");
      System.out.println("2. Update Podcast Info");
      System.out.println("3. Delete Podcast Info");
      System.out.println("4. Add Podcast Episode Info");
      System.out.println("5. Update Podcast Episode Info");
      System.out.println("6. Delete Podcast Episode Info");
      System.out.println("7. Add Podcast Host Info");
      System.out.println("8. Update Podcast Host Info");
      System.out.println("9. Delete Podcast Host Info");
      System.out.println("10. View All Podcasts");
      System.out.println("11. View All Podcast Episodes");
      System.out.println("12. View All Podcast Hosts");
      System.out.println("0. Main Menu");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            PM.add_podcast_info(connection);
            break;
          case 2:
            PM.update_podcast_info(connection);
            break;
          case 3:
            PM.update_podcast_info(connection);
            break;
          case 4:
	        PM.add_podcast_episode_info(connection);
	        break;
	      case 5:
	        PM.update_podcast_episode_info(connection);
	        break;
	      case 6:
	        PM.update_podcast_episode_info(connection);
	        break;
          case 7:
	        PM.add_podcast_host_info(connection);
	        break;
	      case 8:
	        PM.update_podcast_host_info(connection);
	        break;
	      case 9:
	        PM.update_podcast_host_info(connection);
	        break;
          case 10:
	        PM.view_all_podcasts(connection);
	        break;
	      case 11:
	        PM.view_all_podcast_episodes(connection);
	        break;
	      case 12:
	        PM.view_all_podcast_hosts(connection);
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
        System.out.println(
          "You have made an invalid choice. Please pick again."
        );
        get_pm_menu(connection);
      }
    } while (enteredValue != 6);
  }

  private static void add_podcast_info(Connection connection) throws SQLException {
	System.out.println("add podcast info");
  }

  private static void update_podcast_info(Connection connection) throws SQLException {
	System.out.println("update podcast info");
  }

  private static void add_podcast_episode_info(Connection connection) throws SQLException {
	System.out.println("add podcast episode info");
  }

  private static void update_podcast_episode_info(Connection connection) throws SQLException {
	System.out.println("update podcast episode info");
  }

  private static void add_podcast_host_info(Connection connection) throws SQLException {
	System.out.println("add podcast host info");
  }

  private static void update_podcast_host_info(Connection connection) throws SQLException {
	System.out.println("update podcast host info");
  }

  private static void view_all_podcasts(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("List of Podcast");
    System.out.println("");

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
        System.out.println(row + " " + podcastname + "");
        row++;
      }
    } catch (SQLException e) {
        Helper.close(rs);
    	System.out.println("Unfortunately, we don't have any podcasts yet!");
    	view_all_podcasts(connection);
    }
  }

  private static void view_all_podcast_episodes(Connection connection) throws SQLException {
	System.out.println("view podcast episodes");
  }

  private static void view_all_podcast_hosts(Connection connection) throws SQLException {
	System.out.println("view podcast hosts");
  }

  public static Statement statement;

  //	Get podcast List for user
  public static void get_podcast(String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("List of Podcast");
    System.out.println("");

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
        System.out.println(row + " " + podcastname + "");
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
    }
  }

  //    Get podcast episodes given podcast name
  public static void get_podcast_episodes(String podcastname, String u_email_id, Connection connection)
	throws SQLException {
    int row = 1;
    System.out.println("");
    System.out.println("List of Podcast Episode for: " + podcastname);
    System.out.println("");

    statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    String query, podcastepisodename, pe_title = "";

    query = "SELECT pe_title FROM PodcastEpisode where p_name ='" + podcastname + "'";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      while (rs.next()) {
        podcastepisodename = rs.getString("pe_title");
        System.out.println(row + " " + podcastname + " : " + podcastepisodename + "");
        row++;
      }
      while(true) {
    	try {
     	  System.out.println("");
   	      System.out.println("Enter podcast episode title to select");
   	      System.out.println("");

   	      pe_title = sc.next();

          query = "SELECT pe_title FROM PodcastEpisode where pe_title='" + pe_title + "' AND p_name='" + podcastname + "'";
          rs = statement.executeQuery(query);
          if (rs.next()) {
          	podcast_episode_play_count(podcastname, pe_title, u_email_id, connection);
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
    }
  }

  //	  increment play count
  public static void podcast_episode_play_count(String podcastname, String pe_title, String u_email_id, Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("Thank you for listening to Episode: " + pe_title + " from " + podcastname);
    System.out.println("");

    User.getusermenu(u_email_id, connection);
  }
}
