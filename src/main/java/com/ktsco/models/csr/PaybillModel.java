package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PaybillModel {
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	private SimpleIntegerProperty payID;
	private SimpleIntegerProperty expense_id;
	private SimpleStringProperty payDate;
	private SimpleStringProperty employee;
	private SimpleStringProperty bankAccount;
	private SimpleStringProperty amount;

	public PaybillModel(int receiveID, String receiveDate, String bankAccount, double amount) {
		this.payID = new SimpleIntegerProperty(receiveID);
		this.payDate = new SimpleStringProperty(receiveDate);
		this.bankAccount = new SimpleStringProperty(bankAccount);
		this.amount = new SimpleStringProperty(String.valueOf(decimalFormat.format(amount)));
	}

	public PaybillModel(int receiveID, int billID, String receiveDate, String employee, String bankAccount,
			double amount) {
		this.payID = new SimpleIntegerProperty(receiveID);
		this.expense_id = new SimpleIntegerProperty(billID);
		this.payDate = new SimpleStringProperty(receiveDate);
		this.employee = new SimpleStringProperty(employee);
		this.bankAccount = new SimpleStringProperty(bankAccount);
		this.amount = new SimpleStringProperty(String.valueOf(decimalFormat.format(amount)));
	}

	public SimpleIntegerProperty payIDProperty() {
		return this.payID;
	}
	

	public int getPayID() {
		return this.payIDProperty().get();
	}
	

	public void setPayID(final int payID) {
		this.payIDProperty().set(payID);
	}
	

	public SimpleIntegerProperty expense_idProperty() {
		return this.expense_id;
	}
	

	public int getExpense_id() {
		return this.expense_idProperty().get();
	}
	

	public void setExpense_id(final int expense_id) {
		this.expense_idProperty().set(expense_id);
	}
	

	public SimpleStringProperty payDateProperty() {
		return this.payDate;
	}
	

	public String getPayDate() {
		return this.payDateProperty().get();
	}
	

	public void setPayDate(final String payDate) {
		this.payDateProperty().set(payDate);
	}
	

	public SimpleStringProperty employeeProperty() {
		return this.employee;
	}
	

	public String getEmployee() {
		return this.employeeProperty().get();
	}
	

	public void setEmployee(final String employee) {
		this.employeeProperty().set(employee);
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
	

	public SimpleStringProperty amountProperty() {
		return this.amount;
	}
	

	public String getAmount() {
		return this.amountProperty().get().replace(",", "");
	}
	

	public void setAmount(double amount) {
		this.amountProperty().set(String.valueOf(decimalFormat.format(amount)));
	}
	

	

}
