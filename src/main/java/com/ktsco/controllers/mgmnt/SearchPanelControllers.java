package com.ktsco.controllers.mgmnt;

import com.ktsco.enums.Dictionary;
import com.ktsco.utils.Constants;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchPanelControllers {

	private TextField txtFromDate, txtToDate, txtCode;
	private Label labelFromDate, labelTodate, labelComboItems, labelCode;
	private ComboBox<String> comboItems; 
	public boolean isSearchCliked = false;
	public boolean isRefreshClicked = false;
	
	public SearchPanelControllers() {
		labelFromDate = new Label(Dictionary.FromDate.getValue());
		labelTodate = new Label(Dictionary.ToDate.getValue());
		labelCode = new Label();
		txtFromDate = new TextField();
		txtToDate = new TextField();
		txtCode = new TextField();
		labelComboItems = new Label();
		comboItems = new ComboBox<String>();
		labelFromDate.setFont(Constants.elementFonts);
		labelTodate.setFont(Constants.elementFonts);
		labelComboItems.setFont(Constants.elementFonts);
		txtFromDate.setFont(Constants.elementFonts);
		txtToDate.setFont(Constants.elementFonts);
		txtCode.setFont(Constants.elementFonts);
		
	}
	

	public HBox profitLostSearchBox() {
		HBox searchBox = new HBox();

		txtFromDate.setPromptText("1398-01-01");
		txtToDate.setPromptText("1398-12-31");
		

		searchBox.setPadding(new Insets(10, 10, 10, 10));
		searchBox.setSpacing(20);
		searchBox.getChildren().clear();
		searchBox.getChildren().addAll(labelFromDate, txtFromDate, labelTodate, txtToDate);
		return searchBox;
	}

	public HBox BalanceSheetSearchBox() {
		HBox searchBox = new HBox();
		txtFromDate.setPromptText("1398-01-01");
		txtToDate.setPromptText("1398-12-31");
		

		searchBox.setPadding(new Insets(10, 10, 10, 10));
		searchBox.setSpacing(20);
		searchBox.getChildren().clear();
		searchBox.getChildren().addAll(labelTodate, txtToDate);
		return searchBox;
	}
	
	public HBox productionExport() {
		HBox searchBox = new HBox();
		VBox itemsBox = new VBox(5);
		labelComboItems.setText(Dictionary.EmployeeList.getValue());
		itemsBox.getChildren().clear();
		itemsBox.getChildren().addAll(labelComboItems, comboItems);
		HBox.setHgrow(itemsBox, Priority.ALWAYS);
		
		VBox fromDateBox = new VBox(5);
		txtFromDate.setPromptText("1398-01-01");
		
		fromDateBox.getChildren().clear();
		fromDateBox.getChildren().addAll(labelFromDate, txtFromDate);
		HBox.setHgrow(fromDateBox, Priority.ALWAYS);
		
		VBox toDateBox = new VBox(5);
		txtToDate.setPromptText("1398-12-31");
		toDateBox.getChildren().clear();
		toDateBox.getChildren().addAll(labelTodate, txtToDate);
		HBox.setHgrow(toDateBox, Priority.ALWAYS);
		
		VBox codeBox = new VBox(5);
		labelCode.setText(Dictionary.ExportId.getValue());
		codeBox.getChildren().clear();
		codeBox.getChildren().addAll(labelCode, txtCode);
		searchBox.setPadding(Constants.globalPadding);
		searchBox.setSpacing(Constants.globalSpacing);
		searchBox.getChildren().clear();
		searchBox.getChildren().addAll(itemsBox,codeBox, fromDateBox, toDateBox);
		return searchBox;
	}
	
	public String getFromDateText() {
		return txtFromDate.getText();
	}
	public String getToDateText() {
		return txtToDate.getText();
	}
	
	public TextField getFromDateField()  {
		return txtFromDate;
	}
	
	public TextField getToDateField()  {
		return txtToDate;
	}
	
	public ComboBox<String> comboItems() {
		return comboItems;
	}
	
	public String getComboValue() {
		return comboItems.getSelectionModel().getSelectedItem();
	}
	
	public String getCodeText() {
		return txtCode.getText();
	}
	
	public TextField getCodeField() {
		return txtCode;
	}
	

}
