package connecttodb;

//import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.*;
import java.util.Scanner;
// for closing the connection
public class Helper {
  
  public static void close(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (Throwable e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void close(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (Throwable e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void close(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (Throwable e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void close(Scanner sc) {
    if (sc != null) {
      try {
        sc.close();
      } catch (Throwable e) {
        System.out.println(e.getMessage());
      }
    }
  }
  // exiting the program
  public static void exitProgram(Connection connection, Statement statement, Scanner sc, ResultSet rs) {
    Helper.close(connection);
    Helper.close(statement);
    Helper.close(sc);
    Helper.close(rs);
    System.exit((0));
  }

  public static int selectNextOption(Scanner sc, String optionText) {
    boolean selected = false;
    int enteredValue = 0;

    do {
      System.out.println("Choose one of the following options");
      System.out.println("1. " + optionText);
      System.out.println("2. Go Back");

      try {
        enteredValue = sc.nextInt();
        if (enteredValue != 1 && enteredValue != 2) {
          System.out.println(
            "You have made an invalid choice. Please pick again."
          );
        } else {
          selected = true;
        }
      } catch (Exception e) {
        System.out.println("Please pick an option between 1 and 2.");
        sc.next();
      }
    } while (!selected);

    return enteredValue;
  }
}

