package connecttodb;

import java.sql.*;

public class DatabaseOperations {

	protected static void createTables(Connection connection) throws SQLException {

	Statement statement = null;
    
    try {
			statement = connection.createStatement();
				
			// creating user table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS BillingService (\n"
					+ "   bs_id             	INT PRIMARY KEY,\n"
					+ "   bs_date           	DATE,\n"
					+ "   bs_revenue        	DECIMAL(9,2)\n"
					
					+ ");");
			
			statement.executeUpdate("CREATE TABLE  IF NOT EXISTS Album (\n"
					+ "	 l_name 				VARCHAR(128) PRIMARY KEY,\n"
					+ "  l_release_year 		DATE NOT NULL,\n"
					+ "  l_edition 				VARCHAR(128)\n"
					+ ");");
			
			
			statement.executeUpdate("CREATE TABlE IF NOT EXISTS RecordLabel (\n"
					+ " rl_name 				VARCHAR(128) PRIMARY KEY\n"
					+ ");");

			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS User (\n"
					+ "   u_email_id             VARCHAR(128) PRIMARY KEY,\n"
					+ "   u_first_name           VARCHAR(128) NOT NULL,\n"
					+ "   u_last_name            VARCHAR(128) NOT NULL,\n"
					+ "   u_reg_date             DATE         NOT NULL,\n"
					+ "   u_subscription_status  VARCHAR(128) DEFAULT 'ACTIVE',\n"
					+ "   u_phone                INT\n"
					+ ");");
		
			

			statement.executeUpdate("CREATE TABLE  IF NOT EXISTS Podcast (\n"
					+ "	 p_name 				VARCHAR(128) PRIMARY KEY,\n"
					+ "  p_subscriber_count 	INT DEFAULT 0,\n"
					+ "  p_episode_count 		INT DEFAULT 0,\n"
					+ "  p_sponsor 				DECIMAL(9,2) DEFAULT 0,\n"
					+ "  p_language 			VARCHAR(128),\n"
					+ "  p_country 				VARCHAR(128),\n"
					+ "  p_genre 				VARCHAR(128),\n"
					+ "  p_rating 				INT\n"
					+ ");");
			
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS PodcastHost (\n"
					+"ph_email_id 				VARCHAR(128) PRIMARY KEY,\n"
					+"ph_first_name 			VARCHAR(128) NOT NULL,\n"
					+"ph_last_name 				VARCHAR(128) NOT NULL,\n"
					+"ph_phone 					INT,\n"
					+"ph_city 					VARCHAR(128)\n"
					+");");

			statement.executeUpdate("CREATE TABLE  IF NOT EXISTS SpecialGuest (\n"
					+ "	 g_email_id 			VARCHAR(128) PRIMARY KEY,\n"
					+ "  g_name 				VARCHAR(128) NOT NULL\n"
					
					+ ");");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Song (\n"
					+ " s_id 					INT PRIMARY KEY,\n"
					+ " s_title 				VARCHAR(128) NOT NULL,\n"
					+ " s_royalty_rate 			DECIMAL(9,2) NOT NULL,\n"
					+ " s_royalty_paid_status 	BOOLEAN DEFAULT FALSE,\n"
					+ " s_play_count 			INT DEFAULT 0,\n"
					+ " l_name 					VARCHAR(128),\n"
					+ " track_number 			INT,\n"
					+ " s_duration 				TIME,\n"
					+ " s_genre 				VARCHAR(128),\n"
					+ " s_release_date 			DATE,\n"
					+ " s_country 				VARCHAR(128),\n"
					+ " s_language 				VARCHAR(128),\n"
					+ " FOREIGN KEY(l_name) REFERENCES Album(l_name) ON UPDATE CASCADE,\n"
					+ " CHECK(\n"
					+ " (l_name IS NULL AND track_number IS NULL) OR\n"
					+ " (l_name IS NOT NULL AND track_number IS NOT NULL)\n"
					+ " )\n"
					+ ");\n"
					+ "");

			
			statement.executeUpdate("CREATE TABLE  IF NOT EXISTS Artist (\n"
					+ "	 a_email_id 				VARCHAR(128) PRIMARY KEY,\n"
					+ "  a_name 					VARCHAR(128) NOT NULL,\n"
					+ "  a_status 					VARCHAR(128) DEFAULT 'ACTIVE',\n"
					+ "  a_country 					VARCHAR(128),\n"
					+ "  a_primary_genre 			VARCHAR(128),\n"
					+ "  a_monthly_listeners 		INT DEFAULT 0,\n"
					+ "  rl_name 					VARCHAR(128),\n"
					+ " FOREIGN KEY(rl_name) REFERENCES RecordLabel(rl_name) ON UPDATE CASCADE"
					+ ");");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS PodcastEpisode (\n"
					+ " pe_title 					VARCHAR(128),\n"
					+ " pe_release_date 			DATE NOT NULL,\n"
					+ " p_name 						VARCHAR(128)  NOT NULL,\n"
					+ " ph_email_id 				VARCHAR(128) NOT NULL,\n"
					+ " pe_monthly_listeners 		INT DEFAULT 0,\n"
					+ " pe_ad_count 				INT DEFAULT 0,\n"
					+ " pe_duration 				VARCHAR(128),\n"
					+ " PRIMARY KEY(p_name, pe_title),\n"
					+ " FOREIGN KEY(p_name) REFERENCES Podcast(p_name) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(ph_email_id) REFERENCES PodcastHost(ph_email_id) ON UPDATE CASCADE)");
			
			
			
			statement.executeUpdate("CREATE TABLE  IF NOT EXISTS pays_to (\n"
					+ "	 up_id 						INT PRIMARY KEY,\n"
					+ "  up_fee_for_subscription 	INT DEFAULT 100,\n"
					+ "  up_date 					DATE NOT NULL,\n"
					+ "  bs_id 						INT NOT NULL,\n"
					+ "  u_email_id 				VARCHAR(128) NOT NULL,\n"
					+ " FOREIGN KEY(bs_id) REFERENCES BillingService(bs_id) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(u_email_id) REFERENCES User(u_email_id) ON UPDATE CASCADE\n"
					+ ");");
			

			
			
		} finally {
			close(statement);
		}
	}
 
  protected static void clearDatabase(Connection connection) {
    String[] tableNames = new String[] {
//    		"PodcastEpisode"
//    		"BillingService",
//    		"Song",
//    		"Album",
//    		"Artist",
//    		"RecordLabel",
//    		"User",
//    		"Podcast",
//    		"PodcastHost",
//    		"PodcastEpisode",
//    		"SpecialGuest",
//    		"pays_to",
//    		"pays_to_host",
//    		"pays_to_record_label",
//    		"pays_to_artist",
//    		"collects_song_data",
//    		"listens_to_song",
//    		"collects_episode_data",
//    		"listens_to_podcast_episode",
//    		"sings",
//    		"assigned_to",
//    		"features"
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