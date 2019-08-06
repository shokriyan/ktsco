package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BankBalanceModel {

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	private SimpleIntegerProperty bankID;
	private SimpleStringProperty bankAccount;
	private SimpleStringProperty bankName;
	private SimpleStringProperty accountBalance;

	public BankBalanceModel(int bankID, String bankAccount, String bankName, Double accountBalance) {
		this.bankID = new SimpleIntegerProperty(bankID);
		this.bankAccount = new SimpleStringProperty(bankAccount);
		this.bankName = new SimpleStringProperty(bankName);
		this.accountBalance = new SimpleStringProperty(String.valueOf(decimalFormat.format(accountBalance)));
	}

	
	// Getters
	public int getBankID () {
		return this.bankID.get();
	}
	public String getBankAccount() {
		return this.bankAccount.get();
	}
	public String getBankName () {
		return this.bankName.get();
	}
	public String getBankBalance () {
		return (this.accountBalance.get().contains(",")) ? this.accountBalance.get().replace(",", "") : this.accountBalance.get();
	}
	
	//setters
	
	public void setBankID(int bankID) {
		this.bankID = new SimpleIntegerProperty(bankID);
	}
	public void setBankAccount (String bankAccount) {
		this.bankAccount = new SimpleStringProperty(bankAccount);
	}
	public void setBankName (String bankName) {
		this.bankName = new SimpleStringProperty(bankName);
	}
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = new SimpleStringProperty(String.valueOf(decimalFormat.format(accountBalance)));
	}
	
	// get properties
	public SimpleIntegerProperty getBankIDProperty() {
		return this.bankID;
	}
	public SimpleStringProperty getBankNameProperty() {
		return this.bankName;
	}
	public SimpleStringProperty getBankAccountProperty() {
		return this.bankAccount;
	}
	public SimpleStringProperty getAccountBalanceProperty() {
		return this.accountBalance;
	}
}
