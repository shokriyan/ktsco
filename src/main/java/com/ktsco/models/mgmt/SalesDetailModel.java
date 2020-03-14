package com.ktsco.models.mgmt;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesDetailModel {
	private SimpleIntegerProperty billCode;
	private SimpleIntegerProperty productCode;
	private SimpleStringProperty productName;
	private SimpleStringProperty saleDate;
	private SimpleStringProperty currencyType;
	private SimpleStringProperty currencyRate;
	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty unitPrice;
	private SimpleDoubleProperty originalLinePrice;
	private SimpleDoubleProperty usdLinePrice;

	public SalesDetailModel(int billCode, int productCode, String productName, String saleDate, String currencyType,
			String currencyRate, double quantity, double unitPrice, double originalLinePrice, double usdLinePrice) {

		this.billCode = new SimpleIntegerProperty(billCode);
		this.productCode = new SimpleIntegerProperty(productCode);
		this.productName = new SimpleStringProperty(productName);
		this.saleDate = new SimpleStringProperty(saleDate);
		this.currencyType = new SimpleStringProperty(currencyType);
		this.currencyRate = new SimpleStringProperty(currencyRate);
		this.quantity = new SimpleDoubleProperty(quantity);
		this.unitPrice = new SimpleDoubleProperty(unitPrice);
		this.originalLinePrice = new SimpleDoubleProperty(originalLinePrice);
		this.usdLinePrice = new SimpleDoubleProperty(usdLinePrice);
	}

	public SimpleIntegerProperty billCodeProperty() {
		return this.billCode;
	}

	public int getBillCode() {
		return this.billCodeProperty().get();
	}

	public void setBillCode(final int billCode) {
		this.billCodeProperty().set(billCode);
	}

	public SimpleIntegerProperty productCodeProperty() {
		return this.productCode;
	}

	public int getProductCode() {
		return this.productCodeProperty().get();
	}

	public void setProductCode(final int productCode) {
		this.productCodeProperty().set(productCode);
	}

	public SimpleStringProperty productNameProperty() {
		return this.productName;
	}

	public String getProductName() {
		return this.productNameProperty().get();
	}

	public void setProductName(final String productName) {
		this.productNameProperty().set(productName);
	}

	public SimpleStringProperty currencyTypeProperty() {
		return this.currencyType;
	}

	public String getCurrencyType() {
		return this.currencyTypeProperty().get();
	}

	public void setCurrencyType(final String currencyType) {
		this.currencyTypeProperty().set(currencyType);
	}

	public SimpleStringProperty currencyRateProperty() {
		return this.currencyRate;
	}

	public String getCurrencyRate() {
		return this.currencyRateProperty().get();
	}

	public void setCurrencyRate(final String currencyRate) {
		this.currencyRateProperty().set(currencyRate);
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

	public SimpleDoubleProperty unitPriceProperty() {
		return this.unitPrice;
	}

	public double getUnitPrice() {
		return this.unitPriceProperty().get();
	}

	public void setUnitPrice(final double unitPrice) {
		this.unitPriceProperty().set(unitPrice);
	}

	public SimpleDoubleProperty originalLinePriceProperty() {
		return this.originalLinePrice;
	}

	public double getOriginalLinePrice() {
		return this.originalLinePriceProperty().get();
	}

	public void setOriginalLinePrice(final double originalLinePrice) {
		this.originalLinePriceProperty().set(originalLinePrice);
	}

	public SimpleDoubleProperty usdLinePriceProperty() {
		return this.usdLinePrice;
	}

	public double getUsdLinePrice() {
		return this.usdLinePriceProperty().get();
	}

	public void setUsdLinePrice(final double usdLinePrice) {
		this.usdLinePriceProperty().set(usdLinePrice);
	}

	public SimpleStringProperty saleDateProperty() {
		return this.saleDate;
	}

	public String getSaleDate() {
		return this.saleDateProperty().get();
	}

	public void setSaleDate(final String saleDate) {
		this.saleDateProperty().set(saleDate);
	}

}
