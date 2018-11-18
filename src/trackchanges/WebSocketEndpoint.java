package trackchanges;

import java.io.IOException;
import java.nio.ByteBuffer;
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
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint (value="/endpoint")
public class WebSocketEndpoint {

	private static Application app = new Application();
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
			JSONObject body = (JSONObject) json.get("body");
			System.out.println("Decoded Json:");
			System.out.println("request: " + request);
			System.out.println("body: [");
			for(int i = 0; i < body.size(); i++) {
				System.out.print("\t");
				System.out.println(body.get(i) + ", ");
			}
			System.out.println("]");
			
			// Sends request and body to handler to call Application.jaa functions
			handleRequest(request, body);
			
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
	private boolean handleRequest(String request, JSONObject body) {
		boolean handleSuccess = false;
		if(request.equals("add_post")) {
			User newUser = new User();
			newUser.setUserId((String)body.get("id"));
			newUser.setUserDisplayName((String)body.get("displayname"));
			newUser.setUserImageUrl((String)body.get("imageurl"));
			newUser.setUserLoginTimeStamp(new DateTime((String)body.get("logintimestamp")));
			newUser.setUserIsActive(true);
			handleSuccess = app.addUser(newUser);
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