package com.ktsco.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductModel {

	private SimpleIntegerProperty prodId;
	private SimpleStringProperty prodCategory;
	private SimpleStringProperty prodName;
	private SimpleStringProperty prodUm;

	public ProductModel(int prodId, String category, String prodName, String prodUm) {
		this.prodId = new SimpleIntegerProperty(prodId);
		this.prodCategory = new  SimpleStringProperty(category);
		this.prodName = new SimpleStringProperty(prodName);
		this.prodUm = new SimpleStringProperty (prodUm);
	}

	// Getters

	public int getProdId() {
		return prodId.get();
	}

	public String getCategoryId() {
		return prodCategory.get();
	}

	public String getProdName() {
		return prodName.get();
	}

	public String getProdUm() {
		return prodUm.get();
	}
	
	// Setters
	
	public void setProdId(int prodId) {
		this.prodId.set(prodId);
	}
	public void setCategoryId(String category) {
		this.prodCategory.set(category);
	}
	public void setProdName(String prodName) {
		this.prodName.set(prodName);
	}
	public void setProdUm(String prodUm) {
		this.prodUm.set(prodUm);
	}
	
	//Property Getters
	
	public SimpleIntegerProperty getProdIdProperty() {
		return prodId;
	}
	
	public SimpleStringProperty getCategoryIdProperty() {
		return prodCategory;
	}
	
	public SimpleStringProperty getProdNameProperty() {
		return prodName;
	}
	
	public SimpleStringProperty getProdUmProperty() {
		return prodUm;
	}
	
	

}
