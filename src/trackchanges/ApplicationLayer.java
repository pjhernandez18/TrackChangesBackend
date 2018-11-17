package trackchanges;

import javax.websocket.Session;

import org.json.JSONException;
import org.json.JSONObject;




public class ApplicationLayer {
	
	
	
	public ApplicationLayer() {
		
		/* Initially, any content for the feed in the database should be seen 
		on the landing page when application is first constructed. */
//		
//		//Establish JDBC driver
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/TrackChanges?user=root&password=root&useSSL=false");
//			st = conn.createStatement();
//			rs = st.executeQuery("SELECT * FROM Post");
//			while(rs.next()) {
//				
//			}
//			
//			
//			
//		}
		
		
	}
	
	public void parseJSON(JSONObject message, Session session) {
			try {
				if(message.get("message").equals("test")){
					System.out.println("JSON Recieved");
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		 

	}
	
}
