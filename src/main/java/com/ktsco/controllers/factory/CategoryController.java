package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.factory.CategoryModel;
import com.ktsco.modelsdao.CategoryDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CategoryController implements Initializable {

	@FXML
	private Text txtPanelName;

	@FXML
	private Button btnClose, btnAdd, btnDelete, btnModify, btnSearch, btnRefresh;

	@FXML
	private TextField txtCategory , txtCategoryCode;
	@FXML
	private ComboBox<String> comboMainCat;

	@FXML
	private TableView<CategoryModel> tableCategory;
	@FXML
	private TableColumn<CategoryModel, Integer> colNo;
	@FXML
	private TableColumn<CategoryModel, String> colCategory, colMainCat , colCatCode;

	public static Stage categoryStage = new Stage();
	private ObservableList<CategoryModel> catList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateCategoryTable();
		populateMainCatCombo();
	}

	@FXML
	public void onClickAction() {
		if (!tableCategory.getSelectionModel().isEmpty()) {
			CategoryModel catModel = tableCategory.getSelectionModel().getSelectedItem();
			String category = catModel.getCategoryName();
			String mainCat = catModel.getMainCat();
			String catCode = catModel.getCategoryCode();
			if (category != null) {
				txtCategory.setText(category);
				comboMainCat.setValue(mainCat);
				txtCategoryCode.setText(catCode);
			}
		}

	}

	@FXML
	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			if (categoryStage.isShowing())
				categoryStage.getOnHidden().handle(new WindowEvent(categoryStage, WindowEvent.WINDOW_HIDDEN));
			;
			categoryStage.close();
		} else if (event.getSource() == btnAdd) {
			addCategoryItem();
			txtCategory.clear();
			txtCategoryCode.clear();
			comboMainCat.setValue("");
		} else if (event.getSource() == btnDelete) {
			String value = txtCategory.getText();
			deleteCategoryItem(value);
			txtCategory.clear();
		} else if (event.getSource() == btnModify) {
			String value = txtCategory.getText();
			String mainCat = comboMainCat.getValue();
			String categoryCode = txtCategoryCode.getText();
			modifyCategoryItem(value, mainCat , categoryCode);
			txtCategory.clear();
		} else if (event.getSource() == btnSearch) {
			String value = txtCategory.getText();
			searchCategoryItems(value);
		} else if (event.getSource() == btnRefresh) {
			catList = CategoryDAO.selectAllItems();
			setCategoryTable(catList);
			txtCategory.clear();
			txtCategoryCode.clear();
		}
	}

	private void populateMainCatCombo() {
		Commons.populateAllComboBox(comboMainCat, CategoryDAO.comboMainCatItems());
		comboMainCat.setValue("");
	}

	private void populateCategoryTable() {
		catList = CategoryDAO.selectAllItems();
		setCategoryTable(catList);
	}

	private void setCategoryTable(ObservableList<CategoryModel> list) {
		colNo.setCellValueFactory(cellData -> cellData.getValue().catIDProperty().asObject());
		colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
		colMainCat.setCellValueFactory(cellData -> cellData.getValue().mainCatProperty());
		colCatCode.setCellValueFactory(cellData -> cellData.getValue().categoryCodeProperty());
		
		tableCategory.setItems(list);

	}

	public void addCategoryItem() {
		String value = txtCategory.getText();
		String categoryCode = txtCategoryCode.getText();
		int mainCat = Integer.parseInt(comboMainCat.getValue().split("-")[0].trim());
		if (!value.isEmpty()) {
			boolean exist = CategoryDAO.checkExistance(value);
			if (!exist) {
				CategoryDAO.addCategory(value, mainCat, categoryCode);
				populateCategoryTable();
			} else {
				AlertsUtils.repeatItemAlerts(value);
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}

	}

	private int getCategoryid() {
		if (!tableCategory.getSelectionModel().isEmpty()) {
			CategoryModel catModel = tableCategory.getSelectionModel().getSelectedItem();
			return catModel.getCatId();
		} else
			return 0;
	}

	public void deleteCategoryItem(String value) {
		boolean response = AlertsUtils.askForDeleteAlert(value);
		if (response) {
			if (!value.isEmpty()) {
				boolean exist = CategoryDAO.checkExistance(value);
				if (exist) {
					int categoryId = getCategoryid();
					if (categoryId != 0) {
						CategoryDAO.deleteCategory(categoryId);
						populateCategoryTable();
					}
				} else {
					AlertsUtils.nonExistItemsAlerts(value);
				}
			} else {
				AlertsUtils.emptyFieldAlert();
			}
		}
	}

	public void modifyCategoryItem(String value, String mainCat, String categoryCode) {
		if (!value.isEmpty()) {
			int categoryId = 0;
			if (!tableCategory.getSelectionModel().isEmpty()) {
				CategoryModel catModel = tableCategory.getSelectionModel().getSelectedItem();
				categoryId = catModel.getCatId();
			}
			if (categoryId != 0) {
				int mainCatID = Integer.parseInt(mainCat.split("-")[0].trim());
				CategoryDAO.modifyCategory(categoryId, value, mainCatID , categoryCode);
				populateCategoryTable();
			}
		} else {
			AlertsUtils.repeatItemAlerts(value);
		}
	}

	public void searchCategoryItems(String value) {
		if (!value.isEmpty()) {
			catList = CategoryDAO.retrieveSearchItems(value);
			setCategoryTable(catList);
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

}
