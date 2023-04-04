package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PM {
	
    public static Statement statement;
    
//    Get podcast episodes
	public static void getpodcastepisodes(String podcastname, String u_email_id, Connection connection)   throws SQLException 
	{
		int p_id = 0;
		int row = 1;
		  System.out.println("");
		  System.out.println("List of Podcast Episode for: " + podcastname);
		  System.out.println("");

	      statement = connection.createStatement();
	      Scanner sc = new Scanner(System.in);
	      String query;
	      String name;
	      
		  query = "SELECT pe_title FROM PodcastEpisode where p_name ='" + podcastname + "'";
		  
		  
		  ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                    name = rs.getString("pe_title");
	                    System.out.println(row + " " +podcastname + " " + name + "" );
	                    row++;
	                }
	                
	                System.out.println("");
	            	System.out.println("Enter podcast episode id to select");
	            	System.out.println("");	
	            	
	            	
	            
	        }
	            catch (SQLException e) {
	            Helper.close(rs);
	            e.printStackTrace();
	        }
	        
	        
	        

	}
//	Get podcast name
	public static void getpodcast(String u_email_id, Connection connection)   throws SQLException 
	{
		int p_id = 0;
		int row = 1;
		  System.out.println("");
		  System.out.println("List of Podcast");
		  System.out.println("");

	      statement = connection.createStatement();
	      Scanner sc = new Scanner(System.in);
	      String query;
	      String name;
	      
		  query = "SELECT p_name FROM Podcast";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                    name = rs.getString("p_name");
	                    System.out.println(row + " " +name + "" );
	                    row++;
	                }
	                
	                System.out.println("");
	            	System.out.println("Enter podcast id to select");
	            	System.out.println("");	
	            	
	            	
	            	int selection = sc.nextInt();
		            rs.absolute(selection - 1);
		            if(rs.next())
		            {
		            String podcast_name = rs.getString("p_name");
		            System.out.println("Selected value: " + podcast_name);
		            getpodcastepisodes(podcast_name, u_email_id,connection);

		            }
		            else
		            {
		            	getpodcast(u_email_id, connection);
		            }
	        }
	            catch (SQLException e) {
	            Helper.close(rs);
	            e.printStackTrace();
	        }
	}



}
