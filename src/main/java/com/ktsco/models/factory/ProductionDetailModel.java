package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductionDetailModel {
	
	private SimpleIntegerProperty detailID;
	private SimpleIntegerProperty productionID; 
	private SimpleStringProperty productName; 
	private SimpleStringProperty productUnit; 
	private SimpleStringProperty productionQuantity;
	
	/**
	 * For Preparing list to Save 
	 * @param productName
	 * @param ProdUnit
	 * @param quantity
	 */
	public ProductionDetailModel(String productName, String ProdUnit, String quantity) {
		this.productName = new SimpleStringProperty(productName);
		this.productUnit = new SimpleStringProperty(ProdUnit);
		this.productionQuantity = new SimpleStringProperty(quantity);
	}
	/**
	 * for Retrieve the list detail
	 * @param id
	 * @param productionID
	 * @param prodName
	 * @param prodUnit
	 * @param quantity
	 */
	public ProductionDetailModel (int id, int productionID, String prodName, String prodUnit, String quantity) {
		this.detailID = new SimpleIntegerProperty(id);
		this.productionID = new SimpleIntegerProperty(productionID);
		this.productName = new SimpleStringProperty(prodName);
		this.productUnit = new SimpleStringProperty(prodUnit);
		this.productionQuantity = new SimpleStringProperty(quantity);
	}
	
	//Getters 
	public int getDetailID() {
		return this.detailID.get();
	}
	public int getProductionID() {
		return this.productionID.get();
	}
	public String getProductName() {
		return this.productName.get();
	}
	public String getProductUnit() {
		return this.productUnit.get();
	}
	public String getProductionQuantity() {
		return this.productionQuantity.get();
	}
	
	//Setters
	public void setDetailID(int id) {
		this.detailID = new SimpleIntegerProperty(id);
	}
	public void setProductionID(int productionID) {
		this.productionID = new SimpleIntegerProperty(productionID);
	}
	public void setProductName (String prodName) {
		this.productName = new SimpleStringProperty(prodName);
	}
	public void setProductUnit(String prodUnit) {
		this.productUnit = new SimpleStringProperty(prodUnit);
	}
	public void setProductionQuantity(String quantity) {
		this.productionQuantity = new SimpleStringProperty(quantity);
	}
	
	// Property Getters
	public SimpleIntegerProperty getDetailIDProperty() {
		return detailID;
	}
	public SimpleIntegerProperty getProductionIDProperty() {
		return productionID;
	}
	
	public SimpleStringProperty getProductNameProperty() {
		return productName;
	}
	public SimpleStringProperty getProductUnitProperty() {
		return productUnit;
	}
	public SimpleStringProperty getProductionQuantityProperty() {
		return productionQuantity;
	}
	
	

}
