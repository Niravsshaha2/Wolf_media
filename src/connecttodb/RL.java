package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RL {
	
    public static Statement statement;

//    Get All Songs
	  public static void getsongs(String u_email_id, Connection connection)   throws SQLException 
	  {
		  int song_id = 0;
		  System.out.println("");
		  System.out.println("List of Songs");
		  System.out.println("");

	      statement = connection.createStatement();
	      Scanner sc = new Scanner(System.in);
	      String query, title,s_id ="";
	      
		  query = "SELECT s_id,s_title FROM Song";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                    title = rs.getString("s_title");
	                    s_id = rs.getString("s_id");
	                    System.out.println(s_id + " " +title + "" );
	                }
	            while(true) 
	            {
	            	try {
	          		System.out.println("");
	            	System.out.println("Enter Song id to play");
	            	System.out.println("");

	            	song_id = sc.nextInt();
	            	
					query = "SELECT s_title FROM Song where s_id='" + song_id +"'";
			        rs = statement.executeQuery(query);
			        if (rs.next()) {
                        song_play_count(rs.getString("s_title"),u_email_id,connection);
                        
                    } else{
                        System.out.println("Try again");
                        getsongs(u_email_id,connection);
                    }
	            	}
	            	catch (SQLException e) {
	                    System.out.println("Could not fetch wallet details");
	                    getsongs(u_email_id,connection);
	                }
	            }
	        } catch (SQLException e) {
	            Helper.close(rs);
	            e.printStackTrace();
	        }
	  }
	  
	  
	  //given artist/album get songs
	  public static void getsongs(String type, String name, String u_email_id, Connection connection)   throws SQLException 
	  {
		  int song_id = 0;
		  System.out.println("");
		  System.out.println("List of Songs for given: " + type);
		  System.out.println("");

	      statement = connection.createStatement();
	      Scanner sc = new Scanner(System.in);
	      String query, title,s_id ="";
	      ResultSet rs = null;

	      if(type=="Artist")
	      {
		  query = "SELECT Song.s_id, Song.s_title, sings.a_email_id FROM Song JOIN sings ON Song.s_id = sings.s_id WHERE sings.a_email_id = '"+name+"'";

	        try {
	            rs = statement.executeQuery(query);
	                while(rs.next()){
	                    title = rs.getString("s_title");
	                    s_id = rs.getString("s_id");
	                    System.out.println(s_id + " " +title + "" );
	                }
	        }
	        catch (SQLException e) {
                System.out.println(e+"Could not fetch  details");
                getsongs(type,name,u_email_id,connection);
            }
	      }
	      if(type=="Album")
	      {
	    	  query = "SELECT s_id, s_title, l_name, track_number FROM Song S WHERE l_name='" + name+"'";
		        try {
		            rs = statement.executeQuery(query);
		                while(rs.next()){
		                    title = rs.getString("s_title");
		                    s_id = rs.getString("s_id");
		                    System.out.println(s_id + " " +title + "" );
		                }
		        }
		        catch (SQLException e) {
	                System.out.println(e+"Could not fetch  details");
	                getsongs(type,name,u_email_id,connection);
	            }
		   
	      }
	        
	            while(true) 
	            {
	            	try {
	          		System.out.println("");
	            	System.out.println("Enter Song id to play");
	            	System.out.println("");

	            	song_id = sc.nextInt();
	            	
	            	if(type=="Artist")
	            	{
					query = "SELECT Song.s_id, Song.s_title, sings.a_email_id FROM Song JOIN sings ON Song.s_id = sings.s_id WHERE Song.s_id = '" + song_id + "'and  sings.a_email_id = '"+name+"'";
	            	}
	            	else
	            	{
	      	    	query = "SELECT s_id, s_title, l_name, track_number FROM Song S WHERE s_id = '" + song_id + "'and l_name='" + name+"'";

	            	}
			        rs = statement.executeQuery(query);
			        
			        if (rs.next()) {
                        song_play_count(rs.getString("s_title"),u_email_id,connection);
                        
                    } else{
                        System.out.println("Try again");
                        getsongs(type,name, u_email_id,connection);
                    }
	            	}
	            	catch (SQLException e) {
	                    System.out.println(e+"Could not fetch  details");
	                    getsongs(type,name,u_email_id,connection);
	                }
	            }
	        
	  }
	  
//	  Get artist list
	  public static void getartist(String u_email_id, Connection connection)   throws SQLException 
	  {
		  System.out.println("");
		  System.out.println("List of Artists");
		  System.out.println("");
		  
		  
		  statement = connection.createStatement();
	      Scanner sc = new Scanner(System.in);
	      String query, title,s_id ="";
	
		  query = "SELECT a_name,a_email_id FROM Artist";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	            int row = 1;

	                while(rs.next()){
	                    title = rs.getString("a_name");
	                    System.out.println(row + " " +title + "" );
	                    row++;
	                }
	            
	          		System.out.println("");
	            	System.out.println("Enter Artist id to select");
	            	System.out.println("");	            

	            int selection = sc.nextInt();
	            rs.absolute(selection - 1);
	            if(rs.next())
	            {
	            String artist_name = rs.getString("a_email_id");
	            System.out.println("Selected value: " + artist_name);
	            getsongs("Artist",artist_name, u_email_id,connection);

	            }
	            else
	            {
	            	getartist(u_email_id, connection);
	            }


	        }
	        catch (SQLException e) {
		            Helper.close(rs);
		            e.printStackTrace();
		        }
	                  
	   
	  }
	  
	  
//	  Get album list
	  public static void getalbum(String u_email_id, Connection connection)   throws SQLException 
	  {
		  System.out.println("");
		  System.out.println("List of Albums");
		  System.out.println("");
		  
		  
		  statement = connection.createStatement();
	      Scanner sc = new Scanner(System.in);
	      String query, title,s_id ="";
	
		  query = "SELECT l_name FROM Album";

	        ResultSet rs = null;
	        try {
	            rs = statement.executeQuery(query);
	            int row = 1;

	                while(rs.next()){
	                    title = rs.getString("l_name");
	                    System.out.println(row + " " +title + "" );
	                    row++;
	                }
	            
	          		System.out.println("");
	            	System.out.println("Enter Album id to select");
	            	System.out.println("");	            

	            int selection = sc.nextInt();
	            rs.absolute(selection - 1);
	            if(rs.next())
	            {
	            String album_name = rs.getString("l_name");
	            System.out.println("Selected value: " + album_name);
	            getsongs("Album",album_name, u_email_id,connection);

	            }
	            else
	            {
	            	getartist(u_email_id, connection);
	            }


	        }
	        catch (SQLException e) {
		            Helper.close(rs);
		            e.printStackTrace();
		        }
	                  
	   
	  }
	  
//	  increment play count
	  public static void song_play_count(String s_id,String u_email_id, Connection connection) throws SQLException 
	  {
		  System.out.println("");
		  System.out.println("Thank you for listening to Song: " + s_id);
		  System.out.println("");

		  User.getusermenu(u_email_id, connection);
		  
	  }
	  
	  

}
