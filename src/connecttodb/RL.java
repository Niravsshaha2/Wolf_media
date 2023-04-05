package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RL {
	
    public static Statement statement;
 	public static void add_album_info(String rl_name,Connection connection) throws SQLException
 	{
 		System.out.println("");
		System.out.println("Enter Album details: ");

        Statement stmt = connection.createStatement();
 	
        
        try {
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Enter Album Name:");
            String l_name = scanner.nextLine();
            
            System.out.println("Enter Release Year:");
            String l_release_year_str = scanner.nextLine();
            java.sql.Date l_release_date = java.sql.Date.valueOf(l_release_year_str);

            System.out.print(l_release_date);
            System.out.println("Enter Edition:");
            String l_edition = scanner.nextLine();

         
            String sql = "INSERT INTO Album (l_name, l_release_year, l_edition) VALUES ('" + l_name + "', '" +  l_release_year_str + "', '" + l_edition + "')";
            int rows = stmt.executeUpdate(sql);
            System.out.println(rows + " row(s) inserted.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
 	}


 	public static void add_artist_info(String rl_name,Connection connection) throws SQLException
 	{
 		System.out.println("");
		System.out.println("Enter Artist details: ");

        Statement stmt = connection.createStatement();
 	
 		 try {
             Scanner scanner = new Scanner(System.in);
             System.out.println("Enter Artist Email ID:");
             String a_email_id = scanner.next();
             
             System.out.println("Enter Artist Name:");
             String a_name = scanner.next();
               
             System.out.println("Enter Country:");
             String a_country = scanner.next();
             
             System.out.println("Enter Primary Genre:");
             String a_primary_genre = scanner.next();
             

             String sql = "INSERT INTO Artist (a_email_id, a_name, a_status, a_country, a_primary_genre, rl_name) VALUES ('" + a_email_id + "', '" + a_name + "', 'ACTIVE', '" + a_country + "', '" + a_primary_genre + "', '" + rl_name + "')";
             int rows = stmt.executeUpdate(sql);
             System.out.println(rows + " row(s) inserted.");

         } catch (Exception e) {
             System.err.println("Error: " + e.getMessage());
         }
 	}
    
 	public static void add_song_info(String rl_name,Connection connection) throws SQLException
 	{
    	System.out.println("");
		System.out.println("Enter Song details: ");

        Statement stmt = connection.createStatement();
 		try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter Song ID:");
            int s_id = scanner.nextInt();
          
            System.out.println("Enter Song Title:");
            String s_title = scanner.next();
            
            System.out.println("Enter Royalty Rate:");
            double s_royalty_rate = scanner.nextDouble();
             
            System.out.println("Enter Album Name:");
            String l_name = scanner.next();
            
            System.out.println("Enter Track Number:");
            int track_number = scanner.nextInt();
            
            System.out.println("Enter Song Duration:");
            String s_duration = scanner.next();
            
            System.out.println("Enter Release Date (yyyy-mm-dd):");
            String s_release_date = scanner.next();
            
            System.out.println("Enter Country:");
            String s_country = scanner.next();
            
            System.out.println("Enter Language:");
            String s_language = scanner.next();
            
            System.out.println("Enter Main Artist email:");
            String a_email_id = scanner.next();
            
            String sql = "INSERT INTO Song(s_id, s_title, s_royalty_rate, l_name, track_number, s_duration, s_release_date, s_country, s_language) "
            		+ "VALUES (" + s_id + ", '" + s_title + "', " + s_royalty_rate + ", '" + l_name + "', " + track_number + ", '" + s_duration + "', '" + s_release_date + "', '" + s_country + "', '" + s_language + "')";
            int rows = stmt.executeUpdate(sql);
            
            
            sql = "INSERT INTO sings (s_id, a_email_id, is_artist_collaborator_for_song, artist_type_for_song) VALUES (" + s_id + ", '" + a_email_id + "', " + 0 + ", 'Musician')";
            rows = stmt.executeUpdate(sql);
            
            Boolean done = false;
            while(!done) {
            	
                System.out.println("Enter Collab Artist email (or type 'done' to finish):");
            	String a_email_id_c = scanner.next();
            	
            	if (a_email_id_c.equals("done")) {
                    done = true;
                }
            	else {
            	 sql = "INSERT INTO sings (s_id, a_email_id, is_artist_collaborator_for_song, artist_type_for_song) VALUES (" + s_id + ", '" + a_email_id_c + "', " + 1 + ", 'Band')";
                 rows = stmt.executeUpdate(sql);
            	}
               
            }
          
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
		
		
 	}

    
 // RecordLabel Page
 	public static void rl_page(Connection connection) throws SQLException{
 		Scanner sc = new Scanner(System.in);
 		String rl_name;
 		statement = connection.createStatement();
 		do{
 			System.out.println("");
 			System.out.println("Enter record label name: ");
 			try{
 				rl_name = sc.next();
 				rl_name = "EMI";
 				String x = "SELECT rl_name AS rl_name from RecordLabel where rl_name='" + rl_name+"'";
 	                    ResultSet rs5 = statement.executeQuery(x);
 	                    
 	                    if (rs5.next()) {
 	                        System.out.println("Recordlabel found");
 	                       getRecordlabelMenu(rl_name,connection);
 	                        
 	                    } else{
 	                        System.out.println("Try again");
 	                       rl_page(connection);
 	                    }
 	                } catch (SQLException e) {
 	                    System.out.println("Could not fetch details");
 	                }
 	        } while (true);
 			}
 		
 	
    public static void getRecordlabelMenu(String rl_name, Connection connection)   throws SQLException 
	  {
		  	Scanner sc = new Scanner(System.in);
	        int enteredValue = 0;
	        statement = connection.createStatement();
	        
	        do {
	            System.out.println("");
	            System.out.println("Select from the options below");
	            System.out.println("1. Add New Song");  //enter-update
	            System.out.println("2. Update Song Info");  //enter-update
	            System.out.println("3. View All Song");  //enter-update
	            
	            System.out.println("4. Add New Artist");  //enter-update
	            System.out.println("5. Update Artist Info");  //enter-update
	            System.out.println("6. View All Artist");  //enter-update
	            
	            System.out.println("7. Add New Album");  //enter-update
	            System.out.println("8. Update Album Info");  //enter-update
	            System.out.println("9. View All Album");  //enter-update


	            System.out.println("10. Assign Song to Album");
	            System.out.println("11. Assign Artist to Album");

	            System.out.println("12. Exit");
	            System.out.println("");


	            try {
	                enteredValue = sc.nextInt();

	                switch (enteredValue) {
	                    case 1:
	                    	add_song_info(rl_name, connection);
	                    	break;
	                    case 2:
//	                    	update_song_info(rl_name, connection);
	                    	break;
	                    case 3:
	                    		                      
	              		  System.out.println("");
	              		  System.out.println("List of Songs");
	              		  System.out.println("");

	              	      String query;
	              	      
	              		  query = "select * from Song where s_id in (Select s_id from Artist Natural join sings where rl_name ='" + rl_name +"')";


	              	        ResultSet rs = null;
	              	        try {
	              	            rs = statement.executeQuery(query);
	              	                while(rs.next()){
	              	                	
	              	                	//print all columns
	              	                    String title = rs.getString("s_title");
	              	                    String s_id = rs.getString("s_id");
	              	                    System.out.println(s_id + " " +title + "" );
	              	                }
	              	      } catch (SQLException e) {
	          	            Helper.close(rs);
	          	            e.printStackTrace();
	          	        }
	              	                
	              	                
	              	                
	                    	break;
	                    case 4:
	                    	add_artist_info(rl_name, connection);
	                    	break;
	                    case 5:
	                    	break;
	                    case 6:
//	                    	getartist("RL",rl_name,connection);

	                      System.out.println("");
	               		  System.out.println("List of Artists");
	               		  System.out.println("");
	               		  //print all columns 
	               		  
	               		  statement = connection.createStatement();
	               	
	               		  query = "SELECT a_name,a_email_id FROM Artist where rl_name ='" + rl_name +"'";

	               	        try {
	               	            rs = statement.executeQuery(query);
	               	            int row = 1;

	               	                while(rs.next()){
	               	                   String title = rs.getString("a_name");
	               	                    System.out.println(row + " " +title + "" );
	               	                    row++;
	               	                }
	               	     } catch (SQLException e) {
//		          	            Helper.close(rs);
		          	            e.printStackTrace();
		          	        }
	                    	break;
	                    case 7:
	                    	add_album_info(rl_name, connection);
	                    	break;
	                    case 8:
	                    	break;
	                    case 9:
//	                    	getAlbum("RL",rl_name,connection);

	                      System.out.println("");
	              		  System.out.println("List of Albums");
	              		  System.out.println("");
	              		  
	              		  
	              		  statement = connection.createStatement();
	              	
	              		  query = "SELECT l_name FROM assigned_to Natural join Artist where rl_name ='" + rl_name +"'";
	              	        try {
	              	            rs = statement.executeQuery(query);
	              	            int row = 1;

	              	                while(rs.next()){
	              	                    String title = rs.getString("l_name");
	              	                    System.out.println(row + " " +title + "" );
	              	                    row++;
	              	                }
	              	      } catch (SQLException e) {
//		          	            Helper.close(rs);
		          	            e.printStackTrace();
		          	        }
	              	            
	                    	break;
	                    case 10:
	                    	break;
	                    case 11:
	                    	break;
	                    case 12:
	                    	MainMenu.displayMenu(connection);
	                    	break;
	                    default:
	                        System.out.println("You have made an invalid choice. Please pick again.");
	                }
	            } catch (Exception e) {
	                System.out.println("You have made an invalid choice. Please pick again.");
//	                getusermenu(u_email_id,connection);
	            }
	        } while (enteredValue != 12);   
	  }
    
	  public static void getsongs(String type, String rlname, Connection connection)   throws SQLException 
	  {
		  if(type == "RL")
		  {
			  
		  }
	  }

    

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
//	            System.out.println("Selected value: " + artist_name);
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
//	            System.out.println("Selected value: " + album_name);
	            getsongs("Album",album_name, u_email_id,connection);

	            }
	            else
	            {
	            	getalbum(u_email_id, connection);
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
