package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {
	
	
	  public static void getuser(Connection connection)   throws SQLException 
	  {  
	        Scanner sc = new Scanner(System.in);
	        String enteredValue;
	        Statement statement = null;
            statement = connection.createStatement();
	        do {
	            System.out.println("User Login");
	            System.out.println("Enter your Email");
	            try {
	                enteredValue = sc.next();
	                    String x = "select u_email_id AS u_email_id from User where u_email_id='" + enteredValue+"'";
	                    ResultSet rs4 = statement.executeQuery(x);

	                    if (rs4.next()) {
	                        System.out.print("User found");
	                    } else{
	                        System.out.println("Try again");
	                        getuser(connection);
	                    }
	                } catch (SQLException e) {
	                    System.out.println("Could not fetch wallet details");
	                    getuser(connection);
	                }
	        } while (true);
	  }


}
