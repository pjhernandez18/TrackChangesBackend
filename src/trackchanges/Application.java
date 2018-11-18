package trackchanges;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;

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
					+ "user_imageurl, "
					+ "user_isactive) VALUES ('" 
					+ newUser.getUserId() + "', '"
					+ newUser.getUserDisplayName() + "', '"
					+ newUser.getUserLoginTimeStamp() + "', '"
					+ newUser.getUserImageUrl() + "', '"
					+ newUser.getUserIsActive() 
					+ "');");
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
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"UPDATE User SET "
					+ "user_id=" + user.getUserId()
					+ ", user_displayname=" + user.getUserDisplayName()
					+ ", user_logintimestamp=" + user.getUserLoginTimeStamp().toString()
					+ ", user_imageurl=" + user.getUserImageUrl()
					+ ", user_is_active=" +  user.getUserIsActive()
					+ " WHERE user_id=" + user.getUserId());
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
	 * This function will be responsible for deactivating existing users 
	 * in the database by setting “user_is_active” to “False” after a 
	 * connection using the JDBC DriverManager is established. 
	 * Deactivation will also be surrounded by Try, Catch blocks to ensure a 
	 * minimum level of error handling. Function will return “True” if user 
	 * is successfully deactivated and “False” otherwise.
	 */
	private boolean deactivateUser(String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"UPDATE User SET user_is_active=false WHERE user_id="+user_id);
					
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
	 * This function will be responsible for deleting existing users from 
	 * the database permanently using the “DELETE” statement after a connection 
	 * using the JDBC DriverManager is established. Deletion will also be 
	 * surrounded by Try, Catch blocks to ensure a minimum level of error handling. 
	 * Function will return “True” if user is successfully deleted and “False” otherwise.
	 */
	private boolean deleteUser(String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"DELETE FROM User WHERE user_id=?");
			ps.setString(1,  user_id);
			result = ps.execute();
			
			// user won't necessarily have rows in these following tables
			// so the value of result won't be dependent on successful execute()
			ps = conn.prepareStatement(
					"DELETE FROM Follow WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM SongLike WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM Post WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostShare WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostLike WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostAlbum WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostSong WHERE user_id=?");
			ps.setString(1,  user_id);
			ps.execute();
			
			
			
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
	
	// TBD
	/*
	/*
	 * This function will be responsible for adding new artists into the database 
	 * with “INSERT” statements after a connection using the JDBC DriverManager 
	 * is established. Insertion will also be surrounded by Try, Catch blocks to 
	 * ensure a minimum level of error handling. Function will return “True” if 
	 * user is successfully added and “False” otherwise.
	 */
	private boolean addArtist(Artist newArtist) {
		return false;
	}

	/*
	 * This function will be responsible for updating an existing artist in 
	 * the database with “UPDATE” statements after a connection using the JDBC 
	 * DriverManager is established. Update will also be surrounded by Try, 
	 * Catch blocks to ensure a minimum level of error handling. Function will 
	 * return “True” if user is successfully updated and “False” otherwise.
	 */
	private boolean updateArtist(Artist artist) {
		return false;
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
		return false;
	}

	/*
	 * This function will be responsible for adding a new follower relationship 
	 * into the database with “INSERT” statements after a connection using the 
	 * JDBC DriverManager is established. Insertion will also be surrounded by Try, 
	 * Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if follower is successfully added and “False” otherwise.
	 */
	private boolean follow(String user_id, String follower_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"INSERT INTO Follow (user_id, "
					+ "follow_id) VALUES ('" 
					+ user_id + "', '"  
					+ follower_id + "');");
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
	 * This function will be responsible for deleting a follower relationship permanently 
	 * using the “DELETE” statement after a connection using the JDBC DriverManager is 
	 * established. Deletion will also be surrounded by Try, Catch blocks to ensure a 
	 * minimum level of error handling. Function will return “True” if relationship is 
	 * successfully deleted and “False” otherwise.
	 */
	private boolean unfollow(String user_id, String follower_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			// not sure how to delete based off two parameters
			ps = conn.prepareStatement(
					"DELETE FROM Follow WHERE user_id=" + user_id + " AND " + "follower_id=" + follower_id);
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
	 * This function will be responsible for retrieving all the followers of a user 
	 * using the “SELECT” statement after a connection using the JDBC DriverManager 
	 * is established. Deletion will also be surrounded by Try, Catch blocks to ensure 
	 * a minimum level of error handling. Function will return an array of “user_id”(s) 
	 * corresponding to each follower. Size of array will be the number of followers a user has.
	 */
	private String[] getFollowers(String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<String> tempRes = new ArrayList<String>(); 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			// not sure how to delete based off two parameters
			ps = conn.prepareStatement(
					"SELECT * from Follow WHERE user_id LIKE?");
			ps.setString(1, "%" + user_id + "%");
	    	  
	    	  rs = ps.executeQuery();
	    	  while(rs.next()){
	    		  String tempFollower = rs.getString("follower_id");
	    		  tempRes.add(tempFollower);
	    	  }
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
		
		String [] res = new String[tempRes.size()];
		for(int i = 0; i < tempRes.size(); ++i) {
			res[i] = tempRes.get(i);
		}
		return res;
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
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<String> tempRes = new ArrayList<String>(); 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			// not sure how to delete based off two parameters
			ps = conn.prepareStatement(
					"SELECT * from Follow WHERE follower_id LIKE?");
			ps.setString(1, "%" + user_id + "%");
	    	  
	    	  rs = ps.executeQuery();
	    	  while(rs.next()){
	    		  String tempFollower = rs.getString("user_id");
	    		  tempRes.add(tempFollower);
	    	  }
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
		
		String [] res = new String[tempRes.size()];
		for(int i = 0; i < tempRes.size(); ++i) {
			res[i] = tempRes.get(i);
		}
		return res;
	}

	/*
	 * This function will be responsible for adding new albums, album artist(s), and 
	 * all the songs in it  into the database with “INSERT” statements after a connection 
	 * using the JDBC DriverManager is established. Insertion will also be surrounded by 
	 * Try, Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if album is successfully added and “False” otherwise.
	 */
	private boolean addAlbum(String album_id) {
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement("INSERT INTO Album (album_id) VALUES ('" + album_id+ "');");
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
	 * This function will be responsible for deleting existing albums, and all posts that 
	 * included those albums and the songs inside the album from the database permanently 
	 * using the “DELETE” statement after a connection using the JDBC DriverManager is 
	 * established. Deletion will also be surrounded by Try, Catch blocks to ensure a 
	 * minimum level of error handling. Function will return “True” if album is successfully 
	 * deleted and “False” otherwise.
	 */
	private boolean deleteAlbum(String album_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			
			ps = conn.prepareStatement(
					"DELETE FROM Album WHERE album_id=?");
			ps.setString(1,  album_id);
			result = ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostAlbum WHERE album_id=?");
			ps.setString(1,  album_id);
			ps.execute();
			
			
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
	 * This function will be responsible for adding new songs and its corresponding artist(s), 
	 * as well as the updating the “AlbumSong” relationship (storing the fact that the song 
	 * belongs to the album whose “album_id” is specified) in the database with “INSERT” 
	 * statements after a connection using the JDBC DriverManager is established. Insertion 
	 * will also be surrounded by Try, Catch blocks to ensure a minimum level of error 
	 * handling. Function will return “True” if song is successfully added and “False” otherwise.
	 */
	private boolean addSong(String song_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement("INSERT INTO Song (song_id) VALUES ('" + song_id+ "');");
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
	 * This function will be responsible for adding new songs and its corresponding artist(s) 
	 * into the database with “INSERT” statements after a connection using the JDBC 
	 * DriverManager is established. Insertion will also be surrounded by Try, Catch 
	 * blocks to ensure a minimum level of error handling. Function will return “True” 
	 * if song is successfully added and “False” otherwise.
	 */
	private boolean addSong(Song newSong) {
		// dont need this anymore?
		return false;
	}

	/*
	 * This function will be responsible for tracking which users like a particular song 
	 * in the database with “INSERT” statements to the “AlbumSong” table after a connection 
	 * using the JDBC DriverManager is established. Insertion will also be surrounded by 
	 * Try, Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if addition is successful and “False” otherwise.
	 */
	private boolean likeSong(String song_id, String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"INSERT INTO SongLike (song_id, "
					
					+ "user_id) VALUES ('" 
					+ song_id + "', '" 
				
					+ user_id + "');");
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
	 * This function will be responsible for deleting the relationship of the user liking 
	 * the song permanently using the “DELETE” statement after a connection using the JDBC 
	 * DriverManager is established. Deletion will also be surrounded by Try, Catch blocks 
	 * to ensure a minimum level of error handling. Function will return “True” if 
	 * relationship is successfully deleted and “False” otherwise.
	 */
	private boolean unlikeSong(String song_id, String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			// not sure how to delete based off two parameters
			ps = conn.prepareStatement(
					"DELETE FROM SongLike WHERE song_id=" + song_id + " AND " + "user_id=" + user_id);
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
	 * This function will be responsible for deleting existing songs, the “AlbumSong” 
	 * relationships,and all posts that included those songs inside the album from the 
	 * database permanently using the “DELETE” statement after a connection using the JDBC 
	 * DriverManager is established. Deletion will also be surrounded by Try, Catch blocks 
	 * to ensure a minimum level of error handling. Function will return “True” if song is 
	 * successfully deleted and “False” otherwise.
	 */
	private boolean deleteSong(String song_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"DELETE FROM Song WHERE song_id=?");
			ps.setString(1,  song_id);
			result = ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostSong WHERE song_id=?");
			ps.setString(1,  song_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM SongLike WHERE song_id=?");
			ps.setString(1,  song_id);
			ps.execute();
			
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
	 * This function will be responsible for adding a new post in the database with 
	 * “INSERT” statements after a connection using the JDBC DriverManager is 
	 * established (and also updates the “PostAlbum” or “PostSong” table if required). 
	 * Insertion will also be surrounded by Try, Catch blocks to ensure a minimum level 
	 * of error handling. Function will return “True” if post is successfully added and 
	 * “False” otherwise.
	 */
	private boolean addPost(Post newPost) {
		// time stamp needs to figured out here
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"INSERT INTO Post (post_id, "
					+ "post_timestamp, "
					
					
					+ "user_id, "
					+ "post_message) VALUES ('" 
					
					+ newPost.getPostId() + "', '" 
					+ newPost.getPostTimestamp().toString() + "', '" 
					
					+ newPost.getPostUserId() + "', '" 
					
					+ newPost.getPostMessage() + "');");
			result = ps.execute();
			
			// insert into post song id table
			if(newPost.getPostSongId != null) {
				ps = conn.prepareStatement("INSERT INTO PostSong(song_id, "
						+ "post_id) VALUES ('"
						+ newPost.getPostSongId() + "', '" 
						+ newPost.getPostId() + "');");
				
			}
			else { // insert into post album id table
				ps = conn.prepareStatement("INSERT INTO PostAlbum(album_id, "
						+ "post_id) VALUES ('"
						+ newPost.getPostAlbumId() + "', '" 
						+ newPost.getPostId() + "');");
			}
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
	 * This function will be responsible for retrieving the posts from the database 
	 * with “SELECT” statements after a connection using the JDBC DriverManager is 
	 * established. Retrieval will also be surrounded by Try, Catch blocks to ensure 
	 * a minimum level of error handling. Function will return an array of Post objects 
	 * and null if no posts are found.
	 */
	private Post[] getPosts(String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<Post> tempRes = new ArrayList<Post>(); 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			// not sure how to delete based off two parameters
			ps = conn.prepareStatement(
					"SELECT * from Post WHERE user_id LIKE?");
			ps.setString(1, "%" + user_id + "%");
	    	  
	    	  rs = ps.executeQuery();
	    	  while(rs.next()){
	    		  Post tempPost = new Post();
	    		  
	    		  String tempPostId = rs.getString("post_id");
	    		  String tempPostTimeStamp = rs.getString("post_timestamp");
	    		  String tempUserId = rs.getString("user_id");
	    		  String tempPostMessage = rs.getString("post_message");
	    		  
	    		  tempPost.setPostID(tempPostId);
	    		  tempPost.setPostTimeStamp(new DateTime(tempPostTimeStamp));
	    		  tempPost.setPostUserId(tempUserId);
	    		  tempRes.setPostMessage(tempPostMessage);
	    		  
	    		  PreparedStatement ps2 = conn.prepareStatement(
	    				  "SELECT * from PostAlbum WHERE user_id LIKE?");
	    		  ps2.setString(1, "%" + user_id + "%");
	    		  ResultSet rs2 = ps2.executeQuery();
	    		  
	    		  PreparedStatement ps3 = conn.prepareStatement(
	    				  "SELECT * from SongAlbum WHERE user_id LIKE?");
	    		  ps3.setString(1, "%" + user_id + "%");
	    		  ResultSet rs3 = ps3.executeQuery();
	    		  
	    		  if(ps2 != null) {
	    			  tempPost.setPostAlbumId(rs2.getString("album_id"));
	    		  }
	    		  else {
	    			  tempPost.setPostAlbumId(rs2.getString(null));
	    		  }
	    		  
	    		  if(ps3 != null) {
	    			  tempPost.setPostSongId(rs3.getString("song_id"));
	    		  }
	    		  else {
	    			  tempPost.setPostSongId(rs3.getString(null));
	    		  }
	    		  tempRes.add(tempPost);
	    	  }
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
		
		Post [] res = new Post[tempRes.size()];
		for(int i = 0; i < tempRes.size(); ++i) {
			res[i] = tempRes.get(i);
		}
		return res;
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
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<Post> tempRes = new ArrayList<String>(); 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			
			
			
			// first select the users that the target user is following
			ps = conn.prepareStatement(
					"SELECT * from Follow WHERE follower_id LIKE?");
			ps.setString(1, "%" + user_id + "%");
	    	  
	    	  rs = ps.executeQuery();
	    	  ArrayList<String> following = new ArrayList<String>();
	    	  while(rs.next()) {
	    		  following.add(rs.getString("user_id"));
	    	  }
	    	  
	    	  // do we have to reset rs, ps, etc. as null
	    	  // iterate through the users that the target user is following 
	    	  // add posts accordingly
	    	  for(int i = 0; i < following.size(); ++i) {
		  			ps = conn.prepareStatement(
							"SELECT * from Post WHERE user_id LIKE?");
					ps.setString(1, "%" + following.get(i) + "%");
					 rs = ps.executeQuery();
		    	  while(rs.next()){
		    		  Post tempPost = new Post();
		    		  
		    		  String tempPostId = rs.getString("post_id");
		    		  String tempPostTimeStamp = rs.getString("post_timestamp");
		    		  String tempUserId = rs.getString("user_id");
		    		  String tempPostMessage = rs.getString("post_message");
		    		  
		    		  tempPost.setPostID(tempPostId);
		    		  tempPost.setPostTimeStamp(new DateTime(tempPostTimeStamp));
		    		  tempPost.setPostUserId(tempUserId);
		    		  tempRes.setPostMessage(tempPostMessage);
		    		  
		    		  PreparedStatement ps2 = conn.prepareStatement(
		    				  "SELECT * from PostAlbum WHERE user_id LIKE?");
		    		  ps2.setString(1, "%" + user_id + "%");
		    		  ResultSet rs2 = ps2.executeQuery();
		    		  
		    		  PreparedStatement ps3 = conn.prepareStatement(
		    				  "SELECT * from SongAlbum WHERE user_id LIKE?");
		    		  ps3.setString(1, "%" + user_id + "%");
		    		  ResultSet rs3 = ps3.executeQuery();
		    		  
		    		  if(ps2 != null) {
		    			  tempPost.setPostAlbumId(rs2.getString("album_id"));
		    		  }
		    		  else {
		    			  tempPost.setPostAlbumId(rs2.getString(null));
		    		  }
		    		  
		    		  if(ps3 != null) {
		    			  tempPost.setPostSongId(rs3.getString("song_id"));
		    		  }
		    		  else {
		    			  tempPost.setPostSongId(rs3.getString(null));
		    		  }
		    		  tempRes.add(tempPost);
		    	  }
	    	  }

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
		
		Post [] res = new Post[tempRes.size()];
		for(int i = 0; i < tempRes.size(); ++i) {
			res[i] = tempRes.get(i);
		}
		return res;
	}

	/*
	 * This function will be responsible for tracking which users like a particular 
	 * post in the database with “INSERT” statements to the “PostLike” table after 
	 * a connection using the JDBC DriverManager is established. Insertion will also 
	 * be surrounded by Try, Catch blocks to ensure a minimum level of error handling. 
	 * Function will return “True” if addition is successful and “False” otherwise.
	 */
	private boolean likePost(String post_id, String user_id) {
		// time stamp needs to figured out here
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"INSERT INTO PostLike (post_id, "
					+ "user_id) VALUES ('" 
					
					+ post_id + "', '" 
					+ user_id + "');");
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
	 * This function will be responsible for deleting the relationship of the user 
	 * liking the post permanently using the “DELETE” statement after a connection 
	 * using the JDBC DriverManager is established. Deletion will also be surrounded 
	 * by Try, Catch blocks to ensure a minimum level of error handling. Function will 
	 * return “True” if relationship is successfully deleted and “False” otherwise.
	 */
	private boolean unlikePost(String post_id, String user_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"DELETE FROM PostLike WHERE post_id=" + post_id + " AND user_id=" + user_id);
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
	 * This function will be responsible for tracking which users shared a particular 
	 * post in the database with “INSERT” statements to the “PostShare” table and also 
	 * adds the same post to “Post” table (but under current user) after a connection 
	 * using the JDBC DriverManager is established. Insertion will also be surrounded by 
	 * Try, Catch blocks to ensure a minimum level of error handling. Function will return 
	 * “True” if addition is successful and “False” otherwise.
	 */
	private boolean sharePost(String post_id, String user_id, DateTime timeStamp) {
		// ? look at this

		// time stamp needs to figured out here
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"SELECT * from Post WHERE user_id LIKE?");
			ps.setString(1, "%" + post_id + "%");
			rs = ps.executeQuery(); // check for exception here?
			
			
			String postMessage = rs.getString("post_message");
			 
			ps = conn.prepareStatement(
					"INSERT INTO Post (post_id, "
					+ "post_timestamp, "
					+ "user_id, "
					+ "post_message) VALUES ('" 
					
					+ post_id + "', '" 
					+ timeStamp.toString() + "', '" 
					+ user_id + "', '" 
					+ postMessage + "');");
			result = ps.execute();
			
			ps = conn.prepareStatement(
					"INSERT INTO PostShare (post_id, "
					+ "user_id) VALUES ('" 
					
					+ post_id + "', '" 
					+ user_id + "');");
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
	 * This function will be responsible for deleting existing songs, the “AlbumSong” 
	 * relationships,and all posts that included those songs inside the album from the 
	 * database permanently using the “DELETE” statement after a connection using the 
	 * JDBC DriverManager is established. Deletion will also be surrounded by Try, Catch 
	 * blocks to ensure a minimum level of error handling. Function will return “True” 
	 * if song is successfully deleted and “False” otherwise.
	 */
	private boolean deletePost(String post_id) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DATABASE_CONNECTION_URL);
			ps = conn.prepareStatement(
					"DELETE FROM Post WHERE post_id=" + post_id);
			result = ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostLike WHERE post_id=" + post_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostShare WHERE post_id=" + post_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostSong WHERE post_id=" + post_id);
			ps.execute();
			
			ps = conn.prepareStatement(
					"DELETE FROM PostAlbum WHERE post_id=" + post_id);
			ps.execute();
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

}
