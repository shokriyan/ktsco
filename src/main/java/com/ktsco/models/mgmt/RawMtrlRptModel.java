package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RawMtrlRptModel {
	
	private SimpleIntegerProperty code; 
	private SimpleStringProperty item; 
	private SimpleStringProperty unit; 
	private SimpleDoubleProperty imported; 
	private SimpleDoubleProperty used; 
	private SimpleDoubleProperty unitPrice;
	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty lineTotal; 
	
	public RawMtrlRptModel (int code, String item, String unit, double imported, double used, double unitPrice, double lineTotal){
		this.code = new SimpleIntegerProperty(code);
		this.item = new SimpleStringProperty(item);
		this.unit = new SimpleStringProperty(unit);
		this.imported = new SimpleDoubleProperty(imported);
		this.used = new SimpleDoubleProperty(used);
		this.unitPrice = new SimpleDoubleProperty(unitPrice);
		this.lineTotal = new SimpleDoubleProperty(lineTotal);
	}
	
	public RawMtrlRptModel (int code, String item, String unit, double quantity, double unitPrice, double lineTotal){
		this.code = new SimpleIntegerProperty(code);
		this.item = new SimpleStringProperty(item);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleDoubleProperty(quantity);
		this.unitPrice = new SimpleDoubleProperty(unitPrice);
		this.lineTotal = new SimpleDoubleProperty(lineTotal);
	}
	

	public SimpleIntegerProperty codeProperty() {
		return this.code;
	}
	

	public int getCode() {
		return this.codeProperty().get();
	}
	

	public void setCode(final int code) {
		this.codeProperty().set(code);
	}
	

	public SimpleStringProperty itemProperty() {
		return this.item;
	}
	

	public String getItem() {
		return this.itemProperty().get();
	}
	

	public void setItem(final String item) {
		this.itemProperty().set(item);
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
	

	public SimpleDoubleProperty importedProperty() {
		return this.imported;
	}
	

	public double getImported() {
		return this.importedProperty().get();
	}
	

	public void setImported(final double imported) {
		this.importedProperty().set(imported);
	}
	

	public SimpleDoubleProperty usedProperty() {
		return this.used;
	}
	

	public double getUsed() {
		return this.usedProperty().get();
	}
	

	public void setUsed(final double used) {
		this.usedProperty().set(used);
	}
	

	public SimpleDoubleProperty unitPriceProperty() {
		return this.unitPrice;
	}
	

	public double getUnitPrice() {
		return this.unitPriceProperty().get();
	}
	

	public void setUnitPrice(final double unitPrice) {
		this.unitPriceProperty().set(unitPrice);
	}

	public SimpleDoubleProperty quantityProperty() {
		return this.quantity;
	}
	

	public double getQuantity() {
		return this.quantityProperty().get();
	}
	

	public void setQuantity(final double quantity) {
		this.quantityProperty().set(quantity);
	}

	public SimpleDoubleProperty lineTotalProperty() {
		return this.lineTotal;
	}
	

	public double getLineTotal() {
		return this.lineTotalProperty().get();
	}
	

	public void setLineTotal(final double lineTotal) {
		this.lineTotalProperty().set(lineTotal);
	}
	
	
	

}
