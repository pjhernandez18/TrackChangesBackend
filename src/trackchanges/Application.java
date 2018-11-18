package trackchanges;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
	
	private static final long serialVersionUID = 1L;
	
	// Variables:
	/*
	 * This contains the function will store the global url we will 
	 * use to connect to the SQL database that is storing all the data 
	 * necessary for the application, for example:  
	 * “jdbc:mysql://localhost:3306/CalendarApp?user=root&password=&useSSL=false”;
	 */
	private static final String DATABASE_CONNECTION_URL = "jdbc:mysql://localhost:3306/CSCI201ProjectDatabase?user=root&password=&useSSL=false";
	
	// Functions:
	/*
	 * This function will be responsible for adding new users into 
	 * the database with “INSERT” statements after a connection using 
	 * the JDBC DriverManager is established. Insertion will also be 
	 * surrounded by Try, Catch blocks to ensure a minimum level of 
	 * error handling. Function will return “True” if user is successfully 
	 * added and “False” otherwise.
	 */
	public boolean addUser(User newUser) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"INSERT INTO User ("
					+ "user_id, "
					+ "user_displayname, "
					+ "user_logintimestamp, "
					+ "user_imageurl) VALUES ('" 
					+ newUser.getUserId() + "', '"
					+ newUser.getUserDisplayName() + "', '"
					+ newUser.getUserLoginTimeStamp() + "', '"
					+ newUser.getUserImageUrl() + "');");
			result = ps.execute();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// You always need to close the connection to the database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println("sqle closing error: " + sqle.getMessage());
			}
		}
		return result;
	}
	
	/*
	 * This function will be responsible for updating an existing user 
	 * in the database with “UPDATE” statements after a connection using 
	 * the JDBC DriverManager is established. Update will also be surrounded 
	 * by Try, Catch blocks to ensure a minimum level of error handling. 
	 * Function will return “True” if user is successfully updated and “False” otherwise.
	 */
	private boolean updateUser(User user) {
		return true;
	}
	
	/*
	 * This function will be responsible for deactivating existing users 
	 * in the database by setting “user_is_active” to “False” after a 
	 * connection using the JDBC DriverManager is established. 
	 * Deactivation will also be surrounded by Try, Catch blocks to ensure a 
	 * minimum level of error handling. Function will return “True” if user 
	 * is successfully deactivated and “False” otherwise.
	 */
	private boolean deactivateUser(String user_id) {
		return true;
	}
	
	/*
	 * This function will be responsible for deleting existing users from 
	 * the database permanently using the “DELETE” statement after a connection 
	 * using the JDBC DriverManager is established. Deletion will also be 
	 * surrounded by Try, Catch blocks to ensure a minimum level of error handling. 
	 * Function will return “True” if user is successfully deleted and “False” otherwise.
	 */
	private boolean deleteUser(String user_id) {
		return true;
	}

	/*
	 * This function will be responsible for adding new artists into the database 
	 * with “INSERT” statements after a connection using the JDBC DriverManager 
	 * is established. Insertion will also be surrounded by Try, Catch blocks to 
	 * ensure a minimum level of error handling. Function will return “True” if 
	 * user is successfully added and “False” otherwise.
	 */
	private boolean addArtist(Artist newArtist) {
		return true;
	}

	/*
	 * This function will be responsible for updating an existing artist in 
	 * the database with “UPDATE” statements after a connection using the JDBC 
	 * DriverManager is established. Update will also be surrounded by Try, 
	 * Catch blocks to ensure a minimum level of error handling. Function will 
	 * return “True” if user is successfully updated and “False” otherwise.
	 */
	private boolean updateArtist(Artist artist) {
		return true;
	}

	/*
	 * This function will be responsible for deleting existing artists, along 
	 * with all their albums, songs, and all posts that included those albums 
	 * and songs from the database permanently using the “DELETE” statement after 
	 * a connection using the JDBC DriverManager is established. Deletion will 
	 * also be surrounded by Try, Catch blocks to ensure a minimum level of error 
	 * handling. Function will return “True” if artist is successfully deleted 
	 * and “False” otherwise.
	 */
	private boolean deleteArtist(String artist_id) {
		return true;
	}

	/*
	 * This function will be responsible for adding a new follower relationship 
	 * into the database with “INSERT” statements after a connection using the 
	 * JDBC DriverManager is established. Insertion will also be surrounded by Try, 
	 * Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if follower is successfully added and “False” otherwise.
	 */
	private boolean follow(String user_id, String follower_id) {
		return true;
	}

	/*
	 * This function will be responsible for deleting a follower relationship permanently 
	 * using the “DELETE” statement after a connection using the JDBC DriverManager is 
	 * established. Deletion will also be surrounded by Try, Catch blocks to ensure a 
	 * minimum level of error handling. Function will return “True” if relationship is 
	 * successfully deleted and “False” otherwise.
	 */
	private boolean unfollow(String user_id, String follower_id) {
		return true;
	}

	/*
	 * This function will be responsible for retrieving all the followers of a user 
	 * using the “SELECT” statement after a connection using the JDBC DriverManager 
	 * is established. Deletion will also be surrounded by Try, Catch blocks to ensure 
	 * a minimum level of error handling. Function will return an array of “user_id”(s) 
	 * corresponding to each follower. Size of array will be the number of followers a user has.
	 */
	private String[] getFollowers(String user_id) {
		return null;
	}

	/*
	 * This function will be responsible for retrieving all the users that the current 
	 * user is following using the “SELECT” statement after a connection using the JDBC 
	 * DriverManager is established. Deletion will also be surrounded by Try, Catch blocks 
	 * to ensure a minimum level of error handling. Function will return an array of “user_id”(s) 
	 * corresponding to each user the current user is following. Size of array will be the 
	 * number of users the user specified is following.
	 */
	private String[] getFollowing(String user_id) {
		return null;
	}

	/*
	 * This function will be responsible for adding new albums, album artist(s), and 
	 * all the songs in it  into the database with “INSERT” statements after a connection 
	 * using the JDBC DriverManager is established. Insertion will also be surrounded by 
	 * Try, Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if album is successfully added and “False” otherwise.
	 */
	private boolean addAlbum(Album newAlbum) {
		return true;
	}

	/*
	 * This function will be responsible for deleting existing albums, and all posts that 
	 * included those albums and the songs inside the album from the database permanently 
	 * using the “DELETE” statement after a connection using the JDBC DriverManager is 
	 * established. Deletion will also be surrounded by Try, Catch blocks to ensure a 
	 * minimum level of error handling. Function will return “True” if album is successfully 
	 * deleted and “False” otherwise.
	 */
	private boolean deleteAlbum(String album_id) {
		return true;
	}

	/*
	 * This function will be responsible for adding new songs and its corresponding artist(s), 
	 * as well as the updating the “AlbumSong” relationship (storing the fact that the song 
	 * belongs to the album whose “album_id” is specified) in the database with “INSERT” 
	 * statements after a connection using the JDBC DriverManager is established. Insertion 
	 * will also be surrounded by Try, Catch blocks to ensure a minimum level of error 
	 * handling. Function will return “True” if song is successfully added and “False” otherwise.
	 */
	private boolean addSong(Song newSong, String album_id) {
		return true;
	}

	/*
	 * This function will be responsible for adding new songs and its corresponding artist(s) 
	 * into the database with “INSERT” statements after a connection using the JDBC 
	 * DriverManager is established. Insertion will also be surrounded by Try, Catch 
	 * blocks to ensure a minimum level of error handling. Function will return “True” 
	 * if song is successfully added and “False” otherwise.
	 */
	private boolean addSong(Song newSong) {
		return true;
	}

	/*
	 * This function will be responsible for tracking which users like a particular song 
	 * in the database with “INSERT” statements to the “AlbumSong” table after a connection 
	 * using the JDBC DriverManager is established. Insertion will also be surrounded by 
	 * Try, Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if addition is successful and “False” otherwise.
	 */
	private boolean likeSong(String song_id, String user_id) {
		return true;
	}

	/*
	 * This function will be responsible for deleting the relationship of the user liking 
	 * the song permanently using the “DELETE” statement after a connection using the JDBC 
	 * DriverManager is established. Deletion will also be surrounded by Try, Catch blocks 
	 * to ensure a minimum level of error handling. Function will return “True” if 
	 * relationship is successfully deleted and “False” otherwise.
	 */
	private boolean unlikeSong(String song_id, String user_id) {
		return true;
	}

	/*
	 * This function will be responsible for deleting existing songs, the “AlbumSong” 
	 * relationships,and all posts that included those songs inside the album from the 
	 * database permanently using the “DELETE” statement after a connection using the JDBC 
	 * DriverManager is established. Deletion will also be surrounded by Try, Catch blocks 
	 * to ensure a minimum level of error handling. Function will return “True” if song is 
	 * successfully deleted and “False” otherwise.
	 */
	private boolean deleteSong(String song_id) {
		return true;
	}

	/*
	 * This function will be responsible for adding a new post in the database with 
	 * “INSERT” statements after a connection using the JDBC DriverManager is 
	 * established (and also updates the “PostAlbum” and “PostSong” table if required). 
	 * Insertion will also be surrounded by Try, Catch blocks to ensure a minimum level 
	 * of error handling. Function will return “True” if post is successfully added and 
	 * “False” otherwise.
	 */
	private boolean addPost(Post newPost) {
		return true;
	}

	/*
	 * This function will be responsible for retrieving the posts from the database 
	 * with “SELECT” statements after a connection using the JDBC DriverManager is 
	 * established. Retrieval will also be surrounded by Try, Catch blocks to ensure 
	 * a minimum level of error handling. Function will return an array of Post objects 
	 * and null if no posts are found.
	 */
	private Post[] getPosts(String user_id) {
		return null;
	}

	/*
	 * This function will be responsible for retrieving the posts from the users 
	 * that the user specified is following through the database with “SELECT” 
	 * statements after a connection using the JDBC DriverManager is established. 
	 * Retrieval will also be surrounded by Try, Catch blocks to ensure a minimum 
	 * level of error handling. Function will return an array of Post objects and 
	 * null if no posts are found.
	 */
	private Post[] getFeed(String user_id) {
		return null;
	}

	/*
	 * This function will be responsible for tracking which users like a particular 
	 * post in the database with “INSERT” statements to the “PostLike” table after 
	 * a connection using the JDBC DriverManager is established. Insertion will also 
	 * be surrounded by Try, Catch blocks to ensure a minimum level of error handling. 
	 * Function will return “True” if addition is successful and “False” otherwise.
	 */
	private boolean likePost(String post_id, String user_id) {
		return true;
	}

	/*
	 * This function will be responsible for deleting the relationship of the user 
	 * liking the post permanently using the “DELETE” statement after a connection 
	 * using the JDBC DriverManager is established. Deletion will also be surrounded 
	 * by Try, Catch blocks to ensure a minimum level of error handling. Function will 
	 * return “True” if relationship is successfully deleted and “False” otherwise.
	 */
	private boolean unlikePost(String post_id, String user_id) {
		return true;
	}

	/*
	 * This function will be responsible for tracking which users shared a particular 
	 * post in the database with “INSERT” statements to the “PostShare” table and also 
	 * adds the same post to “Post” table (but under current user) after a connection 
	 * using the JDBC DriverManager is established. Insertion will also be surrounded by 
	 * Try, Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if addition is successful and “False” otherwise.
	 */
	private boolean sharePost(String post_id, String user_id) {
		return true;
	}

	/* 
	 * This function will be responsible for deleting existing songs, the “AlbumSong” 
	 * relationships,and all posts that included those songs inside the album from the 
	 * database permanently using the “DELETE” statement after a connection using the 
	 * JDBC DriverManager is established. Deletion will also be surrounded by Try, Catch 
	 * blocks to ensure a minimum level of error handling. Function will return “True” 
	 * if song is successfully deleted and “False” otherwise.
	 */
	private boolean deletePost(String post_id) {
		return true;
	}

}
