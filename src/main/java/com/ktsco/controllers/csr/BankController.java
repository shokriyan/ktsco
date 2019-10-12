package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.csr.BankBalanceModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.utils.Commons;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BankController implements Initializable{
	
	@FXML
	private Button btnAddAccount;
	
	@FXML
	private TableView<BankBalanceModel> tableBankAccounts;
	
	@FXML
	private TableColumn<BankBalanceModel, Integer> colCode; 
	@FXML
	private TableColumn<BankBalanceModel, String> colBankAccount, colBankName, colCurrency,colAccountBalance;
	
	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnAddAccount) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("accountEntryPanel"));
		}
	}
	


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateTableData();
		tableBankAccounts.setVisible(true);
	}
	
	private void generateTableDate(ObservableList<BankBalanceModel> list ) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().getBankIDProperty().asObject());
		colBankAccount.setCellValueFactory(cellData -> cellData.getValue().getBankAccountProperty());
		colBankName.setCellValueFactory(cellData -> cellData.getValue().getBankNameProperty());
		colCurrency.setCellValueFactory(cellData -> cellData.getValue().currencyProperty());
		colAccountBalance.setCellValueFactory(cellData -> cellData.getValue().getAccountBalanceProperty());
		
		tableBankAccounts.setItems(list);
		
	}
	
	private void populateTableData() {
		ObservableList<BankBalanceModel> tableData = AccountsDAO.getBankBalance();
		generateTableDate(tableData);
	}


	

}
