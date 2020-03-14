package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.ReceivedDetailModel;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.modelsdao.PayableDAO;
import com.ktsco.modelsdao.ReceivableDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ReceivedDetailReportController implements Initializable {

	public static String panelName = "sale";
	@FXML
	private ComboBox<String> comboEmployee, comboCurrency;
	@FXML
	private TextField txtFromDate, txtToDate;
	@FXML
	private Button btnSearch, btnReset;
	@FXML
	private TableView<ReceivedDetailModel> tableDetailList;

	@FXML
	private TableColumn<ReceivedDetailModel, Integer> colNo, colBillNo;
	@FXML
	private TableColumn<ReceivedDetailModel, String> colEmployee, colDate, colCurrency, colCurrencyRate;

	@FXML
	private TableColumn<ReceivedDetailModel, Double> colOriginalTotal, colTotalUSD;

	@FXML
	private Label labelTotal;

	ObservableList<ReceivedDetailModel> tableData = FXCollections.observableArrayList();
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	private int totalAmount = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateComboBox();
		populateTableData();
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			populateTableData();
		} else if (event.getSource() == btnReset) {
			comboEmployee.setValue("");
			comboCurrency.setValue("");
			txtFromDate.clear();
			txtToDate.clear();
			populateTableData();
		}
	}

	private void populateComboBox() {
		Commons.populateAllComboBox(comboCurrency, Commons.getListValuesFromMap(Constants.currencies));
		comboCurrency.setValue("");
		Commons.populateAllComboBox(comboEmployee, EmployeeDAO.getEmployeeNameWithID());
		comboEmployee.setValue("");
	}

	private void generateTableColumns(ObservableList<ReceivedDetailModel> list) {
		colNo.setCellValueFactory(data -> data.getValue().receivedIdProperty().asObject());
		colBillNo.setCellValueFactory(data -> data.getValue().billIdProperty().asObject());
		colEmployee.setCellValueFactory(data -> data.getValue().employeeNameProperty());
		colDate.setCellValueFactory(data -> data.getValue().receivedDateProperty());
		colCurrency.setCellValueFactory(data -> data.getValue().currencyTypeProperty());
		colCurrencyRate.setCellValueFactory(data -> data.getValue().currencyRateProperty());
		colOriginalTotal.setCellValueFactory(data -> data.getValue().orignalAmountProperty().asObject());
		colTotalUSD.setCellValueFactory(data -> data.getValue().dolorAmountProperty().asObject());
		formatColumn(colOriginalTotal, formatQuantity);
		formatColumn(colTotalUSD, formatPrice);
		tableDetailList.setItems(list);
	}

	private void formatColumn(TableColumn<ReceivedDetailModel, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<ReceivedDetailModel, Double>() {

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

	private void populateTableData() {
		if (!tableData.isEmpty())
			tableData.clear();
		String employee = comboEmployee.getValue();
		String employeeId = (("").equalsIgnoreCase(employee)) ? "" : employee.split("-")[0].trim();
		String currencyType = comboCurrency.getValue();
		String currency = (("").equalsIgnoreCase(currencyType)) ? "" : Commons.getCurrencyKey(currencyType);
		String fromDate = (("").equalsIgnoreCase(txtFromDate.getText())) ? "1900-01-01"
				: DateUtils.convertJalaliToGregory(txtFromDate.getText());
		String toDate = (("").equalsIgnoreCase(txtToDate.getText())) ? "2900-12-31"
				: DateUtils.convertJalaliToGregory(txtToDate.getText());
		if (("sale").equalsIgnoreCase(panelName))
			tableData = ReceivableDAO.getReceivedDetailReport(employeeId, currency, fromDate, toDate);
		else 
			tableData = PayableDAO.getPaymentsReport(employeeId, currency, fromDate, toDate);
		generateTableColumns(tableData);
		calculateTotalAmount();
	}

	private void calculateTotalAmount() {
		totalAmount = 0;
		if (!tableData.isEmpty()) {
			for (ReceivedDetailModel model : tableData) {
				totalAmount += model.getDolorAmount();
			}

		}
		
		labelTotal.setText(formatPrice.format(totalAmount));
	}

}
