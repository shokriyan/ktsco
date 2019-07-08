package com.ktsco.models.csr;

import javafx.beans.property.SimpleStringProperty;

public class CurrencyEntryModel {

	private SimpleStringProperty currency, rate; 
	
	public CurrencyEntryModel(String currency, String rate) {
		this.currency = new SimpleStringProperty(currency);
		this.rate = new SimpleStringProperty(rate);
	}
	
	
	
	//Getters
	public String getCurrency() {
		return this.currency.get();
	}
	public String getRate() {
		return this.rate.get();
	}
	
	//Setters
	public void setCurrency(String currency) {
		this.currency = new SimpleStringProperty(currency);
	}
	public void setRate (String rate) {
		this.rate = new SimpleStringProperty(rate);
	}
	
	//Get properties
	public SimpleStringProperty getCurrencyProperty() {
		return this.currency;
	}
	public SimpleStringProperty getRateProperty() {
		return this.rate;
	}

}
