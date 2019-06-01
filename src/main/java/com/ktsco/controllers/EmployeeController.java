package com.ktsco.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.utils.Constants;
import com.ktsco.utils.ViewClass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class EmployeeController implements Initializable {
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	private ViewClass view = new ViewClass();
	
	@FXML
	private Button btnEmployeeList; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnEmployeeList) {
			openEmployeeListPanel(Constants.employeeListPanelFxml);
		}
	}
	
	private void openEmployeeListPanel(String fxml) {
			log.info("Loading FXML to penel {}", fxml);
			VBox employeeListPanel = view.setVboxFxml(fxml);
			log.info("Loading stage and show");
			EmpListController.empListStage = view.setSceneAndShowStage(employeeListPanel, "", false, false);
	}
}
