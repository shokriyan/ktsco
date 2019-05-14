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
	private static Button btnInventroy; 
	@FXML
	private static Button btnStock; 
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
		btnProducts = Commons.addMenuButton("محصولات");
		btnProducts.setOnAction(event-> allButtonsAction(event));
		btnInventroy = Commons.addMenuButton("مواد اولیه");
		btnInventroy.setOnAction(event -> allButtonsAction(event));
		btnStock = Commons.addMenuButton("گدام کارخانه");
		btnStock.setOnAction(event-> allButtonsAction(event));
		buttonList.add(btnProducts);
		buttonList.add(btnInventroy);
		buttonList.add(btnStock);
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
		}else if (event.getSource() == btnInventroy) {
			Commons.reloadTopView(factoryBorderPane, btnInventroy);
		}else if (event.getSource() == btnStock) {
			Commons.reloadTopView(factoryBorderPane, btnStock);
		}
	}
	
	/**
	 * Openning Product Panel and set in Center Pane
	 */
	public static void openPorductPanel() {
		Pane productPane = view.setPane(Constants.productPanelFxml);
		factoryBorderPane.setCenter(productPane);
	}

}
