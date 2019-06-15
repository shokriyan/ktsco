package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryModel {
	
	private SimpleIntegerProperty invID ; 
	private SimpleStringProperty invItem; 
	private SimpleStringProperty invCategory; 
	private SimpleStringProperty invUM;
	
	public InventoryModel(int id, String item, String category, String um) {
		this.invID = new SimpleIntegerProperty(id);
		this.invItem = new SimpleStringProperty(item);
		this.invCategory = new SimpleStringProperty(category);
		this.invUM = new SimpleStringProperty(um);
	}
	
	// getter methods 
	public int getInvID() {
		return invID.get();
	}
	
	public String getInvItem() {
		return invItem.get();
	}
	
	public String getInvCategory() {
		return invCategory.get();
	}
	
	public String getInvUM() {
		return invUM.get();
	}
	
	// Setters 
	public void setInvID(int invID) {
		this.invID.set(invID);
	}
	public void setInvItem(String invItem) {
		this.invItem.set(invItem);
	}
	
	public void setInvCategory(String invCategory) {
		this.invCategory.set(invCategory);
	}
	
	public void setInvUM(String invUM) {
		this.invUM.set(invUM);
	}
	
	// get properties
	public SimpleIntegerProperty getInvIDProperty() {
		return invID;
	}
	
	public SimpleStringProperty getInvItemProperty() {
		return invItem;
	}
	public SimpleStringProperty getInvCategoryProperty() {
		return invCategory;
	}
	public SimpleStringProperty getInvUMProperty() {
		return invUM;
	}

}
