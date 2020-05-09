package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CategoryModel {

	private SimpleIntegerProperty catId;
	private SimpleStringProperty categoryName;
	private SimpleStringProperty categoryCode;
	private SimpleStringProperty mainCat; 

	public CategoryModel(int id, String mainCategory , String categoryName, String catCode) {
		this.catId = new SimpleIntegerProperty(id);
		this.mainCat = new SimpleStringProperty(mainCategory);
		this.categoryName = new SimpleStringProperty(categoryName);
		this.categoryCode = new SimpleStringProperty(catCode);
	}

	// getters value
	public int getCatId() {
		return catId.get();
	}

	public String getCategoryName() {
		return categoryName.get();
	}

	// setters value
	public void setCatId(int catId) {
		this.catId.set(catId);
	}

	public void setCategoryName(String categoryName) {
		this.categoryName.set(categoryName);
	}

	// Getters Property
	public SimpleIntegerProperty catIDProperty() {
		return catId;
	}

	public SimpleStringProperty categoryNameProperty() {
		return categoryName;
	}

	public SimpleStringProperty mainCatProperty() {
		return this.mainCat;
	}
	

	public String getMainCat() {
		return this.mainCatProperty().get();
	}
	

	public void setMainCat(final String mainCat) {
		this.mainCatProperty().set(mainCat);
	}

	public SimpleStringProperty categoryCodeProperty() {
		return this.categoryCode;
	}
	

	public String getCategoryCode() {
		return this.categoryCodeProperty().get();
	}
	

	public void setCategoryCode(final String categoryCode) {
		this.categoryCodeProperty().set(categoryCode);
	}
	
	

}
