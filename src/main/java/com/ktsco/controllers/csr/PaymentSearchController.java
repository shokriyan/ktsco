package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ktsco.models.csr.PaybillModel;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;

public class PaymentSearchController implements Initializable {

	@FXML
	private TextField txtBillID, txtStartDate, txtEndDate;

	@FXML
	private ComboBox<String> comboEmployee;

	@FXML
	private Button btnSearch, btnRefresh, btnReturn;

	@FXML
	private Label labelInformation;

	@FXML
	private TableView<PaybillModel> tableDetail;
	@FXML
	private TableColumn<PaybillModel, Integer> colID, colBillCode;
	@FXML
	private TableColumn<PaybillModel, String> colDate, colEmployee, colAmount, colBankAccount;

	@FXML
	private MenuItem menuEdit, menuDelete;

	ObservableList<PaybillModel> tableData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableDetail.setEditable(false);
		loadPrerequisitions();

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
		populateTableData();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			populateTableData();
		} else if (event.getSource() == btnRefresh) {
			txtBillID.clear();
			txtEndDate.clear();
			txtStartDate.clear();
			comboEmployee.setValue("");
			populateTableData();
		} else if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("PayablePanel"));
		} else if (event.getSource() == menuEdit) {
			tableDetail.setEditable(true);
		} else if (event.getSource() == menuDelete) {
			deleteReceiveAmount();
		}
	}

	@FXML
	private void onReceivedEditCommit(CellEditEvent<PaybillModel, String> edittedCell) {
		if (!tableDetail.getSelectionModel().isEmpty()) {

			PaybillModel model = tableDetail.getSelectionModel().getSelectedItem();
			boolean response = AlertsUtils.askForUpdateAlert("فاکتور " + "\n" + model.getExpense_id());
			if (response) {
				Map<String, Object> billMapData = PayableDAO.reteivedBillDetail(model.getExpense_id());

				double billTotal = Double.parseDouble(billMapData.get("billtotal").toString());
				double totalReceived = Double.parseDouble(billMapData.get("totalPaid").toString());
				System.out.println(totalReceived);
				double cellOldValue = Double.parseDouble(edittedCell.getOldValue().replace(",", ""));
				System.out.println("Cell Old Value " + cellOldValue);
				double cellNewVlue = Double.parseDouble(edittedCell.getNewValue());
				if ((totalReceived - cellOldValue) + cellNewVlue <= billTotal) {
					double newRemainAmount = billTotal - ((totalReceived - cellOldValue) + cellNewVlue);
					model.setAmount(Double.parseDouble(edittedCell.getNewValue()));
					PayableDAO.updatePayAmount(edittedCell.getNewValue(), model.getPayID());
					updateBillPaidFlag(newRemainAmount, model.getExpense_id());
					Commons.processMessageLabel(labelInformation, true);
				} else {
					populateTableData();
					Commons.processMessageLabel(labelInformation, false);
				}
			} else {
				AlertsUtils.UnableToChangeAlert();
				populateTableData();
			}
		}
	}

	private void loadPrerequisitions() {
		popuplateComboBox();
	}

	private void popuplateComboBox() {
		List<String> employeeList = EmployeeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, employeeList);
		comboEmployee.setValue("");
	}

	private void generateTableCells(ObservableList<PaybillModel> list) {
		colID.setCellValueFactory(cellData -> cellData.getValue().payIDProperty().asObject());
		colBillCode.setCellValueFactory(cellData -> cellData.getValue().expense_idProperty().asObject());
		colDate.setCellValueFactory(cellData -> cellData.getValue().payDateProperty());
		colEmployee.setCellValueFactory(cellData -> cellData.getValue().employeeProperty());
		colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
		colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
		colBankAccount.setCellValueFactory(cellData -> cellData.getValue().bankAccountProperty());
		tableDetail.setItems(list);
	}

	private void populateTableData() {
		String billID = txtBillID.getText();
		String startDate = txtStartDate.getText();
		String endDate = txtEndDate.getText();
		String employee = comboEmployee.getValue();

		tableData = PayableDAO.searchPayments(billID, startDate, endDate, employee);
		generateTableCells(tableData);
	}

	private void updateBillPaidFlag(double remainAmount, int bill_id) {

		if (remainAmount == 0) {
			PayableDAO.updateExpensePaidFlagSale(true, bill_id);
		} else {
			PayableDAO.updateExpensePaidFlagSale(false, bill_id);
		}
	}

	private void deleteReceiveAmount() {
		if (!tableDetail.getSelectionModel().isEmpty()) {
			PaybillModel model = tableDetail.getSelectionModel().getSelectedItem();
			if (AlertsUtils.askForDeleteAlert(String.valueOf(model.getPayID()))) {
				PayableDAO.deletePaybillRecord(model.getPayID());
				Commons.processMessageLabel(labelInformation, true);
				Map<String, Object> billMapData = PayableDAO.reteivedBillDetail(model.getExpense_id());

				double billTotal = Double.parseDouble(billMapData.get("billtotal").toString());
				double totalReceived = Double.parseDouble(billMapData.get("totalPaid").toString());
				double remainAmount = billTotal - totalReceived;
				updateBillPaidFlag(remainAmount, model.getExpense_id());
				populateTableData();
			}
		} else {
			AlertsUtils.UnableToChangeAlert();
		}
 
	}

}
