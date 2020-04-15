package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AmountOweModal {
	
	private SimpleIntegerProperty id; 
	private SimpleStringProperty company; 
	private SimpleStringProperty currency; 
	private SimpleDoubleProperty billTotal; 
	private SimpleDoubleProperty receivedTotal; 
	private SimpleDoubleProperty paidTotal; 
	private SimpleDoubleProperty amountRemained; 
	
	public AmountOweModal (int id, String company, String currency, double billTotal, double receivedTotal, double paidTotal, double amountRemained) {
		this.id = new SimpleIntegerProperty(id);
		this.company = new SimpleStringProperty(company);
		this.currency = new SimpleStringProperty (currency);
		this.billTotal = new SimpleDoubleProperty(billTotal);
		this.receivedTotal = new SimpleDoubleProperty(receivedTotal);
		this.paidTotal = new SimpleDoubleProperty(paidTotal);
		this.amountRemained = new SimpleDoubleProperty(amountRemained);
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
	

	public SimpleStringProperty companyProperty() {
		return this.company;
	}
	

	public String getCompany() {
		return this.companyProperty().get();
	}
	

	public void setCompany(final String company) {
		this.companyProperty().set(company);
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
	

	public SimpleDoubleProperty billTotalProperty() {
		return this.billTotal;
	}
	

	public double getBillTotal() {
		return this.billTotalProperty().get();
	}
	

	public void setBillTotal(final double billTotal) {
		this.billTotalProperty().set(billTotal);
	}
	

	public SimpleDoubleProperty receivedTotalProperty() {
		return this.receivedTotal;
	}
	

	public double getReceivedTotal() {
		return this.receivedTotalProperty().get();
	}
	

	public void setReceivedTotal(final double receivedTotal) {
		this.receivedTotalProperty().set(receivedTotal);
	}
	

	public SimpleDoubleProperty paidTotalProperty() {
		return this.paidTotal;
	}
	

	public double getPaidTotal() {
		return this.paidTotalProperty().get();
	}
	

	public void setPaidTotal(final double paidTotal) {
		this.paidTotalProperty().set(paidTotal);
	}
	

	public SimpleDoubleProperty amountRemainedProperty() {
		return this.amountRemained;
	}
	

	public double getAmountRemained() {
		return this.amountRemainedProperty().get();
	}
	

	public void setAmountRemained(final double amountRemained) {
		this.amountRemainedProperty().set(amountRemained);
	}
	
	
	
	

}
