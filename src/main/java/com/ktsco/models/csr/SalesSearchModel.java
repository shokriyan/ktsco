package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesSearchModel {
	
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	private SimpleIntegerProperty billID ; 
	private SimpleStringProperty company; 
	private SimpleStringProperty currency ; 
	private SimpleStringProperty billDate; 
	private SimpleStringProperty dueDate; 
	private SimpleStringProperty billTotal;
	
	public SalesSearchModel (int billID, String company, String currency, String billDate, String dueDate, String billTotal) {
		this.billID = new SimpleIntegerProperty(billID);
		this.company = new SimpleStringProperty(company);
		this.currency = new SimpleStringProperty(currency);
		this.billDate = new SimpleStringProperty(billDate);
		this.dueDate = new SimpleStringProperty(dueDate);
		this.billTotal = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(billTotal)));
	}
	//Getters
	
	public int getBillID() {
		return this.billID.get();
	}
	public String getCompany() {
		return this.company.get();
	}
	public String getCurrency() {
		return this.currency.get();
	}
	public String getBillDate() {
		return this.billDate.get();
	}
	public String getDueDate() {
		return this.dueDate.get();
	}
	public String getBillTotal() {
		return this.billTotal.get();
	}
	//Setters
	public void setBillID(int billID) {
		this.billID = new SimpleIntegerProperty(billID);
	}
	public void setCompany (String company) {
		this.company = new SimpleStringProperty(company);
	}
	public void setCurrency (String currency) {
		this.currency = new SimpleStringProperty(currency);
	}
	public void setBillDate (String billDate) {
		this.billDate = new SimpleStringProperty(billDate);
	}
	public void setDueDate (String dueDate) {
		this.dueDate = new SimpleStringProperty(dueDate);
	}
	public void setBillTotal (String billTotal) {
		this.billTotal = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(billTotal)));
	}
	// get Properties
	public SimpleIntegerProperty getBillIDProperty() {
		return this.billID;
	}
	public SimpleStringProperty getCompanyProperty() {
		return this.company;
	}
	public SimpleStringProperty getCurrencyProperty() {
		return this.currency;
	}
	public SimpleStringProperty getBilLDateProperty() {
		return this.billDate;
	}
	public SimpleStringProperty getDueDateProperty() {
		return this.dueDate;
	}
	public SimpleStringProperty getBillTotalProperty() {
		return this.billTotal;
	}
}
