package com.ktsco.models.admin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsersModels {
	private SimpleIntegerProperty userId; 
	private SimpleStringProperty fullname;
	private SimpleStringProperty username; 
	private SimpleStringProperty password; 
	private SimpleStringProperty adminAccess, csrAccess, factoryAccess, mgmntAccess; 
	
	
	public UsersModels (int userId,String fullname,  String username, String password, String admin, String csr, String factory, String mgmnt) {
		this.userId = new SimpleIntegerProperty(userId);
		this.fullname = new SimpleStringProperty(fullname);
		this.username = new SimpleStringProperty(username); 
		this.password = new SimpleStringProperty(password);
		this.adminAccess = new SimpleStringProperty(admin);
		this.csrAccess = new SimpleStringProperty(csr);
		this.mgmntAccess = new SimpleStringProperty(mgmnt);
		this.factoryAccess = new SimpleStringProperty(factory);
	}


	public SimpleIntegerProperty userIdProperty() {
		return this.userId;
	}
	


	public int getUserId() {
		return this.userIdProperty().get();
	}
	


	public void setUserId(final int userId) {
		this.userIdProperty().set(userId);
	}
	


	public SimpleStringProperty usernameProperty() {
		return this.username;
	}
	


	public String getUsername() {
		return this.usernameProperty().get();
	}
	


	public void setUsername(final String username) {
		this.usernameProperty().set(username);
	}
	


	public SimpleStringProperty passwordProperty() {
		return this.password;
	}
	


	public String getPassword() {
		return this.passwordProperty().get();
	}
	


	public void setPassword(final String password) {
		this.passwordProperty().set(password);
	}
	


	public SimpleStringProperty adminAccessProperty() {
		return this.adminAccess;
	}
	


	public String getAdminAccess() {
		return this.adminAccessProperty().get();
	}
	


	public void setAdminAccess(final String adminAccess) {
		this.adminAccessProperty().set(adminAccess);
	}
	


	public SimpleStringProperty csrAccessProperty() {
		return this.csrAccess;
	}
	


	public String getCsrAccess() {
		return this.csrAccessProperty().get();
	}
	


	public void setCsrAccess(final String csrAccess) {
		this.csrAccessProperty().set(csrAccess);
	}
	


	public SimpleStringProperty factoryAccessProperty() {
		return this.factoryAccess;
	}
	


	public String getFactoryAccess() {
		return this.factoryAccessProperty().get();
	}
	


	public void setFactoryAccess(final String factoryAccess) {
		this.factoryAccessProperty().set(factoryAccess);
	}
	


	public SimpleStringProperty mgmntAccessProperty() {
		return this.mgmntAccess;
	}
	


	public String getMgmntAccess() {
		return this.mgmntAccessProperty().get();
	}
	


	public void setMgmntAccess(final String mgmntAccess) {
		this.mgmntAccessProperty().set(mgmntAccess);
	}


	public SimpleStringProperty fullnameProperty() {
		return this.fullname;
	}
	


	public String getFullname() {
		return this.fullnameProperty().get();
	}
	


	public void setFullname(final String fullname) {
		this.fullnameProperty().set(fullname);
	}
	
	
}
