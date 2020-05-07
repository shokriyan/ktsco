package com.ktsco.controllers.mgmnt;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchPanelControllers {

	private TextField txtFromDate, txtToDate;
	private Label labelFromDate, labelTodate;
	
	public boolean isSearchCliked = false;
	public boolean isRefreshClicked = false;
	
	public SearchPanelControllers() {
		labelFromDate = new Label();
		labelTodate = new Label();
		txtFromDate = new TextField();
		txtToDate = new TextField();
		
	}
	

	public HBox profitLostSearchBox() {
		HBox searchBox = new HBox();

		labelFromDate.setText("تاریخ شروع");
		labelTodate.setText("تا");
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
		labelTodate.setText("تا تاریخ");
		txtFromDate.setPromptText("1398-01-01");
		txtToDate.setPromptText("1398-12-31");
		

		searchBox.setPadding(new Insets(10, 10, 10, 10));
		searchBox.setSpacing(20);
		searchBox.getChildren().clear();
		searchBox.getChildren().addAll(labelTodate, txtToDate);
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
	

}
