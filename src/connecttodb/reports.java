package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Arrays;

public class reports {

  public static Statement statement;

  public static String[] podcasthost_list, artist_list, recordlabel_list;

  public static void getreportsmenu(Connection connection)
    throws SQLException {
    System.out.println("");
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    statement = connection.createStatement();
    do {
      System.out.print("");
      System.out.println("Select what you want to update:");
      System.out.println("1. Monthly play count per Song");
      System.out.println("2. Monthly play count per Artist");
      System.out.println("3. Monthly play count per Album");

      System.out.println("4. Report all songs given an artist");
      System.out.println("5. Report all songs given an album");

      System.out.println("6. total payments made out to host per a given time period");
      System.out.println("7. total payments made out to artist per a given time period");
      System.out.println("8. total payments made out to Record Label per a given time period");

      System.out.println("9. Total revenue of the streaming service per month");
      System.out.println("10. Total revenue of the streaming service per year");

      System.out.println("0.  Go to previous menu");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            reports.MPC_song(connection);
            break;
          case 2:
            reports.MPC_artist(connection);
            break;
          case 3:
            reports.MPC_album(connection);
            break;
          case 4:
            artist.getartist("", connection);
            break;
          case 5:
            album.getalbum("", connection);
            break;
          case 6:
            reports.TP_host(connection);
            break;
          case 7:
            reports.TP_artist(connection);
            break;
          case 8:
            reports.TP_recordlabel(connection);
            break;
          case 9:
            reports.report_revenue(connection, "month");
            break;
          case 10:
            reports.report_revenue(connection, "year");
            break;
          case 0:
            admin.getAdminMenu(connection);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    } while (enteredValue != 0);
  }

