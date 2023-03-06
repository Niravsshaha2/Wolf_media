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
			

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS pays_to_host (\n"
					+ " pfh_id 						INT PRIMARY KEY,\n"
					+ " pfh_amount 					INT NOT NULL,\n"
					+ " pfh_date 					DATE NOT NULL,\n"
					+ " bs_id 						INT NOT NULL,\n"
					+ " ph_email_id 				VARCHAR(128) NOT NULL,\n"
					+ " FOREIGN KEY(bs_id) REFERENCES BillingService(bs_id) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(ph_email_id) REFERENCES PodcastHost(ph_email_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS pays_to_record_label (\n"
					+ " pfs_id 						INT PRIMARY KEY,\n"
					+ " pfs_amount 					INT NOT NULL,\n"
					+ " pfs_date 					DATE NOT NULL,\n"
					+ " bs_id 						INT NOT NULL,\n"
					+ " rl_name 					VARCHAR(128) NOT NULL,\n"
					+ " FOREIGN KEY(bs_id) REFERENCES BillingService(bs_id) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(rl_name) REFERENCES RecordLabel(rl_name) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS pays_to_artist (\n"
					+ " pfa_id 						INT PRIMARY KEY,\n"
					+ " pfa_amount 					INT NOT NULL,\n"
					+ " pfa_date 					DATE NOT NULL,\n"
					+ " rl_name 					VARCHAR(128) NOT NULL,\n"
					+ " a_email_id 					VARCHAR(128) NOT NULL,\n"
					+ " FOREIGN KEY(rl_name) REFERENCES RecordLabel(rl_name) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(a_email_id) REFERENCES Artist(a_email_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS collects_song_data (\n"
					+ " bs_id 						INT,\n"
					+ " s_id 						INT,\n"
					+ " PRIMARY KEY(bs_id, s_id),\n"
					+ " FOREIGN KEY(bs_id) REFERENCES BillingService(bs_id) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(s_id) REFERENCES Song(s_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS listens_to_song (\n"
					+ " u_email_id 					VARCHAR(128),\n"
					+ " s_id 						INT,\n"
					+ " ls_date 					DATE NOT NULL,\n"
					+ " PRIMARY KEY(u_email_id, s_id),\n"
					+ " FOREIGN KEY(u_email_id) REFERENCES User(u_email_id) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(s_id) REFERENCES Song(s_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");

			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS collects_episode_data (\n"
					+ " pe_title 					VARCHAR(128),\n"
					+ " p_name 						VARCHAR(128),\n"
					+ " bs_id 						INT,\n"
					+ " PRIMARY KEY(p_name,pe_title,bs_id),\n"
					+ " INDEX f1(p_name,pe_title),INDEX f2(bs_id),"
					+ " FOREIGN KEY(p_name,pe_title) REFERENCES PodcastEpisode(p_name,pe_title) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(bs_id) REFERENCES BillingService(bs_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");			
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS listens_to_podcast_episode (\n"
					+ " pe_title 					VARCHAR(128),\n"
					+ " p_name 						VARCHAR(128),\n"
					+ " u_email_id 					VARCHAR(128),\n"
					+ " lpe_date 					DATE NOT NULL,\n"
					+ " PRIMARY KEY(p_name,pe_title,u_email_id),\n"
					+ " INDEX f1(p_name,pe_title),INDEX f2(u_email_id),"
					+ " FOREIGN KEY(p_name,pe_title) REFERENCES PodcastEpisode(p_name,pe_title) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(u_email_id) REFERENCES User(u_email_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS sings (\n"
					+ " s_id 							INT,\n"
					+ " a_email_id 						VARCHAR(128),\n"
					+ " is_artist_collaborator_for_song BOOLEAN NOT NULL,\n"
					+ " artist_type_for_song 			VARCHAR(128) NOT NULL,\n"
					+ " PRIMARY KEY(s_id, a_email_id),\n"
					+ " FOREIGN KEY(s_id) REFERENCES Song(s_id) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(a_email_id) REFERENCES Artist(a_email_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS assigned_to (\n"
					+ " a_email_id 						VARCHAR(128),\n"
					+ " l_name 							VARCHAR(128),\n"
					+ " PRIMARY KEY(a_email_id, l_name),\n"
					+ " FOREIGN KEY(l_name) REFERENCES Album(l_name) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(a_email_id) REFERENCES Artist(a_email_id) ON UPDATE CASCADE\n"
					+ ");\n"
					+ "");
			
			
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS features (\n"
					+ " pe_title 					VARCHAR(128),\n"
					+ " p_name 						VARCHAR(128),\n"
					+ " g_email_id 					VARCHAR(128),\n"
					+ " PRIMARY KEY(p_name,pe_title,g_email_id),\n"
					+ " INDEX f1(p_name,pe_title),INDEX f2(g_email_id),"
					+ " FOREIGN KEY(p_name,pe_title) REFERENCES PodcastEpisode(p_name,pe_title) ON UPDATE CASCADE,\n"
					+ " FOREIGN KEY(g_email_id) REFERENCES SpecialGuest(g_email_id)ON UPDATE CASCADE \n"
					+ ");\n"
					+ "");
			
			
			
		} finally {
			close(statement);
		}
	}
 
  protected static void clearDatabase(Connection connection) {
    String[] tableNames = new String[] {
    		"listens_to_podcast_episode"
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