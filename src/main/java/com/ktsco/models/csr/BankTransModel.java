package com.ktsco.models.csr;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BankTransModel {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty voucherNo ; 
	private SimpleStringProperty bankAccount; 
	private SimpleStringProperty bankName; 
	private SimpleStringProperty entryDate; 
	private SimpleStringProperty currency; 
	private SimpleDoubleProperty amount; 
	
	public BankTransModel (int id,String voucherNo, String bankAccount, String bankName, String entryDate, String currency, double amount) {
		this.id = new SimpleIntegerProperty(id);
		this.voucherNo = new SimpleStringProperty(voucherNo);
		this.bankAccount = new SimpleStringProperty(bankAccount); 
		this.bankName = new SimpleStringProperty(bankName);
		this.entryDate = new SimpleStringProperty(entryDate); 
		this.currency = new SimpleStringProperty(currency); 
		this.amount = new SimpleDoubleProperty(amount); 
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
	

	public SimpleStringProperty bankAccountProperty() {
		return this.bankAccount;
	}
	

	public String getBankAccount() {
		return this.bankAccountProperty().get();
	}
	

	public void setBankAccount(final String bankAccount) {
		this.bankAccountProperty().set(bankAccount);
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
	

	public SimpleStringProperty entryDateProperty() {
		return this.entryDate;
	}
	

	public String getEntryDate() {
		return this.entryDateProperty().get();
	}
	

	public void setEntryDate(final String entryDate) {
		this.entryDateProperty().set(entryDate);
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
	

	public SimpleDoubleProperty amountProperty() {
		return this.amount;
	}
	

	public double getAmount() {
		return this.amountProperty().get();
	}
	

	public void setAmount(final double amount) {
		this.amountProperty().set(amount);
	}

	public SimpleStringProperty voucherNoProperty() {
		return this.voucherNo;
	}
	

	public String getVoucherNo() {
		return this.voucherNoProperty().get();
	}
	

	public void setVoucherNo(final String voucherNo) {
		this.voucherNoProperty().set(voucherNo);
	}
	
	
	
	

}
