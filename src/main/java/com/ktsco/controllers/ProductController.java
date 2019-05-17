package com.ktsco.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.ProdDetailModel;
import com.ktsco.models.ProductModel;
import com.ktsco.modelsdao.CategoryDAO;
import com.ktsco.modelsdao.InventoryDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.ViewClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ProductController implements Initializable {

	/**
	 * All Global Instance
	 */
	private final Logger log = LoggerFactory.getLogger(ProductController.class);
	ViewClass view = new ViewClass();

	@FXML
	private VBox vboxProdDetail;
	@FXML
	private Label labelMessage;
	@FXML
	private Button btnCategory, btnInventoryList, btnAddProducts, btnAddInvItem, btnClearTable, btnShowProduct;

	@FXML
	private TableView<ProductModel> tableProducts;

	@FXML
	private CheckBox checkFactoryProduct;

	@FXML
	private TableView<ProdDetailModel> tableDetail;

	@FXML
	private TableColumn<ProdDetailModel, Double> colDetailQty;
	@FXML
	private TableColumn<ProdDetailModel, String> colDetailInvItem;

	@FXML
	private ComboBox<String> comboCategory, comboUm, comboInvItem;

	@FXML
	private TextField txtProduct, txtReqQty, txtProdId, txtProdSize;

	@FXML
	private TableColumn<ProductModel, Integer> colNo;

	@FXML
	private TableColumn<ProductModel, String> colCategory, colProduct, colUm;

	private ObservableList<ProductModel> productsList = FXCollections.observableArrayList();
	private ObservableList<ProdDetailModel> prodDetailList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrerequisition();
	}

	private void clearTextFiels() {
		txtProduct.clear();
		txtProdSize.clear();
		txtReqQty.clear();
		tableDetail.getItems().clear();
	}

	private void loadPrerequisition() {
		vboxProdDetail.setVisible(false);
		populateProductTable();
		populateComboItems();
		populateProductId();
		comboCategory.setValue("");
		comboUm.setValue("");
		checkFactoryProduct.setSelected(true);
		onSelectActionForTable();
	}

	/**
	 * Buttons Actions
	 * 
	 * @param event
	 */
	@FXML
	public void allButtonAction(ActionEvent event) {

		if (event.getSource() == btnCategory) {
			openCategoryPanel();
			CategoryController.categoryStage.setOnHidden(e -> populateComboItems());
		} else if (event.getSource() == btnInventoryList) {
			openInventoryPanel();
			InventoryController.invStage.setOnHidden(e -> populateComboItems());
		} else if (event.getSource() == btnAddProducts) {
			addProducts();
			loadPrerequisition();
			clearTextFiels();

		} else if (event.getSource() == btnAddInvItem) {
			addInventoryDetailToTable();

		} else if (event.getSource() == btnClearTable) {
			clearProdDetailTable();
		}

	}

	public void onSelectActionForTable() {
		if (checkFactoryProduct.isSelected()) {
			vboxProdDetail.setVisible(true);
		} else {
			vboxProdDetail.setVisible(false);
		}
	}

	private void clearProdDetailTable() {
		if (!tableDetail.getSelectionModel().isEmpty()) {
			boolean response = AlertsUtils.ResposeAlert("Clear Table", "پاک کردن کل جدول");
			if (response) {
				tableDetail.getItems().clear();
				comboInvItem.getItems().clear();
				populateComboItems();
			}
		}
	}

	private void addInventoryDetailToTable() {
		try {
			if (!comboInvItem.getValue().isEmpty() && !txtReqQty.getText().isEmpty()) {
				addValuesToDetailList();
				addDetailListToTable(prodDetailList);
				comboInvItem.getItems().clear();
				txtReqQty.clear();
				populateComboItems();
			} else {
				AlertsUtils.warningAlert("Empty Field", "لطفا اطلاعات وارد کنید");
			}
		} catch (Exception e) {
			log.error("Empty Fields ");
			AlertsUtils.warningAlert("Empty Field", "لطفا اطلاعات وارد کنید");
		}
	}

	private void openCategoryPanel() {
		log.info("Loading FXML to penel {}", Constants.categoryPanelFxml);
		VBox category = view.setVboxFxml(Constants.categoryPanelFxml);
		log.info("Loading stage and show");
		CategoryController.categoryStage = view.setSceneAndShowStage(category, "", false, false);
	}

	private void openInventoryPanel() {
		log.info("Loading FXML to penel {}", Constants.inventoryListPanelFxml);
		VBox inventoryList = view.setVboxFxml(Constants.inventoryListPanelFxml);
		log.info("Loading stage and show");
		InventoryController.invStage = view.setSceneAndShowStage(inventoryList, "", false, false);
	}

	private void setProductTable(ObservableList<ProductModel> list) {
		colNo.setCellValueFactory(cellData -> cellData.getValue().getProdIdProperty().asObject());
		colCategory.setCellValueFactory(cellData -> cellData.getValue().getCategoryIdProperty());
		colProduct.setCellValueFactory(cellData -> cellData.getValue().getProdNameProperty());
		colUm.setCellValueFactory(cellData -> cellData.getValue().getProdUmProperty());

		tableProducts.setItems(productsList);

	}

	private void populateProductTable() {
		productsList = ProductDAO.selectAll();

		setProductTable(productsList);
	}

	private void populateComboItems() {
		List<String> categoryList = new ArrayList<String>();
		categoryList = CategoryDAO.getCategoryItemsForCombo();
		Commons.populateAllComboBox(comboCategory, categoryList);
		Commons.populateAllComboBox(comboUm, Constants.unitMeasureList);
		List<String> invItemList = new ArrayList<String>();
		invItemList = InventoryDAO.getInvItemsForCombo();
		Commons.populateAllComboBox(comboInvItem, invItemList);

	}

	private void addValuesToDetailList() {
		try {
			String InvName = comboInvItem.getValue();
			double reqQty = Double.parseDouble(txtReqQty.getText());
			ProdDetailModel detailModel = new ProdDetailModel(InvName, reqQty);
			prodDetailList.add(detailModel);
		} catch (NumberFormatException e) {
			log.error("Only numbers are acceptable at Required Qty");
			AlertsUtils.warningAlert("Number format", "عدد وارد کنید");
		}
	}

	/**
	 * Adding detail list to table
	 * 
	 * @param list
	 */

	private void addDetailListToTable(ObservableList<ProdDetailModel> list) {
		colDetailInvItem.setCellValueFactory(cellData -> cellData.getValue().getInvNameProperty());
		colDetailQty.setCellValueFactory(cellData -> cellData.getValue().getReqQtyProperty().asObject());

		tableDetail.setItems(list);

	}

	private void addProducts() {
		int prodId = Integer.parseInt(txtProdId.getText());
		String prodName = (txtProduct.getText().concat("-").concat(txtProdSize.getText()));
		int catId = CategoryDAO.getCategoryID(comboCategory.getValue());
		String prodUm = comboUm.getValue();
		int factoryProd = Commons.getCheckBoxValue(checkFactoryProduct);
		boolean response = false;
		boolean isExist = ProductDAO.isProductExist(prodName.replace("-", ""));
		if (!isExist) {
			if (!prodName.isEmpty() && !comboCategory.getValue().equals("") && !prodUm.equals("")) {
				if (factoryProd == 1) {
					response = ProductDAO.insertProducts(prodId, catId, prodName, prodUm, factoryProd);

					ObservableList<ProdDetailModel> listItems = tableDetail.getItems();
					if (listItems != null) {
						for (ProdDetailModel detailModel : listItems) {

							int invid = InventoryDAO.getInvId(detailModel.getInvName());
							double reqQty = detailModel.getReqQty();

							response = ProductDAO.insertProductDetails(invid, prodId, reqQty);

						}
						Commons.processMessageLabel(labelMessage, response);
					} else {
						log.error("Empty List");
						AlertsUtils.warningAlert("Empty List", "لیست خالی است");

					}
				} else {
					response = ProductDAO.insertProducts(prodId, catId, prodName, prodUm, factoryProd);
					Commons.processMessageLabel(labelMessage, response);
				}
			} else {
				log.error("Empty filed on Product name or category or Unit measure");
				AlertsUtils.warningAlert("Empty List", "لطفا اطلاعات وارد کنید");
			}
		}

	}

	private void populateProductId() {
		int prodId = ProductDAO.getLastProductId();
		txtProdId.setText(String.valueOf(prodId));
	}

}
