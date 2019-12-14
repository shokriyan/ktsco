package com.ktsco.models.mgmt;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductRptModel {
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty items; 
	private SimpleStringProperty unit;
	private SimpleStringProperty quantity; 
	private SimpleStringProperty totalPrice; 
	
	public ProductRptModel (int id, String items, String unit, String quantity, String totalPrice) {
		this.id = new SimpleIntegerProperty(id);
		this.items = new SimpleStringProperty(items);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(quantity)));
		this.totalPrice = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(totalPrice)));
	}
	
	public ProductRptModel (int id, String items, String unit, String totalPrice) {
		this.id = new SimpleIntegerProperty(id);
		this.items = new SimpleStringProperty(items);
		this.unit = new SimpleStringProperty(unit);
		this.totalPrice = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(totalPrice)));
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
	

	public SimpleStringProperty quantityProperty() {
		return this.quantity;
	}
	

	public String getQuantity() {
		return this.quantityProperty().get();
	}
	

	public void setQuantity(final String quantity) {
		this.quantityProperty().set(quantity);
	}
	

	public SimpleStringProperty totalPriceProperty() {
		return this.totalPrice;
	}
	

	public String getTotalPrice() {
		return this.totalPriceProperty().get();
	}
	

	public void setTotalPrice(final String totalPrice) {
		this.totalPriceProperty().set(totalPrice);
	}
	

}
