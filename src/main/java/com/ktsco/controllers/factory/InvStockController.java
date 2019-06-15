package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.InvStockDetailModel;
import com.ktsco.models.factory.InvStockModel;
import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.modelsdao.InventoryDAO;
import com.ktsco.modelsdao.InventoryStockDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DateUtils;
import com.ktsco.utils.ViewClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InvStockController implements Initializable {
	private static final Logger log = LoggerFactory.getLogger(InvStockController.class);
	private static ViewClass view = new ViewClass();
	private InvStockDetailModel stockDetailModel;
	private ObservableList<InvStockModel> rawMaterialList = FXCollections.observableArrayList();
	private ObservableList<InvStockDetailModel> invStockDetailList;

	@FXML
	private Button btnCategory, btnInvetoryItems, btnSaveInvImport, btnSearch;

	@FXML
	private TextField txtImportId, txtImportDate;

	@FXML
	private ComboBox<String> comboEmployee;
	@FXML
	private TableView<InvStockDetailModel> tableImportList;
	@FXML
	private TableColumn<InvStockDetailModel, String> colInvItem;
	@FXML
	private TableColumn<InvStockDetailModel, String> colUM;
	@FXML
	private TableColumn<InvStockDetailModel, String> colImportQty;
	@FXML
	private TableView<InvStockModel> tableInvStock;
	@FXML
	private TableColumn<InvStockModel, String> colInvItem1;
	@FXML
	private TableColumn<InvStockModel, String> colUM1;
	@FXML
	private TableColumn<InvStockModel, String> colTotalQty;

	@FXML
	private Label labelMessage;

	// initialize Methods

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableImportList.setEditable(true);
		reloadPrerequisition();
	}

	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnCategory) {
			openCategoryPanel();
			CategoryController.categoryStage.setOnHidden(e -> reloadPrerequisition());
		} else if (event.getSource() == btnInvetoryItems) {
			openInventoryItemsPanel();
			InventoryController.invStage.setOnHidden(e -> reloadPrerequisition());
		}
		if (event.getSource() == btnSaveInvImport) {
			boolean response = AlertsUtils.askForSaveItems();
			if (response) {
				addInvImportDetail();
				
			}
		}
		if (event.getSource() == btnSearch) {
			openSearchPanel();
			ImportSearchController.importSearchStage.setOnHidden(e -> reloadPrerequisition());
		}
	}

	// Opening Category panel
	private void openCategoryPanel() {
		log.info("Openning Category Panel");
		VBox category = view.setVboxFxml(Constants.categoryPanelFxml);
		CategoryController.categoryStage = view.setSceneAndShowStage(category, "", false, false);
	}

	private void openSearchPanel() {
		log.info("Openning Search panel");
		VBox searchPanel = view.setVboxFxml(Constants.importSearchPanelFxml);
		ImportSearchController.importSearchStage = view.setSceneAndShowStage(searchPanel, "", false, false);
	}

	// Opening InvetoryItem Panel
	private void openInventoryItemsPanel() {
		log.info("Opening Inventory list panel");
		VBox invItems = view.setVboxFxml(Constants.inventoryListPanelFxml);
		InventoryController.invStage = view.setSceneAndShowStage(invItems, "", false, false);
	}

	// Populating Combo Items.
	private void populateComboItems() {
		List<String> empList = EmployeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, empList);
	}

	private void reloadPrerequisition() {
		populatRawMaterialInventory();
		populateInvItemsList();
		populateComboItems();
		generateImportID();
		clearTextBox();
	}

	private void clearTextBox() {
		txtImportDate.clear();
		comboEmployee.setValue("");
	}

	public void generateImportID() {
		int importID = InventoryStockDAO.generateImportID();
		txtImportId.setText(String.valueOf(importID));
	}

	private void generateTableForInventoryList(ObservableList<InvStockDetailModel> list) {
		colInvItem.setCellValueFactory(cellData -> cellData.getValue().getInvNameProperty());
		colUM.setCellValueFactory(cellData -> cellData.getValue().getUMProperty());
		colImportQty.setCellValueFactory(cellData -> cellData.getValue().getImportQtyProperty());

		colImportQty.setCellFactory(TextFieldTableCell.forTableColumn());
		tableImportList.setItems(list);
	}

	private void generateRawMaterialTableList(ObservableList<InvStockModel> list) {
		colInvItem1.setCellValueFactory(cellData -> cellData.getValue().getInvNameProperty());
		colUM1.setCellValueFactory(cellData -> cellData.getValue().getUMProperty());
		colTotalQty.setCellValueFactory(cellData -> cellData.getValue().getImportQtyProperty());

		tableInvStock.setItems(list);
	}

	public void editableImportQty(CellEditEvent<?, ?> editedCell) {

		stockDetailModel = tableImportList.getSelectionModel().getSelectedItem();
		stockDetailModel.setImportQty(editedCell.getNewValue().toString());
	}

	private void populateInvItemsList() {
		invStockDetailList = FXCollections.observableArrayList();
		invStockDetailList = InventoryStockDAO.populateInvList();
		generateTableForInventoryList(invStockDetailList);
	}

	private boolean InsertInvImport() {
		boolean success = false;
		if (!txtImportDate.getText().isEmpty() && !comboEmployee.getValue().isEmpty()) {
			int importID = Integer.parseInt(txtImportId.getText());
			boolean isDateCorrect = DateUtils.checkEntryDateFormat(txtImportDate.getText());
			if (isDateCorrect) {
				String date = DateUtils.convertJalaliToGregory(txtImportDate.getText());
				int empID = EmployeDAO.getEmployeeID(comboEmployee.getValue());

				success = InventoryStockDAO.addImportList(importID, date, empID);
			}
		} else {
			success = false;
			AlertsUtils.emptyFieldAlert();
		}
		return success;
	}

	private void addInvImportDetail() {
		boolean success = false;
		if (!tableImportList.getSelectionModel().isEmpty()) {
			success = InsertInvImport();
			if (success) {
				invStockDetailList = tableImportList.getItems();
				if (!invStockDetailList.isEmpty()) {
					int import_id = Integer.parseInt(txtImportId.getText());
					for (InvStockDetailModel stockDetailModel : invStockDetailList) {
						int inv_id = InventoryDAO.getInvId(stockDetailModel.getInvName());
						double import_qty = Double.parseDouble(stockDetailModel.getImportQty());

						success = InventoryStockDAO.addImportDetailList(inv_id, import_qty, import_id);
						reloadPrerequisition();
					}
				} else {
					success = false;
				}
			}
		}

		Commons.processMessageLabel(labelMessage, success);

	}

	private void populatRawMaterialInventory() {
		rawMaterialList = InventoryStockDAO.getRawMaterialInventory();
		generateRawMaterialTableList(rawMaterialList);
	}
	
	
	

}
