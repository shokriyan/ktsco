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
	
	

}
