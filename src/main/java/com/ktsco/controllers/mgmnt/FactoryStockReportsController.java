package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.ProductsRepoModel;
import com.ktsco.modelsdao.MainStockDAO;
import com.ktsco.modelsdao.ProductDAO;
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
import javafx.scene.paint.Color;

public class FactoryStockReportsController implements Initializable {

	@FXML
	private ComboBox<String> comboProducts;

	@FXML
	private Button btnSearch, btnReset;

	@FXML
	private TableView<ProductsRepoModel> tableDetailList;
	@FXML
	private TableColumn<ProductsRepoModel, Integer> colNo;
	@FXML
	private TableColumn<ProductsRepoModel, String> colItems, colUnit;
	@FXML
	private TableColumn<ProductsRepoModel, Double> colImport, colExport, colRemained, colPrice, colLineTotal;

	private ObservableList<ProductsRepoModel> tableData = FXCollections.observableArrayList();
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	public double totalAmount = 0;

	@FXML
	private Label labelTotal;

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			searchForProducts();
		} else if (event.getSource() == btnReset) {
			comboProducts.setValue("");
			searchForProducts();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateComboProduct();
		labelTotal.setText(formatPrice.format(totalAmount));
		searchForProducts();
	}

	public void populateComboProduct() {
		Commons.populateAllComboBox(comboProducts, ProductDAO.getProductListWithID());
		comboProducts.setValue("");
	}

	public void generateTableColumns(ObservableList<ProductsRepoModel> list) {
		colNo.setCellValueFactory(data -> data.getValue().idProperty().asObject());
		colItems.setCellValueFactory(data -> data.getValue().itemProperty());
		colUnit.setCellValueFactory(data -> data.getValue().unitProperty());
		colImport.setCellValueFactory(data -> data.getValue().importedProperty().asObject());
		formatColumn(colImport, formatQuantity);
		colExport.setCellValueFactory(data -> data.getValue().exportedProperty().asObject());
		formatColumn(colExport, formatQuantity);
		colRemained.setCellValueFactory(data -> data.getValue().remainedProperty().asObject());
		formatColumn(colRemained, formatQuantity);
		colPrice.setCellValueFactory(data -> data.getValue().unitPriceProperty().asObject());
		formatColumn(colPrice, formatPrice);
		colLineTotal.setCellValueFactory(data -> data.getValue().lineTotalProperty().asObject());
		formatColumn(colLineTotal, formatPrice);
		
		tableDetailList.setItems(list);

	}

	public void populateTableData(String code) {
		tableData.clear();
		tableData = MainStockDAO.getFactoryStockReport(code);
		generateTableColumns(tableData);
	}
	

	public void searchForProducts() {
		String comboValue = comboProducts.getValue();
		String prodCode = (!comboValue.equals("")) ? comboValue.split("-")[0].trim() : "";
		populateTableData(prodCode);
		calculateTotal();

	}

	public void calculateTotal() {
		totalAmount = 0; 
		if (!tableData.isEmpty()) {
			for (ProductsRepoModel model : tableData) {
				double lineTotal = model.getLineTotal();
				totalAmount = totalAmount + lineTotal;
			}
		}
		if (totalAmount < 0) {
			labelTotal.setTextFill(Color.RED);
			labelTotal.setText(formatPrice.format(totalAmount));
		} else {
			labelTotal.setText(formatPrice.format(totalAmount));
		}
		
	}
	
	private void formatColumn(TableColumn<ProductsRepoModel, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<ProductsRepoModel, Double>() {

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

}
