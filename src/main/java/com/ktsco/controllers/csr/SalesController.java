package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.MainStockModel;
import com.ktsco.models.factory.ProductionDetailModel;
import com.ktsco.modelsdao.MainStockDAO;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class SalesController implements Initializable{
	private static Logger log = LoggerFactory.getLogger(SalesController.class);
	@FXML
	private Button btnCustomerList, btnCurrencyList, btnBillEntry; 
	
	@FXML
	private TableView<MainStockModel> tableMainStockList;
	@FXML
	private TableColumn<MainStockModel, Integer> colCode; 
	@FXML
	private TableColumn<MainStockModel, String> colItems, colUnit; 
	@FXML
	private TableColumn<MainStockModel, Double> colImport, colSales, colRemain; 
	private ObservableList<MainStockModel> stockList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateMainStockList();
	}
	
	@FXML
	private void allButtonActions(ActionEvent event){
		if (event.getSource() == btnCustomerList) {
			log.debug("Customer list button clicked");
			CustomersController.customerStage = Commons.openPanelsUndecorate(Commons.getFxmlPanel("customerPanelList"));
		}else if (event.getSource() == btnCurrencyList) {
			log.debug("Currency List Button Clicked");
			CurrenciesController.currencyStage = Commons.openPanelsUndecorate(Commons.getFxmlPanel("CurrenciesPanel"));
		}else if (event.getSource() == btnBillEntry) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("SalesBillPanel"));
		}
		
	}
	
	
	private void generateTableStock(ObservableList<MainStockModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty().asObject());
		colItems.setCellValueFactory(cellData -> cellData.getValue().getProductProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().getUnitProperty());
		colImport.setCellValueFactory(cellData -> cellData.getValue().getTotalImportProperty().asObject());
		colSales.setCellValueFactory(cellData -> cellData.getValue().getTotalExportProperty().asObject());
		colRemain.setCellValueFactory(cellData -> cellData.getValue().getReminderProperty().asObject());
		colRemain.setCellFactory(column -> {
		    return new TableCell<MainStockModel, Double>() {
		        @Override
		        protected void updateItem(Double item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		            	setText(null);
		                setStyle("");
		            } else {
		                //Converts String to Double
		            	double quantity = item;

		                // Style all dates in March with a different color.
		                if (quantity < 0) {
		                	setText(String.valueOf(quantity));
		                    setTextFill(Color.YELLOW);
		                    setStyle("-fx-background-color: red");
		                } else {
		                	setText(String.valueOf(quantity));
		                    setTextFill(Color.BLACK);
		                    setStyle("");
		                }
		            }
		        }
		    };
		});
		tableMainStockList.setItems(list);
	}
	
	private void populateMainStockList() {
		stockList = MainStockDAO.retrieveStockItems();
		generateTableStock(stockList);
	}
	
	
	

}
