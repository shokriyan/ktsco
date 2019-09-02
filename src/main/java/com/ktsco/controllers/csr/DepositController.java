package com.ktsco.controllers.csr;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.ktsco.models.csr.DepositModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.modelsdao.DepositDAO;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.modelsdao.ReceivableDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DateUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;

public class DepositController implements Initializable {
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn, btnCalculateSubtotal;

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

	private ObservableList<DepositModel> tableData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboCurrency.setDisable(true);
		comboEmployee.setDisable(true);
		comboAccountNumber.setDisable(true);
		txtVoucherNumber.setDisable(true);
		txtTransactionDate.setDisable(true);
		populateComboBoxs();
		tableDepositDetail.setEditable(true);

		txtTransactionDate.textProperty().addListener(new ChangeListener<String>() {
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
		} else if (event.getSource() == btnCalculateSubtotal) {
			calculateSubTotal();
		} else if (event.getSource() == btnSave) {
			if (AlertsUtils.askForSaveItems()) {
				saveDepositData();
				populateTableValues();
				calculateSubTotal();
			}
		}else if (event.getSource() == btnNew) {
			resetForm();
		}else if (event.getSource() == btnSaveReturn) {
			if (AlertsUtils.askForSaveItems()) {
				saveDepositData();
				populateTableValues();
				calculateSubTotal();
				Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receivablePanel"));
			}
		}
	}
	
	private void resetForm() {
		comboDepositType.setValue("");
	}

	@FXML
	private void onComboDepositAction(ActionEvent event) {
		labelTotalSelectedAmount.setText("0.00");
		if (null != comboDepositType.getValue() && !"".equalsIgnoreCase(comboDepositType.getValue())) {
			comboCurrency.setDisable(false);
			tableDepositDetail.getItems().clear();
			labelTotalSelectedAmount.setText("0.00");
			if (null != comboCurrency.getValue() && !"".equalsIgnoreCase(comboCurrency.getValue())) {
				populateTableValues();
				if (!tableData.isEmpty()) {
					comboEmployee.setDisable(false);
					comboAccountNumber.setDisable(false);
					txtVoucherNumber.setDisable(false);
					txtTransactionDate.setDisable(false);
					labelTotalSelectedAmount.setText("0.00");
					populateAccountCombo();
				}else {
					tableDepositDetail.getItems().clear();
					tableData.clear();
					comboEmployee.setDisable(true);
					comboEmployee.setValue("");
					comboAccountNumber.setDisable(true);
					comboAccountNumber.setValue("");
					txtVoucherNumber.setDisable(true);
					txtTransactionDate.setDisable(true);
					labelTotalSelectedAmount.setText("0.00");
				}
			} else {

				tableDepositDetail.getItems().clear();
				tableData.clear();
				comboEmployee.setDisable(true);
				comboEmployee.setValue("");
				comboAccountNumber.setDisable(true);
				comboAccountNumber.setValue("");
				txtVoucherNumber.setDisable(true);
				txtTransactionDate.setDisable(true);
				labelTotalSelectedAmount.setText("0.00");
			}
		} else {
			tableDepositDetail.getItems().clear();
			comboCurrency.setDisable(true);
			tableData.clear();
			comboCurrency.setValue("");
			comboEmployee.setDisable(true);
			comboEmployee.setValue("");
			comboAccountNumber.setDisable(true);
			comboAccountNumber.setValue("");
			txtVoucherNumber.setDisable(true);
			txtTransactionDate.setDisable(true);
			labelTotalSelectedAmount.setText("0.00");
		}
	}

	@FXML
	private void onComboCurrencyAction(ActionEvent event) {
		labelTotalSelectedAmount.setText("0.00");
		populateAccountCombo();
		if (null != comboCurrency.getValue() && !"".equalsIgnoreCase(comboCurrency.getValue())) {
			populateTableValues();
			if (!tableData.isEmpty()) {
				comboEmployee.setDisable(false);
				comboAccountNumber.setDisable(false);
				txtVoucherNumber.setDisable(false);
				txtTransactionDate.setDisable(false);
				labelTotalSelectedAmount.setText("0.00");
				populateAccountCombo();
			}
		} else {

			tableDepositDetail.getItems().clear();
			tableData.clear();
			comboEmployee.setDisable(true);
			comboEmployee.setValue("");
			comboAccountNumber.setDisable(true);
			comboAccountNumber.setValue("");
			txtVoucherNumber.setDisable(true);
			txtTransactionDate.setDisable(true);
			labelTotalSelectedAmount.setText("0.00");
		}
	}

	private void populateAccountCombo() {
		Commons.populateAllComboBox(comboAccountNumber, AccountsDAO.getBankAccounts(comboCurrency.getValue()));
		comboAccountNumber.setValue("");
	}

	private void populateComboBoxs() {
		Commons.populateAllComboBox(comboDepositType, Constants.depositTypeList);
		comboDepositType.setValue("");
		Commons.populateAllComboBox(comboCurrency, Commons.getListValuesFromMap(Constants.currencies));
		comboCurrency.setValue("");
		Commons.populateAllComboBox(comboEmployee, EmployeeDAO.getEmployeeName());
		comboEmployee.setValue("");
	}

	private void generateTableColumns(ObservableList<DepositModel> list) {
		colReceiveID.setCellValueFactory(cellData -> cellData.getValue().receiveIDProperty().asObject());
		colBillID.setCellValueFactory(cellData -> cellData.getValue().billIDProperty().asObject());
		colCurrency.setCellValueFactory(cellData -> cellData.getValue().currencyProperty());
		colAmountReceived.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
		colCheckBox.setCellValueFactory(new PropertyValueFactory<DepositModel, CheckBox>("select"));
		tableDepositDetail.setItems(list);
	}

	private void populateTableValues() {
		String depositType = comboDepositType.getValue();
		String currecy = comboCurrency.getValue();
		tableData = DepositDAO.getTableData(depositType, currecy);
		generateTableColumns(tableData);
	}

	private void calculateSubTotal() {
		if (!tableDepositDetail.getItems().isEmpty()) {
			double subTotal = 0;
			for (DepositModel model : tableData) {
				boolean isSelect = model.getSelect().isSelected();

				if (isSelect) {
					subTotal += Double.parseDouble(model.getAmount());
				}
			}

			labelTotalSelectedAmount.setText(String.valueOf(decimalFormat.format(subTotal)));
		} else {
			labelTotalSelectedAmount.setText("0.00");
		}
	}

	private void saveDepositData() {
		if (!tableDepositDetail.getItems().isEmpty()) {
			String bank = comboAccountNumber.getValue();
			String voucherNo = txtVoucherNumber.getText();
			String employee = comboEmployee.getValue();
			String date = txtTransactionDate.getText();

			for (DepositModel model : tableData) {
				if (model.getSelect().isSelected()) {
					int receiveID = model.getReceiveID();
					String amount = model.getAmount();

					boolean isSuccess = DepositDAO.insertDataToTable(receiveID, amount, bank, date, voucherNo,
							employee);
					if (isSuccess) {
						ReceivableDAO.updateReceivedFlag(receiveID);
						Commons.processMessageLabel(labelInformation, isSuccess);
					}else {
						Commons.processMessageLabel(labelInformation, isSuccess);
					}
				}
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

}
