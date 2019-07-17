package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.MainStockModel;
import com.ktsco.utils.Commons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	private TableColumn<MainStockModel, Double> colImport, colExport, colReminder; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
	
	
	
	
	
	

}
