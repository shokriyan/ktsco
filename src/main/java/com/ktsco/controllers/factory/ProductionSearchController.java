package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.factory.ProductionDetailModel;
import com.ktsco.models.factory.ProductionListModel;
import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.modelsdao.ProductionDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ProductionSearchController implements Initializable {

	@FXML
	private TextField txtNo, txtStartDate, txtEndDate;
	@FXML
	private ComboBox<String> comboEmployee;

	@FXML
	private Button btnClose, btnDelete, btnModify, btnSearch, btnRefresh;

	@FXML
	private Label labelInfo;

	@FXML
	private TableView<ProductionListModel> tableSearchResult;
	@FXML
	private TableView<ProductionDetailModel> tableItemDetail;
	@FXML
	private TableColumn<ProductionListModel, Integer> colNo;
	@FXML
	private TableColumn<ProductionListModel, String> colDate, colEmployee;
	@FXML
	private TableColumn<ProductionDetailModel, Integer> colDetailID;
	@FXML
	private TableColumn<ProductionDetailModel, String> colItem, colUnit, colQuantity;

	public static Stage productionSearchStage = new Stage();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadingPrerequisitions();
		comboEmployee.setValue("");
		tableItemDetail.setEditable(true);
	}

	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			productionSearchStage.getOnHidden()
					.handle(new WindowEvent(productionSearchStage, WindowEvent.WINDOW_HIDDEN));
			productionSearchStage.close();
		} else if (event.getSource() == btnSearch) {
			populateSearchresultList();
		} else if (event.getSource() == btnDelete) {
			deleteProduction();
			loadingPrerequisitions();
			populateSearchresultList();
		} else if (event.getSource() == btnRefresh) {
			loadingPrerequisitions();

			clearTables();
		} else if (event.getSource() == btnModify) {
			boolean response = AlertsUtils.askForUpdateAlert("تغییر در تعداد");
			if (response) {
				updateDetailList();
				loadingPrerequisitions();
			}
		}
	}

	public void onTableSelectionAction() {
		if (!tableSearchResult.getSelectionModel().isEmpty()) {
			ProductionListModel listModel = tableSearchResult.getSelectionModel().getSelectedItem();
			int productionID = listModel.getProductionID();
			ObservableList<ProductionDetailModel> list = ProductionDAO.retriveDetailList(productionID);
			generateTableProductionDetail(list);
		}
	}

	private void generateTableProductionDetail(ObservableList<ProductionDetailModel> list) {
		colDetailID.setCellValueFactory(cellData -> cellData.getValue().getDetailIDProperty().asObject());
		colItem.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().getProductUnitProperty());
		colQuantity.setCellValueFactory(cellData -> cellData.getValue().getProductionQuantityProperty());
		colQuantity.setCellFactory(TextFieldTableCell.forTableColumn());

		tableItemDetail.setItems(list);
	}

	private void clearTables() {
		tableItemDetail.getItems().clear();
		tableSearchResult.getItems().clear();
	}

	public void editableColumnAction(CellEditEvent<?, ?> cellEditted) {
		ProductionDetailModel detailModel = tableItemDetail.getSelectionModel().getSelectedItem();
		detailModel.setProductionQuantity(cellEditted.getNewValue().toString());
	}

	private void loadingPrerequisitions() {
		generateEmployeeCombo();
		tableSearchResult.getSelectionModel().clearSelection();
	}

	public void generateEmployeeCombo() {
		List<String> list = EmployeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, list);
	}

	private void generateTableSearchResult(ObservableList<ProductionListModel> list) {
		colNo.setCellValueFactory(cellData -> cellData.getValue().getProductionIDProperty().asObject());
		colDate.setCellValueFactory(cellData -> cellData.getValue().getProductionDateProperty());
		colEmployee.setCellValueFactory(cellData -> cellData.getValue().getEmployeeNameProperty());

		tableSearchResult.setItems(list);
	}

	private void populateSearchresultList() {
		String no = txtNo.getText();
		String startDate = txtStartDate.getText();
		String endDate = txtEndDate.getText();
		String employeeName = comboEmployee.getValue();
		ObservableList<ProductionListModel> list = ProductionDAO.retrieveSearchResult(no, startDate, endDate,
				employeeName);
		generateTableSearchResult(list);
	}

	private void deleteProduction() {
		if (!tableSearchResult.getSelectionModel().isEmpty()) {
			ProductionListModel listModel = tableSearchResult.getSelectionModel().getSelectedItem();
			int productionID = listModel.getProductionID();
			boolean response = AlertsUtils.askForDeleteAlert("لیست شماره " + productionID);
			if (response) {
				boolean isSuccess = ProductionDAO.deleteProductionList(productionID);
				Commons.processMessageLabel(labelInfo, isSuccess);
			} else {
				Commons.processMessageLabel(labelInfo, false);
			}
		}
	}

	private void updateDetailList() {
		if (!tableItemDetail.getItems().isEmpty()) {
			ObservableList<ProductionDetailModel> list = tableItemDetail.getItems();
			boolean isSuccess = false;
			for (ProductionDetailModel detailModel : list) {
				int detailID = detailModel.getDetailID();
				String newQuantity = detailModel.getProductionQuantity();

				isSuccess = ProductionDAO.updateProdcutionDetailQuantity(detailID, newQuantity);
				if (isSuccess) {
					Commons.processMessageLabel(labelInfo, isSuccess);
				}
			}
			if (isSuccess) {
				loadingPrerequisitions();
			}

		}
	}

}
