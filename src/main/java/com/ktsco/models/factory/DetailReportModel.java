package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DetailReportModel {
	
	private SimpleIntegerProperty idNumber; 
	private SimpleStringProperty item;
	private SimpleStringProperty unit; 
	private SimpleStringProperty date; 
	private SimpleStringProperty quantity; 
	
	public DetailReportModel (int no, String item, String unit, String date, String quantity) {
		this.idNumber = new SimpleIntegerProperty(no);
		this.item = new SimpleStringProperty(item);
		this.unit = new SimpleStringProperty(unit);
		this.date = new SimpleStringProperty(date);
		this.quantity = new SimpleStringProperty(quantity);
	}
	//Getters
	public int getIdNumber() {
		return this.idNumber.get();
	}
	public String getItem() {
		return this.item.get();
	}
	public String getUnit() {
		return this.unit.get();
	}
	public String getDate() {
		return this.date.get();
	}
	public String getQuantity() {
		return this.quantity.get();
	}
	
	//Setters
	public void setIdNumber(int id) {
		this.idNumber = new SimpleIntegerProperty(id);
	}
	public void setItem(String item) {
		this.item = new SimpleStringProperty(item);
	}
	public void setUnit(String unit) {
		this.unit = new SimpleStringProperty(unit);
	}
	public void setDate(String date) {
		this.date = new SimpleStringProperty(date);
	}
	public void setQuantity(String quantity) {
		this.quantity = new SimpleStringProperty(quantity);
	}
	
	//get properties
	public SimpleIntegerProperty getIdNumberProperty() {
		return this.idNumber;
	}
	public SimpleStringProperty getItemProperty() {
		return this.item;
	}
	public SimpleStringProperty getUnitProperty() {
		return this.unit;
	}
	public SimpleStringProperty getDateProperty() {
		return this.date;
	}
	public SimpleStringProperty getQuantityProperty() {
		return this.quantity;
	}
}
