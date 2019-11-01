package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.ViewClass;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MgmntController implements Initializable{
	private static BorderPane mgmntBorderScene; 
	public static Stage mgmntStage; 
	public static ViewClass views = new ViewClass(); 
	
	private static Button production; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public static void initialMgmntPanel() {
		mgmntBorderScene = views.setBorderPane(Commons.getFxmlPanel("csrPanelFxml"));
		mgmntBorderScene.setTop(views.setVboxFxml(Commons.getFxmlPanel("topViewFxml")));
		mgmntBorderScene.setRight(setSideMenu(Commons.getFxmlPanel("sideMenuFxml")));
		
		mgmntStage = views.setSceneShowStage(mgmntBorderScene, Constants.title, true);
	}
	
	public static VBox setSideMenu(String fxml) {
		VBox vbox = views.setVboxFxml(fxml);
		List<Button> buttonList = new ArrayList<Button>(); 
		production = Commons.addMenuButton("گرازش محصولات");
		production.setOnAction(event -> allButtonActions(event));
		buttonList.add(production);
		
		for (int i = 0; i < buttonList.size() ; i ++) {
			vbox.getChildren().add(buttonList.get(i));
		}
		
		return vbox; 
	}
	
	private static void allButtonActions(ActionEvent event) {
		if (event.getSource() == production) {
			Commons.reloadTopView(mgmntBorderScene, production);
			setCenterPanel(Commons.getFxmlPanel("ProductsReportsPanel"));
		}
	}
	
	public static void setCenterPanel(String fxml) {
		Pane centerPane = views.setPane(fxml);
		mgmntBorderScene.setCenter(centerPane);
	}
	

}
