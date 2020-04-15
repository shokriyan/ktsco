package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.RawMtrlRptModel;
import com.ktsco.modelsdao.InventoryDAO;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class RawMaterialRemainController implements Initializable {

	@FXML
	private ComboBox<String> comboItem;

	@FXML
	private Button btnSearch, btnReset;
	@FXML
	private HBox detailPanel;

	@FXML
	private TableView<RawMtrlRptModel> tableDetailList;

	@FXML
	private TableColumn<RawMtrlRptModel, Integer> colNo;

	@FXML
	private TableColumn<RawMtrlRptModel, String> colItems, colUnit;

	@FXML
	private TableColumn<RawMtrlRptModel, Double> colImported, colUsed, colUnitPrice, colLineTotal;

	@FXML
	private Label labelTotal;
	ObservableList<RawMtrlRptModel> tableData = FXCollections.observableArrayList();
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prerequisitions();

	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			searchItems();
		}else if (event.getSource() == btnReset) {
			comboItem.setValue("");
			searchItems();
		}
	}

	private void prerequisitions() {
		populateItemsCombos();
		populateTableData("");
		calculateTotalValue();

	}

	private void generateTableCol(ObservableList<RawMtrlRptModel> list) {

		colNo.setCellValueFactory(cell -> cell.getValue().codeProperty().asObject());
		colItems.setCellValueFactory(cell -> cell.getValue().itemProperty());
		colUnit.setCellValueFactory(cell -> cell.getValue().unitProperty());
		colImported.setCellValueFactory(cell -> cell.getValue().importedProperty().asObject());
		formatColumn(colImported, formatQuantity);
		colUsed.setCellValueFactory(cell -> cell.getValue().usedProperty().asObject());
		formatColumn(colUsed, formatQuantity);
		colUnitPrice.setCellValueFactory(cell -> cell.getValue().unitPriceProperty().asObject());
		formatColumn(colUnitPrice, formatPrice);
		colLineTotal.setCellValueFactory(cell -> cell.getValue().lineTotalProperty().asObject());
		formatColumn(colLineTotal, formatPrice);
		tableDetailList.setItems(list);
	}

	private void formatColumn(TableColumn<RawMtrlRptModel, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<RawMtrlRptModel, Double>() {

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

	private void populateTableData(String itemCode) {
		tableData= InventoryDAO.getInventoryReport(itemCode);
		
		generateTableCol(tableData);
		calculateTotalValue();
	}

	private void populateItemsCombos() {
		Commons.populateAllComboBox(comboItem, InventoryDAO.getInvItemsForCombo());
	}

	private void calculateTotalValue() {
		double totalValue = 0;
		if (!tableData.isEmpty()) {

			for (RawMtrlRptModel model : tableData) {
				totalValue += model.getLineTotal();
			}
		}
		if (totalValue < 0) {
			labelTotal.setTextFill(Color.RED);
			labelTotal.setText(String.valueOf(formatPrice.format(totalValue)));
		} else {
			labelTotal.setText(String.valueOf(formatPrice.format(totalValue)));
		}
	}
	
	private void searchItems () {
		tableData.clear();
		String comboValue = comboItem.getValue(); 
		if (comboValue.equalsIgnoreCase("")) {
			populateTableData("");
		}else {
			String code = String.valueOf(InventoryDAO.getInvId(comboValue));
			populateTableData(code);
		}
		
		
	}

}
