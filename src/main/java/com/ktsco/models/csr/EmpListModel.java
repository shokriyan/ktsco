package com.ktsco.models.csr;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmpListModel {
	
	private SimpleIntegerProperty empId; 
	private SimpleStringProperty empFullName, position; 
	
	public EmpListModel(int empId, String empFullName, String position) {
		this.empId = new SimpleIntegerProperty(empId);
		this.empFullName = new SimpleStringProperty(empFullName);
		this.position = new SimpleStringProperty(position);
	}
	
	//Getters
	
	public int getEmpID() {
		return this.empId.get(); 
	}
	public String getEmpFullName() {
		return this.empFullName.get();
	}
	public String getPosition() {
		return this.position.get();
	}
	
	//Setters
	public void setEmpID(int empID) {
		this.empId.set(empID);
	}
	public void setEmpFullName(String empFullName) {
		this.empFullName.set(empFullName);
	}
	public void setPostion(String postion) {
		this.position.set(postion);
	}
	
	//Get Object Properties
	
	public SimpleIntegerProperty getEmpIDProperty() {
		return empId;
	}
	public SimpleStringProperty getEmpFullNameProperty() {
		return empFullName;
	}
	public SimpleStringProperty getPositionProperty() {
		return position;
	}
	

}
