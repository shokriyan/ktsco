package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.ktsco.models.factory.ProductionExportDetailModel;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.modelsdao.ProductionExportDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ProductionExportController implements Initializable {

	@FXML
	private Label labelMessage;

	@FXML
	private TableView<ProductionExportDetailModel> tableEntry;

	@FXML
	private TableColumn<ProductionExportDetailModel, String> colQuantity;

	@FXML
	private TableColumn<ProductionExportDetailModel, String> colUnit;

	@FXML
	private TableColumn<ProductionExportDetailModel, String> colItem;

	@FXML
	private Button btnClose, btnSave, btnRefresh, btnAddtoTable;
	@FXML
	private MenuItem btnRemoveRow;

	@FXML
	private TextField txtIDNo, txtDate, txtMemo, txtUnit, txtQuantity;

	@FXML
	private ComboBox<String> comboEmployee, comboProducts;

	public static Stage exportPanelStage = new Stage();

	private ObservableList<ProductionExportDetailModel> exportList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		generateExportID();
		populateComboBoxes();
		comboProducts.setValue("");
		comboEmployee.setValue("");
		comboEmployee.setEditable(true);
		comboProducts.setEditable(true);
		TextFields.bindAutoCompletion(comboEmployee.getEditor(), comboEmployee.getItems());
		TextFields.bindAutoCompletion(comboProducts.getEditor(), comboProducts.getItems());
	}

	@FXML
	void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			exportPanelStage.getOnHidden().handle(new WindowEvent(exportPanelStage, WindowEvent.WINDOW_HIDDEN));
			exportPanelStage.close();
		} else if (event.getSource() == btnAddtoTable) {
			addItemsToList();

		} else if (event.getSource() == btnRefresh) {
			if (tableEntry.getItems().isEmpty()) {
				tableEntry.getItems().clear();
				exportList.clear();
				populateComboBoxes();
				txtDate.clear();
				txtQuantity.clear();
				generateExportID();
			} else {
				boolean response = AlertsUtils.askForReloadPage();
				if (response) {
					tableEntry.getItems().clear();
					exportList.clear();
					populateComboBoxes();
					txtDate.clear();
					txtQuantity.clear();
					generateExportID();
				}
			}

		} else if (event.getSource() == btnRemoveRow) {
			deleteItemfromList();
		} else if (event.getSource() == btnSave) {
			saveExport();
		}

	}

	private void loadPrerequisitions() {
		populateComboBoxes();
		txtDate.clear();
		txtMemo.clear();
		txtQuantity.clear();
		tableEntry.getItems().clear();
		exportList.clear();
		generateExportID();
	}

	private void populateComboBoxes() {

		Commons.populateAllComboBox(comboEmployee, EmployeeDAO.getEmployeeName());
		Commons.populateAllComboBox(comboProducts, ProductDAO.getProductList());
		comboEmployee.setEditable(false);
		comboProducts.setEditable(false);
		comboProducts.setValue("");
		comboEmployee.setValue("");

	}

	private void generateExportID() {
		int exportID = ProductionExportDAO.getExportID();
		txtIDNo.setText(String.valueOf(exportID));
	}

	@FXML
	private void columnEditHandle(CellEditEvent<ProductionExportDetailModel, String> editedCell) {
		ProductionExportDetailModel detailModel = tableEntry.getSelectionModel().getSelectedItem();
		detailModel.setQuantity(editedCell.getNewValue());
	}

	private void deleteItemfromList() {
		if (!tableEntry.getSelectionModel().isEmpty()) {

			int index = tableEntry.getSelectionModel().getSelectedIndex();
			exportList.remove(index);
			populateExportTable();

		}
	}

	@FXML
	private void onComboSelect(ActionEvent event) {
		populateUnitText(comboProducts.getValue());

		if (!tableEntry.getItems().isEmpty()) {

			String productName = comboProducts.getValue();
			ObservableList<ProductionExportDetailModel> list = tableEntry.getItems();
			for (ProductionExportDetailModel detailModel : list) {
				if (detailModel.getItem().equalsIgnoreCase(productName)) {
					AlertsUtils.repeatItemAlerts(productName);
				}
			}

		}

	}

	private void populateUnitText(String velue) {
		txtUnit.setText(ProductDAO.getUnitMeasure(velue));
	}

	private void generateTable(ObservableList<ProductionExportDetailModel> list) {
		colItem.setCellValueFactory(cellData -> cellData.getValue().getItemProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().getUnitProperty());
		colQuantity.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
		colQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
		tableEntry.setItems(list);
	}

	private void populateExportTable() {
		if (!exportList.isEmpty()) {
			generateTable(exportList);
		}
	}

	private void addItemsToList() {
		String item = (!"".equalsIgnoreCase(comboProducts.getValue())) ? comboProducts.getValue() : "";
		String unit = (!"".equalsIgnoreCase(txtUnit.getText())) ? txtUnit.getText() : "";
		String quantity = (!"".equalsIgnoreCase(txtQuantity.getText())) ? txtQuantity.getText() : "";
		if (!"".equalsIgnoreCase(item) && !"".equalsIgnoreCase(unit) && !"".equalsIgnoreCase(quantity)) {
			try {
				double convertedQty = Commons.setDoubleFormat(Double.parseDouble(quantity));
				ProductionExportDetailModel detailModel = new ProductionExportDetailModel(item, unit,
						String.valueOf(convertedQty));
				exportList.add(detailModel);
				populateExportTable();
				comboProducts.setValue("");
				txtQuantity.clear();
			} catch (NumberFormatException e) {
				AlertsUtils.numberEntryFormatErrorAlerts();
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void saveExport() {
		String exportID = ("".equalsIgnoreCase(txtIDNo.getText()) ? "" : txtIDNo.getText());
		String exportDate = ("".equalsIgnoreCase(txtDate.getText()) ? "" : txtDate.getText());
		String employee = ("".equalsIgnoreCase(comboEmployee.getValue()) ? "" : comboEmployee.getValue());
		String memo = ("".equalsIgnoreCase(txtMemo.getText()) ? "" : txtMemo.getText());

		boolean isDateCorrect = DateUtils.checkEntryDateFormat(exportDate);
		if (isDateCorrect) {

			if (!"".equalsIgnoreCase(exportID) && !"".equalsIgnoreCase(exportDate) && !"".equalsIgnoreCase(employee)) {

				String convertedDate = DateUtils.convertJalaliToGregory(exportDate);
				int employeeID = EmployeeDAO.getEmployeeID(employee);

				if (!tableEntry.getItems().isEmpty()) {
					boolean response = AlertsUtils.askForSaveItems();
					if (response) {
						boolean isSuccess = ProductionExportDAO.insertExport(Integer.parseInt(exportID), convertedDate,
								employeeID, memo);
						if (isSuccess) {
							ObservableList<ProductionExportDetailModel> list = tableEntry.getItems();
							for (ProductionExportDetailModel detailModel : list) {
								int productID = ProductDAO.getProductID(detailModel.getItem());
								double quantity = Commons
										.setDoubleFormat(Double.parseDouble(detailModel.getQuantity()));

								isSuccess = ProductionExportDAO.insertIntoExportDetail(Integer.parseInt(exportID),
										productID, quantity);

								if (isSuccess) {
									Commons.processMessageLabel(labelMessage, isSuccess);

								} else {
									Commons.processMessageLabel(labelMessage, false);
								}
							}
							if (isSuccess) {
								loadPrerequisitions();
							}
						} else {
							Commons.processMessageLabel(labelMessage, false);
						}
					}else {
						Commons.processMessageLabel(labelMessage, false);
					}
				} else {
					AlertsUtils.emptyFieldAlert();
				}
			}
		}

	}

}
