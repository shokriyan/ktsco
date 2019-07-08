package com.ktsco.controllers.csr;

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

public class CSRController implements Initializable{
	public static BorderPane csrBorderScene; 
	public static Stage csrStage; 
	
	public static ViewClass views = new ViewClass(); 
	
	
	@FXML
	private static Button btnEmployeePanel, btnSalesPanel;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	} 
	
	public static void initialCSRPanel() {
		csrBorderScene = views.setBorderPane(Commons.getFxmlPanel("csrPanelFxml"));
		csrBorderScene.setTop(views.setVboxFxml(Commons.getFxmlPanel("topViewFxml")));
		csrBorderScene.setRight(setSideMenu(Commons.getFxmlPanel("sideMenuFxml")));
		
		csrStage = views.setSceneAndShowStage(csrBorderScene, Constants.title, true, false);
	}
	
	public static VBox setSideMenu(String fxml) {
		VBox vbox = views.setVboxFxml(fxml);
		List<Button> buttonList = new ArrayList<Button>(); 
		btnEmployeePanel = Commons.addMenuButton("صفحه کارمندان");
		btnEmployeePanel.setOnAction(event -> allButtonActions(event));
		btnSalesPanel = Commons.addMenuButton("فروشات");
		btnSalesPanel.setOnAction(event -> allButtonActions(event));
		
		buttonList.add(btnEmployeePanel);
		buttonList.add(btnSalesPanel);
		for (int i = 0; i < buttonList.size() ; i ++) {
			vbox.getChildren().add(buttonList.get(i));
		}
		
		return vbox; 
	}
	
	public static void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnEmployeePanel) {
			Commons.reloadTopView(csrBorderScene, btnEmployeePanel);
			setCenterPanel(Commons.getFxmlPanel("employeePanelFxml"));
		}else if (event.getSource() == btnSalesPanel) {
			Commons.reloadTopView(csrBorderScene, btnSalesPanel);
			setCenterPanel(Commons.getFxmlPanel("SalepanelFxml"));
		}
	}
	
	public static void setCenterPanel(String fxml) {
		Pane centerPane = views.setPane(fxml);
		csrBorderScene.setCenter(centerPane);
	}
	
	
	
	
}
