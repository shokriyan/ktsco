package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.factory.InvStockModel;
import com.ktsco.models.factory.InventoryImportModel;
import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.modelsdao.InventoryStockDAO;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ImportSearchController implements Initializable {

	public static Stage importSearchStage = new Stage();
	private ObservableList<InventoryImportModel> importList = FXCollections.observableArrayList();

	@FXML
	private Button btnClose, btnSearch, btnRefresh, btnDelete, btnModify;

	@FXML
	private Label labelInfo;
	@FXML
	private Text txtPanelName;
	@FXML
	private ComboBox<String> comboEmployee;
	@FXML
	private TextField txtImportID, txtStartDate, txtEndDate;
	@FXML
	private TableView<InventoryImportModel> tableImport;
	@FXML
	private TableColumn<InventoryImportModel, Integer> colNo;
	@FXML
	private TableColumn<InventoryImportModel, String> colDate;
	@FXML
	private TableColumn<InventoryImportModel, String> colEmployee;

	@FXML
	private TableView<InvStockModel> tableImportDetail;
	@FXML
	private TableColumn<InvStockModel, Integer> colDetailID;
	@FXML
	private TableColumn<InvStockModel, String> colRawMaterial;
	@FXML
	private TableColumn<InvStockModel, String> colUnit;
	@FXML
	private TableColumn<InvStockModel, String> colImportQty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchImport();
		tableImportDetail.setEditable(true);
		reloadPrerequisitions();
	}

	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			importSearchStage.getOnHidden().handle(new WindowEvent(importSearchStage, WindowEvent.WINDOW_HIDDEN));
			importSearchStage.close();
		} else if (event.getSource() == btnSearch) {
			searchImport();
		} else if (event.getSource() == btnRefresh) {
			clearTextBoxes();
			reloadPrerequisitions();
			tableImport.getItems().clear();
			tableImportDetail.getItems().clear();
		} else if (event.getSource() == btnDelete) {
			deleteImport();
			tableImportDetail.getItems().clear();
			searchImport();
		}else if (event.getSource() == btnModify) {
			updateImportDetail();
		}
	}

	public void onTableSelectionAction() {
		populateImportDetailAfterSelection();

	}

	private void clearTextBoxes() {
		txtStartDate.clear();
		txtImportID.clear();
		txtEndDate.clear();
		populateEmployeeListCombo();
	}

	private void reloadPrerequisitions() {
		populateEmployeeListCombo();
	}

	private void populateEmployeeListCombo() {
		List<String> empList = EmployeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, empList);
		comboEmployee.setValue("");
	}

	private String getImportIDText() {
		return txtImportID.getText();
	}

	private String getStartDateText() {
		String startDate = "1900-01-01";
		if (!txtStartDate.getText().isEmpty()) {
			startDate = DateUtils.convertJalaliToGregory(txtStartDate.getText());
		}
		return startDate;
	}

	private String getEndDateText() {
		String endDate = "2200-12-30";
		if (!txtEndDate.getText().isEmpty()) {
			endDate = DateUtils.convertJalaliToGregory(txtEndDate.getText());
		}
		return endDate;
	}

	private String getEmployeeIDText() {
		String employee = "";
		employee = comboEmployee.getValue();
		if (employee != "") {
			int empID = EmployeDAO.getEmployeeID(employee);
			employee = String.valueOf(empID);
		}
		return employee;
	}

	private void generateImportTable(ObservableList<InventoryImportModel> list) {
		colNo.setCellValueFactory(cellData -> cellData.getValue().getImportIDProperty().asObject());
		colDate.setCellValueFactory(cellData -> cellData.getValue().getImportDateProperty());
		colEmployee.setCellValueFactory(cellData -> cellData.getValue().getResponsibleProperty());

		tableImport.setItems(list);
	}

	private void generateImportDetailTable(ObservableList<InvStockModel> list) {
		colDetailID.setCellValueFactory(cellData -> cellData.getValue().getDetailIDProperty().asObject());
		colRawMaterial.setCellValueFactory(cellData -> cellData.getValue().getInvNameProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().getUMProperty());
		colImportQty.setCellValueFactory(cellData -> cellData.getValue().getImportQtyProperty());
		colImportQty.setCellFactory(TextFieldTableCell.forTableColumn());
		tableImportDetail.setItems(list);
	}
	
	public void editableColumnAction(CellEditEvent<?, ?> editedCell) {
		InvStockModel stockModel = tableImportDetail.getSelectionModel().getSelectedItem();
		stockModel.setImportQty(editedCell.getNewValue().toString());
	}

	private void searchImport() {
		
		String importID = getImportIDText();
		String startDate = getStartDateText();
		String endDate = getEndDateText();
		String employeeName = getEmployeeIDText();
		tableImportDetail.getItems().clear();
		importList = InventoryStockDAO.getSearchRecords(importID, startDate, endDate, employeeName);
		generateImportTable(importList);

	}

	private void populateImportDetailAfterSelection() {
		InventoryImportModel importModel;
		int importID = 0;
		if (!tableImport.getSelectionModel().isEmpty()) {
			importModel = tableImport.getSelectionModel().getSelectedItem();
			importID = importModel.getImportID();
			ObservableList<InvStockModel> detailList = FXCollections.observableArrayList();
			detailList = InventoryStockDAO.getImportDetailList(importID);
			generateImportDetailTable(detailList);
		}
	}

	private void deleteImport() {
		InventoryImportModel importModel;
		int importID = 0;
		if (!tableImport.getSelectionModel().isEmpty()) {
			importModel = tableImport.getSelectionModel().getSelectedItem();
			importID = importModel.getImportID();
			if (importID != 0) {
				boolean response = AlertsUtils.askForDeleteAlert("ورودی شماره " + importID);
				if (response) {
					InventoryStockDAO.deleteImport(importID);
				}
			}
		}
	}

	private void updateImportDetail() {
		ObservableList<InvStockModel> detailList = FXCollections.observableArrayList();

		if (!tableImportDetail.getSelectionModel().isEmpty()) {
			boolean response = AlertsUtils.askForUpdateAlert("");
			if (response) {
				detailList = tableImportDetail.getItems();
				for (InvStockModel invStockModel : detailList) {
					int detailID = invStockModel.getDetailID();
					double newImportQty = Double.parseDouble(invStockModel.getImportQty());
					InventoryStockDAO.updateInventoryImportQty(newImportQty, detailID);
				}
			}
		}else {
			AlertsUtils.selectFromListAlert();
		}
	}

}
