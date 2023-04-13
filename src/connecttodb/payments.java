package connecttodb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class payments {

  public static Statement statement;

  public static String addOneMonth(String dateStr) {
    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    LocalDate newDate = date.plusMonths(1);
    return newDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static void generate_rl_payment(Connection connection)
    throws SQLException {
    statement = connection.createStatement();
    try {
      Scanner scanner = new Scanner(System.in);

      System.out.print("Enter Month to pay for(yyyy-mm): ");

      String date = scanner.nextLine();
      String startdate = date + "-01";

      String enddate = addOneMonth(startdate);
//      System.out.print(startdate + " " + enddate);

      String sql;

      sql =
        "INSERT INTO pays_to_record_label(pfs_date, bs_date, rl_name, pfs_amount)\n" +
        "SELECT\n" +
        "    DATE_ADD(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')), INTERVAL 1 DAY),\n" +
        "    DATE_ADD(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')), INTERVAL 1 DAY),\n" +
        //+ "    LAST_DAY(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')) - INTERVAL 1 MONTH) + INTERVAL 1 DAY,\n"
        "    A.rl_name, 0.3 * SUM(X.amount)\n" +
        "FROM Artist A\n" +
        "JOIN (SELECT  So.ls_year, So.ls_month, Si.a_email_id, So.s_id, SUM(So.amount) AS amount\n" +
        "    FROM sings Si JOIN (SELECT\n" +
        "            YEAR(ls.ls_date) AS ls_year, MONTH(ls.ls_date) AS ls_month, S.s_id, SUM(s_royalty_rate * ls.ls_play_count) AS amount\n" +
        "        FROM Song S JOIN  listens_to_song ls ON S.s_id = ls.s_id " +
        " WHERE ls_date >= ? " +
        " and ls_date < ? " +
        " and ls_royalty_paid_status = 0" +
        "        GROUP BY YEAR(ls.ls_date), MONTH(ls.ls_date), S.s_id\n" +
        "    ) So ON Si.s_id = So.s_id and Si.is_artist_collaborator_for_song = 0\n" +
        "    GROUP BY So.ls_year, So.ls_month, Si.a_email_id, So.s_id\n" +
        ") X ON A.a_email_id = X.a_email_id\n" +
        "GROUP BY X.ls_year, X.ls_month, A.rl_name, X.s_id\n" +
        "ON DUPLICATE KEY UPDATE\n" +
        "    pfs_amount = pfs_amount + VALUES(pfs_amount)";

      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, startdate);
      pstmt.setString(2, enddate);
      int rows = pstmt.executeUpdate();
      System.out.println("Paid to record label");

      sql =
        "UPDATE listens_to_song SET ls_royalty_paid_status = 1 where ls_date >='" +
        startdate +
        "' and ls_date< '" +
        enddate +
        "'";
      statement.executeUpdate(sql);

      if (rows > 0) {
        sql =
          "INSERT INTO pays_to_artist(pfa_amount,pfa_date,rl_name,a_email_id) " +
          "SELECT ROUND(SUM(t1.royalties/t2.c_s_id), 2) AS pfa_amount, t1.pfa_date, t2.rl_name, t1.a_email_id " +
          "FROM ( " +
          "  SELECT '" +
          addOneMonth(startdate) +
          "' AS pfa_date, rl_name, a_email_id, ls.s_id, " +
          "    SUM(ls_play_count) AS total_plays, s_royalty_rate * SUM(ls_play_count) * 0.7 AS royalties " +
          "  FROM listens_to_song ls " +
          "  NATURAL JOIN Song s " +
          "  NATURAL JOIN sings " +
          "  NATURAL JOIN Artist " +
          "  WHERE MONTH('" +
          startdate +
          "') = MONTH(ls_date) AND YEAR('" +
          startdate +
          "') = YEAR(ls_date) " +
          "    AND ls_royalty_paid_status = 1 " +
          "  GROUP BY s_id, a_email_id, rl_name " +
          ") AS t1 " +
          "JOIN ( " +
          "  SELECT rl_name, s_id, COUNT(s_id) AS c_s_id " +
          "  FROM ( " +
          "    SELECT rl_name, a_email_id, ls.s_id, SUM(ls_play_count) AS ls_play_count " +
          "    FROM listens_to_song ls " +
          "    NATURAL JOIN Song s " +
          "    NATURAL JOIN sings " +
          "    NATURAL JOIN Artist " +
          "    WHERE MONTH('" +
          startdate +
          "') = MONTH(ls_date) AND YEAR('" +
          startdate +
          "') = YEAR(ls_date) " +
          "      AND ls_royalty_paid_status = 1 " +
          "    GROUP BY s_id, a_email_id, rl_name " +
          "  ) AS X " +
          "  GROUP BY s_id " +
          ") AS t2 " +
          "ON t1.s_id = t2.s_id " +
          "GROUP BY t1.pfa_date, t2.rl_name, t1.a_email_id";

        // Execute SQL statement
        int rowsAffected = statement.executeUpdate(sql);
        System.out.println("Paid to artist");
      }
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public static void generate_ph_payment(Connection connection)
    throws SQLException {
    statement = connection.createStatement();
    try {
      Scanner scanner = new Scanner(System.in);
      Statement statement = connection.createStatement();
      ResultSet rs = null;

      System.out.print("Enter Month to pay for(yyyy-mm): ");

      String date = scanner.next();
      String startdat = date + "-01";
      java.sql.Date startdate = java.sql.Date.valueOf(startdat);
      System.out.println(startdate);


      String sql = "";
      sql = "INSERT INTO pays_to_host (bs_date, pfh_amount, pfh_date, ph_email_id)" +
            " SELECT" +
            "   DATE_ADD(LAST_DAY(CONCAT(YEAR(lpe.lpe_date), '-', MONTH(lpe.lpe_date), '-01')), INTERVAL 1 DAY) AS bs_date," +
            "   ROUND(SUM(p.p_episode_flat_fee + pe.pe_ad_count * p.p_sponsor * lpe.lpe_play_count), 2) AS pfh_amount," +
            "   DATE_ADD(LAST_DAY(CONCAT(YEAR(lpe.lpe_date), '-', MONTH(lpe.lpe_date), '-01')), INTERVAL 1 DAY) AS pfh_date," +
            "   pe.ph_email_id AS ph_email_id" +
            " FROM PodcastEpisode pe" +
            " JOIN listens_to_podcast_episode lpe ON pe.pe_title=lpe.pe_title AND pe.p_name=lpe.p_name" +
            " JOIN Podcast p ON p.p_name=pe.p_name" +
            " WHERE MONTH(lpe.lpe_date)=MONTH('" +
            startdate +
            "') AND YEAR(lpe.lpe_date)=YEAR('" +
            startdate +
            "') GROUP BY MONTH(lpe.lpe_date), YEAR(lpe.lpe_date), pe.ph_email_id" +
            " ON DUPLICATE KEY UPDATE pfh_amount = VALUES(pfh_amount)";


      rs = statement.executeQuery(sql);
      System.out.println("Paid to host!");
      payments.getpaymentmenu(connection);
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  // public static String addOneMonth(String dateStr) {
  //   LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
  //   LocalDate newDate = date.plusMonths(1);
  //   return newDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
  // }

  // public static void generate_rl_payment(Connection connection)
  //   throws SQLException {
  //   statement = connection.createStatement();
  //   try {
  //     Scanner scanner = new Scanner(System.in);

  //     System.out.print("Enter Month to pay for(yyyy-mm): ");

  //     String date = scanner.next();
  //     String startdate = date + "-01";

  //     String enddate = addOneMonth(startdate);
  //     System.out.print(startdate + " " + enddate);

  //     String sql;

  //     sql =
  //       "INSERT INTO pays_to_record_label(pfs_date, bs_date, rl_name, pfs_amount)\n" +
  //       "SELECT\n" +
  //       "    DATE_ADD(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')), INTERVAL 1 DAY),\n" +
  //       "    LAST_DAY(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')) - INTERVAL 1 MONTH) + INTERVAL 1 DAY,\n" +
  //       "    A.rl_name, 0.3 * SUM(X.amount)\n" +
  //       "FROM Artist A\n" +
  //       "JOIN (SELECT  So.ls_year, So.ls_month, Si.a_email_id, So.s_id, SUM(So.amount) AS amount\n" +
  //       "    FROM sings Si JOIN (SELECT\n" +
  //       "            YEAR(ls.ls_date) AS ls_year, MONTH(ls.ls_date) AS ls_month, S.s_id, SUM(s_royalty_rate * ls.ls_play_count) AS amount\n" +
  //       "        FROM Song S JOIN  listens_to_song ls ON S.s_id = ls.s_id " +
  //       " WHERE ls_date >= ? " +
  //       " and ls_date < ? " +
  //       " and ls_royalty_paid_status = 0" +
  //       "        GROUP BY YEAR(ls.ls_date), MONTH(ls.ls_date), S.s_id\n" +
  //       "    ) So ON Si.s_id = So.s_id and Si.is_artist_collaborator_for_song = 0\n" +
  //       "    GROUP BY So.ls_year, So.ls_month, Si.a_email_id, So.s_id\n" +
  //       ") X ON A.a_email_id = X.a_email_id\n" +
  //       "GROUP BY X.ls_year, X.ls_month, A.rl_name, X.s_id\n" +
  //       "ON DUPLICATE KEY UPDATE\n" +
  //       "    pfs_amount = pfs_amount + VALUES(pfs_amount)";

  //     PreparedStatement pstmt = connection.prepareStatement(sql);
  //     pstmt.setString(1, startdate);
  //     pstmt.setString(2, enddate);
  //     pstmt.executeUpdate();

  //     sql =
  //       "UPDATE listens_to_song SET ls_royalty_paid_status = 1 where ls_date >='" +
  //       startdate +
  //       "' and ls_date< '" +
  //       enddate +
  //       "'";
  //     statement.executeUpdate(sql);
  //   } catch (SQLException e) {
  //     System.out.println(e);
  //   }
  // }

  public static void getpaymentmenu(Connection connection) throws SQLException {
    Scanner sc = new Scanner(System.in);

    int enteredValue = 0;

    do {
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. Make payment to all Songs");
      System.out.println("2. Make payment to podcast hosts");
      System.out.println("0. Go back to previous menu");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            generate_rl_payment(connection);
            break;

          case 2:
            generate_ph_payment(connection);
            break;

          case 0:
            System.out.print("Go back to previous menu");
            admin.getAdminMenu(connection);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made an invalid choice. Please pick again.");
        MainMenu.displayMenu(connection);
      }
    } while (enteredValue != 0);
  }
}
