package com.ktsco.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.ViewClass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FactoryController implements Initializable {
	
	public static BorderPane factoryBorderPane; 
	public static Stage factoryStage;
	static ViewClass view = new ViewClass(); 
	
	@FXML
	private static Button btnProducts; 
	@FXML
	private static Button btnInvStock; 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public static void initialFactoryPanel() {
		
		factoryBorderPane = view.setBorderPane(Constants.factoryPanel);
		factoryBorderPane.setTop(view.setVboxFxml(Constants.topViewFxml));
		factoryBorderPane.setRight(setSideMenu(Constants.sideMenuFxml));
		
		factoryStage = view.setSceneAndShowStage(factoryBorderPane, Constants.title, true, false);
		
	}
	
	
	private static VBox setSideMenu(String fxml) {
		
		VBox vbox = view.setVboxFxml(fxml);
		List<Button> buttonList = new ArrayList<Button>();
		btnProducts = Commons.addMenuButton("فهرست محصولات");
		btnProducts.setOnAction(event-> allButtonsAction(event));
		btnInvStock = Commons.addMenuButton("فهرست مواد اولیه");
		btnInvStock.setOnAction(event-> allButtonsAction(event));
		buttonList.add(btnProducts);
		buttonList.add(btnInvStock);
		for (int i = 0 ; i< buttonList.size(); i ++) {
			vbox.getChildren().add(buttonList.get(i));
		}
		
		return vbox; 
		
		
	}
	
	@FXML
	public static void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnProducts) {
			Commons.reloadTopView(factoryBorderPane, btnProducts);
			openPorductPanel();
		}else if (event.getSource() == btnInvStock) {
			Commons.reloadTopView(factoryBorderPane, btnInvStock);
			openInventoryStockPanel();
		}
	}
	
	/**
	 * Openning Product Panel and set in Center Pane
	 */
	public static void openPorductPanel() {
		Pane productPane = view.setPane(Constants.productPanelFxml);
		factoryBorderPane.setCenter(productPane);
	}
	
	//Opening Invetory Stock Panel
	public static void openInventoryStockPanel() {
		Pane invetoryStock = view.setPane(Constants.invetoryStockPanelFxml);
		factoryBorderPane.setCenter(invetoryStock);
	}

}
