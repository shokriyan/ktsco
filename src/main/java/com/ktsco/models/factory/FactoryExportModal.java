package com.ktsco.models.factory;

import com.ktsco.utils.DateUtils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FactoryExportModal {
	
	private SimpleIntegerProperty exportId; 
	private SimpleStringProperty exportDate; 
	private SimpleIntegerProperty employeeId; 
	private SimpleStringProperty employeeName; 
	private SimpleIntegerProperty sequenceId; 
	private SimpleIntegerProperty productId; 
	private SimpleStringProperty productName; 
	private SimpleDoubleProperty exportQuantity; 
	
	public FactoryExportModal() {
		this.exportId = new SimpleIntegerProperty();
		this.exportDate = new SimpleStringProperty();
		this.employeeId = new SimpleIntegerProperty();
		this.employeeName = new SimpleStringProperty();
		this.sequenceId = new SimpleIntegerProperty();
		this.productId = new SimpleIntegerProperty();
		this.productName = new SimpleStringProperty();
		this.exportQuantity = new SimpleDoubleProperty();
	}

	public SimpleIntegerProperty exportIdProperty() {
		return this.exportId;
	}
	

	public int getExportId() {
		return this.exportIdProperty().get();
	}
	

	public void setExportId(final int exportId) {
		this.exportIdProperty().set(exportId);
	}
	

	public SimpleStringProperty exportDateProperty() {
		return this.exportDate;
	}
	

	public String getExportDate() {
		String gregoryDate = DateUtils.convertJalaliToGregory(this.exportDateProperty().get());
		return gregoryDate;
	}
	

	public void setExportDate(final String exportDate) {
		String jalaliDate = DateUtils.convertGregoryToJalali(exportDate);
		this.exportDateProperty().set(jalaliDate);
	}
	

	public SimpleIntegerProperty employeeIdProperty() {
		return this.employeeId;
	}
	

	public int getEmployeeId() {
		return this.employeeIdProperty().get();
	}
	

	public void setEmployeeId(final int employeeId) {
		this.employeeIdProperty().set(employeeId);
	}
	

	public SimpleStringProperty employeeNameProperty() {
		return this.employeeName;
	}
	

	public String getEmployeeName() {
		return this.employeeNameProperty().get();
	}
	

	public void setEmployeeName(final String employeeName) {
		this.employeeNameProperty().set(employeeName);
	}
	

	public SimpleIntegerProperty sequenceIdProperty() {
		return this.sequenceId;
	}
	

	public int getSequenceId() {
		return this.sequenceIdProperty().get();
	}
	

	public void setSequenceId(final int sequenceId) {
		this.sequenceIdProperty().set(sequenceId);
	}
	

	public SimpleIntegerProperty productIdProperty() {
		return this.productId;
	}
	

	public int getProductId() {
		return this.productIdProperty().get();
	}
	

	public void setProductId(final int productId) {
		this.productIdProperty().set(productId);
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
	

	public SimpleDoubleProperty exportQuantityProperty() {
		return this.exportQuantity;
	}
	

	public double getExportQuantity() {
		return this.exportQuantityProperty().get();
	}
	

	public void setExportQuantity(final double exportQuantity) {
		this.exportQuantityProperty().set(exportQuantity);
	}
	
	
	

}
