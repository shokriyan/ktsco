package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.ProdDetailModel;
import com.ktsco.models.factory.ProductModel;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

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
	private Button btnCategory, btnInventoryList, btnAddProducts, btnAddInvItem, btnClearTable, btnShowProduct,
			btnDeleteProduct, btnSaveChange, btnRefresh;

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
	private TableColumn<ProdDetailModel, Void> colDetailDelete;
	@FXML
	private TableColumn<ProductModel, Integer> colNo;

	@FXML
	private TableColumn<ProductModel, String> colCategory, colProduct, colUm;

	private ObservableList<ProductModel> productsList = FXCollections.observableArrayList();
	private ObservableList<ProdDetailModel> prodDetailList = FXCollections.observableArrayList();
	private ProductModel prodModel;
	private ProdDetailModel prodDetailModel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrerequisition();

	}

	private void clearTextFields() {
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

		} else if (event.getSource() == btnAddInvItem) {
			addInventoryDetailToTable();

		} else if (event.getSource() == btnClearTable) {
			clearProdDetailTable();
		} else if (event.getSource() == btnDeleteProduct) {
			deleteProduct();
		} else if (event.getSource() == btnShowProduct) {
			tableDetail.getItems().clear();
			showProducts();
		} else if (event.getSource() == btnRefresh) {
			tableDetail.getItems().clear();
			loadPrerequisition();
			clearProdDetailTable();
			clearTextFields();
		} else if (event.getSource() == btnSaveChange) {
			saveUpdateProducts();
			loadPrerequisition();
			clearProdDetailTable();
			clearTextFields();

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
			boolean response = AlertsUtils.askForDeleteAlert(null);
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
				AlertsUtils.emptyFieldAlert();
			}
		} catch (Exception e) {
			log.error("Empty Fields ");
			AlertsUtils.emptyFieldAlert();
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
			AlertsUtils.numberEntryListAlerts();
		}
	}

	private void deleteDetailItemFromTable(int id) {

		ProductDAO.deleteDetailItem(id);
		showProducts();

	}

	/**
	 * Adding detail list to table
	 * 
	 * @param list
	 */
	private void addDetailListToTable(ObservableList<ProdDetailModel> list) {

		colDetailInvItem.setCellValueFactory(cellData -> cellData.getValue().getInvNameProperty());
		colDetailQty.setCellValueFactory(cellData -> cellData.getValue().getReqQtyProperty().asObject());
		colDetailDelete
				.setCellFactory(new Callback<TableColumn<ProdDetailModel, Void>, TableCell<ProdDetailModel, Void>>() {

					@Override
					public TableCell<ProdDetailModel, Void> call(TableColumn<ProdDetailModel, Void> param) {
						final TableCell<ProdDetailModel, Void> cell = new TableCell<ProdDetailModel, Void>() {

							private final Button btn = new Button("حذف");

							{
								btn.setOnAction((ActionEvent event) -> {
									prodDetailModel = tableDetail.getSelectionModel().getSelectedItem();
									if (!tableDetail.getSelectionModel().isEmpty()) {
										int id = prodDetailModel.getId();
										deleteDetailItemFromTable(id);
									}
								});
							}

							@Override
							public void updateItem(Void item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
								} else {
									setGraphic(btn);
								}
							}
						};
						return cell;
					}
				});
		// addButtonToDetailTable();
		tableDetail.setItems(list);

	}

	/**
	 * private getProdName concating name and size
	 * 
	 * @return String prodName
	 */

	private String getProdName() {
		String prodName = null;
		if (!txtProdSize.getText().isEmpty()) {
			prodName = (txtProduct.getText().concat(" - ").concat(txtProdSize.getText()));
		} else {
			prodName = txtProduct.getText();
		}
		return prodName;
	}

	/**
	 * Adding new products to database
	 */
	private void addProducts() {
		int prodId = Integer.parseInt(txtProdId.getText());
		String prodName = getProdName();

		int catId = CategoryDAO.getCategoryID(comboCategory.getValue());
		String prodUm = comboUm.getValue();
		int factoryProd = Commons.getCheckBoxValue(checkFactoryProduct);
		boolean response = false;
		boolean isExist = ProductDAO.isProductExist(prodName);
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

					} else {
						log.error("Empty List");
						AlertsUtils.selectFromListAlert();
						Commons.processMessageLabel(labelMessage, response);
					}
					loadPrerequisition();
					clearTextFields();
					Commons.processMessageLabel(labelMessage, response);
				} else {
					response = ProductDAO.insertProducts(prodId, catId, prodName, prodUm, factoryProd);
					Commons.processMessageLabel(labelMessage, response);
					loadPrerequisition();
					clearTextFields();
				}
			} else {
				Commons.processMessageLabel(labelMessage, response);
				log.error("Empty filed on Product name or category or Unit measure");
				AlertsUtils.emptyFieldAlert();
			}
		}

	}

	// Get productID calculating for next products

	private void populateProductId() {
		int prodId = ProductDAO.getLastProductId();
		txtProdId.setText(String.valueOf(prodId));
	}

	/**
	 * Deleting product from database
	 */
	private void deleteProduct() {
		if (!tableProducts.getSelectionModel().isEmpty()) {
			prodModel = tableProducts.getSelectionModel().getSelectedItem();
			int prodId = prodModel.getProdId();
			String prodName = prodModel.getProdName();
			boolean success;
			boolean userResponse = AlertsUtils.askForDeleteAlert(prodName);
			if (userResponse) {

				success = ProductDAO.deleteProductDetail(prodId);
				success = ProductDAO.deleteProduct(prodId);
				Commons.processMessageLabel(labelMessage, success);
				loadPrerequisition();
				clearTextFields();
			} else {
				Commons.processMessageLabel(labelMessage, false);
			}
		} else {
			AlertsUtils.selectFromListAlert();
		}
	}

	/**
	 * Show product detail after selection
	 */
	private void showProducts() {
		clearProdDetailTable();
		clearTextFields();
		if (!tableProducts.getSelectionModel().isEmpty()) {
			prodModel = tableProducts.getSelectionModel().getSelectedItem();
			int prodid = prodModel.getProdId();

			Map<String, Object> prodMap = ProductDAO.showProduct(prodid);
			String prod_id = prodMap.get("prod_id").toString();
			String category = prodMap.get("category").toString();
			if (prodMap.get("prod_name").toString().contains("-")) {
				String[] prodName = prodMap.get("prod_name").toString().split("-");
				txtProduct.setText(prodName[0]);
				txtProdSize.setText(prodName[1]);
			} else {
				String prodName = prodMap.get("prod_name").toString();
				txtProduct.setText(prodName);
			}
			String prodUm = prodMap.get("prod_um").toString();
			int factoryProd = Integer.parseInt(prodMap.get("factory_prod").toString());

			txtProdId.setText(prod_id);
			comboCategory.setValue(category);
			comboUm.setValue(prodUm);
			if (factoryProd == 0) {
				checkFactoryProduct.setSelected(false);
				onSelectActionForTable();
			} else {
				checkFactoryProduct.setSelected(true);
				onSelectActionForTable();
			}

			prodDetailList = ProductDAO.showProductDetail(prodid);
			addDetailListToTable(prodDetailList);
		}
	}

	/**
	 * Fill detail section values after selection from detail table
	 */
	public void onTableDetailSelection() {
		if (!tableDetail.getSelectionModel().isEmpty()) {
			prodDetailModel = tableDetail.getSelectionModel().getSelectedItem();
			String invName = prodDetailModel.getInvName();
			double reqQty = prodDetailModel.getReqQty();

			comboInvItem.setValue(invName);
			txtReqQty.setText(String.valueOf(reqQty));
		}
	}

	/**
	 * Save the changes made only for product. not applicable for product detail Any
	 * changes need happen on product detail the product should delete completely.
	 * if the products can't delete for any reason another product should add
	 * 
	 */
	public void saveUpdateProducts() {
		if (!tableProducts.getSelectionModel().isEmpty()) {
			prodModel = tableProducts.getSelectionModel().getSelectedItem();
			int prodid = prodModel.getProdId();
			String prodname = prodModel.getProdName();
			boolean response = AlertsUtils.askForUpdateAlert(prodname);
			if (response) {
				String prodName = getProdName();
				int catId = CategoryDAO.getCategoryID(comboCategory.getValue());
				String produm = comboUm.getValue();
				boolean success = false;
				success = ProductDAO.updateProduct(catId, prodName, produm, prodid);
				Commons.processMessageLabel(labelMessage, success);
			}
		}

	}
	

}
