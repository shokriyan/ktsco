package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReceivedDetailModel {

	private SimpleIntegerProperty receivedId;
	private SimpleIntegerProperty billId;
	private SimpleIntegerProperty employeeId;
	private SimpleStringProperty employeeName;
	private SimpleStringProperty currencyType;
	private SimpleStringProperty currencyRate;
	private SimpleStringProperty receivedDate;
	private SimpleDoubleProperty orignalAmount;
	private SimpleDoubleProperty dolorAmount;

	public ReceivedDetailModel(int receivedId, int billId, int employeeId, String employeeName, String currencyType,
			String currencyRate, String receivedDate, double originalAmount, double dolarAmount) {

		this.receivedId = new SimpleIntegerProperty(receivedId);
		this.billId = new SimpleIntegerProperty(billId);
		this.employeeId = new SimpleIntegerProperty(employeeId);
		this.employeeName = new SimpleStringProperty(employeeName);
		this.currencyType = new SimpleStringProperty(currencyType);
		this.currencyRate = new SimpleStringProperty(currencyRate);
		this.receivedDate = new SimpleStringProperty(receivedDate);
		this.orignalAmount = new SimpleDoubleProperty(originalAmount);
		this.dolorAmount = new SimpleDoubleProperty(dolarAmount);

	}

	public SimpleIntegerProperty receivedIdProperty() {
		return this.receivedId;
	}

	public int getReceivedId() {
		return this.receivedIdProperty().get();
	}

	public void setReceivedId(final int receivedId) {
		this.receivedIdProperty().set(receivedId);
	}

	public SimpleIntegerProperty billIdProperty() {
		return this.billId;
	}

	public int getBillId() {
		return this.billIdProperty().get();
	}

	public void setBillId(final int billId) {
		this.billIdProperty().set(billId);
	}

	public SimpleIntegerProperty employeeIdProperty() {
		return this.employeeId;
	}

	public int getEmployeeId() {
		return this.employeeIdProperty().get();
	}

	public void setEmployeeId(final int employeeId) {
		this.employeeIdProperty().set(employeeId);
	}

	public SimpleStringProperty employeeNameProperty() {
		return this.employeeName;
	}

	public String getEmployeeName() {
		return this.employeeNameProperty().get();
	}

	public void setEmployeeName(final String employeeName) {
		this.employeeNameProperty().set(employeeName);
	}

	public SimpleStringProperty currencyTypeProperty() {
		return this.currencyType;
	}

	public String getCurrencyType() {
		return this.currencyTypeProperty().get();
	}

	public void setCurrencyType(final String currencyType) {
		this.currencyTypeProperty().set(currencyType);
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

	public SimpleStringProperty receivedDateProperty() {
		return this.receivedDate;
	}

	public String getReceivedDate() {
		return this.receivedDateProperty().get();
	}

	public void setReceivedDate(final String receivedDate) {
		this.receivedDateProperty().set(receivedDate);
	}

	public SimpleDoubleProperty orignalAmountProperty() {
		return this.orignalAmount;
	}

	public double getOrignalAmount() {
		return this.orignalAmountProperty().get();
	}

	public void setOrignalAmount(final double orignalAmount) {
		this.orignalAmountProperty().set(orignalAmount);
	}

	public SimpleDoubleProperty dolorAmountProperty() {
		return this.dolorAmount;
	}

	public double getDolorAmount() {
		return this.dolorAmountProperty().get();
	}

	public void setDolorAmount(final double dolorAmount) {
		this.dolorAmountProperty().set(dolorAmount);
	}

}
