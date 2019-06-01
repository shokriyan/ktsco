package com.ktsco.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InvStockDetailModel {
	private SimpleIntegerProperty importID; 
	private SimpleStringProperty invName;
	private SimpleStringProperty category; 
	private SimpleDoubleProperty importQty; 
	
	
	public InvStockDetailModel(int importId, String invName, String category, double importQty) {
		this.importID.set(importId);
		this.invName.set(invName);
		this.category.set(category);
	}
	
	//Getters, 
	
	public int getImportID() {
		return this.importID.get();
	}
	
	public String getInvName() {
		return this.invName.get();
	}
	
	public String getCategory() {
		return this.category.get();
	}
	
	public double getImportQty() {
		return this.importQty.get();
	}
	
	// Setters
	
	public void setImportID(int importID) {
		this.importID.set(importID);
	}
	
	public void setInvName(String invName) {
		this.invName.set(invName);
	}
	
	public void setCategory(String category) {
		this.category.set(category);
	}
	
	public void setImportQty(double importQty) {
		this.importQty.set(importQty);
	}
	
	//Getters for Property
	
	public SimpleIntegerProperty getImportIDProperty() {
		return importID;
	}
	
	public SimpleStringProperty getInvNameProperty() {
		return invName;
	}
	
	public SimpleStringProperty getCategoryProperty() {
		return category;
	}
	
	public SimpleDoubleProperty getImportQtyProperty() {
		return importQty;
	}


}
