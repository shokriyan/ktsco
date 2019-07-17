package com.ktsco.models.csr;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BillDetailModel {

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	private SimpleIntegerProperty id;
	private SimpleStringProperty items;
	private SimpleStringProperty unit;
	private SimpleStringProperty quantity;
	private SimpleStringProperty unitprice;
	private SimpleStringProperty linetotal;

	public BillDetailModel(int id) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.id = new SimpleIntegerProperty(id);
		this.items = new SimpleStringProperty();
		this.unit = new SimpleStringProperty();
		this.quantity = new SimpleStringProperty(decimalFormat.format(0));
		this.unitprice = new SimpleStringProperty(decimalFormat.format(0));
		this.linetotal = new SimpleStringProperty(decimalFormat.format(0));

	}

	public BillDetailModel(int id, String product, String unit, String quantity, String unitprice, String linetotal) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.id = new SimpleIntegerProperty(id);
		this.items = new SimpleStringProperty(product);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleStringProperty(decimalFormat.format(quantity));
		this.unitprice = new SimpleStringProperty(decimalFormat.format(unitprice));
		this.linetotal = new SimpleStringProperty(decimalFormat.format(linetotal));

	}

	// getters
	public int getID() {
		return this.id.get();
	}
	public String getItems() {
		return this.items.get();
	}
	public String getUnit() {
		return this.unit.get();
	}
	public String getQuantity() {
		return this.quantity.get();
	}
	public String getUnitPrice() {
		return this.unitprice.get();
	}
	public String getLineTotal() {
		return this.linetotal.get();
	}
	
	//Setter
	public void setID(int id) {
		this.id = new SimpleIntegerProperty(id);
	}
	public void setItems (String product) {
		this.items = new SimpleStringProperty(product);
	}
	public void setUnit (String unit) {
		this.unit = new SimpleStringProperty(unit);
	}
	public void setQuantity(String quantity) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.quantity = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(quantity)));
	}
	public void setUnitPrice(String unitprice) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.unitprice = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(unitprice)));
	}
	public void setLineTotal (String linetotal) {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		this.linetotal = new SimpleStringProperty(decimalFormat.format(Double.parseDouble(linetotal)));
	}
	
	// Get Properties
	
	public SimpleIntegerProperty getIDProperty() {
		return this.id;
	}
	public SimpleStringProperty getItemsProperty() {
		return this.items;
	}
	public SimpleStringProperty getUnitProperty() {
		return this.unit;
	}
	public SimpleStringProperty getQuantityProperty() {
		return this.quantity;
	}
	public SimpleStringProperty getUnitPriceProperty() {
		return this.unitprice; 
	}
	public SimpleStringProperty getLineTotalProperty() {
		return this.linetotal;
	}

}
