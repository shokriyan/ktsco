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

public class SellsReportController implements Initializable{
	
	private ViewClass views = new ViewClass();
	
	@FXML
	private Button btnSellsReport , btnSaleDetailsReports, btnSellSummeryUSD, btnReceiveDetailReport;

	@FXML
	private Text textPageSubtitle;
	
	@FXML
	private HBox hboxReportResults;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hboxReportResults.getChildren().clear();
	}
	@FXML
	private void allButtonActions (ActionEvent event) {
		if (event.getSource() == btnSellsReport) {
			SellsSummeryController.panelName = "sales";
			textPageSubtitle.setText(btnSellsReport.getText());
			setReportBox("SellsSummeryPanel");
			
		}else if (event.getSource() == btnSellSummeryUSD) {
			SellUSDSummeryController.panelName = "sales";
			textPageSubtitle.setText(btnSellSummeryUSD.getText());
			setReportBox("SellsReceivedUSDPanel");
		}else if (event.getSource() == btnSaleDetailsReports) {
			textPageSubtitle.setText(btnSaleDetailsReports.getText());
			setReportBox("SelesDetailsReport");
		}else if (event.getSource() == btnReceiveDetailReport) {
			textPageSubtitle.setText(btnReceiveDetailReport.getText());
			setReportBox("ReceivedDetailReport");
		}
	}
	
	private void setReportBox(String fxml) {
		hboxReportResults.getChildren().clear();
		hboxReportResults.getChildren().add(views.setVboxFxml(Commons.getFxmlPanel(fxml)));
	}
	

}
