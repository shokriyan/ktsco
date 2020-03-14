package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SellSummaryModel {
	
	private SimpleIntegerProperty billNo;
	private SimpleStringProperty customerName;
	private SimpleStringProperty currency;
	private SimpleStringProperty billDate;
	private SimpleStringProperty currencyRate;
	private SimpleDoubleProperty originalAmount;
	private SimpleDoubleProperty receivedAmount;
	private SimpleDoubleProperty usdAmount;

	public SellSummaryModel(int billNo, String customerName, String currency, String billDate, String currencyRate,
			double originalAmount, double receivedAmount, double usdAmount) {
		this.billNo = new SimpleIntegerProperty(billNo);
		this.customerName = new SimpleStringProperty(customerName);
		this.currency = new SimpleStringProperty(currency);
		this.billDate = new SimpleStringProperty(billDate);
		this.currencyRate = new SimpleStringProperty(currencyRate);
		this.originalAmount = new SimpleDoubleProperty(originalAmount);
		this.receivedAmount = new SimpleDoubleProperty(receivedAmount);
		this.usdAmount = new SimpleDoubleProperty(usdAmount);

	}

	public SimpleIntegerProperty billNoProperty() {
		return this.billNo;
	}

	public int getBillNo() {
		return this.billNoProperty().get();
	}

	public void setBillNo(final int billNo) {
		this.billNoProperty().set(billNo);
	}

	public SimpleStringProperty customerNameProperty() {
		return this.customerName;
	}

	public String getCustomerName() {
		return this.customerNameProperty().get();
	}

	public void setCustomerName(final String customerName) {
		this.customerNameProperty().set(customerName);
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

	public SimpleStringProperty billDateProperty() {
		return this.billDate;
	}

	public String getBillDate() {
		return this.billDateProperty().get();
	}

	public void setBillDate(final String billDate) {
		this.billDateProperty().set(billDate);
	}

	public SimpleStringProperty currencyRateProperty() {
		return this.currencyRate;
	}

	public String getCurrencyRate() {
		return this.currencyRateProperty().get();
	}

	public void setCurrencyRate(final String currencyRate) {
		this.currencyRateProperty().set(currencyRate);
	}

	public SimpleDoubleProperty originalAmountProperty() {
		return this.originalAmount;
	}

	public double getOriginalAmount() {
		return this.originalAmountProperty().get();
	}

	public void setOriginalAmount(final double originalAmount) {
		this.originalAmountProperty().set(originalAmount);
	}

	public SimpleDoubleProperty receivedAmountProperty() {
		return this.receivedAmount;
	}

	public double getReceivedAmount() {
		return this.receivedAmountProperty().get();
	}

	public void setReceivedAmount(final double receivedAmount) {
		this.receivedAmountProperty().set(receivedAmount);
	}

	public SimpleDoubleProperty usdAmountProperty() {
		return this.usdAmount;
	}

	public double getUsdAmount() {
		return this.usdAmountProperty().get();
	}

	public void setUsdAmount(final double usdAmount) {
		this.usdAmountProperty().set(usdAmount);
	}

}
