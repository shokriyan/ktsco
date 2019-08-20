package com.ktsco.models.csr;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReceiveModel {
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	
	private SimpleIntegerProperty receiveID; 
	private SimpleIntegerProperty billID; 
	private SimpleStringProperty receiveDate; 
	private SimpleStringProperty employee; 
	private SimpleStringProperty depositType; 
	private SimpleStringProperty receiveAmount;
	
	public ReceiveModel (int receiveID, String receiveDate, double receiveAmount ) {
		this.receiveID = new SimpleIntegerProperty(receiveID); 
		this.receiveDate = new SimpleStringProperty(receiveDate); 
		this.receiveAmount = new SimpleStringProperty(String.valueOf(decimalFormat.format(receiveAmount)));
	}
	
	public ReceiveModel (int receiveID, int billID, String receiveDate, String employee, String depositType, double receiveAmount ) {
		this.receiveID = new SimpleIntegerProperty(receiveID); 
		this.billID = new SimpleIntegerProperty(billID);
		this.receiveDate = new SimpleStringProperty(receiveDate); 
		this.employee = new SimpleStringProperty(employee);
		this.depositType = new SimpleStringProperty(depositType);
		this.receiveAmount = new SimpleStringProperty(String.valueOf(decimalFormat.format(receiveAmount)));
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
	

	public final SimpleIntegerProperty billIDProperty() {
		return this.billID;
	}
	

	public final int getBillID() {
		return this.billIDProperty().get();
	}
	

	public final void setBillID(final int billID) {
		this.billIDProperty().set(billID);
	}
	

	public final SimpleStringProperty receiveDateProperty() {
		return this.receiveDate;
	}
	

	public final String getReceiveDate() {
		return this.receiveDateProperty().get();
	}
	

	public final void setReceiveDate(final String receiveDate) {
		this.receiveDateProperty().set(receiveDate);
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
	

	public final SimpleStringProperty depositTypeProperty() {
		return this.depositType;
	}
	

	public final String getDepositType() {
		return this.depositTypeProperty().get();
	}
	

	public final void setDepositType(final String depositType) {
		this.depositTypeProperty().set(depositType);
	}
	

	public final SimpleStringProperty receiveAmountProperty() {
		return this.receiveAmount;
	}
	

	public final String getReceiveAmount() {
		return (this.receiveAmount.get().contains(",")) ? this.receiveAmount.get().replace(",","") : this.receiveAmount.get();
	}
	

	public final void setReceiveAmount(double receiveAmount) {
		this.receiveAmount = new SimpleStringProperty(String.valueOf(decimalFormat.format(receiveAmount)));
	}
	
	
	

}
