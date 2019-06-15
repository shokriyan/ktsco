package com.ktsco.models.admin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsersModels {
	private SimpleIntegerProperty userId; 
	private SimpleStringProperty username; 
	private SimpleStringProperty password; 
	private SimpleStringProperty accessType; 
	
	public UsersModels (int userId, String username, String password, String accessPanel) {
		this.userId = new SimpleIntegerProperty(userId);
		this.username = new SimpleStringProperty(username); 
		this.password = new SimpleStringProperty(password);
		this.accessType = new SimpleStringProperty(accessPanel);
	}
	
	/**
	 * return the value of User ID
	 * @return Integer
	 */
	public int getUserID() {
		return userId.get();
	}
	/**
	 * return the property of User ID
	 * 
	 * @return SimpleIntegerProperty
	 */
	public SimpleIntegerProperty userIdProperty() {
		return userId; 
	}
	/**
	 * To set the value of User ID
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId.set(userId);
	}
	
	/**
	 * return the value of Username
	 * @return String
	 */
	public String getUsername() {
		return username.get();
	}
	/**
	 * return the property of Username
	 * 
	 * @return SimpleStringProperty
	 */
	public SimpleStringProperty usernameProperty() {
		return username; 
	}
	/**
	 * To set the value of Username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username.set(username);
	}
	
	
	/**
	 * return the value of password
	 * @return String
	 */
	public String getPassword() {
		return password.get();
	}
	/**
	 * return the property of Password
	 * 
	 * @return SimpleStringProperty
	 */
	public SimpleStringProperty passwordProperty() {
		return password; 
	}
	/**
	 * To set the value of password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password.set(password);
	}
	
	/**
	 * return the value of Access type
	 * @return String
	 */
	public String getAccessType() {
		return accessType.get();
	}
	/**
	 * return the property of Access Type
	 * 
	 * @return SimpleStringProperty
	 */
	public SimpleStringProperty accessTypeProperty() {
		return accessType; 
	}
	/**
	 * To set the value of Access Type
	 * @param accessType
	 */
	public void setAccessType(String accessType) {
		this.accessType.set(accessType);
	}
}
