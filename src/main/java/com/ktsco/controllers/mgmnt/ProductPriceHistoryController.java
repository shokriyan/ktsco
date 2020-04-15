package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.ProductHstModal;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ProductPriceHistoryController implements Initializable{
	@FXML
	private ComboBox<String> comboProducts; 
	@FXML
	private TextField txtDays; 
	@FXML
	private Button btnSearch, btnReset; 
	@FXML
	private HBox detailPanel; 
	@FXML
	private TableView<ProductHstModal> tableDetailList; 
	@FXML
	private TableColumn<ProductHstModal, Integer> colNo; 
	@FXML
	private TableColumn<ProductHstModal, String> colItems, colUnit,colDate; 
	@FXML
	private TableColumn<ProductHstModal, Double> colPrice; 
	
	private ObservableList<ProductHstModal> tableData = FXCollections.observableArrayList(); 
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateComboData();
		searchItems();	
	}
	
	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			searchItems();
		}else if (event.getSource() == btnReset) {
			comboProducts.setValue("");
			txtDays.clear();
			searchItems();
		}
	}
	
	private void generateTableColumns(ObservableList<ProductHstModal> list) {
		colNo.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
		colItems.setCellValueFactory(cell -> cell.getValue().itemsProperty());
		colUnit.setCellValueFactory(cell -> cell.getValue().unitProperty());
		colPrice.setCellValueFactory(cell -> cell.getValue().dolorAmountProperty().asObject());
		formatColumn(colPrice, formatPrice);
		colDate.setCellValueFactory(cell -> cell.getValue().dateProperty());
		tableDetailList.setItems(list);
	}
	
	private void formatColumn(TableColumn<ProductHstModal, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<ProductHstModal, Double>() {

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
	
	private void populateTableData(String code, String days) {
		tableData.clear();
		tableData = ProductDAO.prodPriceHistoryData(code, days);
		generateTableColumns(tableData);
	}
	
	private void populateComboData() {
		Commons.populateAllComboBox(comboProducts, ProductDAO.getProductListWithID());
		comboProducts.setValue("");
	}
	
	private void searchItems() {
		String code =  (comboProducts.getValue().equalsIgnoreCase(""))? "" : comboProducts.getValue().split("-")[0].trim();
		String days = txtDays.getText(); 
		populateTableData(code, days);
	}

}
