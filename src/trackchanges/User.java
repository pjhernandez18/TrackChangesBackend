package trackchanges;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("User_Id")
	@Expose
	private String user_id;
	
	@SerializedName("User_Password")
	@Expose
	private String user_password;
	
	@SerializedName("User_Login")
	@Expose
	private String user_login;
	
	@SerializedName("User_Email")
	@Expose
	private String user_email;
	
	@SerializedName("User_FirstName")
	@Expose
	private String user_firstName;
	
	@SerializedName("User_LastName")
	@Expose
	private String user_lastName;
	
	@SerializedName("User_UserName")
	@Expose
	private String user_userName;
	
	@SerializedName("User_ImageUrl")
	@Expose
	private String user_imageUrl;
	
	@SerializedName("User_IsActive")
	@Expose
	private boolean user_isActive;
	
	public String getUserId() {
		return user_id;
	}
	
	public void setUserId(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUserPassword() {
		return user_password;
	}
	
	public void setUserPassword(String user_password) {
		this.user_password = user_password;
	}
	
	public String getUserLogin() {
		return user_login;
	}
	
	public void setUserLogin(String user_login) {
		this.user_login = user_login;
	}
	
	public String getUserEmail() {
		return user_email;
	}
	
	public void setUserEmail(String user_email) {
		this.user_email = user_email;
	}
	
	public String getUserFirstName() {
		return user_firstName;
	}
	
	public void setUserFirstName(String user_firstName) {
		this.user_firstName = user_firstName;
	}
	
	public String getUserLastName() {
		return user_lastName;
	}
	
	public void setUserLastName(String user_lastName) {
		this.user_lastName = user_lastName;
	}
	
	public String getUserUserName() {
		return user_userName;
	}
	
	public void setUserUserName(String user_userName) {
		this.user_userName = user_userName;
	}
	
	public String getUserImageUrl() {
		return user_imageUrl;
	}
	
	public void setUserImageUrl(String user_imageUrl) {
		this.user_imageUrl = user_imageUrl;
	}
	
	public boolean getUserIsActive() {
		return user_isActive;
	}
	
	public void setUserIsActive(boolean user_isActive) {
		this.user_isActive = user_isActive;
	}
}