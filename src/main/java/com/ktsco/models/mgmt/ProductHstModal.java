package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductHstModal {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty items; 
	private SimpleStringProperty unit;
	private SimpleDoubleProperty dolorAmount; 
	private SimpleStringProperty date;
	
	public ProductHstModal (int id, String items, String unit, double dolorAmount, String date) {
		this.id = new SimpleIntegerProperty(id);
		this.items = new SimpleStringProperty(items);
		this.unit = new SimpleStringProperty(unit);
		this.date = new SimpleStringProperty(date);
		this.dolorAmount = new SimpleDoubleProperty(dolorAmount);
	}

	public SimpleIntegerProperty idProperty() {
		return this.id;
	}
	

	public int getId() {
		return this.idProperty().get();
	}
	

	public void setId(final int id) {
		this.idProperty().set(id);
	}
	

	public SimpleStringProperty itemsProperty() {
		return this.items;
	}
	

	public String getItems() {
		return this.itemsProperty().get();
	}
	

	public void setItems(final String items) {
		this.itemsProperty().set(items);
	}
	

	public SimpleStringProperty unitProperty() {
		return this.unit;
	}
	

	public String getUnit() {
		return this.unitProperty().get();
	}
	

	public void setUnit(final String unit) {
		this.unitProperty().set(unit);
	}
	

	public SimpleDoubleProperty dolorAmountProperty() {
		return this.dolorAmount;
	}
	

	public double getDolorAmount() {
		return this.dolorAmountProperty().get();
	}
	

	public void setDolorAmount(final double dolorAmount) {
		this.dolorAmountProperty().set(dolorAmount);
	}
	

	public SimpleStringProperty dateProperty() {
		return this.date;
	}
	

	public String getDate() {
		return this.dateProperty().get();
	}
	

	public void setDate(final String date) {
		this.dateProperty().set(date);
	}
	

}
