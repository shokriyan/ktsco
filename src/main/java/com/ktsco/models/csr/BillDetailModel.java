package com.ktsco.models.csr;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BillDetailModel {

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	private SimpleIntegerProperty id;
	private SimpleStringProperty prodCode;
	private SimpleIntegerProperty lineNumber;
	private SimpleStringProperty items;
	private SimpleStringProperty unit;
	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty unitprice;
	private SimpleStringProperty linetotal;

	public BillDetailModel(int lineNumber) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.id = new SimpleIntegerProperty(0);
		this.prodCode = new SimpleStringProperty();
		this.lineNumber = new SimpleIntegerProperty(lineNumber);
		this.items = new SimpleStringProperty();
		this.unit = new SimpleStringProperty();
		this.quantity = new SimpleDoubleProperty(0);
		this.unitprice = new SimpleDoubleProperty(0);
		this.linetotal = new SimpleStringProperty("0.0");

	}

	public BillDetailModel(int id,String prodCode, int lineNumber, String product, String unit, double quantity, double unitprice, BigDecimal linetotal) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.id = new SimpleIntegerProperty(id);
		this.prodCode = new SimpleStringProperty(prodCode);
		this.lineNumber = new SimpleIntegerProperty(lineNumber);
		this.items = new SimpleStringProperty(product);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleDoubleProperty(quantity);
		this.unitprice = new SimpleDoubleProperty(unitprice);
		this.linetotal = new SimpleStringProperty(linetotal.toPlainString());

	}

	// getters
	public int getID() {
		return this.id.get();
	}
	public int getLineNumber() {
		return this.lineNumber.get();
	}
	public String getItems() {
		return this.items.get();
	}
	public String getUnit() {
		return this.unit.get();
	}
	public double getQuantity() {
		return this.quantity.get();
	}
	public double getUnitPrice() {
		return this.unitprice.get();
	}
	public String getLineTotal() {
		return this.linetotal.get();
	}
	
	//Setter
	public void setID(int id) {
		this.id = new SimpleIntegerProperty(id);
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = new SimpleIntegerProperty(lineNumber);
	}
	public void setItems (String product) {
		this.items = new SimpleStringProperty(product);
	}
	public void setUnit (String unit) {
		this.unit = new SimpleStringProperty(unit);
	}
	public void setQuantity(double quantity) {
		this.quantity = new SimpleDoubleProperty(quantity);
	}
	public void setUnitPrice(double unitprice) {
		this.unitprice = new SimpleDoubleProperty(unitprice);
	}
	public void setLineTotal (BigDecimal linetotal) {
		this.linetotal = new SimpleStringProperty(linetotal.toPlainString());
	}
	
	// Get Properties
	
	public SimpleIntegerProperty getIDProperty() {
		return this.id;
	}
	public SimpleIntegerProperty getLineNumberProperty() {
		return this.lineNumber;
	}
	public SimpleStringProperty getItemsProperty() {
		return this.items;
	}
	public SimpleStringProperty getUnitProperty() {
		return this.unit;
	}
	public SimpleDoubleProperty getQuantityProperty() {
		return this.quantity;
	}
	public SimpleDoubleProperty getUnitPriceProperty() {
		return this.unitprice; 
	}
	public SimpleStringProperty getLineTotalProperty() {
		return this.linetotal;
	}

	public SimpleStringProperty prodCodeProperty() {
		return this.prodCode;
	}
	

	public String getProdCode() {
		return this.prodCodeProperty().get();
	}
	

	public void setProdCode(final String prodCode) {
		this.prodCodeProperty().set(prodCode);
	}
	

}
