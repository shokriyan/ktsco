package com.ktsco.models.factory;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProdDetailModel {

	DecimalFormat df = new DecimalFormat("#.####");

	private SimpleIntegerProperty id;
	private SimpleStringProperty prodName;
	private SimpleStringProperty invName;
	private SimpleDoubleProperty reqQty;
	private SimpleStringProperty invUnit;

	/**
	 * Constructor for Adding and showing on Table
	 * 
	 * @param invName
	 * @param reqQty
	 */

	public ProdDetailModel(String invName, String invUnit , double reqQty) {
		this.invName = new SimpleStringProperty(invName);
		this.reqQty = new SimpleDoubleProperty(getDoubleFormat(reqQty));
		this.invUnit = new SimpleStringProperty(invUnit);

	}

	/**
	 * Constructor used for SQL and updates.
	 * 
	 * @param id
	 * @param prodName
	 * @param invName
	 * @param regQty
	 */
	public ProdDetailModel(int id, String invName,String invUnit, double reqQty) {
		this.id = new SimpleIntegerProperty(id);
		this.invName = new SimpleStringProperty(invName);
		this.invUnit = new SimpleStringProperty(invUnit);
		this.reqQty = new SimpleDoubleProperty(getDoubleFormat(reqQty));

	}

	private double getDoubleFormat(double reqQty) {
		return Double.parseDouble(df.format(reqQty));
	}

	// getters

	public int getId() {
		return id.get();
	}

	public String getProdName() {
		return prodName.get();
	}

	public String getInvName() {
		return invName.get();
	}

	public double getReqQty() {
		return reqQty.get();
	}
	
	public String getInvUnit() {
		return invUnit.get();
	}

	// Setters

	public void setId(int id) {
		this.id.set(id);
	}

	public void setProdName(String prodName) {
		this.prodName.set(prodName);
	}

	public void setInvName(String invName) {
		this.invName.set(invName);
	}

	public void setReqQty(double reqQty) {
		this.reqQty.set(reqQty);
	}
	public void setInvUnit(String invUnit) {
		this.invUnit.set(invUnit);
	}


	// get properties

	public SimpleIntegerProperty getIDProperty() {
		return id;
	}

	public SimpleStringProperty getProdNameProperty() {
		return prodName;
	}

	public SimpleStringProperty getInvNameProperty() {
		return invName;
	}

	public SimpleDoubleProperty getReqQtyProperty() {
		return reqQty;
	}
	
	public SimpleStringProperty getInvUnitProperty() {
		return invUnit;
	}

}
