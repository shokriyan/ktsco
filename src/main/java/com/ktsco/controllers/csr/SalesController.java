package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.utils.Commons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SalesController implements Initializable{
	private static Logger log = LoggerFactory.getLogger(SalesController.class);
	@FXML
	private Button btnCustomerList, btnCurrencyList; 

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
		}
		
	}
	
	
	
	

}
