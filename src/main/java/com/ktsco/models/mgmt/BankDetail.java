package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BankDetail {
	
	private SimpleIntegerProperty id; 
	private SimpleIntegerProperty bankId;
	private SimpleStringProperty bankName; 
	private SimpleStringProperty currency; 
	private SimpleIntegerProperty fromBankId; 
	private SimpleStringProperty fromBankName; 
	private SimpleStringProperty entryDate; 
	private SimpleDoubleProperty orginalAmount; 
	
	public BankDetail (int id, int bankId, String bankName, String currency, int fromBankId, String fromBankName, String entryDate, double amount) {
		this.id = new SimpleIntegerProperty(id);
		this.bankId = new SimpleIntegerProperty(bankId);
		this.bankName = new SimpleStringProperty(bankName);
		this.currency = new SimpleStringProperty(currency);
		this.fromBankId = new SimpleIntegerProperty(fromBankId);
		this.fromBankName = new SimpleStringProperty(fromBankName);
		this.entryDate = new SimpleStringProperty(entryDate);
		this.orginalAmount = new SimpleDoubleProperty(amount);
		
	}

	public SimpleIntegerProperty idProperty() {
		return this.id;
	}
	

	public int getId() {
		return this.idProperty().get();
	}
	

	public void setId(final int id) {
		this.idProperty().set(id);
	}
	

	public SimpleIntegerProperty bankIdProperty() {
		return this.bankId;
	}
	

	public int getBankId() {
		return this.bankIdProperty().get();
	}
	

	public void setBankId(final int bankId) {
		this.bankIdProperty().set(bankId);
	}
	

	public SimpleStringProperty bankNameProperty() {
		return this.bankName;
	}
	

	public String getBankName() {
		return this.bankNameProperty().get();
	}
	

	public void setBankName(final String bankName) {
		this.bankNameProperty().set(bankName);
	}
	

	public SimpleStringProperty currencyProperty() {
		return this.currency;
	}
	

	public String getCurrency() {
		return this.currencyProperty().get();
	}
	

	public void setCurrency(final String currency) {
		this.currencyProperty().set(currency);
	}
	

	public SimpleIntegerProperty fromBankIdProperty() {
		return this.fromBankId;
	}
	

	public int getFromBankId() {
		return this.fromBankIdProperty().get();
	}
	

	public void setFromBankId(final int fromBankId) {
		this.fromBankIdProperty().set(fromBankId);
	}
	

	public SimpleStringProperty fromBankNameProperty() {
		return this.fromBankName;
	}
	

	public String getFromBankName() {
		return this.fromBankNameProperty().get();
	}
	

	public void setFromBankName(final String fromBankName) {
		this.fromBankNameProperty().set(fromBankName);
	}
	

	public SimpleStringProperty entryDateProperty() {
		return this.entryDate;
	}
	

	public String getEntryDate() {
		return this.entryDateProperty().get();
	}
	

	public void setEntryDate(final String entryDate) {
		this.entryDateProperty().set(entryDate);
	}
	

	public SimpleDoubleProperty orginalAmountProperty() {
		return this.orginalAmount;
	}
	

	public double getOrginalAmount() {
		return this.orginalAmountProperty().get();
	}
	

	public void setOrginalAmount(final double orginalAmount) {
		this.orginalAmountProperty().set(orginalAmount);
	}
	
	
	
	
	

}
