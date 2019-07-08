package com.ktsco.models.csr;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerModel {
	
	private SimpleIntegerProperty code; 
	private SimpleStringProperty company; 
	private SimpleStringProperty poc; 
	private SimpleStringProperty phone; 
	private SimpleStringProperty address; 
	private SimpleStringProperty currency; 
	
	public CustomerModel(int code, String company, String poc, String phone, String address, String currency) {
		this.code = new SimpleIntegerProperty(code);
		this.company = new SimpleStringProperty(company);
		this.poc = new SimpleStringProperty(poc);
		this.phone = new SimpleStringProperty(phone);
		this.address = new SimpleStringProperty (address);
		this.currency = new SimpleStringProperty(currency);
	}
	
	//getters
	public int getCode() {
		return this.code.get();
	}
	public String getCompany() {
		return this.company.get();
	}
	public String getPOC() {
		return this.poc.get();
	}
	public String getPhone() {
		return this.phone.get();
	}
	public String getAddress() {
		return this.address.get();
	}
	public String getCurrency() {
		return this.currency.get();
	}
	
	//Setters
	public void setCode(int code) {
		this.code = new SimpleIntegerProperty(code);
	}
	public void setCompany(String company) {
		this.company = new SimpleStringProperty(company);
	}
	public void setPOC(String poc) {
		this.poc = new SimpleStringProperty(poc);
	}
	public void setPhone(String phone) {
		this.phone = new SimpleStringProperty(phone);
	}
	public void setAddress (String address) {
		this.address = new SimpleStringProperty(address);
	}
	public void setCurrency(String currency) {
		this.currency = new SimpleStringProperty(currency);
	}
	
	//Get Properties
	public SimpleIntegerProperty getCodeProperty() {
		return this.code;
	}
	public SimpleStringProperty getCompanyProperty() {
		return this.company;
	}
	public SimpleStringProperty getPOCProperty() {
		return this.poc;
	}
	public SimpleStringProperty getPhoneProperty() {
		return this.phone;
	}
	public SimpleStringProperty getAddressProperty() {
		return this.address;
	}
	public SimpleStringProperty getCurrencyProperty() {
		return this.currency;
	}

}
