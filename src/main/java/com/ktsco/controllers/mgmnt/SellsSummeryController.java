package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.mgmt.SellSummaryModel;
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

public class SellsSummeryController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(SellsSummeryController.class);
	public static String panelName = "";
	@FXML
	private ComboBox<String> comboCustomer, comboCurrency;

	@FXML
	private TextField txtFromDate, txtToDate;

	@FXML
	private Button btnSearch, btnReset;

	@FXML
	private TableView<SellSummaryModel> tableDetailList;

	@FXML
	private TableColumn<SellSummaryModel, Integer> colNo;

	@FXML
	private TableColumn<SellSummaryModel, String> colCustomer, colSellDate, colCurrency, colCurrencyRate;

	@FXML
	private TableColumn<SellSummaryModel, Double> colOriginalAmount, colUsdAmount;

	@FXML
	private Label labelTotal, labelComboValue;

	private ObservableList<SellSummaryModel> datalist = FXCollections.observableArrayList();
	private List<String> comboValues = new ArrayList<String>();
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	public double totalAmount = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info(panelName);
		if (panelName.equalsIgnoreCase("sales")) {
			comboValues = CustomerDAO.getCustomersList();
			labelComboValue.setText("مشتری");
		} else if (panelName.equalsIgnoreCase("expense")) {
			comboValues = VendorsDAO.getVendorList();
			labelComboValue.setText("فروشنده");
			colCustomer.setText("فروشنده");
		}

		populatingComboBox();
		comboCustomer.setEditable(true);
		TextFields.bindAutoCompletion(comboCustomer.getEditor(), comboCustomer.getItems());
		populateReportData();
		populateTableData();
		labelTotal.setText(formatPrice.format(totalAmount));
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {

		if (event.getSource() == btnSearch) {
			totalAmount = 0;
			populateReportData();
			populateTableData();
		} else if (event.getSource() == btnReset) {
			comboCustomer.setValue("");
			comboCurrency.setValue("");
			txtToDate.clear();
			txtFromDate.clear();
			totalAmount = 0;
			populateReportData();
			populateTableData();
		}

	}

	private void populateReportData() {
		if (panelName.equalsIgnoreCase("sales")) {
			getSalesData();
		} else if (panelName.equalsIgnoreCase("expense")) {
			getExpenseData();
		}
	}

	private void populatingComboBox() {
		Commons.populateAllComboBox(comboCustomer, comboValues);
		comboCustomer.setValue("");
		Commons.populateAllComboBox(comboCurrency, Commons.getListValuesFromMap(Constants.currencies));
		comboCurrency.setValue("");
	}

	private void generateTableColumns(ObservableList<SellSummaryModel> list) {
		colNo.setCellValueFactory(data -> data.getValue().billNoProperty().asObject());
		colCustomer.setCellValueFactory(data -> data.getValue().customerNameProperty());
		colSellDate.setCellValueFactory(data -> data.getValue().billDateProperty());
		colCurrency.setCellValueFactory(data -> data.getValue().currencyProperty());
		colCurrencyRate.setCellValueFactory(data -> data.getValue().currencyRateProperty());

		colOriginalAmount.setCellValueFactory(data -> data.getValue().originalAmountProperty().asObject());
		formatColumn(colOriginalAmount, formatQuantity);
		colUsdAmount.setCellValueFactory(data -> data.getValue().usdAmountProperty().asObject());
		formatColumn(colUsdAmount, formatPrice);

		tableDetailList.setItems(list);
	}

	private void getSalesData() {
		String comboValue = comboCustomer.getValue();
		String customerCode = (comboValue == null || comboValue.equalsIgnoreCase("")) ? ""
				: comboValue.split("-")[0].trim();
		String currencyType = comboCurrency.getValue();
		String fromDate = txtFromDate.getText();
		String toDate = txtToDate.getText();

		datalist = SaleBillDAO.getSalesSummaryReport(customerCode, fromDate, toDate, currencyType);
	}

	private void getExpenseData() {
		String comboValue = comboCustomer.getValue();
		String customerCode = (comboValue == null || comboValue.equalsIgnoreCase("")) ? ""
				: comboValue.split("-")[0].trim();
		String currencyType = comboCurrency.getValue();
		String fromDate = txtFromDate.getText();
		String toDate = txtToDate.getText();

		datalist = ExpenseDAO.getExpenseSummaryReport(customerCode, fromDate, toDate, currencyType);
	}

	private void populateTableData() {
		if (!datalist.isEmpty()) {
			generateTableColumns(datalist);
			calculateTotalAmount();
		}
	}

	private void formatColumn(TableColumn<SellSummaryModel, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<SellSummaryModel, Double>() {

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

	private void calculateTotalAmount() {
		if (!datalist.isEmpty()) {
			for (SellSummaryModel model : datalist) {
				totalAmount += model.getUsdAmount();
			}

			labelTotal.setText(formatPrice.format(totalAmount));
		} else
			labelTotal.setText(formatPrice.format(totalAmount));
	}

}
