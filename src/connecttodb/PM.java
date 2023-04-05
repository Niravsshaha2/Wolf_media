package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.Arrays;
import java.util.Scanner;

public class PM {

  public static String[] podcast_list;

  public static Statement statement;

  public static void get_pm_menu(Connection connection)
    throws SQLException {
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
      System.out.println("15. Assign Podcast Host to all Episodes in a Podcast");
      System.out.println("0. Main Menu");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
	        view_all_podcasts(connection);
	        break;
          case 2:
            add_podcast_info(connection);
            break;
          case 3:
            update_podcast_info(connection, "update");
            break;
          case 4:
            update_podcast_info(connection, "delete");
            break;

	      case 5:
		    view_all_podcast_hosts(connection);
		    break;
          case 6:
	        add_podcast_host_info(connection);
	        break;
	      case 7:
	        update_podcast_host_info(connection, "update");
	        break;
	      case 8:
	        update_podcast_host_info(connection, "delete");
	        break;

          case 9:
	        view_all_podcast_episodes(connection);
	        break;
          case 10:
	        add_podcast_episode_info(connection);
	        break;
	      case 11:
	        update_podcast_episode_info(connection, "update");
	        break;
	      case 12:
	        update_podcast_episode_info(connection, "delete");
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
//      } finally {
//        closers(statement, sc);
      }
    } while (enteredValue != 0);
  }

  //	SWITCH CASE FOR PM MENU
  public static void view_all_podcasts(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("List of Podcast");
    System.out.println("");

    statement = connection.createStatement();
    String query, podcastname;

    query = "SELECT p_name FROM Podcast";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;
      int podcast_count = 0;
      if(rs.last()) {
	    podcast_count = rs.getRow();
	    rs.beforeFirst();
      }
      podcast_list = new String[podcast_count];

      while (rs.next()) {
    	podcastname = rs.getString("p_name");
    	podcast_list[row-1] = podcastname;
        System.out.println(row + "] " + podcastname + "");
        row++;
      }
    } catch (SQLException e) {
    	System.out.println("Unfortunately, we don't have any podcasts yet!");
    	get_pm_menu(connection);
    }
  }

  public static void add_podcast_info(Connection connection) throws SQLException {
//	  p_name, p_sponsor, p_language, p_country, p_rating, p_rated_user_count
	System.out.println("Enter Podcast Details:");
	Statement statement = connection.createStatement();
	Scanner sc = new Scanner(System.in);
	try {
	  System.out.println("Enter Podcast name:");
	  String podcastname = "";
	  podcastname = sc.nextLine();
	  System.out.println(podcastname);
//	  do {
//		  podcastname = sc.nextLine();
//		  if(Arrays.asList(podcast_list).contains(podcastname)) {
//			  System.out.println("Podcast already present, please enter podcast name again:");
//		  }
//	  } while(Arrays.asList(podcast_list).contains(podcastname));

	  System.out.println("Enter podcast language:");
	  String podcastlanguage = sc.nextLine();
	  System.out.println(podcastlanguage);

	  System.out.println("Enter sponsored amount (numerical value):");
	  float podcastsponsor = sc.nextFloat();
	  System.out.println(podcastsponsor);

	  System.out.println("Enter podcast country:");
	  sc.next();
	  String podcastcountry = sc.nextLine();
	  System.out.println(podcastcountry);

	  float podcastrating;
	  int podcastratedusercount = 0;
	  do {
        System.out.println("Enter podcast rating (0 to 5) [default null]:");
	    podcastrating = sc.nextFloat();
	    if ((podcastrating < 0 || podcastrating > 5) && podcastrating != 0.0f) {
		  System.out.println("Rating needs to be between 0 to 5 or leave it blank.");
	    } else podcastratedusercount = 1;
	  } while((podcastrating < 0 || podcastrating > 5) && podcastrating != 0.0f);

      String sql =
        "INSERT INTO Podcast (p_name, p_sponsor, p_language, p_country, p_rating, p_rated_user_count) VALUES ('" +
        podcastname +
        "', '" +
        podcastsponsor +
        "', '" +
        podcastlanguage +
        "', '" +
        podcastcountry +
        "', '" +
        podcastrating +
        "', '" +
        podcastratedusercount +
        "')";
      int rows = statement.executeUpdate(sql);
      System.out.println(rows + " row(s) inserted.");
	} catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
//    } finally {
//      closers(statement, sc);
    }
  }

  public static void update_podcast_info(Connection connection, String type) throws SQLException {
  }

  
  public static void view_all_podcast_hosts(Connection connection)
	throws SQLException {
    System.out.println("");
    System.out.println("List of Podcast Hosts (with podcast and episode being hosted)");
    System.out.println("");

    statement = connection.createStatement();
    String query, podcasthostfirstname, podcasthostlastname;

    query = "SELECT ph_email_id, ph_first_name, ph_last_name FROM PodcastHost";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
    	podcasthostfirstname = rs.getString("ph_first_name");
    	podcasthostlastname = rs.getString("ph_last_name");
        System.out.println(row + "] " + podcasthostfirstname + " " + podcasthostlastname);
        row++;
      }
    } catch (SQLException e) {
    	System.out.println("Unfortunately, we don't have any podcast hosts yet!");
    	get_pm_menu(connection);
    }
  }

  public static void add_podcast_host_info(Connection connection)
	throws SQLException {
//	  ph_email_id, ph_first_name, ph_last_name, ph_phone, ph_city
	System.out.println("Enter Podcast Details:");
	Statement statement = connection.createStatement();
	Scanner sc = new Scanner(System.in);
	try {
	  System.out.println("Enter Podcast Host Email:");
	  String podcasthostemail = sc.next();
	  System.out.println(podcasthostemail);
	  
	  System.out.println("Enter Podcast Host First Name:");
	  String podcasthostfirstname = sc.next();
	  System.out.println(podcasthostfirstname);
	  
	  System.out.println("Enter Podcast Host Last Name:");
	  String podcasthostlastname = sc.next();
	  System.out.println(podcasthostlastname);
	  
	  System.out.println("Enter Podcast Host City:");
	  String podcasthostcity = sc.next();
	  System.out.println(podcasthostcity);
	  
	  System.out.println("Enter Podcast Host Phone number:");
	  long podcasthostphone = sc.nextLong();
	  System.out.println(podcasthostphone);

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
//    } finally {
//      closers(statement, sc);
    }
  }

  public static void update_podcast_host_info(Connection connection, String type)
	throws SQLException {
	System.out.println("update podcast host info");
  }


  public static void view_all_podcast_episodes(Connection connection)
	throws SQLException {
    System.out.println("");
    System.out.println("List of Podcast Episodes (with podcasts they are part of)");
    System.out.println("");

    statement = connection.createStatement();
    String query, podcastepisodename, podcastname;

    query = "SELECT p_name, pe_title FROM PodcastEpisode";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;

      while (rs.next()) {
    	podcastname = rs.getString("p_name");
    	podcastepisodename = rs.getString("pe_title");
        System.out.println(row + "] " + podcastepisodename + " (" + podcastname + ")");
        row++;
      }
    } catch (SQLException e) {
    	System.out.println("Unfortunately, we don't have any podcast episodes yet!");
    	get_pm_menu(connection);
    }
  }

  public static void add_podcast_episode_info(Connection connection)
	throws SQLException {
//	  pe_title, p_name, pe_release_date, pe_ad_count, pe_duration, ph_email_id
	System.out.println("~ Enter Podcast Episode Details ~");
	Statement statement = connection.createStatement();
	Scanner sc = new Scanner(System.in);
	try {
	  System.out.println("Enter Podcast name:");
	  String podcastname = "";
	  podcastname = sc.nextLine();
//	  System.out.println(Arrays.asList(podcast_list).contains(podcastname));
//	  do {
//		  podcastname = sc.nextLine();
//		  if(Arrays.asList(podcast_list).contains(podcastname)) {
//			  System.out.println("Podcast already present, please enter podcast name again:");
//		  }
//	  } while(Arrays.asList(podcast_list).contains(podcastname));

	  System.out.println("Enter Podcast Episode title:");
	  String podcastepisodename = sc.nextLine();

	  System.out.println("Enter episode release date (yyyy-mm-dd):");
      String podcastepisoderelease = sc.nextLine();
	  java.sql.Date podcastepisodereleasedate = java.sql.Date.valueOf(podcastepisoderelease);

	  int podcastepisodeadcount = 0;

	  System.out.println("Enter episode duration:");
      String podcastepisodedura = sc.nextLine();
	  java.sql.Time podcastepisodeduration = java.sql.Time.valueOf(podcastepisodedura);

	  System.out.println("Enter Host for Podcast Episode:");
	  String podcastepisodehost = sc.nextLine();

      String sql =
        "INSERT INTO PodcastEpisode (p_name, pe_title, pe_release_date, pe_ad_count, pe_duration, ph_email_id) VALUES ('" +
        podcastname +
        "', '" +
        podcastepisodename +
        "', '" +
        podcastepisodereleasedate +
        "', '" +
        podcastepisodeadcount +
        "', '" +
        podcastepisodeduration +
        "', '" +
        podcastepisodehost +
        "')";
      int rows = statement.executeUpdate(sql);
      System.out.println(rows + " row(s) inserted.");
	} catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
//    } finally {
//      closers(statement, sc);
    }
  }


  public static void update_podcast_episode_info(Connection connection, String type)
	throws SQLException {
	System.out.println("update podcast episode info");
  }



  public static void assign_podcast_episode_to_podcast(Connection connection) {
	  System.out.println("assign_podcast_episode_to_podcast");
  }

  public static void assign_podcast_host_to_podcast_episode(Connection connection) {
	  System.out.println("assign_podcast_host_to_podcast_episode");
  }

  public static void assign_podcast_host_to_podcast(Connection connection) {
	  System.out.println("assign_podcast_host_to_podcast");
  }


  //	GET PODACST LIST FOR PM TO FURTHER SELECT EPISODE FROM THE PODCAST
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
    } finally {
      closers(statement, sc);
    }
  }


  //	GET PODACST EPISODE GIVEN PODCAST NAME
  public static void get_podcast_episodes(String podcastname, String u_email_id, Connection connection)
	throws SQLException {
    System.out.println("");
    System.out.println("List of Podcast Episode for: " + podcastname);
    System.out.println("");

    statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    String query, podcastepisodename, pe_title;

    query = "SELECT pe_title FROM PodcastEpisode where p_name ='" + podcastname + "'";

    ResultSet rs = null;
    try {
      int row = 1;
      rs = statement.executeQuery(query);
      while (rs.next()) {
        podcastepisodename = rs.getString("pe_title");
        System.out.println("(" + row + ") " + podcastname + " : " + podcastepisodename + "");
        row++;
      }
      while(true) {
    	try {
     	  System.out.println("");
   	      System.out.println("Enter podcast episode title to select");
   	      System.out.println("");

   	      pe_title = sc.nextLine();

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
    } finally {
	  closers(statement, sc);
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
