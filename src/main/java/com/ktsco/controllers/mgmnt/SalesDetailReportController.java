package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.SalesDetailModel;
import com.ktsco.modelsdao.ExpenseDAO;
import com.ktsco.modelsdao.InventoryDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.modelsdao.SaleBillDAO;
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

public class SalesDetailReportController implements Initializable {

	public static String panelName = "sale";
	
	@FXML
	private ComboBox<String> comboProducts, comboCurrency;
	@FXML
	private TextField txtFromDate, txtToDate;
	@FXML
	private Button btnSearch, btnReset;
	@FXML
	private TableView<SalesDetailModel> tableDetailList;

	@FXML
	private TableColumn<SalesDetailModel, Integer> colNo;
	@FXML
	private TableColumn<SalesDetailModel, String> colProducts, colSellDate, colCurrency, colCurrencyRate;

	@FXML
	private TableColumn<SalesDetailModel, Double> colQuantity, colUnitPrice, colOriginalTotal,
			colTotalUSD;

	@FXML
	private Label labelTotal, labelComboItems;

	ObservableList<SalesDetailModel> tableData = FXCollections.observableArrayList();
	List<String> comboData = new ArrayList<String>();
	
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	private int totalAmount = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (("sale").equalsIgnoreCase(panelName)) {
			comboData = ProductDAO.getProductList();
		}else {
			labelComboItems.setText("جنس");
			colProducts.setText("نوع جنس");
			comboData = InventoryDAO.getInvItemsWithCode();
		}
				
		populateComboBox();
		populateTableData();
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			populateTableData();
		} else if (event.getSource() == btnReset) {
			comboProducts.setValue("");
			comboCurrency.setValue("");
			txtFromDate.clear();
			txtToDate.clear();
			populateTableData();
		}
	}

	private void populateComboBox() {
		Commons.populateAllComboBox(comboCurrency, Commons.getListValuesFromMap(Constants.currencies));
		comboCurrency.setValue("");
		Commons.populateAllComboBox(comboProducts, comboData);
		comboProducts.setValue("");
	}

	private void generateTableColumns(ObservableList<SalesDetailModel> list) {
		colNo.setCellValueFactory(data -> data.getValue().billCodeProperty().asObject());
		colProducts.setCellValueFactory(data -> data.getValue().productNameProperty());
		colSellDate.setCellValueFactory(data -> data.getValue().saleDateProperty());
		colCurrency.setCellValueFactory(data -> data.getValue().currencyTypeProperty());
		colCurrencyRate.setCellValueFactory(data -> data.getValue().currencyRateProperty());
		colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
		colUnitPrice.setCellValueFactory(data -> data.getValue().unitPriceProperty().asObject());
		colOriginalTotal.setCellValueFactory(data -> data.getValue().originalLinePriceProperty().asObject());
		colTotalUSD.setCellValueFactory(data -> data.getValue().usdLinePriceProperty().asObject());
		formatColumn(colQuantity, formatQuantity);
		formatColumn(colUnitPrice, formatQuantity);
		formatColumn(colOriginalTotal, formatQuantity);
		formatColumn(colTotalUSD, formatPrice);
		tableDetailList.setItems(list);
	}

	private void formatColumn(TableColumn<SalesDetailModel, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<SalesDetailModel, Double>() {

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
		String productName = comboProducts.getValue();
		String currency = comboCurrency.getValue();
		String fromDate = txtFromDate.getText();
		String toDate = txtToDate.getText();
		if (("sale").equalsIgnoreCase(panelName))
		tableData = SaleBillDAO.getSalesDetailReport(productName, currency, fromDate, toDate);
		else 
			tableData = ExpenseDAO.getExpenseDetailReport(productName, currency, fromDate, toDate);
		generateTableColumns(tableData);
		calculateTotalAmount();
	}

	private void calculateTotalAmount() {
		totalAmount = 0;
		if (!tableData.isEmpty()) {
			for (SalesDetailModel model : tableData) {
				totalAmount += model.getUsdLinePrice();
			}

		}
		labelTotal.setText(formatPrice.format(totalAmount));
	}

}
