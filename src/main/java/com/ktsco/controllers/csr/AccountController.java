package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.csr.AccountsModel;
import com.ktsco.modelsdao.AccountsDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AccountController  implements Initializable{

	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn; 
	
	@FXML
	private Label labelInformation; 
	
	@FXML
	private TextField txtAccountNumber , txtBankName, txtOpeningDate, txtOpeningBalance; 
	
	@FXML
	private TableView<AccountsModel> tableAccounts;
	@FXML
	private TableColumn<AccountsModel, Integer> colCode; 
	@FXML
	private TableColumn<AccountsModel, String> colBankAccount, colBankName , colStartDate, colOpeningBalance; 
	
	private ObservableList<AccountsModel> TableData = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		reloadPrerequisitions();
	}
	
	@FXML
	private void allButtonActions (ActionEvent event) {
		
	}
	private void reloadPrerequisitions() {
		populateObservableList();
		generateTableList(TableData);
	}
	
	private void generateTableList(ObservableList<AccountsModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty().asObject());
		colBankAccount.setCellValueFactory(cellData -> cellData.getValue().getBankAccountProperty());
		colBankName.setCellValueFactory(cellData -> cellData.getValue().getBankNameProperty());
		colStartDate.setCellValueFactory(cellData -> cellData.getValue().getOpeningDateProperty());
		colOpeningBalance.setCellValueFactory(cellData -> cellData.getValue().getOpeningBalanceProperty());
		
		tableAccounts.setItems(list);
	}
	
	private void populateObservableList () {
		TableData = AccountsDAO.reterieveAllRecords();
	}
	
	
	

}
