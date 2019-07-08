package com.ktsco.models.csr;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CurrencyModel {

	private SimpleIntegerProperty idNumber; 
	private SimpleStringProperty currency, entryDate, rate; 
	
	public CurrencyModel(String currency, String rate) {
		this.currency = new SimpleStringProperty(currency);
		this.rate = new SimpleStringProperty(rate);
	}
	
	public CurrencyModel(int idNumber, String currency, String entryDate, String rate) {
		this.idNumber = new SimpleIntegerProperty(idNumber);
		this.currency = new SimpleStringProperty(currency);
		this.entryDate  = new SimpleStringProperty(entryDate);
		this.rate = new SimpleStringProperty(rate);
	}
	
	//Getters
	public int getIDNumber() {
		return this.idNumber.get();
	}
	public String getCurrency() {
		return this.currency.get();
	}
	public String getEntryDate() {
		return this.entryDate.get();
	}
	public String getRate() {
		return this.rate.get();
	}
	
	//Setters
	public void setIDNumber(int idNumber) {
		this.idNumber = new SimpleIntegerProperty(idNumber);
	}
	public void setCurrency(String currency) {
		this.currency = new SimpleStringProperty(currency);
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = new SimpleStringProperty(entryDate);
	}
	public void setRate (String rate) {
		this.rate = new SimpleStringProperty(rate);
	}
	
	//Get properties
	public SimpleIntegerProperty getIdNumberProperty() {
		return this.idNumber;
	}
	public SimpleStringProperty getCurrencyProperty() {
		return this.currency;
	}
	public SimpleStringProperty getEntryDateProperty() {
		return this.entryDate;
	}
	public SimpleStringProperty getRateProperty() {
		return this.rate;
	}
}
