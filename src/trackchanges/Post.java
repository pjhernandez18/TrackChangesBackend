package trackchanges;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

	@SerializedName("Post_Id")
	@Expose
	private String post_id;
	
	@SerializedName("Post_Timestamp")
	@Expose
	private String post_timeStamp;
	
	@SerializedName("Post_UserId")
	@Expose
	private String post_userId;
	
	@SerializedName("Post_Message")
	@Expose
	private String post_message;
	
	public String getPostId() {
		return post_id;
	}
	
	public void setPostId(String post_id) {
		this.post_id = post_id;
	}
	
	public String getPostTimeStamp() {
		return post_timeStamp;
	}
	
	public void setPostTimeStamp(String post_timeStamp) {
		this.post_timeStamp = post_timeStamp;
	}
	
	public String getPostUserId() {
		return post_id;
	}
	
	public void setPostUserId(String post_userId) {
		this.post_userId = post_userId;
	}
	
	public String getPostMessage() {
		return post_message;
	}
	
	public void setPostMessage(String post_message) {
		this.post_message = post_message;
	}
	
}
