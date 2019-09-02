package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AccountsModel {
	
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	private SimpleIntegerProperty code; 
	private SimpleStringProperty bankAccount; 
	private SimpleStringProperty bankName; 
	private SimpleStringProperty openingDate; 
	private SimpleStringProperty openingBalance; 
	private SimpleStringProperty currency;
	
	public AccountsModel (int code, String bankAccount, String bankName, String currency ,String openingDate, Double openingBalance) {
		this.code = new SimpleIntegerProperty(code);
		this.bankAccount = new SimpleStringProperty(bankAccount);
		this.bankName = new SimpleStringProperty (bankName);
		this.currency = new SimpleStringProperty (currency);
		this.openingDate = new SimpleStringProperty (openingDate);
		this.openingBalance = new SimpleStringProperty(String.valueOf(decimalFormat.format(openingBalance)));
	}
	
	// getters
	
	public int getCode () {
		return this.code.get();
	}
	public String getBankAccount() {
		return this.bankAccount.get();
	}
	public String getBankName () {
		return this.bankName.get();
	}
	public String getOpeningDate() {
		return this.openingDate.get();
	}
	public String getOpeningBalance() {
		return (this.openingBalance.get().contains(",")) ? this.openingBalance.get().replace(",","") : this.openingBalance.get();
	}
	
	
	//Setters
	public void setCode(int code) {
		this.code = new SimpleIntegerProperty(code);
	}
	public void setBankAccount (String bankAccount) {
		this.bankAccount = new SimpleStringProperty(bankAccount);
	}
	public void setBankName (String bankName) {
		this.bankName = new SimpleStringProperty(bankName);
	}
	public void setOpeningDate (String openingDate ) {
		this.openingDate = new SimpleStringProperty(openingDate);
	}
	public void setOpeningBalance (Double openingBalance) {
		this.openingBalance = new SimpleStringProperty(String.valueOf(decimalFormat.format(openingBalance)));
	}
	
	// get Properties
	public SimpleIntegerProperty getCodeProperty () {
		return this.code;
	}
	public SimpleStringProperty getBankAccountProperty() {
		return this.bankAccount;
	}
	public SimpleStringProperty getBankNameProperty() {
		return this.bankName;
	}
	public SimpleStringProperty getOpeningDateProperty() {
		return this.openingDate;
	}
	public SimpleStringProperty getOpeningBalanceProperty() {
		return this.openingBalance;
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
	

}
