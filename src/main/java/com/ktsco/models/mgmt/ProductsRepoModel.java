package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductsRepoModel {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty item; 
	private SimpleStringProperty unit;
	private SimpleDoubleProperty imported;
	private SimpleDoubleProperty exported;
	private SimpleDoubleProperty remained;
	private SimpleDoubleProperty unitPrice;
	private SimpleDoubleProperty lineTotal; 
	
	public ProductsRepoModel (int id, String item, String unit, double imported,double exported, double remained, double unitPrice, double lineTotal) {
		this.id = new SimpleIntegerProperty(id); 
		this.item = new SimpleStringProperty(item);
		this.unit = new SimpleStringProperty(unit);
		this.imported = new SimpleDoubleProperty(imported);
		this.exported = new SimpleDoubleProperty(exported);
		this.remained = new SimpleDoubleProperty(remained);
		this.unitPrice = new SimpleDoubleProperty(unitPrice);
		this.lineTotal = new SimpleDoubleProperty(lineTotal);
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
	

	public SimpleDoubleProperty exportedProperty() {
		return this.exported;
	}
	

	public double getExported() {
		return this.exportedProperty().get();
	}
	

	public void setExported(final double exported) {
		this.exportedProperty().set(exported);
	}
	

	public SimpleDoubleProperty remainedProperty() {
		return this.remained;
	}
	

	public double getRemained() {
		return this.remainedProperty().get();
	}
	

	public void setRemained(final double remained) {
		this.remainedProperty().set(remained);
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
