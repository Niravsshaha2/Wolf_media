package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Scanner;

public class PM {

  public static String[] podcast_list, podcasthost_list, podcastepisode_list;

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
            view_all_podcast_hosts(connection, "name");
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
      // } finally {
      //   closers(statement, sc);
      }
    } while (enteredValue != 0);
  }



  //	SWITCH CASE FOR PM MENU
  public static void view_all_podcasts(Connection connection)
    throws SQLException {
    System.out.println("*****");
    System.out.println("List of Podcasts");

    statement = connection.createStatement();
    String query, podcastname = "";

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

  public static void add_podcast_info(Connection connection)
    throws SQLException {
    // p_name, p_sponsor, p_language, p_country, p_rating, p_rated_user_count
    System.out.println("Enter Podcast Details:");
    Statement statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    try {
      System.out.println("Enter Podcast name:");
      String podcastname = "";
      podcastname = sc.nextLine();
      System.out.println(podcastname);
      // do {
      //   podcastname = sc.nextLine();
      //   if(Arrays.asList(podcast_list).contains(podcastname)) {
      //     System.out.println("Podcast already present, please enter podcast name again:");
      //   }
      // } while(Arrays.asList(podcast_list).contains(podcastname));

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
        // System.out.println(podcastrating, (podcastrating == 0.0f), 0 == 0.0f);
        if(podcastrating == 0.0f) {
          break;
        }
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
    // } finally {
    //   closers(statement, sc);
    }
  }

  public static void update_podcast_info(Connection connection, String typ)
    throws SQLException {
    String podcast_continue_choice = "y";
    String podcastname_choice = "";
    Scanner sc = new Scanner(System.in);
    view_all_podcasts(connection);
    do {
    System.out.println("Enter podcast name:");
    try {
      podcastname_choice = sc.nextLine();
      if(!Arrays.asList(podcast_list).contains(podcastname_choice)) {
      System.out.println("Podcast does not exist. Please try again from below list of podcasts!");
      update_podcast_info(connection, typ);
      }
      update_podcast_choice_function(connection, podcastname_choice, typ);
        System.out.println("Do you want to " + typ + " in another podcast? (y/n)");
        podcast_continue_choice = sc.next();
    } catch(Exception e) {
      System.out.println("Podcast does not exist. Please try again!");
      update_podcast_info(connection, typ);
    }
      } while(podcast_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_choice_function(Connection connection, String podcastname_choice, String typ)
    throws SQLException {
    String query = "", new_str_val  = "";
    float new_num_val = 0;
    int rows = 0, update_choice = 0;
    String update_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      System.out.println("Which field to "+ typ + "?:");
        System.out.println("1. Sponsor");
        System.out.println("2. Languauge");
        System.out.println("3. Rating");
        System.out.println("4. Country");
        System.out.println("0. Go back to choosing podcast");
        System.out.println("Enter your option:");
        System.out.println("");
        try {
          update_choice = sc.nextInt();
          switch(update_choice) {
            case 1:
            if(typ.equals("update")) {
              System.out.println("Enter new value for sponsors (decimal):");
              new_num_val = sc.nextFloat();
              query = "UPDATE Podcast SET p_sponsor=" + new_num_val + " WHERE p_name='" + podcastname_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE Podcast SET p_sponsor=" + null + " WHERE p_name='" + podcastname_choice + "'";
            }
            new_num_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 2:
            if(typ.equals("update")) {
              System.out.println("Enter new value for language:");
              new_str_val = sc.next();
              query = "UPDATE Podcast SET p_language='" + new_str_val + "' WHERE p_name='" + podcastname_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE Podcast SET p_language=" + null + " WHERE p_name='" + podcastname_choice + "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 3:
            if(typ.equals("update")) {
            do {
                  System.out.println("Enter new value for rating [0-5] :");
                new_num_val = sc.nextFloat();
              if ((new_num_val < 0 || new_num_val > 5) && new_num_val != 0.0f) {
              System.out.println("Rating needs to be between 0 to 5 or leave it blank.");
              }
            } while((new_num_val < 0 || new_num_val > 5) && new_num_val != 0.0f);

              query = "UPDATE Podcast SET p_rating = "
                  + "IF(p_rating IS NULL," + new_num_val +", (p_rating * p_rated_user_count + " + new_num_val + ")/(p_rated_user_count + 1)), "
                  + "p_rated_user_count = p_rated_user_count + 1 "
                  + "WHERE p_name = '" + podcastname_choice + "';";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE Podcast SET p_rating=" + null + " WHERE p_name='" + podcastname_choice + "'";
            }
            new_num_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 4:
            if(typ.equals("update")) {
              System.out.println("Enter new value for country:");
              new_str_val = sc.next();
              query = "UPDATE Podcast SET p_country='" + new_str_val + "' WHERE p_name='" + podcastname_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE Podcast SET p_country=" + null + " WHERE p_name='" + podcastname_choice + "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 0:
            update_podcast_info(connection, typ);
            break;

          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made a wrong choice. Please choose again:");
        update_podcast_choice_function(connection, podcastname_choice, typ);
      }
      System.out.println("Do you want to " + typ + " more fields for the podcast? (y/n)");
      update_continue_choice = sc.next();
      System.out.println(update_continue_choice);
    } while(update_continue_choice.toLowerCase().equals("y"));
  }



  public static void view_all_podcast_hosts(Connection connection, String typ)
    throws SQLException {
    System.out.println("*****");
    if(typ.equals("name")) {
      System.out.println("List of Podcast Hosts (with podcast and episode being hosted)");
    }
    else if(typ.equals("email")) {
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
      if(rs.last()) {
        podcasthost_count = rs.getRow();
        rs.beforeFirst();
      }
      podcasthost_list = new String[podcasthost_count];

      while (rs.next()) {
        podcasthostfirstname = rs.getString("ph_first_name");
        podcasthostlastname = rs.getString("ph_last_name");
        podcasthostemail = rs.getString("ph_email_id");
        podcasthost_list[row-1] = podcasthostemail;
        if(typ.equals("name")) {
          System.out.println(row + "] " + podcasthostfirstname + " " + podcasthostlastname);
        }
        else if(typ.equals("email")) {
          System.out.println(row + "] " + podcasthostemail);
        }
        row++;
      }
    } catch (SQLException e) {
      System.out.println("Unfortunately, we don't have any podcast hosts yet!");
      get_pm_menu(connection);
    }
  }

  public static void add_podcast_host_info(Connection connection)
    throws SQLException {
    // ph_email_id, ph_first_name, ph_last_name, ph_phone, ph_city
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
    // } finally {
    //   closers(statement, sc);
    }
  }

  public static void update_podcast_host_info(Connection connection, String typ)
    throws SQLException {
    String podcasthost_continue_choice = "y";
    String podcasthostname_choice = "";
    Scanner sc = new Scanner(System.in);
    view_all_podcast_hosts(connection, "email");
    do {
      System.out.println("Enter podcast host email:");
      try {
        podcasthostname_choice = sc.nextLine();
        if(!Arrays.asList(podcasthost_list).contains(podcasthostname_choice)) {
          System.out.println("Podcast host does not exist. Please try again from below list of podcast hosts!");
          update_podcast_host_info(connection, typ);
        }
        update_podcast_host_choice_function(connection, podcasthostname_choice, typ);
        System.out.println("Do you want to " + typ + " in another podcast host? (y/n)");
        podcasthost_continue_choice = sc.next();
      } catch(Exception e) {
        System.out.println("Podcast host does not exist. Please try again!");
        update_podcast_host_info(connection, typ);
      }
    } while(podcasthost_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_host_choice_function(Connection connection, String podcasthostemail_choice, String typ)
    throws SQLException {
    String query = "", new_str_val  = "";
    float new_num_val = 0;
    long new_phone_val = 0;
    int rows = 0, update_choice = 0;
    String update_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      System.out.println("Which field to "+ typ + "?:");
      System.out.println("1. First Name");
      System.out.println("2. Last Name");
      System.out.println("3. Phone");
      System.out.println("4. City");
      System.out.println("0. Go back to choosing podcast host name");
      System.out.println("Enter your option:");
      System.out.println("");
      try {
        update_choice = sc.nextInt();
        switch(update_choice) {
          case 1:
            if(typ.equals("update")) {
              System.out.println("Enter new value for first name:");
              new_str_val = sc.next();
              query = "UPDATE PodcastHost SET ph_first_name='" + new_str_val + "' WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastHost SET ph_first_name=" + null + " WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 2:
            if(typ.equals("update")) {
              System.out.println("Enter new value for last name:");
              new_str_val = sc.next();
              query = "UPDATE PodcastHost SET ph_last_name='" + new_str_val + "' WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastHost SET ph_last_name=" + null + " WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            new_str_val = "";

            System.out.println(query);

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 3:
            if(typ.equals("update")) {
              System.out.println("Enter new value for phone:");
              new_phone_val = sc.nextLong();
              query = "UPDATE PodcastHost SET ph_phone=" + new_phone_val + " WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastHost SET ph_phone=" + null + " WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            new_phone_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 4:
            if(typ.equals("update")) {
              System.out.println("Enter new value for city:");
              new_str_val = sc.next();
              query = "UPDATE PodcastHost SET ph_city='" + new_str_val + "' WHERE ph_email_id='" + podcasthostemail_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastHost SET ph_city=" + null + " WHERE ph_email_id='" + podcasthostemail_choice + "'";
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
        update_podcast_host_choice_function(connection, podcasthostemail_choice, typ);
      }
      System.out.println("Do you want to " + typ + " more fields for the podcast? (y/n)");
      update_continue_choice = sc.next();
      System.out.println(update_continue_choice);
    } while(update_continue_choice.toLowerCase().equals("y"));
  }



  public static void view_all_podcast_episodes(Connection connection)
    throws SQLException {
    System.out.println("*****");
    System.out.println("List of Podcast Episodes (with podcasts they are part of)");

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

  public static String[] view_all_podcast_episodes(Connection connection, String podcastname, String show_type)
    throws SQLException {
    statement = connection.createStatement();
    String query, podcastepisodename;

    query = "SELECT pe_title FROM PodcastEpisode WHERE p_name='" + podcastname + "'";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;
      int podcasthepisode_count = 0;
      if(rs.last()) {
        podcasthepisode_count = rs.getRow();
        rs.beforeFirst();
      }
      podcastepisode_list = new String[podcasthepisode_count];

      if(show_type.equals("show")) {
        System.out.println("~ List of all episodes for " + podcastname);
      }
      while (rs.next()) {
        podcastepisodename = rs.getString("pe_title");
        if(show_type.equals("show")) {
          System.out.println(row + "] " + podcastepisodename);
        }
        podcastepisode_list[row-1] = podcastepisodename;
        row++;
      }
    } catch (SQLException e) {
      System.out.println("There are no episodes for" + podcastname + "!");
      get_pm_menu(connection);
    }
    return podcastepisode_list;
  }

  public static void add_podcast_episode_info(Connection connection)
    throws SQLException {
    // pe_title, p_name, pe_release_date, pe_ad_count, pe_duration, ph_email_id
    Statement statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    view_all_podcasts(connection);
    try {
      System.out.println("Enter Podcast name for which you want to add podcast episode:");
      String podcastname = "";
      podcastname = sc.nextLine();
      System.out.println(podcastname);
      if(!Arrays.asList(podcast_list).contains(podcastname)) {
        System.out.println("Podcast does not exist. Please try again from below list of podcasts!");
        add_podcast_episode_info(connection);
      }

      System.out.println("");
      System.out.println("***** Enter Podcast Episode Details *****");
      System.out.println("");

      podcastepisode_list = view_all_podcast_episodes(connection, podcastname, "hide");
      String podcastepisodename = "";
      do {
        System.out.println("Enter Podcast Episode title:");
        podcastepisodename = sc.nextLine();
        if(Arrays.asList(podcastepisode_list).contains(podcastepisodename)) {
          System.out.println("Podcast episode already exist! Try Again with anothe title!");
        }
      } while(Arrays.asList(podcastepisode_list).contains(podcastepisodename));

      System.out.println("Enter episode release date (yyyy-mm-dd):");
      String podcastepisoderelease = sc.next();
      java.sql.Date podcastepisodereleasedate = java.sql.Date.valueOf(podcastepisoderelease);

      int podcastepisodeadcount = 0;

      System.out.println("Enter episode duration (hh:mm:ss):");
      String podcastepisodedura = sc.next();
      DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern("[HH:mm:ss][HH:mm:s][HH:m:ss][H:mm:ss][HH:m:s][H:m:ss][H:mm:s][H:m:s][HH:mm][HH:m][H:mm][mm:ss][m:ss][mm:s][m:s]")
        .toFormatter();
      LocalTime localTime = LocalTime.parse(podcastepisodedura, formatter);
      java.sql.Time podcastepisodeduration = java.sql.Time.valueOf(localTime);
      // java.sql.Time podcastepisodeduration = java.sql.Time.valueOf(podcastepisodedura);

      String podcastepisodehost = "";
      do {
        System.out.println("Enter Host Email for Podcast Episode (You can choose from list below):");
        view_all_podcast_hosts(connection, "email");
        podcastepisodehost = sc.next();
        if(!Arrays.asList(podcasthost_list).contains(podcastepisodehost)) {
          System.out.println("Podcast host is not present! Please check for any spelling mistake!");
        }
      } while(!Arrays.asList(podcasthost_list).contains(podcastepisodehost));

      String sql =
        "INSERT INTO PodcastEpisode (p_name, pe_title, pe_release_date, pe_ad_count, pe_duration, ph_email_id) VALUES ('" +
        podcastname +
        "', '" +
        podcastepisodename +
        "', '" +
        podcastepisodereleasedate +
        "', " +
        podcastepisodeadcount +
        ", '" +
        podcastepisodeduration +
        "', '" +
        podcastepisodehost +
        "')";
      int rows = statement.executeUpdate(sql);
      System.out.println(rows + " row(s) inserted.");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    // } finally {
    //   closers(statement, sc);
    }
  }

  public static void update_podcast_episode_info(Connection connection, String typ)
    throws SQLException {
    String podcast_continue_choice = "n";
    String podcastname_choice;
    Scanner sc = new Scanner(System.in);
    view_all_podcasts(connection);
    do {
      System.out.println("Enter podcast name (from which the episode is to be" + typ + "d) (Enter 0 to go back to main menu):");
      try {
        podcastname_choice = sc.nextLine();
        if(!podcastname_choice.equals("0")) {
          if(!Arrays.asList(podcast_list).contains(podcastname_choice)) {
            System.out.println("Podcast does not exist. Please try again from below list of podcasts!");
            update_podcast_episode_info(connection, typ);
          }
          update_podcast_episode_choice_function(connection, podcastname_choice, typ);
          System.out.println("Do you want to " + typ + " from episode in another podcast? (y/n)");
          podcast_continue_choice = sc.next();
        }
        else {
          get_pm_menu(connection);
        }
      } catch(Exception e) {
        System.out.println("Podcast does not exist. Please try again!");
        update_podcast_episode_info(connection, typ);
      }
    } while(podcast_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_episode_choice_function(Connection connection, String podcastname_choice, String typ)
    throws SQLException {
    String podcastepisodename_choice = "", podcast_episode_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      podcastepisode_list = view_all_podcast_episodes(connection, podcastname_choice, "show");
      System.out.println("Enter podcast episode name: (Enter 0 to go back to choosing podcast name)");
      try {
        podcastepisodename_choice = sc.nextLine();
        if(!podcastepisodename_choice.equals("0")) {
          if(!Arrays.asList(podcastepisode_list).contains(podcastepisodename_choice)) {
            System.out.println("Podcast episode does not exist. Please try again from below list of podcast episodes!");
            update_podcast_episode_choice_function(connection, podcastname_choice, typ);
          }
          update_podcast_episode_choice_function(connection, podcastepisodename_choice, typ, podcastname_choice);
          System.out.println("Do you want to " + typ + " in other podcast episode? (y/n)");
          podcast_episode_continue_choice = sc.next();
        } else {
          update_podcast_episode_info(connection, typ);
        }
      } catch(Exception e) {
        System.out.println("Podcast episode does not exist. Please try again!");
        update_podcast_episode_info(connection, typ);
      }
    } while(podcast_episode_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_episode_choice_function(Connection connection, String podcastepisodename_choice, String typ, String podcastname_choice)
    throws SQLException {
    // pe_release_date, pe_ad_count, pe_duration
    String query = "", new_str_val  = "";
    float new_num_val = 0;
    int rows = 0, update_choice = 0;
    String update_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      System.out.println("Which field to "+ typ + "?:");
      System.out.println("1. Release Date");
      System.out.println("2. Ad count");
      System.out.println("3. Duration");
      System.out.println("0. Go back to choosing podcast episode");
      System.out.println("Enter your option:");
      System.out.println("");
      try {
        update_choice = sc.nextInt();
        switch(update_choice) {
          case 1:
            if(typ.equals("update")) {
              System.out.println("Enter new value for episode release date (yyyy-mm-dd):");
              new_str_val = sc.next();
              java.sql.Date new_str_val_date = java.sql.Date.valueOf(new_str_val);
              query = "UPDATE PodcastEpisode SET pe_release_date='" + new_str_val_date + "' WHERE p_name='" + podcastname_choice + "' AND pe_title='" + podcastepisodename_choice + "'";
              System.out.println(query);
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastEpisode SET pe_release_date=" + null + " WHERE p_name='" + podcastname_choice + "' AND pe_title='" + podcastepisodename_choice + "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 2:
            if(typ.equals("update")) {
              System.out.println("Enter new value for ad count :");
              new_num_val = sc.nextFloat();
              query = "UPDATE PodcastEpisode SET pe_ad_count=" + new_num_val + " WHERE p_name='" + podcastname_choice + "' AND pe_title='" + podcastepisodename_choice + "'";;
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastEpisode SET pe_ad_count=" + null + " WHERE p_name='" + podcastname_choice + "' AND pe_title='" + podcastepisodename_choice + "'";;
            }
            new_num_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 3:
            if(typ.equals("update")) {
              System.out.println("Enter new value for episode duration (hh:mm:ss):");
              new_str_val = sc.next();
              DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("[HH:mm:ss][HH:mm:s][HH:m:ss][H:mm:ss][HH:m:s][H:m:ss][H:mm:s][H:m:s][HH:mm][HH:m][H:mm][mm:ss][m:ss][mm:s][m:s]")
                .toFormatter();
              LocalTime localTime = LocalTime.parse(new_str_val, formatter);
              java.sql.Time new_str_val_time = java.sql.Time.valueOf(localTime);
              query = "UPDATE PodcastEpisode SET pe_duration='" + new_str_val_time + "' WHERE p_name='" + podcastname_choice + "' AND pe_title='" + podcastepisodename_choice + "'";
            }
            else if(typ.equals("delete")) {
              query = "UPDATE PodcastEpisode SET pe_duration=" + null + " WHERE p_name='" + podcastname_choice + "' AND pe_title='" + podcastepisodename_choice + "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 0:
            update_podcast_episode_choice_function(connection, podcastname_choice, typ);
            break;

          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made a wrong choice. Please choose again:");
        update_podcast_episode_choice_function(connection, podcastepisodename_choice, typ, podcastname_choice);
      }
      System.out.println("Do you want to " + typ + " more fields for the podcast episode? (y/n)");
      update_continue_choice = sc.next();
    } while(update_continue_choice.toLowerCase().equals("y"));
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
