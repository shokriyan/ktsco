package com.ktsco.models.csr;


import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class DepositModel {
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	
	private SimpleIntegerProperty receiveID; 
	private SimpleIntegerProperty billID; 
	private SimpleStringProperty currency; 
	private SimpleStringProperty amount;
	private CheckBox select;

	public DepositModel (int receiveID, int billID, String currency, double amount) {
		this.receiveID = new SimpleIntegerProperty(receiveID);
		this.billID = new SimpleIntegerProperty(billID);
		this.currency = new SimpleStringProperty(currency);
		this.amount = new SimpleStringProperty(String.valueOf(decimalFormat.format(amount)));
		this.select = new CheckBox();
		
	}
	public SimpleIntegerProperty receiveIDProperty() {
		return this.receiveID;
	}
	
	public int getReceiveID() {
		return this.receiveIDProperty().get();
	}
	
	public void setReceiveID(final int receiveID) {
		this.receiveIDProperty().set(receiveID);
	}
	
	public SimpleIntegerProperty billIDProperty() {
		return this.billID;
	}
	
	public int getBillID() {
		return this.billIDProperty().get();
	}
	
	public void setBillID(final int billID) {
		this.billIDProperty().set(billID);
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
	
	public SimpleStringProperty amountProperty() {
		return this.amount;
	}
	
	public String getAmount() {
		return this.amountProperty().get().replace(",", "");
	}
	
	public void setAmount(final double amount) {
		this.amountProperty().set(String.valueOf(decimalFormat.format(amount)));
	}
	
	public CheckBox getSelect() {
		return select;
	}
	public void setSelect(CheckBox select) {
		this.select = select;
	}
	
	

}
