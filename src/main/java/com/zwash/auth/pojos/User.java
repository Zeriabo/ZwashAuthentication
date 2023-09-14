package com.zwash.auth.pojos;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "zwashuser")
@ApiModel(description = "A user entity.")
public class User {

	public User()
	{

	}
	public User(String firstName, String lastName, String username, String password, String dateOfBirth,
			String secretQuestion, String secretAnswer, Boolean admin)
	{
		this.firstName=firstName;
		this.lastName= lastName;
		this.password=password;
		this.secretAnswer=secretAnswer;
		this.secretQuestion=secretQuestion;
		this.username=username;
		this.dateOfBirth=dateOfBirth;
		this.admin=admin;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id=id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getSecretQuestion() {
		return secretQuestion;
	}
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}
	public String getSecretAnswer() {
		return secretAnswer;
	}
	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getString(){

       return this.getFirstName()+" "+this.getLastName()+ " Active: "+this.isActive();
	}

	 public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Boolean isAdmin() {
	    return admin;
	}

	public void setAdmin(Boolean admin) {
	    this.admin = admin;
	}


	public String getDeviceRegistrationToken() {
		return deviceRegistrationToken;
	}
	public void setDeviceRegistrationToken(String deviceRegistrationToken) {
		this.deviceRegistrationToken = deviceRegistrationToken;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	@PrePersist
	protected void onCreate() {
		setCreatedAt(LocalDateTime.now());
		setUpdatedAt(LocalDateTime.now());
	}

	@PreUpdate
	protected void onUpdate() {
		setUpdatedAt(LocalDateTime.now());
	}

	@Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
     @Column(name = "id", unique = true, nullable = false)
	 @ApiModelProperty(value = "The unique ID of the user.", example = "1")
	private Long id;
	 @Column(name = "firstName")
	 @ApiModelProperty(value = "The first name of the user.", example = "John")
	private String firstName;
	 @Column(name = "lastName")
	 @ApiModelProperty(value = "The last name of the user.", example = "Doe")
	private String lastName;
	 @Column(name = "username",unique=true)
	 @ApiModelProperty(value = "The username of the user.", example = "johndoe")
	private String username;
	 @Column(name = "password")
	 @ApiModelProperty(value = "The password of the user.", example = "MyPass123")
	private String password;
	 @Column(name = "dateOfBirth")
	 @ApiModelProperty(value = "The date of birth of the user.", example = "1922-02-22")
	private String dateOfBirth;
	 @Column(name = "secretQuestion")
	 @ApiModelProperty(value = "The secret question of the user.", example = "What is your favorite color?")
	private String secretQuestion;
	 @Column(name = "secretAnswer")
	 @ApiModelProperty(value = "The secret answer of the user.", example = "Blue")
	private String secretAnswer;
	 @ApiModelProperty(value = "Whether the user is active or not.", example = "true")
	 @Column(name = "active")
	private Boolean active;
	 @Column(name = "admin")
	 @ApiModelProperty(value = "Whether the user is an admin or not.", example = "true")
	 private Boolean admin;


	 @Column(name = "deviceRegistrationToken")
     @ApiModelProperty(value = "The Device registeration token of the user.")
    private String deviceRegistrationToken;

	private String token;


	@CreationTimestamp
	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;


}
