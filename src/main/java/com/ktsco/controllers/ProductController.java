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

public class ProductController implements Initializable{
	
	/**
	 * All Global Instance 
	 */
	private final Logger log = LoggerFactory.getLogger(ProductController.class);
	ViewClass view = new ViewClass(); 
	
	@FXML
	private Button btnCategory , btnInventoryList; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Buttons Actions 
	 * 
	 * @param event
	 */
	@FXML
	public void allButtonAction(ActionEvent event) {
		
		if (event.getSource() == btnCategory) {
			openCategoryPanel();
		}else if(event.getSource() == btnInventoryList){
			openInventoryPanel();
		}
		
	}
	
	
	private void openCategoryPanel() {
		log.info("Loading FXML to penel {}" , Constants.categoryPanelFxml) ;
		VBox category = view.setVboxFxml(Constants.categoryPanelFxml);
		log.info("Loading stage and show");
		CategoryController.categoryStage = view.setSceneAndShowStage(category, "", false, false);
	}
	
	private void openInventoryPanel() {
		log.info("Loading FXML to penel {}" , Constants.inventoryListPanelFxml) ;
		VBox inventoryList = view.setVboxFxml(Constants.inventoryListPanelFxml);
		log.info("Loading stage and show");
		InventoryController.invStage = view.setSceneAndShowStage(inventoryList, "", false, false);
	}




	
}
