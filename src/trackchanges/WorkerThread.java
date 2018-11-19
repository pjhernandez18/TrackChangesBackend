package trackchanges;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WorkerThread extends Thread {

	private Session clientSession;

	public WorkerThread(Session clientSession) {
		this.clientSession = clientSession;
		this.start();
	}

	/*
	 * Handles the request received from iOS client
	 */
	@SuppressWarnings("unchecked")
	public boolean handleRequest(String request, JSONObject json, Session session) {
		Application app = new Application();
		boolean handleSuccess = false;
		if(request.equals("add_user")) {

			User newUser = new User();
			newUser.setUserId((String)json.get("user_id"));
			newUser.setUserDisplayName((String)json.get("user_displayname"));
			newUser.setUserImageUrl((String)json.get("user_imageurl"));
			newUser.setUserLoginTimeStamp((String)json.get("user_logintimestamp"));
			handleSuccess = app.addUser(newUser);

		} else if(request.equals("follow")) {

			String user_id = (String)json.get("user_id");
			String follower_id = (String)json.get("follower_id");
			handleSuccess = app.follow(user_id, follower_id);

		} else if(request.equals("unfollow")) {

			String user_id = (String)json.get("user_id");
			String follower_id = (String)json.get("follower_id");
			handleSuccess = app.unfollow(user_id, follower_id);

		} else if(request.equals("get_followers")) {

			String user_id = (String)json.get("user_id");
			ArrayList<User> followers = app.getFollowers(user_id);
			for(User follower : followers) {
				System.out.println(follower.getUserId());
			}
			JSONArray jsonFollowersArray = new JSONArray();
			for(User follower : followers) {
				JSONObject jsonFollower = new JSONObject();
				jsonFollower.put("user_id", follower.getUserId());
				jsonFollower.put("user_displayname", follower.getUserDisplayName());
				jsonFollower.put("user_imageurl", follower.getUserImageUrl());
				jsonFollower.put("user_logintimestamp", follower.getUserLoginTimeStamp());
				jsonFollowersArray.add(jsonFollower);
			}
			JSONObject response = new JSONObject();
			response.put("response", "followers");
			response.put("followers", jsonFollowersArray);
			sendToSession(session, response.toString().getBytes());
			handleSuccess = true;

		} else if(request.equals("get_followings")) {

			String user_id = (String)json.get("user_id");
			ArrayList<User> followings = app.getFollowings(user_id);
			for(User following : followings) {
				System.out.println(following.getUserId());
			}
			JSONArray jsonFollowingsArray = new JSONArray();
			for(User following : followings) {
				JSONObject jsonFollowing = new JSONObject();
				jsonFollowing.put("user_id", following.getUserId());
				jsonFollowing.put("user_displayname", following.getUserDisplayName());
				jsonFollowing.put("user_imageurl", following.getUserImageUrl());
				jsonFollowing.put("user_logintimestamp", following.getUserLoginTimeStamp());
				jsonFollowingsArray.add(jsonFollowing);
			}
			JSONObject response = new JSONObject();
			response.put("response", "followings");
			response.put("followings", jsonFollowingsArray);
			sendToSession(session, response.toString().getBytes());
			handleSuccess = true;

		} else if(request.equals("add_album")) {

			String album_id = (String)json.get("album_id");
			handleSuccess = app.addAlbum(album_id);

		} else if(request.equals("delete_album")) {

			String album_id = (String)json.get("album_id");
			handleSuccess = app.deleteAlbum(album_id);

		} else if(request.equals("add_song")) {

			String song_id = (String)json.get("song_id");
			handleSuccess = app.addSong(song_id);

		} else if(request.equals("delete_song")) {

			String song_id = (String)json.get("song_id");
			handleSuccess = app.deleteSong(song_id);

		} else if(request.equals("like_song")) {

			String song_id = (String)json.get("song_id");
			String user_id = (String)json.get("user_id");
			handleSuccess = app.likeSong(song_id, user_id);

		} else if(request.equals("unlike_song")) {

			String song_id = (String)json.get("song_id");
			String user_id = (String)json.get("user_id");
			handleSuccess = app.unlikeSong(song_id, user_id);

		} else if(request.equals("add_post")) {



			Post newPost = new Post();
			newPost.setPostTimeStamp((String)json.get("post_timestamp"));
			newPost.setPostUserId((String)json.get("post_user_id"));
			System.out.println("1");
			System.out.println((String)json.get("post_user_id"));
			System.out.println("2");
			newPost.setPostMessage((String)json.get("post_message"));
			newPost.setPostSongId((String)json.get("post_song_id"));
			newPost.setPostAlbumId((String)json.get("post_album_id"));
			handleSuccess = app.addPost(newPost);

		} else if(request.equals("get_posts")) {

			String user_id = (String)json.get("user_id");
			Post[] posts = app.getPosts(user_id);
			JSONArray jsonPostArray = new JSONArray();
			for(Post x : posts) {
				jsonPostArray.add(x);
			}

			JSONObject response = new JSONObject();
			response.put("response", "posts");
			response.put("posts", jsonPostArray);
			sendToSession(session, response.toString().getBytes());
			handleSuccess = true;

		} else if(request.equals("get_feed")) {

			String user_id = (String)json.get("user_id");
			Post[] posts = app.getFeed(user_id);
			JSONArray jsonFeedArray = new JSONArray();
			for(Post x : posts) {
				jsonFeedArray.add(x);
			}

			JSONObject response = new JSONObject();
			response.put("response", "feed");
			response.put("feed", jsonFeedArray);
			sendToSession(session, response.toString().getBytes());
			handleSuccess = true;

		} else if(request.equals("like_post")) {

			String user_id = (String)json.get("user_id");
			String post_id = (String)json.get("post_id");
			handleSuccess = app.likePost(post_id, user_id);

		} else if(request.equals("unlike_post")) {

			String user_id = (String)json.get("user_id");
			String post_id = (String)json.get("post_id");
			handleSuccess = app.unlikePost(post_id, user_id);

		} else if(request.equals("share_post")) {

			String user_id = (String)json.get("user_id");
			String post_id = (String)json.get("post_id");
			String timestamp = (String)json.get("timestamp");

			handleSuccess = app.sharePost(post_id, user_id, timestamp);

		} else if(request.equals("delete_post")) {

			String post_id = (String)json.get("post_id");
			handleSuccess = app.deletePost(post_id);

		}
		return handleSuccess;
	}
	
	/*
	 * Function that sends data in byte array to the specified Client Session
	 */
	public void sendToSession(Session session, byte[] data) {

		try {
			ByteBuffer tobeSent = ByteBuffer.wrap(data);
			session.getBasicRemote().sendBinary(tobeSent);

		} catch(IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}

	}

	/*
	 * Overrides run() method because we inherit from Thread class
	 */
	public void run() {
//		try {
//			while(true) {
//				ChatMessage cm = (ChatMessage)ois.readObject();
//				if(cm != null) {
//					cr.broadcast(cm, this);
//				}
//			}
//		} catch (IOException ioe) {
//			System.out.println("ioe in ST.run():" + ioe.getMessage());
//		} catch (ClassNotFoundException cnfe) {
//			System.out.println("cnfe:" + cnfe.getMessage());
//		}
	}

}
