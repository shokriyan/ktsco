package com.ktsco.controllers.csr;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.csr.BankTransModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.modelsdao.DepositDAO;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BankTransController implements Initializable {

	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn;

	@FXML
	private Label labelInformation, labelTableTotal;

	@FXML
	private TextField txtCode, txtCurrency, txtTransDate, txtTransAmount;

	@FXML
	private TableView<BankTransModel> tableAccounts;

	@FXML
	private TableColumn<BankTransModel, Integer> colCode;

	@FXML
	private TableColumn<BankTransModel, String> colBankAccount, colBankName, colCurrency, colTransDate;

	@FXML
	private TableColumn<BankTransModel, Double> colAmount;

	@FXML
	private MenuItem menuEdit, menuDelete;

	@FXML
	private ComboBox<String> comboFromBank, comboToBank, comboEmp;

	ObservableList<BankTransModel> tableData = FXCollections.observableArrayList();
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrerequisitions();

	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnSave) {
			if (AlertsUtils.askForSaveItems()) {
				processDeposit();
			}
		} else if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("banksPanel"));
		} else if (event.getSource() == btnSaveReturn) {
			if (AlertsUtils.askForSaveItems()) {
				processDeposit();
				clearForm();
				Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("banksPanel"));
			}
		} else if (event.getSource() == btnNew) {
			clearForm();
		}
	}

	@FXML
	private void onComboValueChange(ActionEvent event) {
		if (event.getSource() == comboFromBank) {
			populateToBanksItems();

		}
	}

//	Start with populating the combo boxes
	private void loadPrerequisitions() {
		txtCurrency.setEditable(false);
		comboFromBank.setEditable(false);
		comboToBank.setEditable(false);
		populateComboBoxes();
	}

	private void populateComboBoxes() {
		Commons.populateAllComboBox(comboFromBank, AccountsDAO.getBankAccounts());
		comboFromBank.setValue("");
		Commons.populateAllComboBox(comboEmp, EmployeeDAO.getEmployeeName());
		comboEmp.setValue("");
	}

	private void populateToBanksItems() {
		if (comboFromBank.getValue() != null || !comboFromBank.getValue().equalsIgnoreCase("")) {
			String comboValue = comboFromBank.getValue();
			String currValue = comboValue.split("-")[2].trim();
			List<String> bankItems = AccountsDAO.getBankAccounts(currValue);
			bankItems.remove(comboValue);
			Commons.populateAllComboBox(comboToBank, bankItems);
			txtCurrency.setText(currValue);

			populateTableData(comboValue);
		} else {
			comboToBank.getItems().clear();
		}
	}

	private void formatColumn(TableColumn<BankTransModel, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<BankTransModel, Double>() {

			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty) {
					setText(null);
				} else {
					setText(format.format(price));
				}
			}
		});
	}

	private void generateTableColumns(ObservableList<BankTransModel> list) {
		colCode.setCellValueFactory(data -> data.getValue().idProperty().asObject());
		colBankAccount.setCellValueFactory(data -> data.getValue().bankAccountProperty());
		colBankName.setCellValueFactory(data -> data.getValue().bankNameProperty());
		colCurrency.setCellValueFactory(data -> data.getValue().currencyProperty());
		colTransDate.setCellValueFactory(data -> data.getValue().entryDateProperty());
		colAmount.setCellValueFactory(data -> data.getValue().amountProperty().asObject());
		formatColumn(colAmount, formatQuantity);

		tableAccounts.setItems(list);
	}

	private void populateTableData(String selectedBanksName) {
		tableData = DepositDAO.getTransactionDetail(selectedBanksName);
		generateTableColumns(tableData);
	}

	private boolean checkFlag() {
		boolean isPassed = false;
		isPassed = DateUtils.checkEntryDateFormat(txtTransDate.getText());
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtCode.getText())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(comboFromBank.getValue())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(comboToBank.getValue())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtTransAmount.getText())) ? false : true;
		return isPassed;
	}

	private void processDeposit() {

		if (checkFlag()) {
			String voucherNum = txtCode.getText();
			String fromBankName = comboFromBank.getValue();
			String toBankName = comboToBank.getValue();
			String transDate = txtTransDate.getText();
			String employee = comboEmp.getValue();
			String amount = txtTransAmount.getText();

			String amountToDeduct = "-" + amount;
			boolean isDeductSuccess = DepositDAO.saveBankTransactions(0, amountToDeduct, fromBankName, fromBankName,
					transDate, voucherNum, employee);

			if (isDeductSuccess) {
				boolean isProcessSuccess = DepositDAO.saveBankTransactions(0, amount, fromBankName, toBankName,
						transDate, voucherNum, employee);

				Commons.processMessageLabel(labelInformation, isProcessSuccess);
			} else
				Commons.processMessageLabel(labelInformation, isDeductSuccess);
		clearForm();
		} else {
			AlertsUtils.emptyFieldAlert();
		}

	}

	private void clearForm() {
		txtCode.clear();
		comboFromBank.setValue("");
		comboToBank.setValue("");
		comboEmp.setValue("");
		txtCurrency.clear();
		txtTransAmount.clear();
		txtTransDate.clear();
	}
	
	

}
