package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

//add_song_info
public class RL {

  public static Statement statement;

  public static String[] recordlabel_list;

  public static String[] view_all_record_labels(Connection connection, String show_type)
    throws SQLException {
    String query;
    statement = connection.createStatement();
    ResultSet rs = null;

    System.out.println("");
    if(show_type.equals("show")) {
      System.out.println("~ List of Record Labels ~");
      System.out.println("");
    }
    //print all columns

    query = "SELECT rl_name FROM RecordLabel";

    try {
      rs = statement.executeQuery(query);
      int row = 1;
      int recordlabel_count = 0;
      if (rs.last()) {
        recordlabel_count = rs.getRow();
        rs.beforeFirst();
      }
      recordlabel_list = new String[recordlabel_count];

      while (rs.next()) {
        String recordlabelname = rs.getString("rl_name");
        recordlabel_list[row - 1] = recordlabelname;
        if(show_type.equals("show")) {
          System.out.println(row + ". " + recordlabelname + "");
        }
        row++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordlabel_list;
  }

  public static void add_RL(Connection connection) throws SQLException {
    Scanner sc = new Scanner(System.in);
    String rl_name;
    statement = connection.createStatement();

    System.out.println("");
    System.out.println("Enter record label name: ");
    try {
      sc.nextLine();
      rl_name = sc.nextLine();

      String sql = "INSERT INTO RecordLabel(rl_name) " + "VALUES ('" + rl_name + "')";
      int rows = statement.executeUpdate(sql);
      System.out.println("Record Label inserted");
    } catch (SQLException e) {
      System.out.println("Could not fetch details");
    }
  }

  // RecordLabel Page
  public static void rl_page(Connection connection) throws SQLException {
    Scanner sc = new Scanner(System.in);
    String rl_name;
    statement = connection.createStatement();

    //print RL name

    String query, title, s_id = "";

    query = "SELECT rl_name FROM RecordLabel";
    System.out.println("~ List of Record Labels ~\n");

    ResultSet rs = null;
    try {
      rs = statement.executeQuery(query);
      while (rs.next()) {
        rl_name = rs.getString("rl_name");
        System.out.println(rl_name + "");
      }
    } catch (SQLException e) {
      System.out.println("Could not fetch details");
    }

    do {
      System.out.println("");
      System.out.println("Enter record label name: ");
      try {
        rl_name = sc.nextLine();
        // sc.nextLine();

        // rl_name = "EMI";
        String x =
          "SELECT rl_name AS rl_name from RecordLabel where rl_name='" +
          rl_name +
          "'";
        ResultSet rs5 = statement.executeQuery(x);

        if (rs5.next()) {
          System.out.println("Recordlabel found");
          getRecordlabelMenu(rl_name, connection);
        } else {
          System.out.println("Try again");
          rl_page(connection);
        }
      } catch (SQLException e) {
        System.out.println("Could not fetch details");
      }
    } while (true);
  }

  public static void getRecordlabelMenu(String rl_name, Connection connection)
    throws SQLException {
    Scanner sc = new Scanner(System.in);
    int enteredValue = 0;
    String query;
    ResultSet rs = null;
    statement = connection.createStatement();

    do {
      System.out.println("");
      System.out.println("Select from the options below");
      System.out.println("1. Add New Song"); //enter-update
      System.out.println("2. Update Song Info"); //enter-update
      System.out.println("3. Delete Song Info"); //enter-update
      System.out.println("4. View All Song"); //enter-update

      System.out.println("5. Add New Artist"); //enter-update
      System.out.println("6. Update Artist Info"); //enter-update
      System.out.println("7. Delete Artist Info"); //enter-update
      System.out.println("8. View All Artist"); //enter-update

      System.out.println("9. Add New Album"); //enter-update
      System.out.println("10. Update Album Info"); //enter-update
      System.out.println("11. Delete Album Info"); //enter-update
      System.out.println("12. Delete Song Info"); //enter-update
      System.out.println("13. View All Album"); //enter-update

      System.out.println("14. Assign Song and Artist to Album");

      System.out.println("0. Go to previous menu");
      System.out.println("");

      String so_id;

      try {
        enteredValue = sc.nextInt();

        switch (enteredValue) {
          case 1:
            song.add_song_info(rl_name, connection);
            break;
          case 2:
            System.out.println("Enter Song id to update");
            do {
              so_id = sc.next();
              String x = "SELECT * from Song where s_id='" + so_id + "'";
              ResultSet rs5 = statement.executeQuery(x);
              if (rs5.next()) {
                System.out.println("Song found");
                break;
              } else {
                System.out.println("Try again");
              }
            } while (true);

            song.update_song_info(rl_name, so_id, connection);
            break;
          case 3:
            System.out.println("Enter Song id to Delete info");
            do {
              so_id = sc.next();
              String x = "SELECT * from Song where s_id='" + so_id + "'";
              ResultSet rs5 = statement.executeQuery(x);
              if (rs5.next()) {
                System.out.println("Song found");
                break;
              } else {
                System.out.println("Try again");
              }
            } while (true);

            song.delete_song_info(rl_name, so_id, connection);
            break;
          case 4:
            song.viewsongs(rl_name, connection);
            break;
          case 5:
            artist.add_artist_info(rl_name, connection);
            break;
          case 6:
            //	                    	so_id = sc.next();

            
          System.out.println("List of Artist:");

  	      statement = connection.createStatement();
  	      String title1;
  	      
  		  query = "SELECT a_email_id from Artist where rl_name ='"+rl_name+"'";
  		  
  	       
  	            rs = statement.executeQuery(query);
  	                while(rs.next()){
  	                    title1 = rs.getString("a_email_id");
  	                    System.out.println(title1 + "" );
  	                }
  	       
  	                
  	              System.out.println("Enter Artist email id to update");
      
  	                
            do {
              so_id = sc.next();
              String x =
                "SELECT * from Artist where a_email_id='" + so_id + "'";
              ResultSet rs5 = statement.executeQuery(x);
              if (rs5.next()) {
                System.out.println("Artist found");
                break;
              } else {
                System.out.println("Try again");
              }
            } while (true);

            artist.update_artist_info(rl_name, so_id, connection);
            break;
          case 7:
        	  
        	  System.out.println("List of Artist:");

      	      statement = connection.createStatement();
      	      String title2;
      	      
      		  query = "SELECT a_email_id from Artist where rl_name ='"+rl_name+"'";
      		  
      	       
      	            rs = statement.executeQuery(query);
      	                while(rs.next()){
      	                    title2 = rs.getString("a_email_id");
      	                    System.out.println(title2 + "" );
      	                }
      	       
      	          
            System.out.println("Enter Artist email id to delete");

            do {
              so_id = sc.next();
              String x =
                "SELECT * from Artist where a_email_id='" + so_id + "'";
              ResultSet rs5 = statement.executeQuery(x);
              if (rs5.next()) {
                System.out.println("Artist found");
                break;
              } else {
                System.out.println("Try again");
              }
            } while (true);
            artist.delete_artist_info(rl_name, so_id, connection);
            break;
          case 8:
            artist.viewartist(rl_name, connection);
            break;
          case 9:
            album.add_album_info(rl_name, connection);
            break;
          case 10:
            System.out.println("Enter Album name to update");

            do {
              sc.nextLine();
              so_id = sc.nextLine();
              String x = "SELECT * from Album where l_name='" + so_id + "'";
              ResultSet rs5 = statement.executeQuery(x);
              if (rs5.next()) {
                System.out.println("Album found");
                break;
              } else {
                System.out.println("Try again");
              }
            } while (true);

            album.update_album_info(rl_name, so_id, connection);
            break;
            
          case 11:
              System.out.println("Enter Album name to delete");

              do {
                sc.nextLine();
                so_id = sc.nextLine();
                String x = "SELECT * from Album where l_name='" + so_id + "'";
                ResultSet rs5 = statement.executeQuery(x);
                if (rs5.next()) {
                  System.out.println("Album found");
                  break;
                } else {
                  System.out.println("Try again");
                }
              } while (true);

              album.delete_album_info(rl_name, so_id, connection);
              break;
              
          case 12:
            System.out.println("Enter Album name to delete");
            do {
              sc.nextLine();
              so_id = sc.nextLine();
              String x = "SELECT * from Album where l_name='" + so_id + "'";
              ResultSet rs5 = statement.executeQuery(x);
              if (rs5.next()) {
                System.out.println("Album found");
                break;
              } else {
                System.out.println("Try again");
              }
            } while (true);

            album.delete_album_info(rl_name, so_id, connection);
            break;
          case 13:
            album.viewalbum(rl_name, connection);
            break;
          case 14:
            try {
              System.out.println("Enter song ID: ");
              String s_id = sc.next();
              System.out.println("");
              String l_name = null;
              rs =
                statement.executeQuery(
                  "SELECT l_name FROM Song WHERE s_id = '" + s_id +"'"
                );
              if (rs.next()) {
                l_name = rs.getString("l_name");
              }

              if (l_name == null) {
                System.out.println("Enter Album Name: ");
                sc.nextLine();
                l_name = sc.nextLine();
                // Updating the database
                statement.executeUpdate(
                  "UPDATE Song SET l_name = '" +
                  l_name +
                  "' WHERE s_id = '" +
                  s_id +"'"
                );
                String sql1 =
                  "INSERT INTO assigned_to (a_email_id, l_name)  SELECT Artist.a_email_id, '" +
                  l_name +
                  "' FROM Artist JOIN sings ON  Artist.a_email_id = sings.a_email_id where sings.s_id = '" +
                  s_id +
                  "'";
                statement.executeUpdate(sql1);
                System.out.println("Album name updated for song " + s_id);
              } else {
                System.out.println("Song " + s_id + " is in album " + l_name);
              }
            } catch (Exception e) {
              System.err.println("Error: " + e.getMessage());
            }

            break;
          case 0:
            MainMenu.displayMenu(connection);
            break;
          default:
            System.out.println("You have made an invalid choice. Please pick again.");
        }
      } catch (Exception e) {
        System.out.println("You have made an invalid choice. Please pick again.");
      }
    } while (enteredValue != 0);
  }
}