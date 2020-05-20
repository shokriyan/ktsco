package com.ktsco.models.mgmt;

import com.ktsco.utils.DateUtils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExpenseModal {
	
	private SimpleIntegerProperty expenseId; 
	private SimpleStringProperty expenseDate; 
	private SimpleStringProperty currencyType;
	private SimpleDoubleProperty totalAmount;
	
	public ExpenseModal() {
		this.expenseId = new SimpleIntegerProperty();
		this.expenseDate = new SimpleStringProperty();
		this.currencyType = new SimpleStringProperty();
		this.totalAmount = new SimpleDoubleProperty();
	}

	public SimpleIntegerProperty expenseIdProperty() {
		return this.expenseId;
	}
	

	public int getExpenseId() {
		return this.expenseIdProperty().get();
	}
	

	public void setExpenseId(final int expenseId) {
		this.expenseIdProperty().set(expenseId);
	}
	

	public SimpleStringProperty expenseDateProperty() {
		return this.expenseDate;
	}
	

	public String getExpenseDate() {
		String gregoryDate = DateUtils.convertJalaliToGregory(this.expenseDateProperty().get());
		return gregoryDate;
	}
	

	public void setExpenseDate(final String expenseDate) {
		String jalaliDate = DateUtils.convertGregoryToJalali(expenseDate);
		this.expenseDateProperty().set(jalaliDate);
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
	

	public SimpleDoubleProperty totalAmountProperty() {
		return this.totalAmount;
	}
	

	public double getTotalAmount() {
		return this.totalAmountProperty().get();
	}
	

	public void setTotalAmount(final double totalAmount) {
		this.totalAmountProperty().set(totalAmount);
	}
	

}
