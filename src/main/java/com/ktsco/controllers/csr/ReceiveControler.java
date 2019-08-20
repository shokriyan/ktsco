package com.ktsco.controllers.csr;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ktsco.models.csr.ReceiveModel;
import com.ktsco.modelsdao.CurrencyDAO;
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

public class ReceiveControler implements Initializable {

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	@FXML
	private Button btnNew, btnSave, btnSaveReturn, btnReturn, btnSearchBill;

	@FXML
	private Label labelInformation, labelBillDate, labelCompany, labelCurrency, labelBillTotal, labelTotalReceived,
			labelRemainAmount;

	@FXML
	private TextField txtCode, txtReceiveDate, txtReceiveAmount;

	@FXML
	private ComboBox<String> comboEmployee, comboDepositType;

	@FXML
	private TableView<ReceiveModel> tableReceiveDetail;
	@FXML
	private TableColumn<ReceiveModel, Integer> colCode;
	@FXML
	private TableColumn<ReceiveModel, String> colReceiveDate, colAmount;

	@FXML
	private ImageView receivedBanner;

	@FXML
	private MenuItem menuEdit, menuDelete;

	ObservableList<ReceiveModel> receiveTableData = FXCollections.observableArrayList();
	public static int saleBillID = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tableReceiveDetail.setEditable(false);
		receivedBanner.setVisible(false);
		reloadPreRequisition();

		txtReceiveDate.textProperty().addListener(new ChangeListener<String>() {
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
		if (saleBillID != 0) {
			txtCode.setText(String.valueOf(saleBillID));
			searchForBillDetail();
		}
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receivablePanel"));
		} else if (event.getSource() == btnSearchBill) {
			searchForBillDetail();
		} else if (event.getSource() == btnSave) {
			SaveReceiveRecords();
		} else if (event.getSource() == btnNew) {
			clearLabelsAndText();
		} else if (event.getSource() == menuEdit) {
			tableReceiveDetail.setEditable(true);
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
		labelTotalReceived.setText("");
		labelRemainAmount.setText("");
		tableReceiveDetail.getItems().clear();
		txtCode.clear();
		txtReceiveAmount.clear();
		txtReceiveDate.clear();
		comboEmployee.setValue("");
		comboDepositType.setValue(Constants.depositTypeList.get(1));
		if ("".equalsIgnoreCase(labelRemainAmount.getText())) {
			receivedBanner.setVisible(false);
		}
	}

	@FXML
	private void onReceivedEditCommit(CellEditEvent<ReceiveModel, String> cellEdited) {
		ReceiveModel model = tableReceiveDetail.getSelectionModel().getSelectedItem();
		if (!ReceivableDAO.getDepositFlag(model.getReceiveID())) {
			boolean response = AlertsUtils.askForUpdateAlert(model.getReceiveAmount());
			if (response) {
				model.setReceiveAmount(Double.parseDouble(cellEdited.getNewValue()));
				ObservableList<ReceiveModel> tableList = tableReceiveDetail.getItems();
				double newReceivedTotal = 0;
				for (ReceiveModel ReceiveModel : tableList) {
					newReceivedTotal += Double.parseDouble(ReceiveModel.getReceiveAmount());
				}
				double newRemainAmount = Double.parseDouble(labelBillTotal.getText().replace(",", ""))
						- newReceivedTotal;
				if (newRemainAmount >= 0) {
					ReceivableDAO.updateReceiveAmount(model.getReceiveAmount(), model.getReceiveID());
					Commons.processMessageLabel(labelInformation, true);
					searchForBillDetail();
					checkFlagForUpdate();
				} else {
					Commons.processMessageLabel(labelInformation, false);
					searchForBillDetail();
				}
			}
		} else {
			AlertsUtils.UnableToChangeAlert();
		}
	}

	private void generateReceiveDetail(ObservableList<ReceiveModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().receiveIDProperty().asObject());
		colReceiveDate.setCellValueFactory(cellData -> cellData.getValue().receiveDateProperty());
		colAmount.setCellValueFactory(cellData -> cellData.getValue().receiveAmountProperty());
		colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
		tableReceiveDetail.setItems(list);
	}

	private void generateBillSummary(int billID) {
		Map<String, Object> billData = ReceivableDAO.reteivedBillDetail(billID);
		if (!billData.isEmpty() && billData != null) {
			labelBillDate.setText(billData.get("billdate").toString());
			labelCompany.setText(billData.get("company").toString());
			labelCurrency.setText(billData.get("currency").toString());
			double billTotal = Double.parseDouble(billData.get("billTotal").toString());
			labelBillTotal.setText(String.valueOf(decimalFormat.format(billTotal)));
			double totalReceived = Double.parseDouble(billData.get("totalReceived").toString());
			labelTotalReceived.setText(String.valueOf(decimalFormat.format(totalReceived)));
			double remainAmount = billTotal - totalReceived;
			labelRemainAmount.setText(String.valueOf(decimalFormat.format(remainAmount)));
			if ("0".equalsIgnoreCase(labelRemainAmount.getText())) {
				receivedBanner.setVisible(true);
			} else {
				receivedBanner.setVisible(false);
			}
			receiveTableData = ReceivableDAO.getReceiveDetailByID(billID);
			generateReceiveDetail(receiveTableData);
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
		Commons.populateAllComboBox(comboDepositType, Constants.depositTypeList);
		comboDepositType.setValue(Constants.depositTypeList.get(1));
	}

	private boolean checkFlag() {
		boolean isPassed = false;
		isPassed = DateUtils.checkEntryDateFormat(txtReceiveDate.getText());
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtCode.getText())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtReceiveDate.getText())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(comboEmployee.getValue())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(comboDepositType.getValue())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtReceiveAmount.getText())) ? false : true;
		return isPassed;
	}

	private void SaveReceiveRecords() {
		if (checkFlag()) {
			int billID = Integer.parseInt(txtCode.getText());
			String receiveDate = txtReceiveDate.getText();
			String employee = comboEmployee.getValue();
			String depositType = comboDepositType.getValue();
			String amount = txtReceiveAmount.getText();

			if (Double.parseDouble(amount) - Double.parseDouble(labelRemainAmount.getText().replace(",", "")) <= 0) {
				boolean isSuccess = ReceivableDAO.insertIntoReceiveTable(billID, receiveDate, employee, amount,
						depositType);
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

	private void updateBillPaidFlag(int bill_id) {
		if (Double.parseDouble(labelRemainAmount.getText().replace(",", "")) == 0) {
			ReceivableDAO.updateBillPaidFlagSale(true, bill_id);
		} else {
			ReceivableDAO.updateBillPaidFlagSale(false, bill_id);
		}
	}

	private void deleteReceiveDetail() {
		if (!tableReceiveDetail.getSelectionModel().isEmpty()) {
			ReceiveModel model = tableReceiveDetail.getSelectionModel().getSelectedItem();
			if (!ReceivableDAO.getDepositFlag(model.getReceiveID())) {
				boolean response = AlertsUtils.askForDeleteAlert(model.getReceiveAmount());
				if (response) {
					ReceivableDAO.deleteReceiveRecord(model.getReceiveID());
					Commons.processMessageLabel(labelInformation, true);
					searchForBillDetail();
					checkFlagForUpdate();
				}
			} else {
				AlertsUtils.UnableToChangeAlert();
			}
		}
	}
}
