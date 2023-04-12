package connecttodb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class song {
	
    public static Statement statement;
    public static void add_song_genres(String s_id, Connection connection) throws SQLException{
    
        Scanner scanner = new Scanner(System.in);
        Boolean done = false;
        System.out.println("Enter Genre (or type 'done' to finish):");
        String sql;
        int rows;
        statement = connection.createStatement();

        while(!done) {
        	
            artist.show_artist_genre(connection);

	                
	                
        	String genre = scanner.next();
        	
        	if (genre.equals("done")) {
                done = true;
            }
        	else {
        	 sql = "INSERT INTO SongGenre (s_id, sg_genre) VALUES ('" + s_id + "', '" + genre + "')";
             rows = statement.executeUpdate(sql);
             System.out.println("Enter more Genre (or type 'done' to finish):");

        	}
           
        }
    }
    
 // delete song info
    public static void delete_song_info(String rl_name, String s_id, Connection connection) throws SQLException{
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        int enteredValue = 0;
        statement = connection.createStatement();
        String sql;
        int rows;
        do{
            System.out.print("");
            System.out.println("Select what you want to update:");
            System.out.println("1. Delete Song country");
            System.out.println("2. Delete Song language");
            System.out.println("0.  Go to previous menu");
            System.out.println("");
        
        try{
            enteredValue = sc.nextInt();

            switch (enteredValue){
                case 1:
                    System.out.println("");
                    sql = "update Song set s_country = NULL WHERE s_id = '"+s_id+"'";
    				rows = statement.executeUpdate(sql);
    				System.out.println("Country deleted");
                    break;
                case 2:
                    System.out.println("");

                    sql = "Update Song set s_language = NULL WHERE s_id = '"+s_id+"'";
    				rows = statement.executeUpdate(sql);
    				System.out.println("Language deleted");
                    break;
                case 0:
                    RL.getRecordlabelMenu(rl_name, connection);
    	            break;
                default:
                    System.out.println("You have made an invalid choice. Please pick again.");
            }
            } catch(Exception e){
                System.err.println("Error: " + e.getMessage());
            }
        } while (enteredValue != 0);

    }
    
    
    
    
    public static void update_song_info(String rl_name,String s_id, Connection connection) throws SQLException{
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        int enteredValue = 0;
        statement = connection.createStatement();

        do{
            System.out.print("");
            System.out.println("Select what you want to update:");
            System.out.println("1. Update Song title");
            System.out.println("2. Update Song royalty rate");
            System.out.println("0.  Go to previous menu");
            System.out.println("");
        
        try{
            enteredValue = sc.nextInt();

            switch (enteredValue){
                case 1:
                    System.out.println("");
                    System.out.println("Song title: ");
                    sc.nextLine();
                    String s_title = sc.nextLine();
    				String sql = "UPDATE Song SET s_title='"+s_title+"' WHERE s_id = '"+s_id+"'";
    				int rows = statement.executeUpdate(sql);
    				System.out.println("Song Title updated");

                    // song.update_song_title(s_id, connection);
                    break;
                case 2:
                    System.out.println("");
                    System.out.println("Song Royalty Rate: ");

                    double s_royalty_rate = sc.nextDouble();
    				sql = "UPDATE Song SET s_royalty_rate="+s_royalty_rate+" WHERE s_id = '"+s_id+"'";
    				rows = statement.executeUpdate(sql);
    				System.out.println("Song Royalty Rate updated");

                    // song.update_song_royalty_rate(s_id, connection);
                    break;
                case 0:
                	RL.getRecordlabelMenu(rl_name, connection);
                	break;
                default:
                    System.out.println("You have made an invalid choice. Please pick again.");
            	}
            } catch(Exception e){
                System.err.println("Error: " + e.getMessage());
            }
        } while (enteredValue != 0);

    }
    public static void viewsongs (String rl_name,Connection connection) throws SQLException
    {
    	  System.out.println("");
		  System.out.println("List of Songs");
		  System.out.println("");
	        statement = connection.createStatement();

	      String query;
	      
	    	  query = "select * from Song;";
	      

	        ResultSet rs = null;
	        try {

	            rs = statement.executeQuery(query);

	                while(rs.next()){
	                	
	                	//print all columns
	                    String title = rs.getString("s_title");
	                    String s_id = rs.getString("s_id");
	                    System.out.println(s_id + " " +title );
	                }
	      } catch (SQLException e) {
	    	  System.out.println(e);
            Helper.close(rs);
            e.printStackTrace();
        }
	               
    }
   
	public static void add_song_info(String rl_name,Connection connection) throws SQLException
 	{
    	System.out.println("");
		System.out.println("Enter Song details: ");
int track_number = 0;
        Statement stmt = connection.createStatement();
 		try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter Song ID:");
            String s_id = scanner.next();
          
            System.out.println("Enter Song Title:");
            scanner.nextLine();
            String s_title = scanner.nextLine();
            
            System.out.println("Enter Royalty Rate:");
            double s_royalty_rate = scanner.nextDouble();
             
            System.out.println("List of Albums (type none if no album)");
            String query;
    	      
	    	  query = "Select l_name from Album ";
	      

	        ResultSet rs = null;
		      statement = connection.createStatement();
	           rs = statement.executeQuery(query);

	     
            while(rs.next()){
            	
            	//print all columns
                String l_name = rs.getString("l_name");
                System.out.println(l_name );
            }

            System.out.println("Enter Album Name:");
            scanner.nextLine();
            String l_name = scanner.nextLine();
            
            if(!l_name.equals("none"))
            {
            System.out.println("Enter Track Number:");
            track_number = scanner.nextInt();
            }            
            System.out.println("Enter Song Duration (HH:MM:SS):");
            String s_duration = scanner.next();
            
            System.out.println("Enter Release Date (yyyy-mm-dd):");
            String s_release_date = scanner.next();
            
            System.out.println("Enter Country:");
            scanner.nextLine();
            String s_country = scanner.nextLine();
            
            System.out.println("Enter Language:");
            String s_language = scanner.next();
            
            
  	      
	    	  query = "Select a_email_id from Artist where rl_name = '"+rl_name+"'";
	          rs = statement.executeQuery(query);
	          System.out.println("List of artist in Record Label");

	           while(rs.next())
	           {
	                	
	                	//print all columns
	                    String a_email = rs.getString("a_email_id");
	                    System.out.println(a_email );
	           }
	           System.out.println("Enter Main Artist email:");
	           String a_email_id = scanner.next();
	           String sql;
	            if(l_name.equals("none"))
	            {
	            	 sql = "INSERT INTO Song(s_id, s_title, s_royalty_rate, l_name, track_number, s_duration, s_release_date, s_country, s_language) "
	                 		+ "VALUES ('" + s_id + "', '" + s_title + "', " + s_royalty_rate + ", null , null , '" + s_duration + "', '" + s_release_date + "', '" + s_country + "', '" + s_language + "')";
	           
	            }
	            else
	            {
	            	  sql = "INSERT INTO Song(s_id, s_title, s_royalty_rate, l_name, track_number, s_duration, s_release_date, s_country, s_language) "
	                 		+ "VALUES ('" + s_id + "', '" + s_title + "', " + s_royalty_rate + ", '" + l_name + "', " + track_number + ", '" + s_duration + "', '" + s_release_date + "', '" + s_country + "', '" + s_language + "')";
	                 
	            }
           int rows = stmt.executeUpdate(sql);
            
            
            sql = "INSERT INTO sings (s_id, a_email_id, is_artist_collaborator_for_song, artist_type_for_song) VALUES ('" + s_id + "', '" + a_email_id + "', " + 0 + ", 'Musician')";
            rows = stmt.executeUpdate(sql);
            
            Boolean done = false;
            while(!done) {
            	
            	  query = "Select a_email_id from Artist where a_email_id != '"+a_email_id+"'";
        	      


  	            rs = statement.executeQuery(query);
  	            System.out.println("List of Colabs");

  	                while(rs.next()){
  	                	
  	                	//print all columns
  	                    String a_email = rs.getString("a_email_id");
  	                    System.out.println(a_email );
  	                }
  	    
  	                
  	                
                System.out.println("Enter Collab Artist email (or type 'done' to finish):");
            	String a_email_id_c = scanner.next();
            	
            	if (a_email_id_c.equals("done")) {
                    done = true;
                }
            	else {
            	 sql = "INSERT INTO sings (s_id, a_email_id, is_artist_collaborator_for_song, artist_type_for_song) VALUES ('" + s_id + "', '" + a_email_id_c + "', " + 1 + ", 'Band')";
                 rows = stmt.executeUpdate(sql);
            	}
               
            }
            
            
            
            
            add_song_genres(s_id,connection);
            
            
            
            System.out.println("Song added");

            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
		
		
 	}
	
	
	
//  Get All Songs
	  public static void getsongs(String u_email_id, Connection connection)   throws SQLException 
	  {
		  String song_id ;
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
	                System.out.println();
	                if(u_email_id!="")
	                {
	            while(true) 
	            {
	            	try {
	          		System.out.println("");
	            	System.out.println("Enter Song id to play");
	            	System.out.println("");

	            	song_id = sc.next();
	            	
					query = "SELECT s_id,s_title FROM Song where s_id='" + song_id +"'";
			        rs = statement.executeQuery(query);
			        if (rs.next()) {
			        	
                      song_play_count(rs.getString("s_id"),u_email_id,connection);
			        	
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
	        } 
	        }catch (SQLException e) {
	            Helper.close(rs);
	            e.printStackTrace();
	        }
	  }
	  
	  
	  //given artist/album get songs
	  public static void getsongs(String type, String name, String u_email_id, Connection connection)   throws SQLException 
	  {
		  String song_id;
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
          System.out.println();

	      if(u_email_id!="")
	      {
	    	  
	            while(true) 
	            {
	            	try {
	            		
	          		System.out.println("");
	            	System.out.println("Enter Song id to play");
	            	System.out.println("");

	            	song_id = sc.next();
	            	
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
			        	
                      song_play_count(rs.getString("s_id"),u_email_id,connection);
			        	
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
	        
	  }
	  
	  
//	  increment play count
	  public static void song_play_count(String s_id,String u_email_id, Connection connection) throws SQLException 
	  {
		  

		  System.out.println("");
		  System.out.println("Thank you for listening to Song");
		  System.out.println("");
		  
	       statement = connection.createStatement();
		try
		  {
	       String query = "INSERT INTO listens_to_song (u_email_id, s_id, ls_date) " +
	               "VALUES ('" + u_email_id + "', '" + s_id + "', DATE_FORMAT(CURRENT_DATE, '%Y-%m-%d')) " +
	               "ON DUPLICATE KEY UPDATE ls_play_count = ls_play_count + 1";
	        
	       ResultSet rs = statement.executeQuery(query);

		  }
		catch (SQLException e) {
              System.out.println(e+"Could not fetch  details");
             
          }
		  User.getusermenu(u_email_id, connection);
	  }

}
