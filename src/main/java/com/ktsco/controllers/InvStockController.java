package com.ktsco.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.InvStockDetailModel;
import com.ktsco.models.InvStockModel;
import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InvStockController implements Initializable {
	private static final Logger log = LoggerFactory.getLogger(InvStockController.class);
	private static ViewClass view = new ViewClass();
	private InvStockModel invStockModel; 
	private ObservableList<InvStockModel> invStockList = FXCollections.observableArrayList();
	private ObservableList<InvStockDetailModel> invImportDetail = FXCollections.observableArrayList();
	
	@FXML
	private Button btnCategory, btnInvetoryItems;
	
	@FXML
	private TextField txtImportId, txtImportDate;
	
	@FXML
	private ComboBox<String> comboEmployee;
	
	@FXML
	private TableColumn<InvStockDetailModel, String> comInvItem;

	@FXML
	private TableView<InvStockDetailModel> tableImportList;

	@FXML
	private TableColumn<InvStockDetailModel, String> colCategory;

	@FXML
	private TableColumn<InvStockDetailModel, Double> colImportQty;

	@FXML
	private Label labelMessage;

	// initialize Methods

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateComboItems();
	}

	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnCategory) {
			openCategoryPanel();
			CategoryController.categoryStage.setOnHidden(e -> populateComboItems());
		} else if (event.getSource() == btnInvetoryItems) {
			openInventoryItemsPanel();
			InventoryController.invStage.setOnHidden(e -> populateComboItems());
		}
	}

	// Opening Category panel
	private void openCategoryPanel() {
		log.info("Openning Category Panel");
		VBox category = view.setVboxFxml(Constants.categoryPanelFxml);
		CategoryController.categoryStage = view.setSceneAndShowStage(category, "", false, false);
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
	
	
	

}
