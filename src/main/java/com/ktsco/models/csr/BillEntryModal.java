package com.ktsco.models.csr;

import org.apache.commons.lang.builder.ToStringBuilder;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BillEntryModal {
	
	private SimpleIntegerProperty lineNo; 
	private SimpleIntegerProperty id;
	private SimpleIntegerProperty prodCode;
	private SimpleStringProperty items;
	private SimpleStringProperty unit;
	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty unitprice;
	private SimpleDoubleProperty linetotal;
	
	public BillEntryModal() {
		this.lineNo = new SimpleIntegerProperty();
		this.id = new SimpleIntegerProperty();
		this.prodCode = new SimpleIntegerProperty();
		this.items = new SimpleStringProperty();
		this.unit = new SimpleStringProperty();
		this.quantity = new SimpleDoubleProperty();
		this.unitprice = new SimpleDoubleProperty();
		this.linetotal = new SimpleDoubleProperty();
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
	

	public SimpleIntegerProperty prodCodeProperty() {
		return this.prodCode;
	}
	

	public int getProdCode() {
		return this.prodCodeProperty().get();
	}
	

	public void setProdCode(final int prodCode) {
		this.prodCodeProperty().set(prodCode);
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
	

	public SimpleDoubleProperty quantityProperty() {
		return this.quantity;
	}
	

	public double getQuantity() {
		return this.quantityProperty().get();
	}
	

	public void setQuantity(final double quantity) {
		this.quantityProperty().set(quantity);
	}
	

	public SimpleDoubleProperty unitpriceProperty() {
		return this.unitprice;
	}
	

	public double getUnitprice() {
		return this.unitpriceProperty().get();
	}
	

	public void setUnitprice(final double unitprice) {
		this.unitpriceProperty().set(unitprice);
	}
	

	public SimpleDoubleProperty linetotalProperty() {
		return this.linetotal;
	}
	

	public double getLinetotal() {
		return this.linetotalProperty().get();
	}
	

	public void setLinetotal(final double linetotal) {
		this.linetotalProperty().set(linetotal);
	}

	public SimpleIntegerProperty lineNoProperty() {
		return this.lineNo;
	}
	

	public int getLineNo() {
		return this.lineNoProperty().get();
	}
	

	public void setLineNo(final int lineNo) {
		this.lineNoProperty().set(lineNo);
	}

}
