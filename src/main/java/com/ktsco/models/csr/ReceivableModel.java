package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReceivableModel {
	
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	
	private SimpleIntegerProperty billID; 
	private SimpleStringProperty company; 
	private SimpleStringProperty billdate; 
	private SimpleStringProperty duedate; 
	private SimpleStringProperty billTotal; 
	private SimpleStringProperty currency;
	
	public ReceivableModel (int billID, String company, String billDate, String dueDate, double billTotal, String currency) {
		this.billID = new SimpleIntegerProperty(billID);
		this.company = new SimpleStringProperty (company);
		this.billdate = new SimpleStringProperty (billDate);
		this.duedate = new SimpleStringProperty(dueDate);
		this.billTotal = new SimpleStringProperty(String.valueOf(decimalFormat.format(billTotal)));
		this.currency = new SimpleStringProperty (currency);
		
	}

	public SimpleIntegerProperty getBillIDProperty() {
		return billID;
	}

	public SimpleStringProperty getCompanyProperty() {
		return company;
	}

	public SimpleStringProperty getBilldateProperty() {
		return billdate;
	}

	public SimpleStringProperty getDuedateProperty() {
		return duedate;
	}

	public SimpleStringProperty getBillTotalProperty() {
		return billTotal;
	}

	public SimpleStringProperty getCurrencyProperty() {
		return currency;
	}

	public void setBillID(int billID) {
		this.billID = new SimpleIntegerProperty(billID);
	}

	public void setCompany(String company) {
		this.company = new SimpleStringProperty (company);
	}

	public void setBilldate(String billDate) {
		this.billdate = new SimpleStringProperty (billDate);
	}

	public void setDuedate(String dueDate) {
		this.duedate = new SimpleStringProperty(dueDate);
	}

	public void setBillTotal(double billTotal) {
		this.billTotal = new SimpleStringProperty(String.valueOf(decimalFormat.format(billTotal)));
		
	}

	public void setCurrency(String currency) {
		this.currency = new SimpleStringProperty (currency);
	}
	
	public int getBillID() {
		return this.billID.get();
	}
	public String getCompany() {
		return this.company.get();
	}
	public String getBilLDate() {
		return this.billdate.get();
	}
	public String getDueDate() {
		return this.duedate.get();
	}
	public String getBillTotal() {
		return (this.billTotal.get().contains(",")) ? this.billTotal.get().replace(",","") : this.billTotal.get();
	}
	public String getCurrency() {
		return this.currency.get();
	}
	
	
	

}
