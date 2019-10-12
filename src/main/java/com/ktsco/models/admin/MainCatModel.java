package com.ktsco.models.admin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainCatModel {
	
	private SimpleIntegerProperty code; 
	private SimpleStringProperty desc; 
	
	public MainCatModel (int code, String desc) {
		this.code = new SimpleIntegerProperty(code);
		this.desc = new SimpleStringProperty(desc);
	}

	public SimpleIntegerProperty codeProperty() {
		return this.code;
	}
	

	public int getCode() {
		return this.codeProperty().get();
	}
	

	public void setCode(final int code) {
		this.codeProperty().set(code);
	}
	

	public SimpleStringProperty descProperty() {
		return this.desc;
	}
	

	public String getDesc() {
		return this.descProperty().get();
	}
	

	public void setDesc(final String desc) {
		this.descProperty().set(desc);
	}
	
	
	

}
