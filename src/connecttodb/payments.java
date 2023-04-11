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

      String date = scanner.next();
      String startdate = date + "-01";

      String enddate = addOneMonth(startdate);
      System.out.print(startdate + " " + enddate);

      String sql;

      sql =
        "INSERT INTO pays_to_record_label(pfs_date, bs_date, rl_name, pfs_amount)\n" +
        "SELECT\n" +
        "    DATE_ADD(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')), INTERVAL 1 DAY),\n" +
        "    LAST_DAY(LAST_DAY(CONCAT(X.ls_year, '-', X.ls_month, '-01')) - INTERVAL 1 MONTH) + INTERVAL 1 DAY,\n" +
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
      pstmt.executeUpdate();

      sql =
        "UPDATE listens_to_song SET ls_royalty_paid_status = 1 where ls_date >='" +
        startdate +
        "' and ls_date< '" +
        enddate +
        "'";
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public static void getpaymentmenu(Connection connection)
    throws SQLException {
    Scanner sc = new Scanner(System.in);

    int enteredValue = 0;

    do {
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. Make payment to all Songs");
      System.out.println("2. Make payment to podcast hosts");
      System.out.println("0. Exit");
      System.out.println("Enter your option:");
      System.out.println("");

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            generate_rl_payment(connection);
            break;
          case 2:
            break;
          case 0:
            System.out.print("Go back to previous menu");
            admin.getAdminMenu(connection);
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
        MainMenu.displayMenu(connection);
      }
    } while (enteredValue != 0);
  }
}