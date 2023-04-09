package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Arrays;

public class podcast {
  public static String[] podcast_list;
  public static Statement statement;
  Scanner sc = new Scanner(System.in);

  public static String[] view_all_podcasts(Connection connection, String show_type)
    throws SQLException {
    if(show_type == "show") {
      System.out.println();
      System.out.println("~ List of Podcasts ~");
    }

    statement = connection.createStatement();
    String query, podcastname = "";

    query = "SELECT p_name FROM Podcast";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      int row = 1;
      int podcast_count = 0;
      if (rs.last()) {
        podcast_count = rs.getRow();
        rs.beforeFirst();
      }
      podcast_list = new String[podcast_count];

      while (rs.next()) {
        podcastname = rs.getString("p_name");
        podcast_list[row - 1] = podcastname;
        if(show_type == "show") {
          System.out.println(row + "] " + podcastname + "");
        }
        row++;
      }
    } catch (SQLException e) {
      System.out.println("Unfortunately, we don't have any podcasts yet!");
      PM.get_pm_menu(connection);
    }
    return podcast_list;
  }

  public static void add_podcast_info(Connection connection)
    throws SQLException {
    // p_name, p_sponsor, p_language, p_country, p_rating, p_rated_user_count
    System.out.println("***** Enter Podcast Details *****");
    Statement statement = connection.createStatement();
    Scanner sc = new Scanner(System.in);
    try {
      String podcastname = "";
      podcast_list = view_all_podcasts(connection, "hide");
      do {
        System.out.println();
        System.out.println("Enter Podcast name (Enter 0 to go back to main menu):");
        podcastname = sc.nextLine();
        if (!podcastname.equals("0")) {
          if(Arrays.asList(podcast_list).contains(podcastname)) {
            System.out.println("Podcast already present, please try again:");
          }
        } else {
          PM.get_pm_menu(connection);
        }
      } while(Arrays.asList(podcast_list).contains(podcastname));

      System.out.println();
      System.out.println("Enter podcast language:");
      String podcastlanguage = sc.next();

      System.out.println();
      System.out.println("Enter sponsored amount (numerical value):");
      float podcastsponsor = sc.nextFloat();

      System.out.println();
      System.out.println("Enter podcast country:");
      sc.nextLine();
      String podcastcountry = sc.nextLine();
      System.out.println(podcastcountry);

      float podcastrating;
      int podcastratedusercount = 0;
      do {
        System.out.println();
        System.out.println("Enter podcast rating (0 to 5) [default null]:");
        podcastrating = sc.nextFloat();
        // System.out.println(podcastrating, (podcastrating == 0.0f), 0 == 0.0f);
        if (podcastrating == 0.0f) {
          break;
        }
        if ((podcastrating < 0 || podcastrating > 5) && podcastrating != 0.0f) {
          System.out.println("Rating needs to be between 0 to 5 or leave it blank.");
        } else podcastratedusercount = 1;
      } while (
        (podcastrating < 0 || podcastrating > 5) && podcastrating != 0.0f
      );

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
    podcast_list = view_all_podcasts(connection, "show");
    do {
      System.out.println("Enter podcast name:");
      try {
        podcastname_choice = sc.nextLine();
        if (!Arrays.asList(podcast_list).contains(podcastname_choice)) {
          System.out.println("Podcast does not exist. Please try again from below list of podcasts!");
          update_podcast_info(connection, typ);
        }
        update_podcast_choice_function(connection, podcastname_choice, typ);
        System.out.println("Do you want to " + typ + " in another podcast? (y/n)");
        podcast_continue_choice = sc.nextLine();
      } catch (Exception e) {
        System.out.println("Podcast does not exist. Please try again!");
        update_podcast_info(connection, typ);
      }
    } while (podcast_continue_choice.toLowerCase().equals("y"));
  }

  public static void update_podcast_choice_function(Connection connection, String podcastname_choice, String typ)
    throws SQLException {
    String query = "", new_str_val = "";
    float new_num_val = 0;
    int rows = 0, update_choice = 0;
    String update_continue_choice = "n";
    Scanner sc = new Scanner(System.in);
    do {
      System.out.println();
      System.out.println("Which field to " + typ + "?:");
      System.out.println("1. Sponsor");
      System.out.println("2. Languauge");
      System.out.println("3. Rating");
      System.out.println("4. Country");
      System.out.println("0. Go back to choosing podcast");
      System.out.println("Enter your option:");
      System.out.println();
      try {
        update_choice = sc.nextInt();
        switch (update_choice) {
          case 1:
            if (typ.equals("update")) {
              System.out.println("Enter new value for sponsors (decimal):");
              new_num_val = sc.nextFloat();
              query =
                "UPDATE Podcast SET p_sponsor=" +
                new_num_val +
                " WHERE p_name='" +
                podcastname_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE Podcast SET p_sponsor=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "'";
            }
            new_num_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 2:
            if (typ.equals("update")) {
              System.out.println("Enter new value for language:");
              new_str_val = sc.next();
              query =
                "UPDATE Podcast SET p_language='" +
                new_str_val +
                "' WHERE p_name='" +
                podcastname_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE Podcast SET p_language=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "'";
            }
            new_str_val = "";

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 3:
            if (typ.equals("update")) {
              do {
                System.out.println("Enter new value for rating [0-5] :");
                new_num_val = sc.nextFloat();
                if (
                  (new_num_val < 0 || new_num_val > 5) && new_num_val != 0.0f
                ) {
                  System.out.println(
                    "Rating needs to be between 0 to 5 or leave it blank."
                  );
                }
              } while (
                (new_num_val < 0 || new_num_val > 5) && new_num_val != 0.0f
              );

              query =
                "UPDATE Podcast SET p_rating = " +
                "IF(p_rating IS NULL," +
                new_num_val +
                ", (p_rating * p_rated_user_count + " +
                new_num_val +
                ")/(p_rated_user_count + 1)), " +
                "p_rated_user_count = p_rated_user_count + 1 " +
                "WHERE p_name = '" +
                podcastname_choice +
                "';";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE Podcast SET p_rating=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "'";
            }
            new_num_val = 0;

            rows = statement.executeUpdate(query);
            System.out.println(rows + " row(s) " + typ + "d.");
            break;

          case 4:
            if (typ.equals("update")) {
              System.out.println("Enter new value for country:");
              new_str_val = sc.next();
              query =
                "UPDATE Podcast SET p_country='" +
                new_str_val +
                "' WHERE p_name='" +
                podcastname_choice +
                "'";
            } else if (typ.equals("delete")) {
              query =
                "UPDATE Podcast SET p_country=" +
                null +
                " WHERE p_name='" +
                podcastname_choice +
                "'";
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
    } while (update_continue_choice.toLowerCase().equals("y"));
  }
}