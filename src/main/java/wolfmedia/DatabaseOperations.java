package main.java.wolfmedia;
import java.sql.*;

public class DatabaseOperations {

	protected static void createTables(Connection connection) throws SQLException {

	Statement statement = null;
    
    try {
			statement = connection.createStatement();
				
			// creating user table
			statement.executeUpdate("CREATE TABLE User (\n"
					+ "   u_email_id             VARCHAR(128) PRIMARY KEY,\n"
					+ "   u_first_name           VARCHAR(128) NOT NULL,\n"
					+ "   u_last_name            VARCHAR(128) NOT NULL,\n"
					+ "   u_reg_date             DATE         NOT NULL,\n"
					+ "   u_subscription_status  VARCHAR(128) DEFAULT 'ACTIVE',\n"
					+ "   u_phone                INT\n"
					+ ");");

		} finally {
			close(statement);
		}
	}
 
  protected static void clearDatabase(Connection connection) {
    String[] tableNames = new String[] {
    		"BillingService",
    		"Song",
    		"Album",
    		"Artist",
    		"RecordLabel",
    		"User",
    		"Podcast",
    		"PodcastHost",
    		"PodcastEpisode",
    		"SpecialGuest",
    		"pays_to",
    		"pays_to_host",
    		"pays_to_record_label",
    		"pays_to_artist",
    		"collects_song_data",
    		"listens_to_song",
    		"collects_episode_data",
    		"listens_to_podcast_episode",
    		"sings",
    		"assigned_to",
    		"features"
	};
	
	for(String tableName : tableNames) {
		Statement statement = null;
		try {
		  statement = connection.createStatement();
		  statement.executeUpdate(String.format("DROP TABLE %s;", tableName));
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println(String.format("%s did not exist to delete.", tableName));
		} finally {
			close(statement);
		}
	}
  }
	
  private static void close(Statement statement) {
	  if(statement != null) {
		  try {
			  statement.close();
		  } catch(Throwable whatever) {}
	  }
  }
}
