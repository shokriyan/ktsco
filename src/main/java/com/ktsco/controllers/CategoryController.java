package com.ktsco.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.CategoryModel;
import com.ktsco.modelsdao.CategoryDAO;
import com.ktsco.utils.AlertsUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CategoryController implements Initializable {

	@FXML
	private Text txtPanelName;

	@FXML
	private Button btnClose, btnAdd, btnDelete, btnModify, btnSearch, btnRefresh;

	@FXML
	private TextField txtCategory;

	@FXML
	private TableView<CategoryModel> tableCategory;
	@FXML
	private TableColumn<CategoryModel, Integer> colNo;
	@FXML
	private TableColumn<CategoryModel, String> colCategory;

	public static Stage categoryStage = new Stage();
	private ObservableList<CategoryModel> catList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateCategoryTable();
	}

	@FXML
	public void onClickAction() {
		if (!tableCategory.getSelectionModel().isEmpty()) {
			CategoryModel catModel = tableCategory.getSelectionModel().getSelectedItem();
			String category = catModel.getCategoryName();
			if (category != null) {
				txtCategory.setText(category);
			}
		}

	}

	@FXML
	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			if (categoryStage.isShowing())
				categoryStage.close();
		} else if (event.getSource() == btnAdd) {
			String value = txtCategory.getText();
			addCategoryItem(value);
			txtCategory.clear();
		} else if (event.getSource() == btnDelete) {
			String value = txtCategory.getText();
			deleteCategoryItem(value);
			txtCategory.clear();
		} else if (event.getSource() == btnModify) {
			String value = txtCategory.getText();
			modifyCategoryItem(value);
			txtCategory.clear();
		}else if (event.getSource() == btnSearch) {
			String value = txtCategory.getText();
			searchCategoryItems(value);
		}else if (event.getSource() == btnRefresh) {
			catList = CategoryDAO.selectAllItems();
			setCategoryTable(catList);
			txtCategory.clear();
		}
	}

	private void populateCategoryTable() {
		catList = CategoryDAO.selectAllItems();
		setCategoryTable(catList);
	}
	
	private void setCategoryTable(ObservableList<CategoryModel> list) {
		colNo.setCellValueFactory(cellData -> cellData.getValue().catIDProperty().asObject());
		colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());

		tableCategory.setItems(list);
		
	}

	public void addCategoryItem(String value) {
		if (!value.isEmpty()) {
			boolean exist = CategoryDAO.checkExistance(value);
			if (!exist) {
				CategoryDAO.addCategory(value);
				populateCategoryTable();
			} else {
				AlertsUtils.warningAlert("آیتم تکراری", "‌این آیتم از قبل وجود دارد " + "\n" + value);

			}
		} else {
			AlertsUtils.warningAlert("Empty Field", "لطفا اطلاعات وارد کنید");
		}

	}

	public void deleteCategoryItem(String value) {
		if (!value.isEmpty()) {
			boolean exist = CategoryDAO.checkExistance(value);
			if (exist) {
				int categoryId = CategoryDAO.getCategoryID(value);
				if (categoryId != 0) {
					CategoryDAO.deleteCategory(categoryId);
					populateCategoryTable();
				}
			} else {
				AlertsUtils.warningAlert("آیتم تکراری", "‌این آیتم از قبل وجود ندارد " + "\n" + value);
			}
		} else {
			AlertsUtils.warningAlert("Empty Field", "لطفا اطلاعات وارد کنید");
		}
	}

	public void modifyCategoryItem(String value) {
		if (!value.isEmpty()) {
			int categoryId = 0;
			if (!tableCategory.getSelectionModel().isEmpty()) {
				CategoryModel catModel = tableCategory.getSelectionModel().getSelectedItem();
				categoryId = catModel.getCatId();
			}
			boolean exist = CategoryDAO.checkExistance(value);
			if (!exist) {
				if (categoryId != 0) {
					CategoryDAO.modifyCategory(categoryId, value);
					populateCategoryTable();
				}
			} else {
				AlertsUtils.warningAlert("آیتم تکراری", "‌این آیتم از قبل وجود ندارد " + "\n" + value);
			}
		} else {
			AlertsUtils.warningAlert("Empty Field", "لطفا اطلاعات وارد کنید");

		}
	}
	
	public void searchCategoryItems(String value) {
		if (!value.isEmpty()) {
			catList = CategoryDAO.retrieveSearchItems(value);
			setCategoryTable(catList);
		}else {
			AlertsUtils.warningAlert("Empty Field", "لطفا اطلاعات وارد کنید");
		}
	}

}
