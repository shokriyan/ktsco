package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.ProductRptModel;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
	private TableView<ProductRptModel> tableDetailList; 
	@FXML
	private TableColumn<ProductRptModel, Integer> colNo; 
	@FXML
	private TableColumn<ProductRptModel, String> colItems, colUnit, colPrice; 
	
	private ObservableList<ProductRptModel> tableData = FXCollections.observableArrayList(); 
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
	
	private void generateTableColumns(ObservableList<ProductRptModel> list) {
		colNo.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
		colItems.setCellValueFactory(cell -> cell.getValue().itemsProperty());
		colUnit.setCellValueFactory(cell -> cell.getValue().unitProperty());
		colPrice.setCellValueFactory(cell -> cell.getValue().totalPriceProperty());
		
		tableDetailList.setItems(list);
	}
	
	private void populateTableData(String code, String days) {
		tableData.clear();
		ProductDAO.prodPriceHistoryData(code, days).forEach(map -> {
			int id = Integer.parseInt(map.get("prod_id").toString());
			String items = map.get("prod_name").toString();
			String unit = map.get("prod_um").toString(); 
			String price = map.get("price").toString(); 
			ProductRptModel model = new ProductRptModel(id, items, unit, price);
			tableData.add(model); 
		});
		
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
