package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ktsco.models.csr.ReceiveModel;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.modelsdao.ReceivableDAO;
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

public class ReceiveSearchController implements Initializable {

	@FXML
	private TextField txtBillID, txtStartDate, txtEndDate;

	@FXML
	private ComboBox<String> comboEmployee;

	@FXML
	private Button btnSearch, btnRefresh, btnReturn;

	@FXML
	private Label labelInformation;

	@FXML
	private TableView<ReceiveModel> tableReceiveDetail;
	@FXML
	private TableColumn<ReceiveModel, Integer> colID, colBillCode;
	@FXML
	private TableColumn<ReceiveModel, String> colReceiveDate, colEmployee, colAmount, colDepositType;

	@FXML
	private MenuItem menuEdit, menuDelete;

	ObservableList<ReceiveModel> tableData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableReceiveDetail.setEditable(false);
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
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receivablePanel"));
		} else if (event.getSource() == menuEdit) {
			tableReceiveDetail.setEditable(true);
		}else if (event.getSource() == menuDelete) {
			deleteReceiveAmount();
		}
	}

	@FXML
	private void onReceivedEditCommit(CellEditEvent<ReceiveModel, String> edittedCell) {
		if (!tableReceiveDetail.getSelectionModel().isEmpty()) {

			ReceiveModel model = tableReceiveDetail.getSelectionModel().getSelectedItem();
			boolean isDeposit = ReceivableDAO.getDepositFlag(model.getReceiveID());
			if (!isDeposit) {
				boolean response = AlertsUtils.askForUpdateAlert("فاکتور " + "\n" + model.getBillID());
				if (response) {
					Map<String, Object> billMapData = ReceivableDAO.reteivedBillDetail(model.getBillID());

					double billTotal = Double.parseDouble(billMapData.get("billTotal").toString());
					double totalReceived = Double.parseDouble(billMapData.get("totalReceived").toString());
					System.out.println(totalReceived);
					double cellOldValue = Double.parseDouble(edittedCell.getOldValue().replace(",", ""));
					System.out.println("Cell Old Value " + cellOldValue);
					double cellNewVlue = Double.parseDouble(edittedCell.getNewValue());
					if ((totalReceived - cellOldValue) + cellNewVlue <= billTotal) {
						double newRemainAmount = billTotal - ((totalReceived - cellOldValue) + cellNewVlue);
						model.setReceiveAmount(Double.parseDouble(edittedCell.getNewValue()));
						ReceivableDAO.updateReceiveAmount(edittedCell.getNewValue(), model.getReceiveID());
						updateBillPaidFlag(newRemainAmount, model.getBillID());
						Commons.processMessageLabel(labelInformation, true);
					} else {
						populateTableData();
						Commons.processMessageLabel(labelInformation, false);
					}
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

	private void generateTableCells(ObservableList<ReceiveModel> list) {
		colID.setCellValueFactory(cellData -> cellData.getValue().receiveIDProperty().asObject());
		colBillCode.setCellValueFactory(cellData -> cellData.getValue().billIDProperty().asObject());
		colReceiveDate.setCellValueFactory(cellData -> cellData.getValue().receiveDateProperty());
		colEmployee.setCellValueFactory(cellData -> cellData.getValue().employeeProperty());
		colAmount.setCellValueFactory(cellData -> cellData.getValue().receiveAmountProperty());
		colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
		colDepositType.setCellValueFactory(cellData -> cellData.getValue().depositTypeProperty());
		tableReceiveDetail.setItems(list);
	}

	private void populateTableData() {
		String billID = txtBillID.getText();
		String startDate = txtStartDate.getText();
		String endDate = txtEndDate.getText();
		String employee = comboEmployee.getValue();

		tableData = ReceivableDAO.searchReceiveRecords(billID, startDate, endDate, employee);
		generateTableCells(tableData);
	}

	private void updateBillPaidFlag(double remainAmount, int bill_id) {

		if (remainAmount == 0) {
			ReceivableDAO.updateBillPaidFlagSale(true, bill_id);
		} else {
			ReceivableDAO.updateBillPaidFlagSale(false, bill_id);
		}
	}

	private void deleteReceiveAmount() {
		if (!tableReceiveDetail.getSelectionModel().isEmpty()) {
			ReceiveModel model = tableReceiveDetail.getSelectionModel().getSelectedItem();
			boolean isDeposit = ReceivableDAO.getDepositFlag(model.getReceiveID());
			if (!isDeposit) {
				if (AlertsUtils.askForDeleteAlert(String.valueOf(model.getReceiveID()))) {
					ReceivableDAO.deleteReceiveRecord(model.getReceiveID());
					Commons.processMessageLabel(labelInformation, true);
					Map<String, Object> billMapData = ReceivableDAO.reteivedBillDetail(model.getBillID());

					double billTotal = Double.parseDouble(billMapData.get("billTotal").toString());
					double totalReceived = Double.parseDouble(billMapData.get("totalReceived").toString());
					double remainAmount = billTotal - totalReceived; 
					updateBillPaidFlag(remainAmount, model.getBillID());
					populateTableData();
				}
			} else {
				AlertsUtils.UnableToChangeAlert();
			}
		}

	}

}
