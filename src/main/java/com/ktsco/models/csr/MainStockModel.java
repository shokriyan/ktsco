package com.ktsco.models.csr;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainStockModel {
	
	private SimpleIntegerProperty code; 
	private SimpleStringProperty product, unit;
	private SimpleDoubleProperty totalImport, totalExport, reminder; 
	
	public MainStockModel(int code, String product, String unit, double totalImport, double totalExport, double reminder) {
		this.code = new SimpleIntegerProperty(code); 
		this.product = new SimpleStringProperty(product);
		this.unit = new SimpleStringProperty(unit);
		this.totalImport = new SimpleDoubleProperty(totalImport);
		this.totalExport = new SimpleDoubleProperty(totalExport);
		this.reminder = new SimpleDoubleProperty(reminder);
		
	}
	
	public int getCode() {
		return this.code.get();
	}
	public String getProduct() {
		return this.product.get();
	}
	public String getUnit() {
		return this.unit.get();
	}
	public double getTotalImport() {
		return this.totalImport.get();
	}
	public double getTotalExport() {
		return this.totalExport.get();
	}
	public double getReminder() {
		return this.reminder.get();
	}
	
	//Setter
	public void setCode(int code) {
		this.code = new SimpleIntegerProperty(code);
	}
	public void setProduct(String product) {
		this.product = new SimpleStringProperty(product);
	}
	public void setUnit(String unit) {
		this.unit = new SimpleStringProperty(unit);
	}
	public void setTotalImport(double totalImport) {
		this.totalImport = new SimpleDoubleProperty(totalImport);
	}
	public void setTotalExport(double totalExport) {
		this.totalExport = new SimpleDoubleProperty(totalExport);
	}
	public void setReminder(double reminder) {
		this.reminder = new SimpleDoubleProperty(reminder);
	}
	
	// get Properties
	
	public SimpleIntegerProperty getCodeProperty() {
		return this.code;
	}
	public SimpleStringProperty getProductProperty() {
		return this.product;
	}
	public SimpleStringProperty getUnitProperty() {
		return this.unit;
	}
	public SimpleDoubleProperty getTotalImportProperty() {
		return this.totalImport;
	}
	public SimpleDoubleProperty getTotalExportProperty() {
		return this.totalExport;
	}
	public SimpleDoubleProperty getReminderProperty() {
		return this.reminder;
	}

	
}
