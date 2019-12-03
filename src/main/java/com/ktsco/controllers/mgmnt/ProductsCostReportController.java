package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.ktsco.models.mgmt.ProductCostModel;
import com.ktsco.modelsdao.InventoryDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;

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

public class ProductsCostReportController implements Initializable {

	@FXML
	private ComboBox<String> comboProductList;
	@FXML
	private Button btnSearch, btnCalculate, btnSave;
	@FXML
	private TextField txtWastePerc, txtExpensePerc, txtOverheadPerc;

	@FXML
	private TableView<ProductCostModel> tableDetailList;
	@FXML
	private TableColumn<ProductCostModel, Integer> colNo;
	@FXML
	private TableColumn<ProductCostModel, String> colItems, colUnit, colQuantity, colPrice;

	@FXML
	private Label labelProdUnit, labelWaste, labelOtherExpense, labelOfficeOverhead, labelTotalCost;
	@FXML
	private HBox detailPanel;
	double totalCost; 
	int code; 
	ObservableList<ProductCostModel> dataList = FXCollections.observableArrayList();
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		detailPanel.setVisible(false);
		prerequisitions();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			SearchForProducts();
			if (!dataList.isEmpty()) {
				setDefaultValues();
				detailPanel.setVisible(true);
			}
		}else if (event.getSource() == btnCalculate) {
			calucateProductCost();
		}else if (event.getSource() == btnSave) {
			savePriceHistory();
		}
	}

	private void setDefaultValues() {
		txtWastePerc.setText("0");
		txtExpensePerc.setText("0");
		txtOverheadPerc.setText("0");
		labelWaste.setText("0");
		labelOfficeOverhead.setText("0");
		labelOtherExpense.setText("0");
		labelTotalCost.setText("0");
	}

	private void prerequisitions() {
		populateProductCombo();
		setDefaultValues();

	}

	private void populateProductCombo() {
		Commons.populateAllComboBox(comboProductList, ProductDAO.getProductListWithID());
		comboProductList.setValue("");
	}

	private void generateColumns(ObservableList<ProductCostModel> list) {
		colNo.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
		colItems.setCellValueFactory(cell -> cell.getValue().itemsProperty());
		colUnit.setCellValueFactory(cell -> cell.getValue().unitProperty());
		colQuantity.setCellValueFactory(cell -> cell.getValue().quantityProperty());
		colPrice.setCellValueFactory(cell -> cell.getValue().unitPriceProperty());
		tableDetailList.setItems(list);
	}

	private void populateTableData(int productCode) {

		dataList = InventoryDAO.getProductDetailList(productCode);
		generateColumns(dataList);
	}

	private void SearchForProducts() {
		if (!comboProductList.getValue().equalsIgnoreCase("")) {
			int code = Integer.parseInt(comboProductList.getValue().split("-")[0].trim());
			this.code = code; 
			
			populateTableData(code);
			labelProdUnit.setText(ProductDAO.getUnitMeasure(code));
		}
	}
	
	private double calculateCostSubtotal() {
		double subTotal = 0; 
		for (ProductCostModel model : dataList) {
			double quantity = Double.parseDouble(model.getQuantity().replace(",", ""));
			double unitPrice = Double.parseDouble(model.getUnitPrice().replace(",", ""));
			subTotal += (quantity * unitPrice);
		}
		
		return subTotal; 
	}
	
	private void calucateProductCost() {
		double prodSubtotal = calculateCostSubtotal(); 
		double waste = calculatePercentage(txtWastePerc);
		double expense = calculatePercentage(txtExpensePerc);
		double overHead = calculatePercentage(txtOverheadPerc);
		double totalCost = prodSubtotal + waste + expense + overHead; 
		labelWaste.setText(decimalFormat.format(waste));
		labelOtherExpense.setText(decimalFormat.format(expense));
		labelOfficeOverhead.setText(decimalFormat.format(overHead));
		labelTotalCost.setText(decimalFormat.format(totalCost));
		this.totalCost = totalCost; 
	}
	
	private double calculatePercentage(TextField textField) {
		double percentage = Double.parseDouble(textField.getText());
		return calculateCostSubtotal() * (percentage/100);
	}
	
	private void savePriceHistory() {
		if (this.totalCost != 0 && this.code != 0) {
			ProductDAO.saveProdHistory(this.code, this.totalCost);
			AlertsUtils.SuccessfullyDoneAlrt();
		}
	}

}
