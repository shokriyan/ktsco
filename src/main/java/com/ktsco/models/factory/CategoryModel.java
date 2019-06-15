package com.ktsco.models.factory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CategoryModel {

	private SimpleIntegerProperty catId;
	private SimpleStringProperty categoryName;

	public CategoryModel(int id, String categoryName) {
		this.catId = new SimpleIntegerProperty(id);
		this.categoryName = new SimpleStringProperty(categoryName);
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

}