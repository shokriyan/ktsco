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

public class ExpenseReportController implements Initializable{
	
	private ViewClass views = new ViewClass();
	
	@FXML
	private Button btnExpenseSummary, btnPaymentSummary, btnExpenseDetailReport, btnPaymentDetailReport;

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
		if (event.getSource() == btnExpenseSummary) {
			SellsSummeryController.panelName = "expense";
			textPageSubtitle.setText(btnExpenseSummary.getText());
			setReportBox("SellsSummeryPanel");
			
		}else if (event.getSource() == btnPaymentSummary) {
			SellUSDSummeryController.panelName = "expense";
			textPageSubtitle.setText(btnPaymentSummary.getText());
			setReportBox("SellsReceivedUSDPanel");
		}else if (event.getSource() == btnPaymentDetailReport) {
			ReceivedDetailReportController.panelName = "expense";
			textPageSubtitle.setText(btnPaymentDetailReport.getText());
			setReportBox("ReceivedDetailReport");
		}else if (event.getSource() == btnExpenseDetailReport) {
			SalesDetailReportController.panelName = "expense";
			textPageSubtitle.setText(btnExpenseDetailReport.getText());
			setReportBox("SelesDetailsReport");
		}
		
	}
	
	private void setReportBox(String fxml) {
		hboxReportResults.getChildren().clear();
		hboxReportResults.getChildren().add(views.setVboxFxml(Commons.getFxmlPanel(fxml)));
	}
	

}
