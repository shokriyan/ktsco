package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.utils.Commons;
import com.ktsco.utils.ViewClass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StocksReportController implements Initializable{
	private ViewClass views = new ViewClass();
	
	@FXML
	private Button btnCentralStock, btnFactoryStock; 
	@FXML
	private Text textPageSubtitle; 
	@FXML
	private HBox hboxReportResults; 
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hboxReportResults.getChildren().clear();		
	}
	
	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnCentralStock) {
			textPageSubtitle.setText(btnCentralStock.getText());
			setReportBox("CentralStockReport");
		}else if (event.getSource() == btnFactoryStock) {
			textPageSubtitle.setText(btnFactoryStock.getText());
			setReportBox("FactoryStockReport");
		}
	}
	
	private void setReportBox(String fxml) {
		hboxReportResults.getChildren().clear();
		hboxReportResults.getChildren().add(views.setVboxFxml(Commons.getFxmlPanel(fxml)));
	}

}
