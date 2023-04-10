package connecttodb;

import java.sql.*;

public class DatabaseOperations {

  protected static void createTables(Connection connection)
    throws SQLException {
    Statement statement = null;

    try {
      statement = connection.createStatement();

      // creating user table
      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS BillingService (\n" +
        " bs_date                            DATE         PRIMARY KEY,\n" +
        " bs_revenue                         DECIMAL(9,2) DEFAULT 0\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS User (\n" +
        " u_email_id                         VARCHAR(128) PRIMARY KEY,\n" +
        " u_first_name                       VARCHAR(128) NOT NULL,\n" +
        " u_last_name                        VARCHAR(128),\n" +
        " u_reg_date                         DATE         NOT NULL,\n" +
        " u_subscription_status              VARCHAR(128) DEFAULT 'INACTIVE',\n" +
        " u_phone                            DECIMAL(10,0)\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABlE IF NOT EXISTS RecordLabel (\n" +
        " rl_name                            VARCHAR(128) PRIMARY KEY\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Artist (\n" +
        " a_email_id                         VARCHAR(128) PRIMARY KEY,\n" +
        " a_name                             VARCHAR(128) NOT NULL,\n" +
        " a_status                           VARCHAR(128) DEFAULT 'ACTIVE',\n" +
        " a_country                          VARCHAR(128),\n" +
        " a_primary_genre                    VARCHAR(128) NOT NULL,\n" +
        " rl_name                            VARCHAR(128) NOT NULL,\n" +
        " FOREIGN KEY(rl_name) REFERENCES RecordLabel(rl_name) ON UPDATE CASCADE ON DELETE CASCADE" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Album (\n" +
        " l_name                             VARCHAR(128) PRIMARY KEY,\n" +
        " l_release_year                     DATE NOT NULL,\n" +
        " l_edition                          VARCHAR(128) CHECK (l_edition IN ('Special', 'Limited', 'Collector'))\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Song (\n" +
        " s_id                               INT PRIMARY KEY,\n" +
        " s_title                            VARCHAR(128) NOT NULL,\n" +
        " s_royalty_rate                     DECIMAL(9,2) NOT NULL,\n" +
//        " s_royalty_paid_status              BOOLEAN      DEFAULT FALSE,\n" +
        " s_duration                         TIME         NOT NULL,\n" +
        " s_release_date                     DATE         NOT NULL,\n" +
        " s_country                          VARCHAR(128),\n" +
        " s_language                         VARCHAR(128),\n" +
        " l_name                             VARCHAR(128),\n" +
        " track_number                       INT,\n" +
        " FOREIGN KEY(l_name) REFERENCES Album(l_name) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " CHECK(\n" +
        "  (l_name IS NULL AND track_number IS NULL) OR\n" +
        "  (l_name IS NOT NULL AND track_number IS NOT NULL)\n" +
        " )\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS SongGenre (\n" +
        " s_id                               INT,\n" +
        " sg_genre                           VARCHAR(128) NOT NULL,\n" +
        " PRIMARY KEY(s_id, sg_genre),\n" +
        " FOREIGN KEY(s_id) REFERENCES Song(s_id) ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Podcast (\n" +
        " p_name                             VARCHAR(128) PRIMARY KEY,\n" +
        " p_sponsor                          DECIMAL(9,2) DEFAULT 0,\n" +
        " p_language                         VARCHAR(128) NOT NULL,\n" +
        " p_country                          VARCHAR(128),\n" +
        " p_rating                           FLOAT,\n" +
        " p_rated_user_count                 INT          DEFAULT 0\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS PodcastGenre (\n" +
        " p_name                             VARCHAR(128),\n" +
        " pg_genre                           VARCHAR(128) NOT NULL,\n" +
        " PRIMARY KEY(p_name, pg_genre),\n" +
        " FOREIGN KEY(p_name) REFERENCES Podcast(p_name) ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS PodcastHost (\n" +
        " ph_email_id                        VARCHAR(128) PRIMARY KEY,\n" +
        " ph_first_name                      VARCHAR(128) NOT NULL,\n" +
        " ph_last_name                       VARCHAR(128),\n" +
        " ph_phone                           DECIMAL(10,0),\n" +
        " ph_city                            VARCHAR(128)\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS PodcastEpisode (\n" +
        " pe_title                           VARCHAR(128),\n" +
        " p_name                             VARCHAR(128),\n" +
        " pe_release_date                    DATE         NOT NULL,\n" +
        " pe_ad_count                        INT          DEFAULT 0,\n" +
        " pe_duration                        VARCHAR(128) NOT NULL,\n" +
        " ph_email_id                        VARCHAR(128),\n" +
        " PRIMARY KEY(p_name, pe_title),\n" +
        " FOREIGN KEY(p_name)      REFERENCES Podcast(p_name)          ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(ph_email_id) REFERENCES PodcastHost(ph_email_id) ON UPDATE CASCADE ON DELETE CASCADE" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS SpecialGuest (\n" +
        " g_email_id                         VARCHAR(128) PRIMARY KEY,\n" +
        " g_name                             VARCHAR(128) NOT NULL\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS pays_to (\n" +
        " up_fee_for_subscription            FLOAT        DEFAULT 100,\n" +
        " up_date                            DATE,\n" +
        " bs_date                            DATE,\n" +
        " u_email_id                         VARCHAR(128),\n" +
        " PRIMARY KEY(u_email_id, bs_date, up_date),\n" +
        " FOREIGN KEY(bs_date)    REFERENCES BillingService(bs_date) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(u_email_id) REFERENCES User(u_email_id)        ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS pays_to_host (\n" +
        " pfh_amount                         FLOAT        NOT NULL,\n" +
        " pfh_date                           DATE,\n" +
        " bs_date                            DATE,\n" +
        " ph_email_id                        VARCHAR(128),\n" +
        " PRIMARY KEY(ph_email_id, bs_date, pfh_date),\n" +
        " FOREIGN KEY(bs_date)     REFERENCES BillingService(bs_date)  ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(ph_email_id) REFERENCES PodcastHost(ph_email_id) ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS pays_to_record_label (\n" +
        " pfs_amount                         FLOAT        NOT NULL,\n" +
        " pfs_date                           DATE,\n" +
        " bs_date                            DATE,\n" +
        " rl_name                            VARCHAR(128),\n" +
        " PRIMARY KEY(rl_name, bs_date, pfs_date),\n" +
        " FOREIGN KEY(bs_date) REFERENCES BillingService(bs_date) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(rl_name) REFERENCES RecordLabel(rl_name)    ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS pays_to_artist (\n" +
        " pfa_amount                         FLOAT        NOT NULL,\n" +
        " pfa_date                           DATE,\n" +
        " rl_name                            VARCHAR(128),\n" +
        " a_email_id                         VARCHAR(128),\n" +
        " PRIMARY KEY(a_email_id, rl_name, pfa_date),\n" +
        " FOREIGN KEY(rl_name)    REFERENCES RecordLabel(rl_name) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(a_email_id) REFERENCES Artist(a_email_id)   ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS listens_to_song (\n" +
        " u_email_id                         VARCHAR(128),\n" +
        " s_id                               INT,\n" +
        " ls_date                            DATE         ,\n" +
        " ls_play_count                      INT          DEFAULT 1,\n" +
        " ls_royalty_paid_status              BOOLEAN      DEFAULT FALSE,\n" +
        " PRIMARY KEY(u_email_id, s_id, ls_date),\n" +
        " FOREIGN KEY(u_email_id) REFERENCES User(u_email_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(s_id)       REFERENCES Song(s_id)       ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS sings (\n" +
        " s_id                               INT,\n" +
        " a_email_id                         VARCHAR(128),\n" +
        " is_artist_collaborator_for_song    BOOLEAN      NOT NULL,\n" +
        " artist_type_for_song               VARCHAR(128) CHECK (artist_type_for_song IN ('Musician', 'Composer', 'Band')),\n" +
        " PRIMARY KEY(s_id, a_email_id),\n" +
        " FOREIGN KEY(s_id)       REFERENCES Song(s_id)         ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(a_email_id) REFERENCES Artist(a_email_id) ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS assigned_to (\n" +
        " a_email_id                         VARCHAR(128),\n" +
        " l_name                             VARCHAR(128),\n" +
        " PRIMARY KEY(a_email_id, l_name),\n" +
        " FOREIGN KEY(l_name)     REFERENCES Album(l_name)      ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(a_email_id) REFERENCES Artist(a_email_id) ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS listens_to_podcast_episode (\n" +
        " pe_title                           VARCHAR(128),\n" +
        " p_name                             VARCHAR(128),\n" +
        " u_email_id                         VARCHAR(128),\n" +
        " lpe_date                           DATE,\n" +
        " lpe_play_count                     INT          DEFAULT 1,\n" +
        " PRIMARY KEY(p_name, pe_title, u_email_id, lpe_date),\n" +
        " INDEX f1(p_name, pe_title), INDEX f2(u_email_id)," +
        " FOREIGN KEY(p_name,pe_title) REFERENCES PodcastEpisode(p_name,pe_title) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(u_email_id)      REFERENCES User(u_email_id)                ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS features (\n" +
        " pe_title                           VARCHAR(128),\n" +
        " p_name                             VARCHAR(128),\n" +
        " g_email_id                         VARCHAR(128),\n" +
        " PRIMARY KEY(p_name, pe_title, g_email_id),\n" +
        " INDEX f1(p_name, pe_title),INDEX f2(g_email_id)," +
        " FOREIGN KEY(p_name, pe_title) REFERENCES PodcastEpisode(p_name, pe_title) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(g_email_id)       REFERENCES SpecialGuest(g_email_id)         ON UPDATE CASCADE ON DELETE CASCADE \n" +
        ");"
      );
    } finally {
      close(statement);
    }
  }

  protected static void insertIntoTables(Connection connection)
    throws SQLException {
    Statement statement = null;

    try {
      statement = connection.createStatement();

      // creating user table
      statement.executeUpdate(
        "INSERT INTO BillingService (bs_date, bs_revenue) VALUES\n" +
        " ('2022-01-01', 100.00),\n" +
        " ('2022-02-01', 200.00),\n" +
        " ('2022-03-01', 300.00),\n" +
        " ('2022-04-01', 400.00),\n" +
        " ('2022-05-01', 500.00),\n" +
        " ('2022-06-01', 600.00),\n" +
        " ('2022-07-01', 700.00),\n" +
        " ('2022-08-01', 800.00);"
      );

      statement.executeUpdate(
        "INSERT INTO User (u_email_id, u_first_name, u_last_name, u_reg_date, u_subscription_status, u_phone) VALUES\n" +
        " ('user1@example.com', 'User1', 'A', '2022-01-01', 'ACTIVE', 9182736450),\n" +
        " ('user2@example.com', 'User2', 'B', '2022-02-01', 'ACTIVE', NULL),\n" +
        " ('user3@example.com', 'User3', 'C', '2022-03-01', 'INACTIVE', 9218364750),\n" +
        " ('user4@example.com', 'User4', 'D', '2022-04-01', 'ACTIVE', 8923716450),\n" +
        " ('user5@example.com', 'User5', 'E', '2022-05-01', 'ACTIVE', NULL),\n" +
        " ('user6@example.com', 'User6', 'F', '2022-06-01', 'ACTIVE', 9517283406),\n" +
        " ('user7@example.com', 'User7', 'G', '2022-07-01', 'INACTIVE', NULL);"
      );

      statement.executeUpdate(
        "INSERT INTO RecordLabel (rl_name) VALUES\n" +
        " ('Universal Music Group'),\n" +
        " ('Sony Music Entertainment'),\n" +
        " ('Warner Music Group'),\n" +
        " ('EMI');"
      );

      statement.executeUpdate(
        "INSERT INTO Artist (a_email_id, a_name, a_status, a_country, a_primary_genre, rl_name) VALUES \n" +
        " ('artist1@example.com', 'Artist A', 'ACTIVE', 'USA', 'Pop', 'Universal Music Group'),\n" +
        " ('artist2@example.com', 'Artist B', 'ACTIVE', 'Canada', 'Rock', 'Sony Music Entertainment'),\n" +
        " ('artist3@example.com', 'Artist C', 'ACTIVE', 'Hong Kong', 'Hip Hop', 'Warner Music Group'),\n" +
        " ('artist4@example.com', 'Artist D', 'ACTIVE', 'USA', 'Country', 'EMI');"
      );

      statement.executeUpdate(
        "INSERT INTO Album (l_name, l_release_year, l_edition) VALUES\n" +
        " ('Thriller', '1982-11-30', 'Special'),\n" +
        " ('Back in Black', '1980-07-25', 'Special'),\n" +
        " ('The Dark Side of the Moon', '1973-03-01', 'Limited'),\n" +
        " ('Appetite for Destruction', '1987-07-21', 'Collector'),\n" +
        " ('Nevermind', '1991-09-24', 'Special'),\n" +
        " ('Led Zeppelin IV', '1971-11-08', 'Collector'),\n" +
        " ('The Joshua Tree', '1987-03-09', 'Limited');"
      );

      statement.executeUpdate(
        "INSERT INTO Song (s_id, s_title, s_royalty_rate, l_name, track_number, s_duration, s_release_date, s_country, s_language) VALUES \n" +
        " (1, 'Song 1', 0.5, 'Thriller', 1, '00:03:45', '2022-01-01', 'USA', 'English'),\n" +
        " (2, 'Song 2', 0.4, 'Back in Black', 2, '00:04:10', '2021-12-15', 'Canada', 'English'),\n" +
        " (3, 'Song 3', 0.3, 'The Dark Side of the Moon', 3, '00:03:30', '2022-02-28', 'UK', 'English'),\n" +
        " (4, 'Song 4', 0.2, 'Appetite for Destruction', 4, '00:02:50', '2022-03-05', 'Australia', 'English'),\n" +
        " (5, 'Song 5', 0.1, 'Nevermind', 5, '00:05:15', '2022-01-15', 'USA', 'English'),\n" +
        " (6, 'Song 6', 0.2, 'Led Zeppelin IV', 6, '00:03:55', '2022-02-01', 'Canada', 'French'),\n" +
        " (7, 'Song 7', 0.3, 'The Joshua Tree', 7, '00:04:20', '2022-03-10', 'UK', 'English');"
      );

      statement.executeUpdate(
        "INSERT INTO SongGenre (s_id, sg_genre) VALUES \n" +
        " (1, 'Funk'),\n" +
        " (1, 'Pop'),\n" +
        " (2, 'Hard Rock'),\n" +
        " (2, 'Heavy Metal'),\n" +
        " (3, 'Rock'),\n" +
        " (4, 'Hard Rock'),\n" +
        " (5, 'Grunge'),\n" +
        " (6, 'Funk'),\n" +
        " (7, 'Rock');"
      );

      statement.executeUpdate(
        "INSERT INTO Podcast (p_name, p_sponsor, p_language, p_country, p_rating, p_rated_user_count) VALUES\n" +
        " ('The Joe Rogan Experience', 5.00, 'English', 'USA', 5, 2),\n" +
        " ('How I Built This', 2.00, 'English', 'USA', 4, 1),\n" +
        " ('Revisionist History', 15.00, 'English', 'USA', 4, 5),\n" +
        " ('The Tim Ferriss Show', 10.00, 'English', 'USA', 4, 2),\n" +
        " ('My Favorite Murder', 20.00, 'English', 'USA', 4, 3);"
      );

      statement.executeUpdate(
        "INSERT INTO PodcastGenre (p_name, pg_genre) VALUES \n" +
        " ('The Joe Rogan Experience', 'General Interest'),\n" +
        " ('The Joe Rogan Experience', 'Variety'),\n" +
        " ('How I Built This', 'Business'),\n" +
        " ('How I Built This', 'Entrepreneurship'),\n" +
        " ('Revisionist History', 'History'),\n" +
        " ('The Tim Ferriss Show', 'Business'),\n" +
        " ('My Favorite Murder', 'True Crime'),\n" +
        " ('My Favorite Murder', 'Comedy');"
      );

      statement.executeUpdate(
        "INSERT INTO PodcastHost (ph_email_id, ph_first_name, ph_last_name, ph_phone, ph_city) VALUES \n" +
        " ('host1@example.com', 'John', 'Doe', 1234567890, 'New York'),\n" +
        " ('host2@example.com', 'Jane', 'Doe', 2345678901, 'San Francisco'),\n" +
        " ('host3@example.com', 'Bob', 'Smith', 3456789012, 'Chicago'),\n" +
        " ('host4@example.com', 'Alice', 'Johnson', 4567890123, 'Los Angeles'),\n" +
        " ('host5@example.com', 'Sam', 'Wilson', 5678901234, 'Miami');"
      );

      statement.executeUpdate(
        "INSERT INTO PodcastEpisode (pe_title, p_name, pe_release_date, ph_email_id, pe_ad_count, pe_duration) VALUES \n" +
        " ('Episode 1', 'The Joe Rogan Experience', '2022-01-01', 'host1@example.com', 3, '2:30:00'),\n" +
        " ('Episode 2', 'The Joe Rogan Experience', '2022-01-04', 'host1@example.com', 4, '1:15:00'),\n" +
        " ('Episode 3', 'The Joe Rogan Experience', '2022-01-05', 'host2@example.com', 3, '1:30:00'),\n" +
        " ('Episode 1', 'How I Built This', '2022-01-06', 'host3@example.com', 2, '1:00:00'),\n" +
        " ('Episode 1', 'Revisionist History', '2022-01-07', 'host4@example.com', 1, '45:00'),\n" +
        " ('Episode 1', 'The Tim Ferriss Show', '2022-01-08', 'host5@example.com', 4, '1:30:00'),\n" +
        " ('Episode 1', 'My Favorite Murder', '2022-01-09', 'host5@example.com', 2, '45:00');"
      );

      statement.executeUpdate(
        "INSERT INTO SpecialGuest (g_email_id, g_name) VALUES \n" +
        " ('guest1@example.com', 'Guest A'),\n" +
        " ('guest2@example.com', 'Guest B'),\n" +
        " ('guest3@example.com', 'Guest C'),\n" +
        " ('guest4@example.com', 'Guest D'),\n" +
        " ('guest5@example.com', 'Guest E'),\n" +
        " ('guest6@example.com', 'Guest F');"
      );

      statement.executeUpdate(
        "INSERT INTO pays_to (up_fee_for_subscription, up_date, bs_date, u_email_id) VALUES\n" +
        " (100, '2022-02-01', '2022-01-01', 'user1@example.com'),\n" +
        " (100, '2022-03-01', '2022-02-01', 'user2@example.com'),\n" +
        " (100, '2022-04-01', '2022-03-01', 'user3@example.com'),\n" +
        " (100, '2022-05-01', '2022-04-01', 'user4@example.com'),\n" +
        " (100, '2022-06-01', '2022-05-01', 'user5@example.com'),\n" +
        " (100, '2022-07-01', '2022-06-01', 'user6@example.com'),\n" +
        " (100, '2022-08-01', '2022-07-01', 'user7@example.com');"
      );

      statement.executeUpdate(
        "INSERT INTO pays_to_host (pfh_amount, pfh_date, bs_date, ph_email_id) VALUES\n" +
        " (150.00, '2022-02-01', '2022-01-01', 'host1@example.com'),\n" +
        " (100.00, '2022-03-01', '2022-02-01', 'host2@example.com'),\n" +
        " (150.00, '2022-04-01', '2022-03-01', 'host3@example.com'),\n" +
        " (200.00, '2022-05-01', '2022-04-01', 'host4@example.com'),\n" +
        " (250.00, '2022-06-01', '2022-05-01', 'host5@example.com'),\n" +
        " (100.00, '2022-03-01', '2022-02-01', 'host1@example.com'),\n" +
        " (150.00, '2022-04-01', '2022-03-01', 'host2@example.com');"
      );

      statement.executeUpdate(
        "INSERT INTO pays_to_record_label (pfs_amount, pfs_date, bs_date, rl_name) VALUES\n" +
        " (50.00, '2022-02-01', '2022-01-01', 'Universal Music Group'),\n" +
        " (100.00, '2022-03-01', '2022-02-01', 'Sony Music Entertainment'),\n" +
        " (150.00, '2022-04-01', '2022-03-01', 'Warner Music Group'),\n" +
        " (200.00, '2022-05-01', '2022-04-01', 'EMI');"
      );

      statement.executeUpdate(
        "INSERT INTO pays_to_artist (pfa_amount, pfa_date, rl_name, a_email_id) VALUES\n" +
        " (100, '2022-02-01', 'Universal Music Group', 'artist1@example.com'),\n" +
        " (200, '2022-03-01', 'Sony Music Entertainment', 'artist2@example.com'),\n" +
        " (300, '2022-04-01', 'Warner Music Group', 'artist3@example.com'),\n" +
        " (250, '2022-05-01', 'Warner Music Group', 'artist3@example.com'),\n" +
        " (150, '2022-06-01', 'Warner Music Group', 'artist3@example.com'),\n" +
        " (200, '2022-06-01', 'EMI', 'artist4@example.com');"
      );

      statement.executeUpdate(
        "INSERT INTO listens_to_song (s_id, u_email_id, ls_date, ls_play_count, ls_royalty_paid_status) VALUES\n"+
        " (1, 'user1@example.com', '2022-01-01', 21, FALSE),\n"+
        " (1, 'user1@example.com', '2022-01-02', 10, TRUE),\n"+
        " (1, 'user1@example.com', '2022-02-01', 1, TRUE),\n"+
        " (2, 'user2@example.com', '2022-01-01', 30, FALSE),\n"+
        " (3, 'user4@example.com', '2022-01-02', 25, FALSE),\n"+
        " (4, 'user4@example.com', '2022-01-02', 25, TRUE),\n"+
        " (4, 'user7@example.com', '2022-01-02', 20);"
      );

      statement.executeUpdate(
        "INSERT INTO sings (s_id, is_artist_collaborator_for_song, artist_type_for_song, a_email_id) VALUES\n" +
        " (1, false, 'Musician', 'artist1@example.com'),\n" +
        " (2, false, 'Musician', 'artist2@example.com'),\n" +
        " (3, false, 'Band', 'artist3@example.com'),\n" +
        " (3, true, 'Band', 'artist4@example.com'),\n" +
        " (4, false, 'Musician', 'artist1@example.com'),\n" +
        " (5, false, 'Band', 'artist2@example.com'),\n" +
        " (5, true, 'Band', 'artist3@example.com'),\n" +
        " (6, false, 'Musician', 'artist3@example.com'),\n" +
        " (7, false, 'Musician', 'artist4@example.com');"
      );

      statement.executeUpdate(
        "INSERT INTO assigned_to (a_email_id, l_name) VALUES\n" +
        " ('artist1@example.com', 'Thriller'),\n" +
        " ('artist2@example.com', 'Back in Black'),\n" +
        " ('artist3@example.com', 'The Dark Side of the Moon'),\n" +
        " ('artist4@example.com', 'Appetite for Destruction');"
      );

      statement.executeUpdate(
        "INSERT INTO listens_to_podcast_episode (pe_title, p_name, u_email_id, lpe_date, lpe_play_count) VALUES \n" +
        " ('Episode 1', 'The Joe Rogan Experience', 'user1@example.com', '2022-01-01', 25),\n" +
        " ('Episode 1', 'The Joe Rogan Experience', 'user2@example.com', '2022-01-02', 15),\n" +
        " ('Episode 2', 'The Joe Rogan Experience', 'user1@example.com', '2022-01-03', 5),\n" +
        " ('Episode 2', 'The Joe Rogan Experience', 'user4@example.com', '2022-01-04', 35),\n" +
        " ('Episode 3', 'The Joe Rogan Experience', 'user5@example.com', '2022-01-05', 25),\n" +
        " ('Episode 1', 'How I Built This', 'user6@example.com', '2022-01-07', 25),\n" +
        " ('Episode 1', 'Revisionist History', 'user6@example.com', '2022-01-09', 25);"
      );

      statement.executeUpdate(
        "INSERT INTO features (pe_title, p_name, g_email_id) VALUES\n" +
        " ('Episode 1', 'The Joe Rogan Experience', 'guest1@example.com'),\n" +
        " ('Episode 2', 'The Joe Rogan Experience', 'guest1@example.com'),\n" +
        " ('Episode 3', 'The Joe Rogan Experience', 'guest2@example.com'),\n" +
        " ('Episode 1', 'How I Built This', 'guest3@example.com'),\n" +
        " ('Episode 1', 'Revisionist History', 'guest4@example.com'),\n" +
        " ('Episode 1', 'The Tim Ferriss Show', 'guest5@example.com'),\n" +
        " ('Episode 1', 'My Favorite Murder', 'guest6@example.com');"
      );
    } finally {
      close(statement);
    }
  }

  protected static void clearDatabase(Connection connection) {
    String[] tableNames = new String[] {
      "features",
      "listens_to_podcast_episode",
      "assigned_to",
      "sings",
      "listens_to_song",
      "pays_to_artist",
      "pays_to_record_label",
      "pays_to_host",
      "pays_to",
      "SpecialGuest",
      "PodcastEpisode",
      "PodcastHost",
      "PodcastGenre",
      "Podcast",
      "SongGenre",
      "Song",
      "Album",
      "User",
      "Artist",
      "RecordLabel",
      "BillingService"
    };

    for (String tableName : tableNames) {
      Statement statement = null;
      try {
        statement = connection.createStatement();
        statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s;", tableName));
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(
          String.format("%s did not exist to delete.", tableName)
        );
      } finally {
        close(statement);
      }
    }
  }

  private static void close(Statement statement) {
  if (statement != null) {
      try {
        statement.close();
      } catch (Throwable whatever) {}
    }
  }
}
