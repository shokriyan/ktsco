package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.ktsco.models.factory.DetailReportModel;
import com.ktsco.modelsdao.InventoryDAO;
import com.ktsco.modelsdao.InventoryStockDAO;
import com.ktsco.utils.Commons;

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
import javafx.stage.Stage;

public class ImportDetailReportController implements Initializable{
	


    @FXML
    private Button btnClose ,btnModify, btnRefresh, btnDelete, btnSearch;
    
    @FXML
    private ComboBox<String> comboItem;

    @FXML
    private Label labelInfo;
    @FXML
    private TableView<DetailReportModel> tableImport;

    @FXML
    private TableColumn<DetailReportModel, Integer> colNo;

    @FXML
    private TableColumn<DetailReportModel, String> colUnit;

    @FXML
    private TableColumn<DetailReportModel, String> colDate;
    @FXML
    private TableColumn<DetailReportModel, String> colItem;
	@FXML
    private TableColumn<DetailReportModel, String> colQuantity;

	private String inventoryID; 
	public static Stage importDetailStage = new Stage();
    @FXML
    void allButtonAction(ActionEvent event) {
    	if (event.getSource() == btnClose) {
    		if (importDetailStage.isShowing()) {
    			importDetailStage.close();
    		}
    	}else if (event.getSource() == btnSearch) {
    		populateTableRecords();
    	}else if (event.getSource() == btnRefresh) {
    		loadPrerequisitions();
    		populateTableRecords();
    	}

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrerequisitions();
		populateTableRecords();
		comboItem.setEditable(true);
		TextFields.bindAutoCompletion(comboItem.getEditor(), comboItem.getItems());
	}
	
	private void loadPrerequisitions() {
		generateComboItems();
	}
	
	private void generateComboItems() {
		List<String> items = InventoryDAO.getInvItemsForCombo();
		Commons.populateAllComboBox(comboItem, items);
		comboItem.setValue("");
	}
	
	public String getComboValue() {
		return comboItem.getValue();
	}
	
	private String getInventoryID(String lookupValue) {
		return String.valueOf(InventoryDAO.getInvId(lookupValue));
	}
	
	private void generateTableColumns(ObservableList<DetailReportModel> list) {
		colNo.setCellValueFactory(cellData -> cellData.getValue().getIdNumberProperty().asObject());
		colItem.setCellValueFactory(cellData -> cellData.getValue().getItemProperty());
		colUnit.setCellValueFactory(cellData -> cellData.getValue().getUnitProperty());
		colDate.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
		colQuantity.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
		tableImport.setItems(list);
	}
	
	public void populateTableRecords() {
		String lookupValue = getComboValue();
		inventoryID = ("".equals(lookupValue)) ? "" : getInventoryID(lookupValue);
		ObservableList<DetailReportModel> list = FXCollections.observableArrayList();
		list = InventoryStockDAO.getDetailItemRecord(inventoryID);
		generateTableColumns(list);
	}
	
	


}
