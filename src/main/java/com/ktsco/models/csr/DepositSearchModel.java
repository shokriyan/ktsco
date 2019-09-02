package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DepositSearchModel {
	
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	private SimpleIntegerProperty receiveID; 
	private SimpleStringProperty bank; 
	private SimpleStringProperty employee; 
	private SimpleStringProperty voucherNo; 
	private SimpleStringProperty depositDate; 
	private SimpleStringProperty depositAmount; 
	
	public DepositSearchModel(int receiveID, String bank, String employee, String voucherNo, String depositDate, double depostiAmount) {
		this.receiveID = new SimpleIntegerProperty(receiveID);
		this.bank = new SimpleStringProperty(bank);
		this.employee = new SimpleStringProperty(employee);
		this.voucherNo = new SimpleStringProperty(voucherNo);
		this.depositDate = new SimpleStringProperty(depositDate);
		this.depositAmount = new SimpleStringProperty(String.valueOf(decimalFormat.format(depostiAmount)));
		
	}

	public final SimpleIntegerProperty receiveIDProperty() {
		return this.receiveID;
	}
	

	public final int getReceiveID() {
		return this.receiveIDProperty().get();
	}
	

	public final void setReceiveID(final int receiveID) {
		this.receiveIDProperty().set(receiveID);
	}
	

	public final SimpleStringProperty bankProperty() {
		return this.bank;
	}
	

	public final String getBank() {
		return this.bankProperty().get();
	}
	

	public final void setBank(final String bank) {
		this.bankProperty().set(bank);
	}
	

	public final SimpleStringProperty employeeProperty() {
		return this.employee;
	}
	

	public final String getEmployee() {
		return this.employeeProperty().get();
	}
	

	public final void setEmployee(final String employee) {
		this.employeeProperty().set(employee);
	}
	

	public final SimpleStringProperty voucherNoProperty() {
		return this.voucherNo;
	}
	

	public final String getVoucherNo() {
		return this.voucherNoProperty().get();
	}
	

	public final void setVoucherNo(final String voucherNo) {
		this.voucherNoProperty().set(voucherNo);
	}
	

	public final SimpleStringProperty depositDateProperty() {
		return this.depositDate;
	}
	

	public final String getDepositDate() {
		return this.depositDateProperty().get();
	}
	

	public final void setDepositDate(final String depositDate) {
		this.depositDateProperty().set(depositDate);
	}
	

	public final SimpleStringProperty depositAmountProperty() {
		return this.depositAmount;
	}
	

	public final String getDepositAmount() {
		return this.depositAmountProperty().get().replace(",", "");
	}
	

	public final void setDepositAmount(double depositAmount) {
		this.depositAmountProperty().set(String.valueOf(decimalFormat.format(depositAmount)));
	}
	
	

}
