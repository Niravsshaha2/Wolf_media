package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Scanner;
import java.util.Arrays;

public class podcastepisode {
  public static String[] podcastepisode_list, podcast_list, podcasthost_list;
  public static Statement statement;
  Scanner sc = new Scanner(System.in);

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
      PM.get_pm_menu(connection);
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
      if (rs.last()) {
        podcasthepisode_count = rs.getRow();
        rs.beforeFirst();
      }
      podcastepisode_list = new String[podcasthepisode_count];

      if (show_type.equals("show")) {
        System.out.println("~ List of all episodes for " + podcastname);
      }
      while (rs.next()) {
        podcastepisodename = rs.getString("pe_title");
        if (show_type.equals("show")) {
          System.out.println(row + "] " + podcastepisodename);
        }
        podcastepisode_list[row - 1] = podcastepisodename;
        row++;
      }
    } catch (SQLException e) {
      System.out.println("There are no episodes for" + podcastname + "!");
      PM.get_pm_menu(connection);
    }
    return podcastepisode_list;
  }

  public static void add_podcast_episode_info(Connection connection)
    throws SQLException {
    // pe_title, p_name, pe_release_date, pe_ad_count, pe_duration, ph_email_id
    Statement statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    podcast_list = podcast.view_all_podcasts(connection, "show");
    try {
      System.out.println("Enter Podcast name for which you want to add podcast episode:");
      String podcastname = "";
      podcastname = sc.nextLine();
      System.out.println(podcastname);
      if (!Arrays.asList(podcast_list).contains(podcastname)) {
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
        if (Arrays.asList(podcastepisode_list).contains(podcastepisodename)) {
          System.out.println("Podcast episode already exist! Try Again with anothe title!");
        }
      } while(Arrays.asList(podcastepisode_list).contains(podcastepisodename));

      System.out.println("Enter episode release date (yyyy-mm-dd):");
      String podcastepisoderelease = sc.nextLine();
      java.sql.Date podcastepisodereleasedate = java.sql.Date.valueOf(podcastepisoderelease);

      int podcastepisodeadcount = 0;

      System.out.println("Enter episode duration (hh:mm:ss):");
      String podcastepisodedura = sc.nextLine();
      DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern(
          "[HH:mm:ss][HH:mm:s][HH:m:ss][H:mm:ss][HH:m:s][H:m:ss][H:mm:s][H:m:s][HH:mm][HH:m][H:mm][mm:ss][m:ss][mm:s][m:s]"
        ).toFormatter();
      LocalTime localTime = LocalTime.parse(podcastepisodedura, formatter);
      java.sql.Time podcastepisodeduration = java.sql.Time.valueOf(localTime);
      // java.sql.Time podcastepisodeduration = java.sql.Time.valueOf(podcastepisodedura);

      String podcastepisodehost = "";
      do {
        System.out.println("Enter Host Email for Podcast Episode (You can choose from list below):");
        podcasthost_list = podcasthost.view_all_podcast_hosts(connection, "email");
        podcastepisodehost = sc.nextLine();
        if (!Arrays.asList(podcasthost_list).contains(podcastepisodehost)) {
          System.out.println("Podcast host is not present! Please check for any spelling mistake!");
        }
      } while (!Arrays.asList(podcasthost_list).contains(podcastepisodehost));

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
      String query2 = "UPDATE Podcast SET p_episode_count = p_episode_count + 1 WHERE p_name='" + podcastname + "'";
      rows = statement.executeUpdate(query2);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      // } finally {
      //   closers(statement, sc);
    }
  }

  public static void update_podcast_episode_info(Connection connection, String typ)
    throws SQLException {
    String podcast_continue_choice = "n";
    String podcastname_choice;
    Scanner sc = new Scanner(System.in);
    podcast_list = podcast.view_all_podcasts(connection, "show");
    do {
      System.out.println("Enter podcast name (from which the episode is to be" + typ + "d) (Enter 0 to go back to main menu):");
      try {
        podcastname_choice = sc.nextLine();
        if (!podcastname_choice.equals("0")) {
          if (!Arrays.asList(podcast_list).contains(podcastname_choice)) {
            System.out.println("Podcast does not exist. Please try again from below list of podcasts!");
            update_podcast_episode_info(connection, typ);
          }
          update_podcast_episode_choice_function(connection, podcastname_choice, typ);
          System.out.println("Do you want to " + typ + " from episode in another podcast? (y/n)");
          podcast_continue_choice = sc.next();
        } else {
          PM.get_pm_menu(connection);
        }
      } catch (Exception e) {
        System.out.println("Podcast does not exist. Please try again!");
        update_podcast_episode_info(connection, typ);
      }
    } while (podcast_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_episode_choice_function(Connection connection, String podcastname_choice, String typ)
    throws SQLException {
    String podcastepisodename_choice = "", podcast_episode_continue_choice =
      "n";
    Scanner sc = new Scanner(System.in);
    do {
      podcastepisode_list = view_all_podcast_episodes(connection, podcastname_choice, "show");
      System.out.println("Enter podcast episode name: (Enter 0 to go back to choosing podcast name)");
      try {
        podcastepisodename_choice = sc.nextLine();
        if (!podcastepisodename_choice.equals("0")) {
          if (!Arrays.asList(podcastepisode_list).contains(podcastepisodename_choice)) {
            System.out.println("Podcast episode does not exist. Please try again from below list of podcast episodes!");
            update_podcast_episode_choice_function(connection, podcastname_choice, typ);
          }
          update_podcast_episode_choice_function(connection, podcastepisodename_choice, typ, podcastname_choice);
          System.out.println("Do you want to " + typ + " in other podcast episode? (y/n)");
          podcast_episode_continue_choice = sc.next();
        } else {
          update_podcast_episode_info(connection, typ);
        }
      } catch (Exception e) {
        System.out.println("Podcast episode does not exist. Please try again!");
        update_podcast_episode_info(connection, typ);
      }
    } while (podcast_episode_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_episode_choice_function(Connection connection, String podcastepisodename_choice, String typ, String podcastname_choice)
    throws SQLException {
    // pe_release_date, pe_ad_count, pe_duration
    String query = "", new_str_val = "";
    float new_num_val = 0;
    int rows = 0, update_choice = 0;
    String update_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      System.out.println("Which field to " + typ + "?:");
      System.out.println("1. Release Date");
      System.out.println("2. Ad count");
      System.out.println("3. Duration");
      System.out.println("0. Go back to choosing podcast episode");
      System.out.println("Enter your option:");
      System.out.println("");
      try {
        update_choice = sc.nextInt();
        switch (update_choice) {
          case 1:
            if (typ.equals("update")) {
              System.out.println("Enter new value for episode release date (yyyy-mm-dd):");
              new_str_val = sc.next();
              java.sql.Date new_str_val_date = java.sql.Date.valueOf(new_str_val);
              query =
                "UPDATE PodcastEpisode SET pe_release_date='" +
                new_str_val_date +
                "' WHERE p_name='" +
                podcastname_choice +
                "' AND pe_title='" +
                podcastepisodename_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastEpisode SET pe_release_date=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "' AND pe_title='" +
                podcastepisodename_choice +
                "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 2:
            if (typ.equals("update")) {
              System.out.println("Enter new value for ad count :");
              new_num_val = sc.nextFloat();
              query =
                "UPDATE PodcastEpisode SET pe_ad_count=" +
                new_num_val +
                " WHERE p_name='" +
                podcastname_choice +
                "' AND pe_title='" +
                podcastepisodename_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastEpisode SET pe_ad_count=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "' AND pe_title='" +
                podcastepisodename_choice +
                "'";
            }
            new_num_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 3:
            if (typ.equals("update")) {
              System.out.println("Enter new value for episode duration (hh:mm:ss):");
              new_str_val = sc.next();
              DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(
                  "[HH:mm:ss][HH:mm:s][HH:m:ss][H:mm:ss][HH:m:s][H:m:ss][H:mm:s][H:m:s][HH:mm][HH:m][H:mm][mm:ss][m:ss][mm:s][m:s]"
                ).toFormatter();
              LocalTime localTime = LocalTime.parse(new_str_val, formatter);
              java.sql.Time new_str_val_time = java.sql.Time.valueOf(localTime);
              query =
                "UPDATE PodcastEpisode SET pe_duration='" +
                new_str_val_time +
                "' WHERE p_name='" +
                podcastname_choice +
                "' AND pe_title='" +
                podcastepisodename_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE PodcastEpisode SET pe_duration=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "' AND pe_title='" +
                podcastepisodename_choice +
                "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 0:
            update_podcast_episode_choice_function(
              connection,
              podcastname_choice,
              typ
            );
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
    } while (update_continue_choice.toLowerCase().equals("y"));
  }
}