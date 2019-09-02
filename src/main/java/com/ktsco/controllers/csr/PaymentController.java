package com.ktsco.controllers.csr;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ktsco.models.csr.PaybillModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.modelsdao.CurrencyDAO;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.modelsdao.PayableDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PaymentController implements Initializable {

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn, btnSearchBill;

	@FXML
	private Label labelInformation, labelBillDate, labelCompany, labelCurrency, labelBillTotal, labelTotalPaid,
			labelRemainAmount;

	@FXML
	private TextField txtCode, txtPayDate, txtAmount;

	@FXML
	private ComboBox<String> comboEmployee, comboAccount;

	@FXML
	private TableView<PaybillModel> tablePaymentDetail;
	@FXML
	private TableColumn<PaybillModel, Integer> colCode;
	@FXML
	private TableColumn<PaybillModel, String> colPayDate, colAmount, colBankAccount;

	@FXML
	private ImageView payBanner;

	@FXML
	private MenuItem menuEdit, menuDelete;

	ObservableList<PaybillModel> tableData = FXCollections.observableArrayList();
	public static int expenseID = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tablePaymentDetail.setEditable(false);
		payBanner.setVisible(false);
		reloadPreRequisition();

		txtPayDate.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					String getDate = DateUtils.convertJalaliToGregory(newValue);
					String currencyType = Commons.getCurrencyKey(labelCurrency.getText());
					String exRate = CurrencyDAO.getCurrencyRate(currencyType, getDate);
					if ("".equalsIgnoreCase(exRate)) {
						AlertsUtils.CurrencyEntryAlert(currencyType);

					}
				}
			}
		});

		txtCode.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					if (!"".equalsIgnoreCase(txtCode.getText())) {
						searchForBillDetail();
					}
				}
			}

		});
		setBillIDShowData();
	}

	private void reloadPreRequisition() {
		populateComboBox();
	}

	private void setBillIDShowData() {
		if (expenseID != 0) {
			txtCode.setText(String.valueOf(expenseID));
			searchForBillDetail();
		}
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("PayablePanel"));
		} else if (event.getSource() == btnSearchBill) {
			searchForBillDetail();
		} else if (event.getSource() == btnSave) {
			SaveReceiveRecords();
		} else if (event.getSource() == btnNew) {
			clearLabelsAndText();
		} else if (event.getSource() == menuEdit) {
			tablePaymentDetail.setEditable(true);
		} else if (event.getSource() == menuDelete) {
			deleteReceiveDetail();
		} else if (event.getSource() == btnSaveReturn) {
			boolean response = AlertsUtils.askForReturn();
			if (response) {
				SaveReceiveRecords();
				Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receivablePanel"));
			}
		}
	}

	private void clearLabelsAndText() {
		labelBillDate.setText("");
		labelCompany.setText("");
		labelCurrency.setText("");
		labelBillTotal.setText("");
		labelTotalPaid.setText("");
		labelRemainAmount.setText("");
		tablePaymentDetail.getItems().clear();
		txtCode.clear();
		txtAmount.clear();
		txtPayDate.clear();
		comboEmployee.setValue("");
		comboAccount.setValue("");
		if ("".equalsIgnoreCase(labelRemainAmount.getText())) {
			payBanner.setVisible(false);
		}
	}

	@FXML
	private void onAmountEditCommit(CellEditEvent<PaybillModel, String> cellEdited) {
		PaybillModel model = tablePaymentDetail.getSelectionModel().getSelectedItem();

		boolean response = AlertsUtils.askForUpdateAlert(model.getAmount());
		if (response) {
			model.setAmount(Double.parseDouble(cellEdited.getNewValue()));
			ObservableList<PaybillModel> tableList = tablePaymentDetail.getItems();
			double newPayTotal = 0;
			for (PaybillModel paymodel : tableList) {
				newPayTotal += Double.parseDouble(paymodel.getAmount());
			}
			double newRemainAmount = Double.parseDouble(labelBillTotal.getText().replace(",", "")) - newPayTotal;
			if (newRemainAmount >= 0) {
				PayableDAO.updatePayAmount(model.getAmount(), model.getPayID());
				Commons.processMessageLabel(labelInformation, true);
				searchForBillDetail();
				checkFlagForUpdate();
			} else {
				Commons.processMessageLabel(labelInformation, false);
				searchForBillDetail();
			}
		}
	}

	private void generateReceiveDetail(ObservableList<PaybillModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().payIDProperty().asObject());
		colPayDate.setCellValueFactory(cellData -> cellData.getValue().payDateProperty());
		colBankAccount.setCellValueFactory(cellData -> cellData.getValue().bankAccountProperty());
		colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
		colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
		tablePaymentDetail.setItems(list);
	}

	private void generateBillSummary(int billID) {
		Map<String, Object> billData = PayableDAO.reteivedBillDetail(billID);
		if (!billData.isEmpty() && billData != null) {
			labelBillDate.setText(billData.get("expns_id").toString());
			labelCompany.setText(billData.get("company").toString());
			labelCurrency.setText(billData.get("currency").toString());
			double billTotal = Double.parseDouble(billData.get("billtotal").toString());
			labelBillTotal.setText(String.valueOf(decimalFormat.format(billTotal)));
			double totalPaid = Double.parseDouble(billData.get("totalPaid").toString());
			labelTotalPaid.setText(String.valueOf(decimalFormat.format(totalPaid)));
			double remainAmount = billTotal - totalPaid;
			labelRemainAmount.setText(String.valueOf(decimalFormat.format(remainAmount)));
			if ("0".equalsIgnoreCase(labelRemainAmount.getText())) {
				payBanner.setVisible(true);
			} else {
				payBanner.setVisible(false);
			}
			tableData = PayableDAO.getPaymentsDetail(billID);
			generateReceiveDetail(tableData);
		} else {
			clearLabelsAndText();
		}
	}

	private void searchForBillDetail() {
		if (!"".equalsIgnoreCase(txtCode.getText())) {
			int billID = Integer.parseInt(txtCode.getText());
			generateBillSummary(billID);
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void populateComboBox() {
		List<String> employeeList = EmployeeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, employeeList);
		comboEmployee.setValue("");
		List<String> accountsList = AccountsDAO.getBankAccounts();
		Commons.populateAllComboBox(comboAccount, accountsList);
		comboAccount.setValue("");
	}

	private boolean checkFlag() {
		boolean isPassed = false;
		isPassed = DateUtils.checkEntryDateFormat(txtPayDate.getText());
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtCode.getText())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtPayDate.getText())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(comboEmployee.getValue())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(comboAccount.getValue())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtAmount.getText())) ? false : true;
		return isPassed;
	}

	private void SaveReceiveRecords() {
		if (checkFlag()) {
			int billID = Integer.parseInt(txtCode.getText());
			String receiveDate = txtPayDate.getText();
			String employee = comboEmployee.getValue();
			String bankAccount = comboAccount.getValue();
			String amount = txtAmount.getText();

			if (Double.parseDouble(amount) - Double.parseDouble(labelRemainAmount.getText().replace(",", "")) <= 0) {
				boolean isSuccess = PayableDAO.insertIntoPayment(billID, receiveDate, employee, amount, bankAccount);
				Commons.processMessageLabel(labelInformation, isSuccess);
				searchForBillDetail();
				checkFlagForUpdate();
			} else {

				Commons.processMessageLabel(labelInformation, false);
			}
		} else {
			Commons.processMessageLabel(labelInformation, false);
		}
	}

	private void checkFlagForUpdate() {
		int billID = Integer.parseInt(txtCode.getText());
		updateBillPaidFlag(billID);
	}

	private void updateBillPaidFlag(int expnsID) {
		if (Double.parseDouble(labelRemainAmount.getText().replace(",", "")) == 0) {
			PayableDAO.updateExpensePaidFlagSale(true, expnsID);
		} else {
			PayableDAO.updateExpensePaidFlagSale(false, expnsID);
		}
	}

	private void deleteReceiveDetail() {
		if (!tablePaymentDetail.getSelectionModel().isEmpty()) {
			PaybillModel model = tablePaymentDetail.getSelectionModel().getSelectedItem();
			boolean response = AlertsUtils.askForDeleteAlert(model.getAmount());
			if (response) {
				PayableDAO.deletePaybillRecord(model.getPayID());
				Commons.processMessageLabel(labelInformation, true);
				searchForBillDetail();
				checkFlagForUpdate();
			}
		}
	}
}