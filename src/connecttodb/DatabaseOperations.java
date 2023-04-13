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
        "CREATE TABLE IF NOT EXISTS Genre (\n" +
        " genre                              VARCHAR(128) NOT NULL,\n" +
        " PRIMARY KEY(genre)\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Artist (\n" +
        " a_email_id                         VARCHAR(128) PRIMARY KEY,\n" +
        " a_name                             VARCHAR(128) ,\n" +
        " a_status                           VARCHAR(128) DEFAULT 'ACTIVE',\n" +
        " a_country                          VARCHAR(128),\n" +
        " ag_genre                           VARCHAR(128) NOT NULL,\n" +
        " rl_name                            VARCHAR(128) NOT NULL,\n" +
        " FOREIGN KEY(rl_name)  REFERENCES RecordLabel(rl_name) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(ag_genre) REFERENCES Genre(genre)         ON UPDATE CASCADE ON DELETE CASCADE" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Album (\n" +
        " l_name                             VARCHAR(128) PRIMARY KEY,\n" +
        " l_release_year                     DATE ,\n" +
        " l_edition                          VARCHAR(128) CHECK (l_edition IN ('Special', 'Limited', 'Collector'))\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Song (\n" +
        " s_id                               VARCHAR(128) PRIMARY KEY,\n" +
        " s_title                            VARCHAR(128) ,\n" +
        " s_royalty_rate                     DECIMAL(9,2) ,\n" +
        " s_duration                         TIME         ,\n" +
        " s_release_date                     DATE         ,\n" +
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
        "CREATE TABLE IF NOT EXISTS song_genre (\n" +
        " s_id                               VARCHAR(128),\n" +
        " sg_genre                           VARCHAR(128) NOT NULL,\n" +
        " PRIMARY KEY(s_id, sg_genre),\n" +
        " FOREIGN KEY(s_id)     REFERENCES Song(s_id)   ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(sg_genre) REFERENCES Genre(genre) ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS Podcast (\n" +
        " p_name                             VARCHAR(128) PRIMARY KEY,\n" +
        " p_sponsor                          DECIMAL(9,2) DEFAULT 0,\n" +
        " p_language                         VARCHAR(128) NOT NULL,\n" +
        " p_country                          VARCHAR(128),\n" +
        " p_rating                           FLOAT,\n" +
        " p_episode_flat_fee                 FLOAT,\n" +
        " p_episode_count                    INT          DEFAULT 0,\n" +
        " p_rated_user_count                 INT          DEFAULT 0\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS podcast_genre (\n" +
        " p_name                             VARCHAR(128),\n" +
        " pg_genre                           VARCHAR(128) NOT NULL,\n" +
        " PRIMARY KEY(p_name, pg_genre),\n" +
        " FOREIGN KEY(p_name)   REFERENCES Podcast(p_name) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(pg_genre) REFERENCES Genre(genre)    ON UPDATE CASCADE ON DELETE CASCADE\n" +
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
        " s_id                               VARCHAR(128),\n" +
        " ls_date                            DATE         ,\n" +
        " ls_play_count                      INT          DEFAULT 1,\n" +
        " ls_royalty_paid_status             BOOLEAN      DEFAULT FALSE,\n" +
        " PRIMARY KEY(u_email_id, s_id, ls_date),\n" +
        " FOREIGN KEY(u_email_id) REFERENCES User(u_email_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
        " FOREIGN KEY(s_id)       REFERENCES Song(s_id)       ON UPDATE CASCADE ON DELETE CASCADE\n" +
        ");"
      );

      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS sings (\n" +
        " s_id                               VARCHAR(128),\n" +
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
        " ('2023-01-01', 1111),\n" +
        " ('2023-02-01', 2222),\n" +
        " ('2023-03-01', 3333),\n" +
        " ('2023-04-01', 123000);"
      );

      statement.executeUpdate(
        "INSERT INTO User (u_email_id, u_first_name, u_last_name, u_reg_date, u_subscription_status, u_phone) VALUES\n" +
        " ('u8001@example.com', 'Alex', 'A', '2023-01-01', 'ACTIVE', 9182736450),\n" +
        " ('u8002@example.com', 'John', 'J', '2023-01-01', 'ACTIVE', NULL);"
      );

      statement.executeUpdate(
        "INSERT INTO RecordLabel (rl_name) VALUES\n" +
        " ('Elevate Records'),\n" +
        " ('Melodic Avenue Music');"
      );

      statement.executeUpdate(
        "INSERT INTO Genre (genre) VALUES \n" +
        " ('Pop'),\n" +
        " ('Hard Rock'),\n" +
        " ('Grunge'),\n" +
        " ('Rock'),\n" +
        " ('Funk'),\n" +
        " ('Motivation'),\n" +
        " ('Variety');"
      );

      statement.executeUpdate(
        "INSERT INTO Artist (a_email_id, a_name, a_status, a_country, rl_name, ag_genre) VALUES \n" +
        " ('ar2001@example.com', 'Forest', 'ACTIVE', 'USA', 'Elevate Records', 'Pop'),\n" +
        " ('ar2002@example.com', 'Rain', 'ACTIVE', 'Canada', 'Melodic Avenue Music', 'Rock');"
      );

      statement.executeUpdate(
        "INSERT INTO Album (l_name, l_release_year, l_edition) VALUES\n" +
        " ('Electric Oasis', '1971-11-08', 'Collector'),\n" +
        " ('Lost in the Echoes', '1987-03-09', 'Limited');"
      );

      statement.executeUpdate(
        "INSERT INTO Song (s_id, s_title, s_royalty_rate, l_name, track_number, s_duration, s_release_date, s_country, s_language) VALUES \n" +
        " ('s1001', 'Electric Dreamscape', 0.1, 'Electric Oasis', 6, '00:03:55', '2020-02-01', 'Canada', 'English'),\n" +
        " ('s1002', 'Midnight Mirage', 0.1, 'Electric Oasis', 6, '00:03:55', '2020-02-10', 'Canada', 'English'),\n" +
        " ('s1003', 'Echoes of You', 0.1, 'Lost in the Echoes', 7, '00:04:20', '2018-03-10', 'UK', 'English'),\n" +
        " ('s1004', 'Rainy Nights', 0.1, 'Lost in the Echoes', 7, '00:04:20', '2019-03-10', 'UK', 'English');"
      );

      statement.executeUpdate(
        "INSERT INTO song_genre (s_id, sg_genre) VALUES \n" +
        " ('s1001', 'Hard Rock'),\n" +
        " ('s1002', 'Grunge'),\n" +
        " ('s1003', 'Funk'),\n" +
        " ('s1004', 'Rock');"
      );

      statement.executeUpdate(
        "INSERT INTO Podcast (p_name, p_sponsor, p_language, p_country, p_rating, p_rated_user_count, p_episode_flat_fee, p_episode_count) VALUES\n" +
        " ('Mind Over Matter: Exploring the Power of the Human Mind', 0.00, 'English', 'USA', 4.5, 2, 10, 5);"
      );

      statement.executeUpdate(
        "INSERT INTO podcast_genre (p_name, pg_genre) VALUES \n" +
        " ('Mind Over Matter: Exploring the Power of the Human Mind', 'Variety');"
      );

      statement.executeUpdate(
        "INSERT INTO PodcastHost (ph_email_id, ph_first_name, ph_last_name, ph_phone, ph_city) VALUES \n" +
        " ('ph6001@example.com', 'Matthew', 'Wilson', 1234567890, 'New York');"
      );

      statement.executeUpdate(
        "INSERT INTO PodcastEpisode (pe_title, p_name, pe_release_date, ph_email_id, pe_ad_count, pe_duration) VALUES \n" +
        " ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', '2023-01-01', 'ph6001@example.com', 0, '2:30:00'),\n" +
        " ('Unlocking Your Potential', 'Mind Over Matter: Exploring the Power of the Human Mind', '2023-01-04', 'ph6001@example.com', 0, '1:15:00');"
      );

       statement.executeUpdate(
         "INSERT INTO pays_to (up_fee_for_subscription, up_date, bs_date, u_email_id) VALUES\n" +
         " (100, '2023-01-01', '2023-02-01', 'u8001@example.com'),\n" +
         " (100, '2023-01-01', '2023-02-01', 'u8002@example.com');"
       );

    //  statement.executeUpdate(
    //    "INSERT INTO pays_to_host (pfh_amount, pfh_date, bs_date, ph_email_id) VALUES\n" +
    //    "  (20.00, '2023-02-01', '2023-01-01', 'ph6001@example.com'),\n" +
    //    "  (30.00, '2023-03-01', '2023-02-01', 'ph6001@example.com'),\n" +
    //    "  (40.00, '2023-04-01', '2023-03-01', 'ph6001@example.com');"
    //  );

    //   statement.executeUpdate(
    //     "INSERT INTO pays_to_record_label (pfs_amount, pfs_date, bs_date, rl_name) VALUES\n" +
    //     "  (3.3, '2023-02-01', '2023-01-01', 'Elevate Records'),\n" +
    //     "  (6.6, '2023-03-01', '2023-02-01', 'Elevate Records'),\n" +
    //     "  (9.9, '2023-04-01', '2023-03-01', 'Elevate Records'),\n" +
    //     "  (330.00, '2023-02-01', '2023-01-01', 'Melodic Avenue Music'),\n" +
    //     "  (660.00, '2023-03-01', '2023-02-01', 'Melodic Avenue Music'),\n" +
    //     "  (990.00, '2023-04-01', '2023-03-01', 'Melodic Avenue Music');"
    //   );

    //   statement.executeUpdate(
    //     "INSERT INTO pays_to_artist (pfa_amount, pfa_date, rl_name, a_email_id) VALUES\n" +
    //     "  (4.2, '2023-02-01', 'Elevate Records', 'ar2001@example.com'),\n" +
    //     "  (8.4, '2023-03-01', 'Melodic Avenue Music', 'ar2002@example.com'),\n" +
    //     "  (12.6, '2023-04-01', 'Elevate Records', 'ar2001@example.com'),\n" +
    //     "  (703.5, '2023-02-01', 'Melodic Avenue Music', 'ar2002@example.com'),\n" +
    //     "  (1547, '2023-03-01', 'Elevate Records', 'ar2001@example.com'),\n" +
    //     "  (2320.5, '2023-04-01', 'Melodic Avenue Music', 'ar2002@example.com');"
    //   );

    //   statement.executeUpdate(
    //       "INSERT INTO listens_to_podcast_episode (pe_title, p_name, u_email_id, lpe_date, lpe_play_count) VALUES\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-01-01', 5),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8002@example.com', '2023-01-02', 5),\n" +
    //     "  ('Unlocking Your Potential', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-01-03', 20),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-02-01', 10),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8002@example.com', '2023-02-02', 10),\n" +
    //     "  ('Unlocking Your Potential', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-02-03', 40),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-03-01', 15),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8002@example.com', '2023-03-02', 15),\n" +
    //     "  ('Unlocking Your Potential', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-03-03', 30),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-04-01', 50),\n" +
    //     "  ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8002@example.com', '2023-04-02', 50),\n" +
    //     "  ('Unlocking Your Potential', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-04-03', 200);"
    //   );

      statement.executeUpdate(
        "INSERT INTO listens_to_song (s_id, u_email_id, ls_date, ls_play_count, ls_royalty_paid_status) VALUES\n"+
        " ('s1001', 'u8001@example.com', '2023-01-01', 1, FALSE),\n"+
        " ('s1001', 'u8002@example.com', '2023-01-01', 1, FALSE),\n"+
        " ('s1001', 'u8001@example.com', '2023-01-10', 2, FALSE),\n"+
        " ('s1001', 'u8001@example.com', '2023-01-12', 3, FALSE),\n"+
        " ('s1001', 'u8001@example.com', '2023-01-13', 1, FALSE),\n"+
        " ('s1001', 'u8002@example.com', '2023-01-30', 2, FALSE),\n"+

        " ('s1001', 'u8001@example.com', '2023-02-02', 10, FALSE),\n"+
        " ('s1001', 'u8002@example.com', '2023-02-25', 10, FALSE),\n"+

        " ('s1001', 'u8001@example.com', '2023-03-10', 10, FALSE),\n"+
        " ('s1001', 'u8002@example.com', '2023-03-11', 10, FALSE),\n"+
        " ('s1001', 'u8001@example.com', '2023-03-12', 10, FALSE),\n"+

        " ('s1001', 'u8001@example.com', '2023-04-01', 100, FALSE),\n"+
        " ('s1001', 'u8001@example.com', '2023-04-02', 100, FALSE),\n"+
        " ('s1001', 'u8001@example.com', '2023-04-03', 100, FALSE),\n"+
        " ('s1001', 'u8002@example.com', '2023-04-04', 100, FALSE),\n"+
        " ('s1001', 'u8002@example.com', '2023-04-05', 100, FALSE),\n"+

        " ('s1002', 'u8001@example.com', '2023-01-02', 10, FALSE),\n"+
        " ('s1002', 'u8002@example.com', '2023-01-02', 10, FALSE),\n"+
        " ('s1002', 'u8001@example.com', '2023-01-11', 20, FALSE),\n"+
        " ('s1002', 'u8001@example.com', '2023-01-14', 30, FALSE),\n"+
        " ('s1002', 'u8001@example.com', '2023-01-15', 10, FALSE),\n"+
        " ('s1002', 'u8002@example.com', '2023-01-29', 20, FALSE),\n"+

        " ('s1002', 'u8001@example.com', '2023-02-03', 100, FALSE),\n"+
        " ('s1002', 'u8002@example.com', '2023-02-26', 100, FALSE),\n"+

        " ('s1002', 'u8001@example.com', '2023-03-13', 100, FALSE),\n"+
        " ('s1002', 'u8002@example.com', '2023-03-14', 100, FALSE),\n"+
        " ('s1002', 'u8001@example.com', '2023-03-15', 100, FALSE),\n"+

        " ('s1002', 'u8001@example.com', '2023-04-04', 200, FALSE),\n"+
        " ('s1002', 'u8001@example.com', '2023-04-05', 200, FALSE),\n"+
        " ('s1002', 'u8001@example.com', '2023-04-06', 200, FALSE),\n"+
        " ('s1002', 'u8002@example.com', '2023-04-01', 200, FALSE),\n"+
        " ('s1002', 'u8002@example.com', '2023-04-02', 200, FALSE),\n"+

        " ('s1003', 'u8001@example.com', '2023-01-03', 100, FALSE),\n"+
        " ('s1003', 'u8002@example.com', '2023-01-03', 100, FALSE),\n"+
        " ('s1003', 'u8001@example.com', '2023-01-16', 200, FALSE),\n"+
        " ('s1003', 'u8001@example.com', '2023-01-17', 300, FALSE),\n"+
        " ('s1003', 'u8001@example.com', '2023-01-18', 100, FALSE),\n"+
        " ('s1003', 'u8002@example.com', '2023-01-20', 200, FALSE),\n"+

        " ('s1003', 'u8001@example.com', '2023-02-04', 1000, FALSE),\n"+
        " ('s1003', 'u8002@example.com', '2023-02-27', 1000, FALSE),\n"+

        " ('s1003', 'u8001@example.com', '2023-03-05', 1000, FALSE),\n"+
        " ('s1003', 'u8002@example.com', '2023-03-06', 1000, FALSE),\n"+
        " ('s1003', 'u8001@example.com', '2023-03-07', 1000, FALSE),\n"+

        " ('s1003', 'u8001@example.com', '2023-04-01', 20, FALSE),\n"+
        " ('s1003', 'u8001@example.com', '2023-04-02', 20, FALSE),\n"+
        " ('s1003', 'u8001@example.com', '2023-04-03', 20, FALSE),\n"+
        " ('s1003', 'u8002@example.com', '2023-04-04', 20, FALSE),\n"+
        " ('s1003', 'u8002@example.com', '2023-04-05', 20, FALSE),\n"+

        " ('s1004', 'u8001@example.com', '2023-01-01', 1000, FALSE),\n"+
        " ('s1004', 'u8002@example.com', '2023-01-01', 1000, FALSE),\n"+
        " ('s1004', 'u8001@example.com', '2023-01-10', 2000, FALSE),\n"+
        " ('s1004', 'u8001@example.com', '2023-01-12', 3000, FALSE),\n"+
        " ('s1004', 'u8001@example.com', '2023-01-13', 1000, FALSE),\n"+
        " ('s1004', 'u8002@example.com', '2023-01-30', 2000, FALSE),\n"+

        " ('s1004', 'u8001@example.com', '2023-02-02', 10000, FALSE),\n"+
        " ('s1004', 'u8002@example.com', '2023-02-25', 10000, FALSE),\n"+

        " ('s1004', 'u8001@example.com', '2023-03-10', 10000, FALSE),\n"+
        " ('s1004', 'u8002@example.com', '2023-03-11', 10000, FALSE),\n"+
        " ('s1004', 'u8001@example.com', '2023-03-12', 10000, FALSE),\n"+

        " ('s1004', 'u8001@example.com', '2023-04-01', 20, FALSE),\n"+
        " ('s1004', 'u8001@example.com', '2023-04-02', 20, FALSE),\n"+
        " ('s1004', 'u8001@example.com', '2023-04-03', 20, FALSE),\n"+
        " ('s1004', 'u8002@example.com', '2023-04-04', 20, FALSE),\n"+
        " ('s1004', 'u8002@example.com', '2023-04-05', 20, FALSE);"
      );

      statement.executeUpdate(
        "INSERT INTO sings (s_id, is_artist_collaborator_for_song, artist_type_for_song, a_email_id) VALUES\n" +
        " ('s1001', false, 'Musician', 'ar2001@example.com'),\n" +
        " ('s1002', false, 'Band', 'ar2001@example.com'),\n" +
        " ('s1002', true, 'Band', 'ar2002@example.com'),\n" +
        " ('s1003', false, 'Musician', 'ar2002@example.com'),\n" +
        " ('s1004', false, 'Musician', 'ar2002@example.com');"
      );

      statement.executeUpdate(
        "INSERT INTO assigned_to (a_email_id, l_name) VALUES\n" +
        " ('ar2001@example.com', 'Electric Oasis'),\n" +
        " ('ar2002@example.com', 'Lost in the Echoes');"
      );

      statement.executeUpdate(
        "INSERT INTO listens_to_podcast_episode (pe_title, p_name, u_email_id, lpe_date, lpe_play_count) VALUES \n" +
        " ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-01-01', 50),\n" +
        " ('The Science of Mindfulness', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8002@example.com', '2023-01-02', 50),\n" +
        " ('Unlocking Your Potential', 'Mind Over Matter: Exploring the Power of the Human Mind', 'u8001@example.com', '2023-01-03', 200);"
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
      "podcast_genre",
      "Podcast",
      "song_genre",
      "Song",
      "Album",
      "User",
      "Artist",
      "Genre",
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