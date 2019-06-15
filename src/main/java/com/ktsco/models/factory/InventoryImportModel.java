package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryImportModel {
	
	private SimpleIntegerProperty importID;
	private SimpleStringProperty importDate;
	private SimpleStringProperty responsible; 
	
	
	public InventoryImportModel(int importID, String importDate, String responsible) {
		this.importID = new SimpleIntegerProperty(importID);
		this.importDate = new SimpleStringProperty(importDate);
		this.responsible = new SimpleStringProperty(responsible);
	}
	
	//getters
	
	public int getImportID () {
		return importID.get();
	}
	
	public String getImportDate() {
		return importDate.get();
	}
	
	public String getResponsible() {
		return responsible.get();
	}
	
	// Setters
	public void setImportID(int importID) {
		this.importID = new SimpleIntegerProperty(importID);
	}
	
	public void setImportDate (String importDate) {
		this.importDate = new SimpleStringProperty(importDate);
	}
	
	public void setResponsible(String responsible) {
		this.responsible = new SimpleStringProperty(responsible);
	}
	
	//Get propertys 
	
	public SimpleIntegerProperty getImportIDProperty() {
		return importID;
	}
	
	public SimpleStringProperty getImportDateProperty() {
		return importDate;
	}
	
	public SimpleStringProperty getResponsibleProperty() {
		return responsible; 
	}

}
