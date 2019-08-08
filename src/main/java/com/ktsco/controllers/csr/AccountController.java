package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.csr.AccountsModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AccountController implements Initializable {

	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn;

	@FXML
	private Label labelInformation;

	@FXML
	private TextField txtCode, txtAccountNumber, txtBankName, txtOpeningDate, txtOpeningBalance;

	@FXML
	private TableView<AccountsModel> tableAccounts;
	@FXML
	private TableColumn<AccountsModel, Integer> colCode;
	@FXML
	private TableColumn<AccountsModel, String> colBankAccount, colBankName, colStartDate, colOpeningBalance;

	@FXML
	private MenuItem menuEdit, menuDelete;

	private ObservableList<AccountsModel> TableData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		generateAccountCode();
		reloadPrerequisitions();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("banksPanel"));
		} else if (event.getSource() == btnSave) {
			addAccount();
			reloadPrerequisitions();
		} else if (event.getSource() == btnNew) {
			clearEntryForm();
		}else if (event.getSource() == menuEdit) {
			extractDataToTextFields();
		}else if (event.getSource() == menuDelete) {
			deleteAccount();
			reloadPrerequisitions();
		}

	}

	private void reloadPrerequisitions() {
		populateObservableList();
		generateTableList(TableData);
	}

	private void generateAccountCode() {
		txtCode.setText(String.valueOf(AccountsDAO.generateAccountID()));
	}

	private void generateTableList(ObservableList<AccountsModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty().asObject());
		colBankAccount.setCellValueFactory(cellData -> cellData.getValue().getBankAccountProperty());
		colBankName.setCellValueFactory(cellData -> cellData.getValue().getBankNameProperty());
		colStartDate.setCellValueFactory(cellData -> cellData.getValue().getOpeningDateProperty());
		colOpeningBalance.setCellValueFactory(cellData -> cellData.getValue().getOpeningBalanceProperty());

		tableAccounts.setItems(list);
	}

	private void populateObservableList() {
		TableData = AccountsDAO.reterieveAllRecords();
	}

	private boolean checkFlag() {
		boolean isPassed = false;
		isPassed = (txtCode == null || txtCode.getText().equalsIgnoreCase("")) ? false : true;
		isPassed = (txtAccountNumber == null || txtAccountNumber.getText().equalsIgnoreCase("")) ? false : true;
		isPassed = (txtOpeningDate == null || txtOpeningDate.getText().equalsIgnoreCase("")) ? false
				: DateUtils.checkEntryDateFormat(txtOpeningDate.getText());
		isPassed = (txtOpeningBalance == null || txtOpeningBalance.getText().equalsIgnoreCase("")) ? false : true;

		return isPassed;
	}

	private void addAccount() {
		if (checkFlag()) {
			int code = Integer.parseInt(txtCode.getText());
			String bankAccount = txtAccountNumber.getText();
			String bankName = txtBankName.getText();
			String openingDate = txtOpeningDate.getText();
			double openingBalance = Double.parseDouble(txtOpeningBalance.getText());
			boolean isSuccess = AccountsDAO.insertOrUpdateIntoAccountTableAPI(code, bankAccount, bankName, openingDate,
					openingBalance);
			Commons.processMessageLabel(labelInformation, isSuccess);
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void clearEntryForm() {
		boolean isNotExist = false;
		int code = Integer.parseInt(txtCode.getText());
		for (AccountsModel model : TableData) {
			if (model.getCode() == code) {
				break;
			} else
				isNotExist = true;
		}

		if (isNotExist) {
			boolean response = AlertsUtils.askForReloadPage();
			if (response) {
				generateAccountCode();
				txtAccountNumber.clear();
				txtBankName.clear();
				txtOpeningBalance.clear();
				txtOpeningDate.clear();
			}
		}
	}

	private void extractDataToTextFields() {
		if (!tableAccounts.getSelectionModel().isEmpty()) {
			AccountsModel model = tableAccounts.getSelectionModel().getSelectedItem();
			txtCode.setText(String.valueOf(model.getCode()));
			txtAccountNumber.setText(model.getBankAccount());
			txtBankName.setText(model.getBankName());
			txtOpeningDate.setText(model.getOpeningDate());
			txtOpeningBalance.setText(model.getOpeningBalance());
		}
	}
	
	private void deleteAccount() {
		if (!tableAccounts.getSelectionModel().isEmpty()) {
			AccountsModel model = tableAccounts.getSelectionModel().getSelectedItem();
			int code = model.getCode();
			String object = model.getBankAccount() + "\n" + model.getBankName();
			
			if (AlertsUtils.askForDeleteAlert(object)) {
				boolean isSuccess = AccountsDAO.deleteRecord(code);
				Commons.processMessageLabel(labelInformation, isSuccess);
			}else {
				Commons.processMessageLabel(labelInformation, false);
			}
		}
	}

}
