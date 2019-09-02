package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.SalesSearchModel;
import com.ktsco.modelsdao.ExpenseDAO;
import com.ktsco.modelsdao.VendorsDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExpenseSearchController implements Initializable{
private static Logger log = LoggerFactory.getLogger(SaleSearchController.class);
	
	@FXML
	private Button btnClose, btnSearch, btnRefresh;
	
	@FXML
	private TextField txtBillID, txtStartDate,txtEndDate;
	
	@FXML
	private ComboBox<String> comboVendor; 
	
	@FXML
	private TableView<SalesSearchModel> tableItemDetail;
	
	@FXML
	private TableColumn<SalesSearchModel, Integer> colBillID;
	
	@FXML
	private TableColumn<SalesSearchModel, String> colCustomer, colCurrency, colBillDate, colDueDate;
	
	@FXML
	private TableColumn<SalesSearchModel, String> colBillTotal;
	@FXML
	private MenuItem menuCopy;
	
	private ObservableList<SalesSearchModel> searchItems = FXCollections.observableArrayList();
	public static Stage expenseStage = new Stage();
	@FXML
	private void allButtonAction (ActionEvent event) {
		if (event.getSource()== btnClose) {
			if (expenseStage.isShowing())
				expenseStage.hide();
		}else if (event.getSource() == btnSearch) {
			populateSearchItems();
		}else if (event.getSource() == btnRefresh) {
			txtBillID.clear();
			txtStartDate.clear();
			txtEndDate.clear();
			populateCustomerCombo();
			populateSearchItems();
		}else if (event.getSource() == menuCopy) {
			if (!tableItemDetail.getSelectionModel().isEmpty()) {
				SalesSearchModel model = tableItemDetail.getSelectionModel().getSelectedItem();
				Commons.copyToClipboard(model.getBillID());
				log.debug("Copied :::: " + String.valueOf(model.getBillID()) );
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateCustomerCombo();
		populateSearchItems();
		
		txtStartDate.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					DateUtils.checkEntryDateFormat(newValue);
				}
			}
		});
		txtEndDate.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					DateUtils.checkEntryDateFormat(newValue);
				}
			}
		});
	}
	
	private void generateTableColumns (ObservableList<SalesSearchModel> list) {
		colBillID.setCellValueFactory(cellData -> cellData.getValue().getBillIDProperty().asObject());
		colCustomer.setCellValueFactory(cellData -> cellData.getValue().getCompanyProperty());
		colCurrency.setCellValueFactory(cellData -> cellData.getValue().getCurrencyProperty());
		colBillDate.setCellValueFactory(cellData -> cellData.getValue().getBilLDateProperty());
		colBillTotal.setCellValueFactory(cellData -> cellData.getValue().getBillTotalProperty());
		tableItemDetail.setItems(list);
	}
	
	private void populateCustomerCombo() {
		List<String> customerList = VendorsDAO.getVendorList();
		Commons.populateAllComboBox(comboVendor, customerList);
		comboVendor.setValue("");
	}

	
	private void populateSearchItems() {
		String billID = txtBillID.getText();
		String company = ("".equalsIgnoreCase(comboVendor.getValue()))?"":comboVendor.getValue().split("-")[0].trim();
		String startDate = txtStartDate.getText();
		String toDate = txtEndDate.getText();
		
		searchItems = ExpenseDAO.searchExpense(billID, company, startDate, toDate);
		generateTableColumns(searchItems);
	}
	
}
