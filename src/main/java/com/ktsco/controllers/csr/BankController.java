package com.ktsco.controllers.csr;

import com.ktsco.models.csr.BankBalanceModel;
import com.ktsco.utils.Commons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BankController {
	
	@FXML
	private Button btnAddAccount;
	
	@FXML
	private TableView<BankBalanceModel> tableBankAccounts;
	
	@FXML
	private TableColumn<BankBalanceModel, Integer> colCode; 
	@FXML
	private TableColumn<BankBalanceModel, String> colBankAccount, colBankName,colAccountBalance;
	
	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnAddAccount) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("accountEntryPanel"));
		}
	}
	
	
	

}
