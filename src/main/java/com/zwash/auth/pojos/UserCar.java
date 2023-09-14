package com.zwash.auth.pojos;

import java.util.Date;

public class UserCar {

	public UserCar() {

	}

	/**
	 * @return the registrationPlate
	 */
	public String getRegistrationPlate() {
		return registrationPlate;
	}

	/**
	 * @param registrationPlate the registrationPlate to set
	 * @throws Exception
	 */
	public void setRegistrationPlatePlate(String registrationPlate) throws Exception {
		this.registrationPlate = registrationPlate;
		

	}

	/**
	 * @return the mark
	 */
	public String getManufacture() {
		return manufacture;
	}

	/**
	 * @param mark the mark to set
	 */
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	/**
	 * @return the datetime
	 */
	public Date getDateOfManufacture() {
		return dateOfManufacture;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDateOfManufacture(Date dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDeviceRegistrationToken() {
		return deviceRegistrationToken;
	}

	public void setDeviceRegistrationToken(String deviceRegistrationToken) {
		this.deviceRegistrationToken = deviceRegistrationToken;
	}
    public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}
	
	private int carId;
	
	private String token;

	private String registrationPlate;

	private String manufacture;

	private Date dateOfManufacture;

	private String deviceRegistrationToken;

}
