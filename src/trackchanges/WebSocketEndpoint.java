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
	private boolean handleRequest(String request, JSONObject json, Session session) {
		Application app = new Application();
		boolean handleSuccess = false;
		if(request.equals("add_user")) {
			
			User newUser = new User();
			newUser.setUserId((String)json.get("id"));
			newUser.setUserDisplayName((String)json.get("displayname"));
			newUser.setUserImageUrl((String)json.get("imageurl"));
			newUser.setUserLoginTimeStamp((String)json.get("logintimestamp"));
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
			ArrayList<String> followers = app.getFollowers(user_id);
			for(String follower : followers) {
				System.out.println(follower);
			}
			JSONArray jsonFollowersArray = new JSONArray();
			for(String follower : followers) {
				jsonFollowersArray.add(follower);
			}
			JSONObject response = new JSONObject();
			response.put("response", "followers");
			response.put("followers", jsonFollowersArray);
			sendToSession(session, response.toString().getBytes());
			handleSuccess = true;
			
		} else if(request.equals("get_following")) {
			
			String user_id = (String)json.get("user_id");
			String[] followings = app.getFollowing(user_id);
			JSONArray jsonFollowingsArray = new JSONArray();
			for(String following : followings) {
				jsonFollowingsArray.add(following);
			}
			JSONObject jsonFollowings = new JSONObject();
			jsonFollowings.put("followings", jsonFollowingsArray);
			sendToSession(session, jsonFollowings.toString().getBytes());
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