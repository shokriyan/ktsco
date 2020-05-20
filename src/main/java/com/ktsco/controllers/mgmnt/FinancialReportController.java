package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.enums.Dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FinancialReportController implements Initializable {

	@FXML
	private Button btnProfitLostReport, btnBalanceSheet;

	@FXML
	private Text textPageSubtitle;

	@FXML
	private VBox hboxReportResults;
	@FXML
	private Pane mainPane;
	private Button btnSearch, btnRefresh;
	SearchPanelControllers searchController = new SearchPanelControllers();
	ProfitAndLostReportController profLost = new ProfitAndLostReportController();
	BalanceSheetReportController balanceSheet = new BalanceSheetReportController();

	private static String reportName = "";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnRefresh = new Button();
		btnSearch = new Button();
		
		HBox.setHgrow(mainPane, Priority.ALWAYS);
		VBox.setVgrow(mainPane, Priority.ALWAYS);
		HBox.setHgrow(hboxReportResults, Priority.ALWAYS);
		VBox.setVgrow(hboxReportResults, Priority.ALWAYS);
		hboxReportResults.getChildren().clear();

	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnProfitLostReport) {
			reportName = "profitLost";
			textPageSubtitle.setText(btnProfitLostReport.getText());
			searchController.getFromDateField().clear();
			searchController.getToDateField().clear();
			searchFunction();
			financialReportCreation();
			
		} else if (event.getSource() == btnBalanceSheet) {
			reportName = "balanceSheet";
			textPageSubtitle.setText(btnBalanceSheet.getText());
			searchController.getFromDateField().clear();
			searchController.getToDateField().clear();
			searchFunction();
			financialReportCreation();
		}else if (event.getSource() == btnRefresh) {
			searchController.getFromDateField().clear();
			searchController.getToDateField().clear();
		}else if (event.getSource() == btnSearch) {
			searchFunction();
			financialReportCreation();
		}
	}

	private VBox createProfitLostPanel() {
		VBox mainReportPane = new VBox(10);
		mainReportPane.setPadding(new Insets(5, 5, 5, 5));
		VBox profitDetail = profLost.profitAndLostDetailReport();
		mainReportPane.getChildren().add(profitDetail);
		return mainReportPane;
	}

	private VBox createBalanceSheetPanel() {
		VBox mainReportPanel = new VBox(10);
		HBox.setHgrow(mainReportPanel, Priority.ALWAYS);
		VBox.setVgrow(mainReportPanel, Priority.ALWAYS);
		ScrollPane balanceSheetPanel = balanceSheet.createBalanceSheetPage();
		mainReportPanel.getChildren().add(balanceSheetPanel);
		return mainReportPanel;
	}

	private void searchFunction() {
			if (reportName.equalsIgnoreCase("profitLost")) {
				String fromDate = searchController.getFromDateText();
				String toDate = searchController.getToDateText();
				profLost.setSearchDates(fromDate, toDate);
			} else if (reportName.equalsIgnoreCase("balanceSheet")) {
				String toDate = searchController.getToDateText();
				balanceSheet.setSearchToDate(toDate);
			}
	}

	private void financialReportCreation() {
		hboxReportResults.getChildren().clear();
		HBox searchPanel = new HBox();
		HBox.setHgrow(searchPanel, Priority.ALWAYS);
		HBox mainReportPanel = new HBox();
		VBox.setVgrow(mainReportPanel, Priority.ALWAYS);
		HBox.setHgrow(mainReportPanel, Priority.ALWAYS);
		if (reportName.equalsIgnoreCase("profitLost")) {
			HBox profitLostSearch = searchController.profitLostSearchBox();
			VBox profitLostReport = createProfitLostPanel();
			searchPanel.getChildren().clear();
			searchPanel.getChildren().addAll(profitLostSearch, addSearchButton());
			mainReportPanel.getChildren().clear();
			mainReportPanel.getChildren().add(profitLostReport);
		} else if (reportName.equalsIgnoreCase("balanceSheet")) {
			HBox balanceSheetSearch = searchController.BalanceSheetSearchBox();
			VBox balanceSheetReport = createBalanceSheetPanel();
			searchPanel.getChildren().clear();
			searchPanel.getChildren().addAll(balanceSheetSearch, addSearchButton());
			mainReportPanel.getChildren().clear();
			mainReportPanel.getChildren().add(balanceSheetReport);
		}
		
		hboxReportResults.getChildren().addAll(searchPanel, mainReportPanel);
	}
	
	private HBox addSearchButton() {
		HBox searchButtons = new HBox(20);
		searchButtons.setPadding(new Insets(10, 10, 10, 10));
		btnSearch.setText(Dictionary.Search.getValue());
		btnRefresh.setText(Dictionary.Refresh.getValue());
		btnSearch.setPrefWidth(150);
		btnRefresh.setPrefWidth(150);
		btnSearch.setOnAction(event -> allButtonActions(event));
		btnRefresh.setOnAction(event -> allButtonActions(event));
		searchButtons.getChildren().clear();
		searchButtons.getChildren().addAll(btnSearch , btnRefresh);
		return searchButtons;
	}

}
