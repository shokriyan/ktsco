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

public class SellUSDSummeryController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(SellsSummeryController.class);
	public static String panelName = "";

	@FXML
	private ComboBox<String> comboCustomer;

	@FXML
	private TextField txtFromDate, txtToDate;

	@FXML
	private Button btnSearch, btnReset;

	@FXML
	private TableView<SellSummaryModel> tableDetailList;

	@FXML
	private TableColumn<SellSummaryModel, Integer> colNo;

	@FXML
	private TableColumn<SellSummaryModel, String> colCustomer, colSellDate;

	@FXML
	private TableColumn<SellSummaryModel, Double> colBillTotal, colReceiveTotal, colRemained;

	@FXML
	private Label labelReceivedTotal, labelRemainTotal, labelBillTotal, labelComboText, labelTableRecText;

	private List<String> comboData = new ArrayList<String>();
	private ObservableList<SellSummaryModel> datalist = FXCollections.observableArrayList();
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	public double totalBill = 0;
	public double totalReceived = 0;
	public double totalRemained = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info(panelName);
		if (panelName.equalsIgnoreCase("expense")) {
			labelComboText.setText("فروشنده");
			colCustomer.setText("فروشنده");
			colReceiveTotal.setText("پرداخت");
			labelTableRecText.setText("جمع کل پرداخت");
			comboData = VendorsDAO.getVendorList();
		} else {
			comboData = CustomerDAO.getCustomersList();
		}
		populatingComboBoxes();
		comboCustomer.setEditable(true);
		TextFields.bindAutoCompletion(comboCustomer.getEditor(), comboCustomer.getItems());
		populateTableData();
		labelBillTotal.setText(formatPrice.format(totalBill));
		labelReceivedTotal.setText(formatPrice.format(totalReceived));
		labelRemainTotal.setText(formatPrice.format(totalRemained));
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			populateTableData();

		} else if (event.getSource() == btnReset) {
			populatingComboBoxes();
			txtToDate.clear();
			txtFromDate.clear();
			populateTableData();
		}
	}

	private void populatingComboBoxes() {
		Commons.populateAllComboBox(comboCustomer, comboData);
		comboCustomer.setValue("");
	}

	private void generateTableColumns(ObservableList<SellSummaryModel> list) {
		colNo.setCellValueFactory(data -> data.getValue().billNoProperty().asObject());
		colCustomer.setCellValueFactory(data -> data.getValue().customerNameProperty());
		colSellDate.setCellValueFactory(data -> data.getValue().billDateProperty());
		colBillTotal.setCellValueFactory(data -> data.getValue().originalAmountProperty().asObject());
		formatColumn(colBillTotal, formatPrice);
		colReceiveTotal.setCellValueFactory(data -> data.getValue().receivedAmountProperty().asObject());
		formatColumn(colReceiveTotal, formatPrice);
		colRemained.setCellValueFactory(data -> data.getValue().usdAmountProperty().asObject());
		formatColumn(colRemained, formatPrice);

		tableDetailList.setItems(list);
	}
	

	private void populateTableData() {
		String comboValue = comboCustomer.getValue();
		String customerCode = (comboValue == null || comboValue.equalsIgnoreCase("")) ? ""
				: comboValue.split("-")[0].trim();
		String fromDate = txtFromDate.getText();
		String toDate = txtToDate.getText();
		if (panelName.equalsIgnoreCase("expense")) {
			datalist = ExpenseDAO.getExpensePaymentSummaryReport(customerCode, fromDate, toDate);
		} else {
			datalist = SaleBillDAO.getSalesUSDSummaryReport(customerCode, fromDate, toDate);
		}
		generateTableColumns(datalist);
		calculateTotalAmount();
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
		totalBill = 0;
		totalReceived = 0;
		totalRemained = 0;
		if (!datalist.isEmpty()) {
			for (SellSummaryModel model : datalist) {
				totalBill += model.getOriginalAmount();
				totalReceived += model.getReceivedAmount();
				totalRemained += model.getUsdAmount();
			}

			labelBillTotal.setText(formatPrice.format(totalBill));
			labelReceivedTotal.setText(formatPrice.format(totalReceived));
			labelRemainTotal.setText(formatPrice.format(totalRemained));
		} else {
			labelBillTotal.setText(formatPrice.format(totalBill));
			labelReceivedTotal.setText(formatPrice.format(totalReceived));
			labelRemainTotal.setText(formatPrice.format(totalRemained));
		}
	}

}
