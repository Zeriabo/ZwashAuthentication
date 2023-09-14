package com.zwash.auth.pojos;

import java.time.LocalDateTime;
import java.util.List;


public class LoggedUser {

	public LoggedUser() {

	}

	public LoggedUser(String firstName, String lastName, String username, String dateOfBirth,
			String deviceRegistrationToken) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.dateOfBirth = dateOfBirth;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getString() {
		return this.getFirstName() + " " + this.getLastName() + " Active: " + this.isActive();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}


	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}


	private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String dateOfBirth;
	private Boolean active;
	private String token;
	private LocalDateTime createDateTime;
	private LocalDateTime updateDateTime;
	

	private boolean admin;
}
