package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.AmountOweModal;
import com.ktsco.modelsdao.CustomerDAO;
import com.ktsco.modelsdao.ExpenseDAO;
import com.ktsco.modelsdao.SaleBillDAO;
import com.ktsco.modelsdao.VendorsDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;

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

public class AmountOweController implements Initializable {


	public static String reportType = "sale";
	@FXML
	private Button btnSearch, btnReset;
	@FXML
	private Label labelComboValue;
	@FXML
	private ComboBox<String> comboList, comboCurrency;
	@FXML
	private TextField txtFromDate, txtToDate;
	@FXML
	private TableView<AmountOweModal> tableDetailList;
	@FXML
	private TableColumn<AmountOweModal, Integer> colNo;
	@FXML
	private TableColumn<AmountOweModal, String> colCustomer, colCurrency;
	@FXML
	private TableColumn<AmountOweModal, Double> colBillTotal, colRecPay, colRemained;

	ObservableList<AmountOweModal> tableData = FXCollections.observableArrayList();
	List<String> comboItems = new ArrayList<String>();

	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (reportType.equalsIgnoreCase("expense")) {
			labelComboValue.setText("فروشنده");
			colCustomer.setText("فروشنده");
			colRecPay.setText("کل پرداخت");
			comboItems = VendorsDAO.getVendorList();
		} else {
			comboItems = CustomerDAO.getCustomersList();
		}
		populateComboItems();

		populateTableData();
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			tableData.clear();
			populateTableData();
		} else if (event.getSource() == btnReset) {
			comboList.setValue("");
			comboCurrency.setValue("");
			txtFromDate.clear();
			txtToDate.clear();
			populateTableData();
		}
	}

	private void populateComboItems() {

		Commons.populateAllComboBox(comboList, comboItems);
		Commons.populateAllComboBox(comboCurrency, Commons.getListValuesFromMap(Constants.currencies));
		comboList.setValue("");
		comboCurrency.setValue("");
	}

	private void generateTableColumns(ObservableList<AmountOweModal> list) {
		colNo.setCellValueFactory(data -> data.getValue().idProperty().asObject());
		colCustomer.setCellValueFactory(data -> data.getValue().companyProperty());
		colCurrency.setCellValueFactory(data -> data.getValue().currencyProperty());
		colBillTotal.setCellValueFactory(data -> data.getValue().billTotalProperty().asObject());
		if (reportType.equalsIgnoreCase("sale"))
			colRecPay.setCellValueFactory(data -> data.getValue().receivedTotalProperty().asObject());
		else if (reportType.equalsIgnoreCase("expense"))
			colRecPay.setCellValueFactory(data -> data.getValue().paidTotalProperty().asObject());
		colRemained.setCellValueFactory(data -> data.getValue().amountRemainedProperty().asObject());
		formatColumn(colBillTotal, formatQuantity);
		formatColumn(colRecPay, formatQuantity);
		formatColumn(colRemained, formatQuantity);
		tableDetailList.setItems(list);
	}

	private void formatColumn(TableColumn<AmountOweModal, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<AmountOweModal, Double>() {

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
		String customer = comboList.getValue();
		String customerId = (!("").equalsIgnoreCase(customer)) ? customer.split("-")[0].trim() : "";
		String currencyType = comboCurrency.getValue();
		String fromDate = txtFromDate.getText();
		String toDate = txtToDate.getText();
		if (reportType.equalsIgnoreCase("sale"))
			tableData = SaleBillDAO.getSalesAmountOweReport(customerId, currencyType, fromDate, toDate);
		else if (reportType.equalsIgnoreCase("expense"))
			tableData = ExpenseDAO.getExpenseAmountOweReport(customer, currencyType, fromDate, toDate);
		generateTableColumns(tableData);

	}

}
