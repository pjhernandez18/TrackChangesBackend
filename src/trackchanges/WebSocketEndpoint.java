package trackchanges;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint (value="/endpoint")
public class WebSocketEndpoint {

	private static final Map<String, Session> sessions = new HashMap<String, Session>();
	private static final Logger log = Logger.getLogger("TrackChanges");
	private static final JSONParser parser = new JSONParser(); 

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());        
		log.info("Connection openend by id: " + session.getId());
	}

	@OnClose
	public void close(Session session) {
		System.out.println("onClose:: " + session.getId());	
		log.info("Connection closed by id: " + session.getId());
	}

	@OnMessage
	public void onMessage (byte[] b, Session session) {
		
		boolean parseSuccess = false;
		String base64Encoded = Base64.encodeBase64String(b);
		byte[] base64Decoded = DatatypeConverter.parseBase64Binary(base64Encoded);
		String parsedJson = new String(base64Decoded);
		try {
			JSONObject json = (JSONObject) parser.parse(parsedJson);
			String request = (String) json.get("request");
			System.out.println("Decoded Json:");
			System.out.println(request);
//			System.out.println(json.get("id"));
//			System.out.println(json.get("displayname"));
//			System.out.println(json.get("logintimestamp"));
//			System.out.println(json.get("imageurl"));
			
			// Sends request and body to handler to call Application.jaa functions
			parseSuccess = handleRequest(request, json, session);
			
		} catch (ParseException pe) {
			System.out.println("pe: " + pe.getMessage());
		} finally {
			try {
				if(parseSuccess) {
					session.getBasicRemote().sendText("success");
				} else {
					session.getBasicRemote().sendText("failure");
				}
			} catch (IOException ioe) {
				System.out.println("ioe: " + ioe.getMessage());
			}
			
		}

	}
	
	/*
	 * Handles the request received from iOS client
	 */
	@SuppressWarnings("unchecked")
	private boolean handleRequest(String request, JSONObject json, Session session) {
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

	@OnError
	public void onError(Throwable e) {
		System.out.println("onError::" + e.getMessage());

	}

	public void sendToSession(Session session, byte[] data) {

		try {
			ByteBuffer tobeSent = ByteBuffer.wrap(data);
			session.getBasicRemote().sendBinary(tobeSent);

		}catch(IOException ioe) {
			sessions.remove(session.getId());   
		}

	}
	   
//	public void sendToAllSessions(Session session, byte[] data) {
//
//		lock.lock();
//		try {
//			ByteBuffer tobeSent = ByteBuffer.wrap(data);
//			//loop through sessions
//			for (String key : sessions.keySet()) {
//				if (sessions.get(key)!=session) {
//					sessions.get(key).getBasicRemote().sendBinary(tobeSent);
//				}
//			}		   
//		}catch(IOException ioe) {   
//			ioe.printStackTrace();
//		}
//		lock.unlock();
//
//	}


}