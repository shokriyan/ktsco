package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.factory.ProductionDetailModel;
import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.modelsdao.ProductionDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.ViewClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ProductionController implements Initializable {

	private ViewClass view = new ViewClass();

	@FXML
	private Button btnSave, btnArrowDown, btnReload, btnSearch, btnDetailList, btnExportProduction;
	@FXML
	private Label labelMessage;
	@FXML
	private TextField txtNo, txtDate, txtStockQuantity;
	@FXML
	private TableView<ProductionDetailModel> tableProductList;
	@FXML
	private TableColumn<ProductionDetailModel, String> colProductItemList, colUMList, colQuantityList;
	@FXML
	private MenuItem menuItemDeleteRow;

	@FXML
	private TableView<ProductionDetailModel> tableProductionStock;
	@FXML
	private TableColumn<ProductionDetailModel, String> colProductStock, colUMStock, colQuantityStock;
	@FXML
	private ComboBox<String> comboEmployee, comboProducts;

	ObservableList<ProductionDetailModel> productionList = FXCollections.observableArrayList();

	@Override

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadPrerequisitions();
		comboProducts.setValue("");
	}

	private void loadPrerequisitions() {
		populateProductionStockList();
		generateProductionID();
		populateResponsiblePersonCombo();
		populateProductsCombo();
		clearTextFiels();
		productionList.clear();
	}

	private void generateProductionID() {
		txtNo.setText(String.valueOf(ProductionDAO.retrieveProductionID()));
	}

	public void handleDeleteRow(ActionEvent event) {
		if (!tableProductList.getSelectionModel().isEmpty()) {
			int selectedRowIndex = tableProductList.getSelectionModel().getSelectedIndex();
			if (!productionList.isEmpty()) {
				productionList.remove(selectedRowIndex);
				generateProductionListTable(productionList);
			}
			
		}
	}

	public void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnArrowDown) {
			generateProductionListItems();
			generateProductionListTable(productionList);
		} else if (event.getSource() == btnSave) {
			saveProductionList();
			loadPrerequisitions();
		} else if (event.getSource() == btnReload) {
			if (tableProductList.getItems().isEmpty()) {
				loadPrerequisitions();
			} else {
				boolean response = AlertsUtils.askForReloadPage();
				if (response) {
					loadPrerequisitions();
				}
			}
		} else if (event.getSource() == btnSearch) {
			openingSearchPanel();
			ProductionSearchController.productionSearchStage.setOnHidden(e -> loadPrerequisitions());
		}
		else if (event.getSource() == btnDetailList) {
			openDetailListPanel();
		}else if (event.getSource() == btnExportProduction) {
			openProductionExport();
			ProductionExportController.exportPanelStage.setOnHidden(e -> loadPrerequisitions());
			
		}
	}

	// Opening Search Panel
	private void openingSearchPanel() {
		VBox searchPanel = view.setVboxFxml(Commons.getFxmlPanel("productionSearchPanel"));
		ProductionSearchController.productionSearchStage = view.setSceneAndShowStage(searchPanel, "", false, false);
	}
	
	//Opening Detail list panel
	private void openDetailListPanel() {
		VBox detailList = view.setVboxFxml(Commons.getFxmlPanel("productionDetailList"));
		ProductionDetailReportController.productionDetailStage = view.setSceneAndShowStage(detailList, "", false, false);
	}
	
	//Opening Export Production Panel
	private void openProductionExport() {
		VBox productionExport = view.setVboxFxml(Commons.getFxmlPanel("productExportPanel"));
		ProductionExportController.exportPanelStage = view.setSceneAndShowStage(productionExport, "", false, false);
	}

	// Populating Employee List for ComboBox Responsible Person
	private void populateResponsiblePersonCombo() {
		List<String> empList = EmployeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, empList);
		comboEmployee.setValue("");
	}
	
	private void clearTextFiels() {
		txtDate.clear();
	}

	private void populateProductsCombo() {
		List<String> productList = ProductDAO.getProductList();
		Commons.populateAllComboBox(comboProducts, productList);
	}

	private void generateProductionListTable(ObservableList<ProductionDetailModel> list) {
		colProductItemList.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
		colUMList.setCellValueFactory(cellData -> cellData.getValue().getProductUnitProperty());
		colQuantityList.setCellValueFactory(cellData -> cellData.getValue().getProductionQuantityProperty());
		tableProductList.setItems(list);
	}

	private void generateProductionListItems() {
		String productName = comboProducts.getValue();
		double quantity = 0;
		try {
			quantity = Double.parseDouble(txtStockQuantity.getText());
		} catch (NumberFormatException e) {
			AlertsUtils.numberEntryFormatErrorAlerts();
		}

		String productUnit = ProductDAO.getUnitMeasure(productName);
		if (!productName.equalsIgnoreCase("") && quantity > (0)) {
			ProductionDetailModel productionModel = new ProductionDetailModel(productName, productUnit,
					String.valueOf(quantity));
			productionList.add(productionModel);
			comboProducts.setValue("");
			txtStockQuantity.clear();
		} else {
			AlertsUtils.emptyFieldAlert();
		}

	}

	public void onComboChangeAction(ActionEvent event) {
		if (!tableProductList.getItems().isEmpty()) {
			String productName = comboProducts.getValue();
			ObservableList<ProductionDetailModel> observe = tableProductList.getItems();
			for (ProductionDetailModel productionDetailModel : observe) {
				if (productionDetailModel.getProductName().equalsIgnoreCase(productName)) {
					AlertsUtils.repeatItemAlerts(productName);
				}
			}
		}
	}

	private void saveProductionList() {
		int productionID = Integer.parseInt(txtNo.getText());
		String inputDate = txtDate.getText();
		String employeeName = comboEmployee.getValue();

		if (!inputDate.equals("") && !employeeName.equals("")) {
			boolean response = AlertsUtils.askForSaveItems();
			if (response) {
				boolean isSuccess = ProductionDAO.insertProductionList(productionID, inputDate, employeeName);
				if (isSuccess) {
					if (!tableProductList.getItems().isEmpty()) {
						ObservableList<ProductionDetailModel> detailModel = tableProductList.getItems();
						for (ProductionDetailModel productionDetailModel : detailModel) {
							String prodName = productionDetailModel.getProductName();
							double quantity = Double.parseDouble(productionDetailModel.getProductionQuantity());
							boolean isProcessSuccess = ProductionDAO.insertProductionDetail(productionID, prodName,
									quantity);
							Commons.processMessageLabel(labelMessage, isProcessSuccess);
						}
					} else {
						AlertsUtils.emptyFieldAlert();
					}
				} else {
					Commons.processMessageLabel(labelMessage, false);
				}
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void generateProductionStockTable(ObservableList<ProductionDetailModel> list) {
		colProductStock.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
		colUMStock.setCellValueFactory(cellData -> cellData.getValue().getProductUnitProperty());
		colQuantityStock.setCellValueFactory(cellData -> cellData.getValue().getProductionQuantityProperty());
		colQuantityStock.setCellFactory(column -> {
		    return new TableCell<ProductionDetailModel, String>() {
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		            	setText(null);
		                setStyle("");
		            } else {
		                //Converts String to Double
		            	double quantity = Double.parseDouble(item);

		                // Style all dates in March with a different color.
		                if (quantity < 0) {
		                	setText(String.valueOf(quantity));
		                    setTextFill(Color.CHOCOLATE);
		                    setStyle("-fx-background-color: yellow");
		                } else {
		                	setText(String.valueOf(quantity));
		                    setTextFill(Color.BLACK);
		                    setStyle("");
		                }
		            }
		        }
		    };
		});
		tableProductionStock.setItems(list);
	}

	private void populateProductionStockList() {
		ObservableList<ProductionDetailModel> list = ProductionDAO.getProductionStockQuantity();
		generateProductionStockTable(list);
	}

}
