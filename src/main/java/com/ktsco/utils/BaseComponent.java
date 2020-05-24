package com.ktsco.utils;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class BaseComponent {
	
	public TextField createTextField(String prompText, double size) {
		TextField textField = new TextField();
		HBox.setHgrow(textField, Priority.ALWAYS);
		textField.setPrefWidth(size);
		textField.setPrefHeight(Constants.fieldsPrefHeight);
		textField.setPromptText(prompText);
		textField.setFont(Constants.elementFonts);
		return textField;
	}
	
	public Label createLabel (String text, double size) {
		Label label = new Label();
		HBox.setHgrow(label, Priority.ALWAYS);
		label.setPrefWidth(size);
		label.setFont(Constants.elementFonts);
		label.setPrefHeight(Constants.fieldsPrefHeight);
		label.setText(text);
		
		return label;
		
	}
	
	public Button createButton (double size, String text) {
		Button button = new Button();
		button.setFont(Constants.elementFonts);
		if (size == 0)
			button.setPrefWidth(size);
		button.setText(text);
		button.setPrefHeight(Constants.fieldsPrefHeight);
		return button;
	}
	
	public ComboBox<String> createComboBox(double size) {
		ComboBox<String> comboBox = new ComboBox <String> ();
		HBox.setHgrow(comboBox, Priority.ALWAYS);
		comboBox.setPrefWidth(size);
		comboBox.setPrefHeight(Constants.fieldsPrefHeight);
		return comboBox;
	}
	
	

}
