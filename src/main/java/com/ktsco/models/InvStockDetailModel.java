package com.ktsco.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InvStockDetailModel {
	private SimpleIntegerProperty importID; 
	private SimpleStringProperty invName;
	private SimpleStringProperty um; 
	private SimpleStringProperty importQty; 
	
	public InvStockDetailModel(String invName, String um, String importQty) {
		this.invName = new SimpleStringProperty(invName);
		this.um = new SimpleStringProperty(um);
		this.importQty = new SimpleStringProperty(importQty);
	}
	
	
	public InvStockDetailModel(int importId, String invName, String um, String importQty) {
		this.importID = new SimpleIntegerProperty(importId);
		this.invName = new SimpleStringProperty(invName);
		this.um = new SimpleStringProperty(um);
		this.importQty = new SimpleStringProperty(importQty);
	}
	
	
	//Getters, 
	
	public int getImportID() {
		return this.importID.get();
	}
	
	public String getInvName() {
		return this.invName.get();
	}
	
	public String getUm() {
		return this.um.get();
	}
	
	public String getImportQty() {
		return this.importQty.get();
	}
	
	// Setters
	
	public void setImportID(int importID) {
		this.importID = new SimpleIntegerProperty(importID);
	}
	
	public void setInvName(String invName) {
		this.invName = new SimpleStringProperty(invName);
	}
	
	public void setUm(String um) {
		this.um = new SimpleStringProperty (um);
	}
	
	public void setImportQty(String importQty) {
		this.importQty = new SimpleStringProperty(importQty);
	}
	
	//Getters for Property
	
	public SimpleIntegerProperty getImportIDProperty() {
		return importID;
	}
	
	public SimpleStringProperty getInvNameProperty() {
		return invName;
	}
	
	public SimpleStringProperty getUMProperty() {
		return um;
	}
	
	public SimpleStringProperty getImportQtyProperty() {
		return importQty;
	}

	

}
