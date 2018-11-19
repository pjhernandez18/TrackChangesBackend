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

	private static final Map<String, WorkerThread> sessions = new HashMap<String, WorkerThread>();
	private static final Logger log = Logger.getLogger("TrackChanges");
	private static final JSONParser parser = new JSONParser(); 

	@OnOpen
	public void onOpen(Session session) {
		sessions.put(session.getId(), new WorkerThread(session));
		System.out.println("onOpen:: " + session.getId());        
		log.info("Connection openend by id: " + session.getId());
	}

	@OnClose
	public void close(Session session) {
		System.out.println("onClose:: " + session.getId());	
		log.info("Connection closed by id: " + session.getId());
	}

	@OnMessage
	public void onMessage (byte[] b, Session session) {
		
		for(String sessionId : sessions.keySet()) {
			if(sessionId.equals(session.getId())) {
				boolean parseSuccess = false;
				String base64Encoded = Base64.encodeBase64String(b);
				byte[] base64Decoded = DatatypeConverter.parseBase64Binary(base64Encoded);
				String parsedJson = new String(base64Decoded);
				try {
					JSONObject json = (JSONObject) parser.parse(parsedJson);
					String request = (String) json.get("request");
					System.out.println("Decoded Json: " + request);
					
					// Sends request and body to handler to call Application.jaa functions
					parseSuccess = sessions.get(sessionId).handleRequest(request, json, session);
					
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
		}

	}

	@OnError
	public void onError(Throwable e) {
		System.out.println("onError::" + e.getMessage());

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