  public static void MPC_artist(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("***** List of Artist and its monthly count *****");
    System.out.println("");

    statement = connection.createStatement();
    String query, a_email_id, Artist_play_count;

    query = "Select a_email_id,sum(ls_play_count) as Artist_play_count from sings natural join listens_to_song group by a_email_id;";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      while (rs.next()) {
        a_email_id = rs.getString("a_email_id");
        Artist_play_count = rs.getString("Artist_play_count");

        System.out.println(a_email_id + "  " + Artist_play_count);
      }
      System.out.println();
    } catch (SQLException e) {
      Helper.close(rs);
      e.printStackTrace();
    }
  }

  public static void MPC_album(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("***** List of Album and its monthly count *****");
    System.out.println("");

    statement = connection.createStatement();
    String query, l_name, l_play_count;

    query =
      "Select sum(l.ls_play_count) as l_play_count,s_title,l_name from Song s join listens_to_song l on s.s_id=l.s_id group by l_name;";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      while (rs.next()) {
        l_name = rs.getString("l_name");
        l_play_count = rs.getString("l_play_count");

        System.out.println(l_name + "  " + l_play_count);
      }
      System.out.println();
    } catch (SQLException e) {
      Helper.close(rs);
      e.printStackTrace();
    }
  }

  public static void MPC_song(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("***** List of Songs and its monthly count *****");
    System.out.println("");

    statement = connection.createStatement();
    String query, title, s_id, ls_play_count;

    query = "Select s.s_id as s_id,l.ls_play_count as ls_play_count,s_title from Song s join listens_to_song l on s.s_id=l.s_id group by s_id";

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      while (rs.next()) {
        title = rs.getString("s_title");
        s_id = rs.getString("s_id");
        ls_play_count = rs.getString("ls_play_count");

        System.out.println(s_id + "-> " + title + "  " + ls_play_count);
      }
      System.out.println();
    } catch (SQLException e) {
      Helper.close(rs);
      e.printStackTrace();
    }
  }

  public static void TP_host(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("***** View payments made out to Podcast Hosts *****");
    System.out.println("");
    
    Scanner sc = new Scanner(System.in);
    int host_choice = 0;

    statement = connection.createStatement();
    String query = "", podcasthostname = "", from_ = "", to_ = "";
    System.out.println("Enter From Date (yyyy-mm-dd):");
    from_ = sc.next();
    java.sql.Date from_date = java.sql.Date.valueOf(from_);

    System.out.println("Enter To Date (yyyy-mm-dd):");
    to_ = sc.next();
    java.sql.Date to_date = java.sql.Date.valueOf(to_);

    System.out.println("Select what you want to view:");
    System.out.println("1. For particular podcast host");
    System.out.println("2. For all podcast hosts");
    System.out.println("0. Main Menu");

    try {
      host_choice = sc.nextInt();
      String ph_email_id, total_payment;
      sc.nextLine();
      ResultSet rs = null;

      switch (host_choice) {
        case 1:
          podcasthost_list = podcasthost.view_all_podcast_hosts(connection, "email");
          do {
            System.out.println("Enter Podcast Host email:");
            podcasthostname = sc.nextLine();
            if (!Arrays.asList(podcasthost_list).contains(podcasthostname)) {
              System.out.println("Podcast Host does not exist! Please check for spelling mistakes!");
            }
          } while(!Arrays.asList(podcasthost_list).contains(podcasthostname));

          query =
            "SELECT ph.ph_email_id, IFNULL(SUM(pfh.pfh_amount), 0) AS total_payment " +
            "FROM PodcastHost ph LEFT JOIN pays_to_host pfh ON ph.ph_email_id = pfh.ph_email_id " +
            "AND (pfh.pfh_date >= '" +
            from_date +
            "' AND pfh.pfh_date <= '" +
            to_date +
            "' OR pfh.pfh_date IS NULL) " +
            "WHERE ph.ph_email_id = '" +
            podcasthostname +
            "' GROUP BY ph.ph_email_id";
          break;

        case 2:
          query =
            "SELECT SUM(ifnull(pfh.pfh_amount, 0)) as total_payment, ph.ph_email_id" +
            " FROM pays_to_host pfh RIGHT JOIN PodcastHost ph ON pfh.ph_email_id=ph.ph_email_id" +
            " AND (pfh.pfh_date >= '" +
            from_date +
            "' AND pfh.pfh_date <= '" +
            to_date +
            "' OR pfh.pfh_date IS NULL) GROUP BY ph.ph_email_id";
          break;

        case 0:
          MainMenu.displayMenu(connection);
          break;

        default:
          System.out.println("You have made an invalid choice. Please pick again.");
      }
      rs = statement.executeQuery(query);
      while (rs.next()) {
        ph_email_id = rs.getString("ph_email_id");
        total_payment = rs.getString("total_payment");

        System.out.println(ph_email_id + " -> " + total_payment);
      }
      System.out.println();
    } catch (Exception e) {
      System.out.println("You have made a wrong choice. Please pick again.");
      MainMenu.displayMenu(connection);
    }
  }

  public static void TP_artist(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("***** View payments made out to Artists *****");
    System.out.println("");
    
    Scanner sc = new Scanner(System.in);
    int artist_choice = 0;

    statement = connection.createStatement();
    String query = "", artistemail = "", from_ = "", to_ = "";
    System.out.println("Enter From Date (yyyy-mm-dd):");
    from_ = sc.next();
    java.sql.Date from_date = java.sql.Date.valueOf(from_);

    System.out.println("Enter To Date (yyyy-mm-dd):");
    to_ = sc.next();
    java.sql.Date to_date = java.sql.Date.valueOf(to_);

    System.out.println("Select what you want to view:");
    System.out.println("1. For particular artist");
    System.out.println("2. For all artists");
    System.out.println("0. Main Menu");

    try {
      artist_choice = sc.nextInt();
      String a_email_id, total_payment;
      sc.nextLine();
      ResultSet rs = null;

      switch (artist_choice) {
        case 1:
          artist_list = artist.view_all_artists(connection, "show");
          do {
            System.out.println("Enter Artist email:");
            artistemail = sc.nextLine();
            if (!Arrays.asList(artist_list).contains(artistemail)) {
              System.out.println("Artist does not exist! Please check for spelling mistakes!");
            }
          } while(!Arrays.asList(artist_list).contains(artistemail));

          query =
            "SELECT Artist.a_email_id, ROUND(SUM(IFNULL(pays_to_artist.pfa_amount, 0)), 2) AS total_payment " +
            "FROM Artist LEFT JOIN pays_to_artist ON Artist.a_email_id = pays_to_artist.a_email_id " +
            "AND (pays_to_artist.pfa_date >= '" +
            from_date +
            "' AND pays_to_artist.pfa_date <= '" +
            to_date +
            "' OR pays_to_artist.pfa_date IS NULL) " +
            "WHERE Artist.a_email_id = '" +
            artistemail +
            "' GROUP BY Artist.a_email_id";
          
          
          break;

        case 2:
          query =
            "SELECT ROUND(SUM(IFNULL(pfa.pfa_amount, 0)), 2) as total_payment, a.a_email_id FROM pays_to_artist pfa RIGHT JOIN Artist a ON pfa.a_email_id=a.a_email_id AND (pfa_date >= '" +
            from_date +
            "' AND pfa_date <= '" +
            to_date +
            "' OR pfa_date IS NULL) GROUP BY a.a_email_id";
          break;

        case 0:
          MainMenu.displayMenu(connection);
          break;

        default:
          System.out.println("You have made an invalid choice. Please pick again.");
      }
      rs = statement.executeQuery(query);
      while (rs.next()) {
        a_email_id = rs.getString("a_email_id");
        total_payment = rs.getString("total_payment");

        System.out.println(a_email_id + " -> " + total_payment);
      }
      System.out.println();
    } catch (Exception e) {
      System.out.println(e+"You have made a wrong choice. Please pick again.");
      MainMenu.displayMenu(connection);
    }
  }

  public static void TP_recordlabel(Connection connection)
    throws SQLException {
    System.out.println("");
    System.out.println("***** View payments made out to Record Labels *****");
    System.out.println("");
    
    Scanner sc = new Scanner(System.in);
    int recordlabel_choice = 0;

    statement = connection.createStatement();
    String query = "", recordlabelname = "", from_ = "", to_ = "";
    System.out.println("Enter From Date (yyyy-mm-dd):");
    from_ = sc.next();
    java.sql.Date from_date = java.sql.Date.valueOf(from_);

    System.out.println("Enter To Date (yyyy-mm-dd):");
    to_ = sc.next();
    java.sql.Date to_date = java.sql.Date.valueOf(to_);

    System.out.println("Select what you want to view:");
    System.out.println("1. For particular record label");
    System.out.println("2. For all record labels");
    System.out.println("0. Main Menu");

    try {
      recordlabel_choice = sc.nextInt();
      String rl_name, total_payment;
      sc.nextLine();
      ResultSet rs = null;

      switch (recordlabel_choice) {
        case 1:
          recordlabel_list = RL.view_all_record_labels(connection, "show");
          do {
            System.out.println("Enter record label name:");
            recordlabelname = sc.nextLine();
            if (!Arrays.asList(recordlabel_list).contains(recordlabelname)) {
              System.out.println("Record label does not exist! Please check for spelling mistakes!");
            }
          } while(!Arrays.asList(recordlabel_list).contains(recordlabelname));

          query =
            "SELECT rl.rl_name, ROUND(SUM(IFNULL(pfs.pfs_amount, 0)), 2) AS total_payment " +
            "FROM RecordLabel rl LEFT JOIN pays_to_record_label pfs ON rl.rl_name = pfs.rl_name " +
            "AND (pfs.pfs_date >= '" +
            from_date +
            "' AND pfs.pfs_date <= '" +
            to_date +
            "' OR pfs.pfs_date IS NULL) " +
            "WHERE rl.rl_name = '" +
            recordlabelname +
            "' GROUP BY rl.rl_name";
          break;

        case 2:
          query =
            "SELECT ROUND(SUM(IFNULL(pfs.pfs_amount, 0)), 2) as total_payment, rl.rl_name" +
            " FROM pays_to_record_label pfs RIGHT JOIN RecordLabel rl ON pfs.rl_name=rl.rl_name" +
            " AND (pfs.pfs_date >= '" +
            from_date +
            "' AND pfs.pfs_date <= '" +
            to_date +
            "' OR pfs.pfs_date IS NULL) GROUP BY rl.rl_name";
          break;

        case 0:
          MainMenu.displayMenu(connection);
          break;

        default:
          System.out.println("You have made an invalid choice. Please pick again.");
      }
      rs = statement.executeQuery(query);
      while (rs.next()) {
        rl_name = rs.getString("rl_name");
        total_payment = rs.getString("total_payment");

        System.out.println(rl_name + " -> " + total_payment);
      }
      System.out.println();
    } catch (Exception e) {
      System.out.println("You have made a wrong choice. Please pick again.");
      MainMenu.displayMenu(connection);
    }
  }

  public static void report_revenue(Connection connection, String metric)
    throws SQLException {
    System.out.println("");
    System.out.println("***** Revenue Generated *****");
    System.out.println("");
    
    Scanner sc = new Scanner(System.in);
    statement = connection.createStatement();
    
    int metric_choice = 0;
    String query = "";

    System.out.println("Select what you want to view:");
    System.out.println("1. For particular " + metric);
    System.out.println("2. For all " + metric + "s");
    System.out.println("0. Main Menu");

    try {
      metric_choice = sc.nextInt();
      int monthname;
	String yearname = "", revenue = "", result = "";
      sc.nextLine();
      ResultSet rs = null;

      switch (metric_choice) {
        case 1:
          System.out.println("Enter year:");
          yearname = sc.nextLine();

          if(metric.equals("month")) {
            System.out.println("Enter month number:\n1. January, 2. February ... 12. December");
            monthname = sc.nextInt();
            sc.nextLine();
            monthname = monthname+1;
            
            query =
              "SELECT MONTH(bs_date) - 1 AS month, YEAR(bs_date) AS year, SUM(bs_revenue) AS revenue" +
              " FROM BillingService WHERE YEAR(bs_date)=" +
              yearname +
              " AND MONTH(bs_date)=" +
              monthname +
              " GROUP BY month, year";
          }
          else if(metric.equals("year")) {
            query =
              "SELECT YEAR(bs_date) AS year, SUM(bs_revenue) AS revenue FROM BillingService" +
              " WHERE YEAR(bs_date)=" +
              yearname +
              " GROUP BY year";
          }
          break;

        case 2:
          if(metric.equals("month")) {
            query =
              "SELECT MONTH(bs_date) - 1 AS month, YEAR(bs_date) AS year, bs_revenue AS revenue" +
              " FROM BillingService GROUP BY month, year ORDER BY YEAR(bs_date) DESC, MONTH(bs_date)";
          }
          else if(metric.equals("year")) {
            query =
              "SELECT YEAR(bs_date) AS year, SUM(bs_revenue) AS revenue FROM BillingService" +
              " GROUP BY year ORDER BY YEAR(bs_date) DESC";
          }
          break;

        case 0:
          MainMenu.displayMenu(connection);
          break;

        default:
          System.out.println("You have made an invalid choice. Please pick again.");
      }
      rs = statement.executeQuery(query);
      String month_name = "";
      while (rs.next()) {
        if(metric.equals("month")) {
          month_name = rs.getString("month");
        }
        yearname = rs.getString("year");
        revenue = rs.getString("revenue");

        result = yearname + " -> ";
        if(metric.equals("month")) {
          result += month_name + " -> ";
        }

        System.out.println(result + revenue);
      }
      System.out.println();
    } catch (Exception e) {
      System.out.println("You have made a wrong choice. Please pick again.");
      MainMenu.displayMenu(connection);
    }
  }
}
