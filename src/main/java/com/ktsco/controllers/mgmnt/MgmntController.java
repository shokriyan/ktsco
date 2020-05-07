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
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MgmntController implements Initializable{
	private static BorderPane mgmntBorderScene; 
	public static Stage mgmntStage; 
	public static ViewClass views = new ViewClass(); 
	
	private static Button production, rawMaterial, Stocks, Sells, expenses, banks, financialReport;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public static void initialMgmntPanel() {
		mgmntBorderScene = views.setBorderPane(Commons.getFxmlPanel("csrPanelFxml"));
		mgmntBorderScene.setTop(views.setVboxFxml(Commons.getFxmlPanel("topViewFxml")));
		mgmntBorderScene.setRight(setSideMenu(Commons.getFxmlPanel("sideMenuFxml")));
		
		mgmntStage = views.setSceneShowStage(mgmntBorderScene, Constants.title, true);
		views.setWindowMax(mgmntStage);
	}
	
	public static VBox setSideMenu(String fxml) {
		VBox vbox = views.setVboxFxml(fxml);
		List<Button> buttonList = new ArrayList<Button>(); 
		production = Commons.addMenuButton("گرازش محصولات");
		rawMaterial = Commons.addMenuButton("گزارش مواد اولیه");
		Stocks = Commons.addMenuButton("انبارها");
		Sells = Commons.addMenuButton("فروشات");
		expenses = Commons.addMenuButton("هزینه ها");
		banks = Commons.addMenuButton("بانک");
		financialReport = Commons.addMenuButton("گزارش حساب مالی");
		production.setOnAction(event -> allButtonActions(event));
		rawMaterial.setOnAction(event -> allButtonActions(event));
		Stocks.setOnAction(event -> allButtonActions(event));
		Sells.setOnAction(event -> allButtonActions(event));
		expenses.setOnAction(event -> allButtonActions(event));
		banks.setOnAction(event -> allButtonActions(event));
		financialReport.setOnAction(event -> allButtonActions(event));
		buttonList.add(production);
		buttonList.add(rawMaterial);
		buttonList.add(Stocks);
		buttonList.add(Sells);
		buttonList.add(expenses);
		buttonList.add(banks);
		buttonList.add(financialReport);
		
		for (int i = 0; i < buttonList.size() ; i ++) {
			vbox.getChildren().add(buttonList.get(i));
		}
		
		return vbox; 
	}
	
	private static void allButtonActions(ActionEvent event) {
		if (event.getSource() == production) {
			Commons.reloadTopView(mgmntBorderScene, production);
			setCenterPanel(Commons.getFxmlPanel("ProductsReportsPanel"));
		}else if (event.getSource() == rawMaterial) {
			Commons.reloadTopView(mgmntBorderScene, rawMaterial);
			setCenterPanel(Commons.getFxmlPanel("RawMaterailReportPanel"));
		}else if (event.getSource() == Stocks) {
			Commons.reloadTopView(mgmntBorderScene, Stocks);
			setCenterPanel(Commons.getFxmlPanel("StocksReportPanel"));
		}else if (event.getSource() == Sells) {
			Commons.reloadTopView(mgmntBorderScene, Sells);
			setCenterPanel(Commons.getFxmlPanel("SellsReports"));
		}else if (event.getSource() == expenses) {
			Commons.reloadTopView(mgmntBorderScene, expenses);
			setCenterPanel(Commons.getFxmlPanel("ExpenseReports"));
		}else if (event.getSource() == banks) {
			Commons.reloadTopView(mgmntBorderScene, banks);
			setCenterPanel(Commons.getFxmlPanel("BanksReports"));
		}else if (event.getSource() == financialReport) {
			Commons.reloadTopView(mgmntBorderScene, financialReport);
			setCenterPanel(Commons.getFxmlPanel("FinancialReports"));
		}
	}
	
	public static void setCenterPanel(String fxml) {
		Pane centerPane = views.setPane(fxml);
		mgmntBorderScene.setCenter(centerPane);
		HBox.setHgrow(centerPane, Priority.ALWAYS);
		VBox.setVgrow(centerPane, Priority.ALWAYS);
		Bounds bounds = mgmntBorderScene.getCenter().getBoundsInLocal();
		centerPane.setMinWidth(bounds.getWidth());
		centerPane.setMinHeight(bounds.getHeight());
		centerPane.setPrefSize(bounds.getWidth(), bounds.getHeight());
		centerPane.setMaxWidth(Double.MAX_VALUE);
		centerPane.setMaxHeight(Double.MAX_VALUE);
		
		
		
	}
	

}
