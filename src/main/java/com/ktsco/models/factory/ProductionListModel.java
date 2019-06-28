package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductionListModel {
	
	private SimpleIntegerProperty productionID; 
	private SimpleStringProperty productionDate; 
	private SimpleStringProperty employeeName; 
	
	public ProductionListModel (int id, String prodDate, String empName) {
		this.productionID = new SimpleIntegerProperty(id);
		this.productionDate = new SimpleStringProperty (prodDate);
		this.employeeName = new SimpleStringProperty(empName);
		
	}
	
	//Getters 
	public int getProductionID() {
		return this.productionID.get();
	}
	public String getProductionDate() {
		return this.productionDate.get();
	}
	public String getEmployeeName() {
		return this.employeeName.get();
	}
	
	//Setters
	public void setProductionID(int id) {
		this.productionID = new SimpleIntegerProperty(id);
	}
	public void setProductionDate(String date) {
		this.productionDate = new SimpleStringProperty(date);
	}
	public void setEmployeeName(String empName) {
		this.productionDate = new SimpleStringProperty(empName);
	}
	
	//Getters for Properties
	public SimpleIntegerProperty getProductionIDProperty() {
		return productionID;
	}
	public SimpleStringProperty getProductionDateProperty() {
		return productionDate;
	}
	public SimpleStringProperty getEmployeeNameProperty() {
		return employeeName;
	}

}
