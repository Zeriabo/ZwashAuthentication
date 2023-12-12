package com.zwash.auth.pojos;


public class SignInfo {


	public SignInfo()
	{

	}

	public SignInfo(String username,String password, String deviceRegistrationToken)
	{
		this.password=password;
		this.setUsername(username);
		this.setDeviceRegistrationToken(deviceRegistrationToken);
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceRegistrationToken() {
		return deviceRegistrationToken;
	}

	public void setDeviceRegistrationToken(String deviceRegistrationToken) {
		this.deviceRegistrationToken = deviceRegistrationToken;
	}

	private String username;
	private String password;
	 private String deviceRegistrationToken;

}
