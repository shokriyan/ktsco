package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.csr.DepositModel;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DepositController implements Initializable{
	
	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn;
	
	@FXML
	private Label labelInformation, labelTotalSelectedAmount; 
	
	@FXML
	private ComboBox<String> comboDepositType, comboCurrency, comboAccountNumber, comboEmployee; 
	
	@FXML
	private TextField txtVoucherNumber, txtTransactionDate; 
	
	@FXML
	private TableView<DepositModel> tableDepositDetail; 
	
	@FXML
	private TableColumn<DepositModel, CheckBox> colCheckBox; 
	
	@FXML
	private TableColumn<DepositModel, Integer> colReceiveID, colBillID; 
	
	@FXML
	private TableColumn<DepositModel, String> colCurrency, colAmountReceived; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboCurrency.setDisable(true);
		comboEmployee.setDisable(true);
		comboAccountNumber.setDisable(true);
		txtVoucherNumber.setDisable(true);
		txtTransactionDate.setDisable(true);
	}
	
	@FXML
	private void allButtonActions(ActionEvent event) {
		
	}
	
	@FXML
	private void onComboDepositAction(ActionEvent event) {
		
	}
	@FXML
	private void onComboCurrencyAction(ActionEvent event) {
		
	}
	
	@FXML
	private void onCheckBoxCheckedAction() {
		
	}
	
	private void populateComboBoxs() {
		Commons.populateAllComboBox(comboDepositType, Constants.depositTypeList);
		comboDepositType.setValue("");
		Commons.populateAllComboBox(comboCurrency, Commons.getListValuesFromMap(Constants.currencies));
		comboCurrency.setValue("");
		
	}

}
