package connecttodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class connect {

public static void main(String args[]) {
Connection conn = null;
String url = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/nshah28";
String user = "nshah28";
String pswd = "200474980";
try {
System.out.println("Successfully ");
conn = DriverManager.getConnection(url, user, pswd);

Statement statement = conn.createStatement();

//DatabaseOperations.clearDatabase(conn);
DatabaseOperations.createTables(conn);

ResultSet rs = statement.executeQuery("Show tables");
System.out.println("Tables in the current database: ");
while(rs.next()) {
   System.out.print(rs.getString(1));
   System.out.println();

}




System.out.println("Successfully ");


} catch (SQLException e){
System.out.println("F ");
e.printStackTrace();
System.out.println("F");

}
//System.out.println("end connected to DB");
}
}
