package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class reports {
    public static Statement statement;
    public static void MPC_artist(Connection connection) throws SQLException
    {
		  System.out.println("");
		  System.out.println("List of Artist and its monthly count");
		  System.out.println("");

	      statement = connection.createStatement();
	      String query, a_email_id,Artist_play_count;
	      
		  query = "Select a_email_id,sum(ls_play_count) as Artist_play_count from sings natural join listens_to_song group by a_email_id;";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                	a_email_id = rs.getString("a_email_id");
	                	Artist_play_count = rs.getString("Artist_play_count");

	                    System.out.println(a_email_id + "  " + Artist_play_count);
	                }
                  System.out.println();

	        }
	        catch (SQLException e) {
	    	            Helper.close(rs);
	    	            e.printStackTrace();
	    	        }
    }

    public static void MPC_album(Connection connection) throws SQLException
    {
		  System.out.println("");
		  System.out.println("List of Album and its monthly count");
		  System.out.println("");

	      statement = connection.createStatement();
	      String query, l_name,l_play_count;
	      
		  query = "Select sum(l.ls_play_count) as l_play_count,s_title,l_name from Song s join listens_to_song l on s.s_id=l.s_id group by l_name;";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                    l_name = rs.getString("l_name");
	                    l_play_count = rs.getString("l_play_count");

	                    System.out.println(l_name + "  " + l_play_count);
	                }
                    System.out.println();

	        }
	        catch (SQLException e) {
	    	            Helper.close(rs);
	    	            e.printStackTrace();
	    	        }
    }
 	public static void MPC_song(Connection connection) throws SQLException
    {
		  System.out.println("");
		  System.out.println("List of Songs and its monthly count");
		  System.out.println("");

	      statement = connection.createStatement();
	      String query, title,s_id, ls_play_count;
	      
		  query = "Select s.s_id as s_id,l.ls_play_count as ls_play_count,s_title from Song s join listens_to_song l on s.s_id=l.s_id group by s_id";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                    title = rs.getString("s_title");
	                    s_id = rs.getString("s_id");
	                    s_id = rs.getString("s_id");
	                    ls_play_count = rs.getString("ls_play_count");

	                    System.out.println(s_id + "-> " +title + "  " + ls_play_count);
	                }
                    System.out.println();

	        }
	        catch (SQLException e) {
	    	            Helper.close(rs);
	    	            e.printStackTrace();
	    	        }
    }
 	
 	
 	public static void getreportsmenu(Connection connection) throws SQLException
 	{
 		 System.out.println("");
         Scanner sc = new Scanner(System.in);
         int enteredValue = 0;
         statement = connection.createStatement();
         String sql;
         int rows;
         do{
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
         
         try{
             enteredValue = sc.nextInt();

             switch (enteredValue){
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
                	 artist.getartist("",connection);
                	 break;
                 case 5:
                	 album.getalbum("",connection);
                	 break;
                 case 6:
                	 break;
                 case 7:
                	 break;
                 case 8:
                	 break;
                 case 9:
                	 break;
                 case 10:
                	 break;
                	 	 
                 case 0:
                     admin.getAdminMenu(connection);
     	            break;
                 default:
                     System.out.println("You have made an invalid choice. Please pick again.");
             }
             } catch(Exception e){
                 System.err.println("Error: " + e.getMessage());
             }
         } while (enteredValue != 0);
 	}


}
