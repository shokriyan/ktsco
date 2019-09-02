package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.csr.DepositSearchModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.modelsdao.DepositDAO;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DepositSearchController implements Initializable{

	@FXML
	private Button btnSearch,btnRefresh, btnReturn;
	
	@FXML
	private ComboBox<String> comboBanks, comboEmployee;
	
	@FXML
	private TextField txtVoucherNo, txtStartDate, txtEndDate; 
	
	@FXML
	private TableView<DepositSearchModel> tableDepositTable; 
	
	@FXML
	private TableColumn<DepositSearchModel, Integer> colReceiveID;
	
	@FXML
	private TableColumn<DepositSearchModel, String> colBank,colEmployee, colVoucherNo,colDepositDate,  colDepositAmount;
	
	private ObservableList<DepositSearchModel> tableData = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateBankCombo();
		populateEmployeeCombo();
		populateTableData();
		
		txtStartDate.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					DateUtils.checkEntryDateFormat(newValue);
				}
			}
		});
		
		txtEndDate.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					DateUtils.checkEntryDateFormat(newValue);
				}
			}
		});
	}
	
	
	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receivablePanel"));
		}else if (event.getSource() == btnSearch) {
			populateTableData();
		}else if (event.getSource() == btnRefresh) {
			populateBankCombo();
			populateEmployeeCombo();
			txtVoucherNo.clear();
			txtStartDate.clear();
			txtEndDate.clear();
			populateTableData();
		}
	}
	
	
	private void generateTableColumns(ObservableList<DepositSearchModel>list) {
		colReceiveID.setCellValueFactory(data -> data.getValue().receiveIDProperty().asObject());
		colBank.setCellValueFactory(data -> data.getValue().bankProperty());
		colEmployee.setCellValueFactory(data -> data.getValue().employeeProperty());
		colVoucherNo.setCellValueFactory(data -> data.getValue().voucherNoProperty());
		colDepositDate.setCellValueFactory(data -> data.getValue().depositDateProperty());
		colDepositAmount.setCellValueFactory(data -> data.getValue().depositAmountProperty());
		
		tableDepositTable.setItems(list);
	}
	
	private void populateTableData() {
		String bank = comboBanks.getValue();
		String employee = comboEmployee.getValue();
		String voucherNo = txtVoucherNo.getText();
		String startDate = txtStartDate.getText();
		String endDate = txtEndDate.getText();
		
		
		tableData = DepositDAO.depositSearchList(bank, employee, startDate, endDate, voucherNo);
		
		generateTableColumns(tableData);
		
	}
	
	private void populateBankCombo() {
		List<String> list = AccountsDAO.getBankAccounts();
		Commons.populateAllComboBox(comboBanks, list);
		comboBanks.setValue("");
	}
	private void populateEmployeeCombo() {
		List<String> list = EmployeeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, list);
		comboEmployee.setValue("");
	}

}
