package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.factory.InventoryModel;
import com.ktsco.modelsdao.CategoryDAO;
import com.ktsco.modelsdao.InventoryDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.ProcessService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class InventoryController implements Initializable {

	@FXML
	private Text txtPanelName;

	@FXML
	private Button btnClose, btnAdd, btnDelete, btnModify, btnSearch, btnRefresh;
	@FXML
	private Label labelInfo;
	@FXML
	private TextField txtItem;
	@FXML
	private ComboBox<String> comboCategory, comboUM;

	@FXML
	private TableView<InventoryModel> tableInventory;
	@FXML
	private TableColumn<InventoryModel, Integer> colNo;
	@FXML
	private TableColumn<InventoryModel, String> colCategory, colItems, colUM;

	public static Stage invStage = new Stage();
	private ObservableList<InventoryModel> invList = FXCollections.observableArrayList();
	private List<String> items = new ArrayList<String>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		items = CategoryDAO.getCategoryItemsForCombo();
		Commons.populateAllComboBox(comboCategory, items);
		populateInventoryItems();
		Commons.populateAllComboBox(comboUM, Constants.unitMeasureList);

	}

	@FXML
	public void clearInfoLabel() {
		txtItem.setOnKeyPressed(event -> labelInfo.setVisible(false));
	}

	/**
	 * Buttons Action
	 * 
	 * @param event
	 */
	@FXML
	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			invStage.getOnHidden().handle(new WindowEvent(invStage, WindowEvent.WINDOW_HIDDEN));
			invStage.close();
		} else if (event.getSource() == btnAdd) {
			SaveNewInventory();
			populateInventoryItems();
			txtItem.clear();
		} else if (event.getSource() == btnDelete) {
			deleteInventory();
			populateInventoryItems();
			txtItem.clear();
		} else if (event.getSource() == btnRefresh) {
			populateInventoryItems();
			txtItem.clear();
			comboCategory.setValue("");
			comboUM.setValue("");
			labelInfo.setVisible(false);
		} else if (event.getSource() == btnModify) {
			updateInventory();
			populateInventoryItems();
			txtItem.clear();
		} else if (event.getSource() == btnSearch) {
			searchInventory();
			setLabelInfo(true);
		}
	}

	/**
	 * Table Click Actions
	 */
	@FXML
	public void onClickAction() {

		if (!tableInventory.getSelectionModel().isEmpty()) {
			if (labelInfo.isVisible())
				labelInfo.setVisible(false);
			InventoryModel invMode = tableInventory.getSelectionModel().getSelectedItem();
			txtItem.setText(invMode.getInvItem());
			comboCategory.setValue(invMode.getInvCategory());
			comboUM.setValue(invMode.getInvUM());
		}

	}

	private int getSelectedInvId() {
		if (!tableInventory.getSelectionModel().isEmpty()) {
			InventoryModel invModel = tableInventory.getSelectionModel().getSelectedItem();
			return invModel.getInvID();
		}
		return 0;
	}

	private void populateInventoryItems() {
		invList = InventoryDAO.getAllInvetoryRecord();
		setInventoryTable(invList);
	}

	private void setInventoryTable(ObservableList<InventoryModel> list) {

		colNo.setCellValueFactory(cellData -> cellData.getValue().getInvIDProperty().asObject());
		colItems.setCellValueFactory(cellData -> cellData.getValue().getInvItemProperty());
		colCategory.setCellValueFactory(cellData -> cellData.getValue().getInvCategoryProperty());
		colUM.setCellValueFactory(cellData -> cellData.getValue().getInvUMProperty());

		tableInventory.setItems(list);

	}

	private void SaveNewInventory() {
		String items = txtItem.getText().trim();
		int catId = CategoryDAO.getCategoryID(comboCategory.getValue());
		String um = comboUM.getValue();

		if (!items.isEmpty()) {
			boolean isExist = InventoryDAO.checkItemisExist(items);
			if (!isExist) {
				boolean success = InventoryDAO.insertInvetoryItems(items, catId, um);
				setLabelInfo(success);
			} else {
				AlertsUtils.repeatItemAlerts(items);
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void setLabelInfo(boolean success) {
		Service<Void> service = new ProcessService();
		if (success) {
			labelInfo.setStyle("-fx-text-fill:green");
			labelInfo.setText("انجام شد");
			labelInfo.setVisible(true);
			if (!service.isRunning()) {
				service.start();
			}
			
			service.setOnSucceeded(event -> {
				labelInfo.setVisible(false);
				service.reset();
			});
			
		} else {
			labelInfo.setStyle("-fx-text-fill:red");
			labelInfo.setText("انجام نشد");
			labelInfo.setVisible(true);
			if (!service.isRunning()) {
				service.start();
			}
			
			service.setOnSucceeded(event -> {
				labelInfo.setVisible(false);
				service.reset();
			});
			
		}
	}

	private void deleteInventory() {
		String invValue = getInvetoryItem();
		boolean response = AlertsUtils.askForDeleteAlert(invValue);
		if (response) {
			int inv_id = getSelectedInvId();
			if (inv_id != 0) {
				boolean success = InventoryDAO.deleteInventoryItem(inv_id);
				setLabelInfo(success);
			} else
				setLabelInfo(false);
		}

	}
	
	private String getInvetoryItem() {
		if (!tableInventory.getSelectionModel().isEmpty()) {
			InventoryModel invModel = tableInventory.getSelectionModel().getSelectedItem();
			return invModel.getInvItem();
		}else
			return null;
	}

	private void updateInventory() {

		int invId = getSelectedInvId();
		String invItem = txtItem.getText();
		int catId = CategoryDAO.getCategoryID(comboCategory.getValue());
		String invUm = comboUM.getValue();

		if (!invItem.isEmpty()) {
			boolean success = InventoryDAO.updateInventoryItem(invId, invItem, catId, invUm);
			setLabelInfo(success);
		} else {
			setLabelInfo(false);
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void searchInventory() {
		String item = txtItem.getText();
		String category = "";

		category = comboCategory.getValue();
		String invUm = "";
		invUm = comboUM.getValue();

		invList = InventoryDAO.searchInventroyItems(item, category, invUm);
		setInventoryTable(invList);
	}

}
