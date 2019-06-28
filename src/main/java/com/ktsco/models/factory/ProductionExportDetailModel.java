package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductionExportDetailModel {
	private SimpleIntegerProperty number;
	private SimpleIntegerProperty exportID; 
	private SimpleStringProperty item; 
	private SimpleStringProperty unit; 
	private SimpleStringProperty quantity;
	
	public ProductionExportDetailModel(String item, String unit, String quantity) {
		this.item= new SimpleStringProperty(item);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleStringProperty(quantity);
	}
	
	public ProductionExportDetailModel(int number, int exportID, String item, String unit, String quantity) {
		this.number = new SimpleIntegerProperty(number);
		this.exportID = new SimpleIntegerProperty(exportID);
		this.item= new SimpleStringProperty(item);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleStringProperty(quantity);
	}
	
	// getters 
	public int getNumber() {
		return this.number.get();
	}
	public int getExportID() {
		return this.exportID.get();
	}
	public String getItem() {
		return this.item.get();
	}
	public String getUnit() {
		return this.unit.get();
	}
	public String getQuantity() {
		return this.quantity.get();
	}
	
	// Setters
	public void setNumber(int number) {
		this.number = new SimpleIntegerProperty(number);
	}
	public void setExportID(int exportID) {
		this.exportID = new SimpleIntegerProperty(exportID);
	}
	public void setItem(String item) {
		this.item = new SimpleStringProperty(item);
	}
	public void setUnit(String unit) {
		this.unit = new SimpleStringProperty(unit);
	}
	public void setQuantity(String quantity) {
		this.quantity = new SimpleStringProperty(quantity);
	}
	
	// get properties
	public SimpleIntegerProperty getNumberProperty() {
		return this.number;
	}
	public SimpleIntegerProperty getExportIDProperty() {
		return this.exportID;
	}
	public SimpleStringProperty getItemProperty() {
		return this.item;
	}
	public SimpleStringProperty getUnitProperty() {
		return this.unit;
	}
	public SimpleStringProperty getQuantityProperty() {
		return this.quantity;
	}
	
}
