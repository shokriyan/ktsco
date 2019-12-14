package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.ProductRptModel;
import com.ktsco.modelsdao.ProductDAO;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ProductSellRptController implements Initializable {

	@FXML
	private TextField txtStartDate, txtEndDate;
	@FXML
	private ComboBox<String> comboProducts; 
	@FXML
	private Button btnSearch, btnReset;
	@FXML
	private TableView<ProductRptModel> tableDetailList;
	@FXML
	private TableColumn<ProductRptModel, Integer> colNo;
	@FXML
	private TableColumn<ProductRptModel, String> colItems, colUnit, colQuantity, colPrice;

	@FXML
	private Label  labelTotalCost;
	@FXML
	private HBox detailPanel;
	private static DecimalFormat decimalFormat; 
	
	ObservableList<ProductRptModel> dataList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prerequisitions();
		applyReportFilter();
		dateTextFieldChecks(txtStartDate);
		dateTextFieldChecks(txtEndDate);
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			applyReportFilter();
		}else if (event.getSource() == btnReset) {
			resetFilter();
		}
		
	}

	private void setDefaultValues() {
		labelTotalCost.setText("0");
		comboProducts.setValue("");
		txtEndDate.clear();
		txtStartDate.clear();
	}
	
	private void populateProductCombo() {
		Commons.populateAllComboBox(comboProducts, ProductDAO.getProductListWithID());
		comboProducts.setValue("");
	}

	private void prerequisitions() {
		setDefaultValues();
		populateProductCombo();
	}


	private void generateColumns(ObservableList<ProductRptModel> list) {
		colNo.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
		colItems.setCellValueFactory(cell -> cell.getValue().itemsProperty());
		colUnit.setCellValueFactory(cell -> cell.getValue().unitProperty());
		colQuantity.setCellValueFactory(cell -> cell.getValue().quantityProperty());
		colPrice.setCellValueFactory(cell -> cell.getValue().totalPriceProperty());
		tableDetailList.setItems(list);
	}

	private void populateTableData(String productCode, String startDate, String endDate) {
		if (!dataList.isEmpty())
			dataList.clear();
		List<Map<String, Object>> mapData = ProductDAO.retrieveProductsSalesReport(productCode, startDate, endDate);
		mapData.forEach(maps ->{
			int id = Integer.parseInt(maps.get("prod_id").toString());
			String item = maps.get("prod_name").toString();
			String unit = maps.get("prod_um").toString();
			String quantity = maps.get("quantity").toString();
			String totalSells = maps.get("usdTotal").toString();
			ProductRptModel model = new ProductRptModel(id, item, unit, quantity, totalSells);
			dataList.add(model);	
		});
		
		if (!dataList.isEmpty())
			generateColumns(dataList);
	}
	
	private void calculateSumTotal() {
		decimalFormat = new DecimalFormat("$ ###,###.##");
		if (!dataList.isEmpty()) {
			double totalPrice = 0; 
			for (ProductRptModel models : dataList) {
				totalPrice += Double.parseDouble(models.getTotalPrice().replace(",", ""));
				labelTotalCost.setText(decimalFormat.format(totalPrice));
			}
		}
	}
	
	private void dateTextFieldChecks(TextField dateTextField) {
		dateTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					boolean isDateCorrect = DateUtils.checkEntryDateFormat(newValue);
					if (!isDateCorrect) 
						AlertsUtils.dateEntryWarning(newValue);
				}
			}
		});
	}
	
	private void applyReportFilter() {
		String comboValue = comboProducts.getValue();
		String productCode = (!comboValue.equalsIgnoreCase("")) ? comboValue.split("-")[0].trim() : "";
		String startDate = txtStartDate.getText();
		String endDate = txtEndDate.getText();
		populateTableData(productCode, startDate, endDate);
		calculateSumTotal();
	}
	
	

	private void resetFilter() {
		setDefaultValues();
		String productCode = comboProducts.getValue();
		String startDate = txtStartDate.getText();
		String endDate = txtEndDate.getText();
		populateTableData(productCode, startDate, endDate);
		calculateSumTotal();
	}
	
	

}
