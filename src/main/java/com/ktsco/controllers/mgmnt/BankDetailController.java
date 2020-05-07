package com.ktsco.controllers.mgmnt;


import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.csr.AccountsModel;
import com.ktsco.models.mgmt.BankDetail;
import com.ktsco.models.mgmt.SellSummaryModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class BankDetailController implements Initializable {
	
	@FXML
	private ComboBox<String> comboAccounts;
	@FXML
	private Button btnSearch;
	@FXML
	private TextField txtFromDate, txtToDate ;
	@FXML
	private TableView<BankDetail> tableDetailList;
	@FXML
	private TableColumn<BankDetail, Integer> colNo;
	@FXML
	private TableColumn<BankDetail, String> colFromBank, colDate, colCurr;
	@FXML
	private TableColumn<BankDetail, Double> colAmount;
	@FXML
	private Label labelOpeningBalance, labelTotalAnount;
	@FXML
	private HBox detailPanel;
	
	private ObservableList<BankDetail> datalist = FXCollections.observableArrayList();
	private List<String> comboValues = new ArrayList<String>();
	private ObservableList<AccountsModel> accountsDetails = FXCollections.observableArrayList();
	private double totalOrignalAmount = 0; 
	NumberFormat formatQuantity = NumberFormat.getNumberInstance();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		detailPanel.setVisible(false);
		populateComboBox();
	}
	
	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			searchForAccounts();
		}
	}
	
	private void populateComboBox() {
		accountsDetails = AccountsDAO.reterieveAllRecords();
		accountsDetails.forEach(model -> {
			int code = model.getCode(); 
			String bankName = model.getBankName(); 
			String bankAccount = model.getBankAccount(); 
			String accountInfoForCombo=String.join(" - " , String.valueOf(code),bankName,bankAccount);
			comboValues.add(accountInfoForCombo);
		});
		Commons.populateAllComboBox(comboAccounts, comboValues);
		comboAccounts.setValue("");
	}
	
	private void searchForAccounts() {
		String comboValue = comboAccounts.getValue(); 
		if (!comboValue.equalsIgnoreCase("")) {
			if (!accountsDetails.isEmpty()) {
				detailPanel.setVisible(true);
				int selectedItem =comboAccounts.getSelectionModel().getSelectedIndex();
				AccountsModel accountModal = accountsDetails.get(selectedItem-1);
				int bankId = accountModal.getCode();
				double openingBalance = Double.parseDouble(accountModal.getOpeningBalance());
				labelOpeningBalance.setText(formatQuantity.format(openingBalance));
				populateTableData(bankId , openingBalance );
			}
		}else {
			detailPanel.getChildren().clear();
			detailPanel.setVisible(false);
		}
	}
	
	private void generateTableColumns(ObservableList<BankDetail> list) {
		colNo.setCellValueFactory(data -> data.getValue().idProperty().asObject());
		colFromBank.setCellValueFactory(data -> data.getValue().fromBankNameProperty());
		colDate.setCellValueFactory(data -> data.getValue().entryDateProperty());
		colCurr.setCellValueFactory(data -> data.getValue().currencyProperty());
		colAmount.setCellValueFactory(data -> data.getValue().orginalAmountProperty().asObject());
		formatColumn(colAmount, formatQuantity);
		tableDetailList.setItems(list);
	}
	
	
	private void formatColumn(TableColumn<BankDetail, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<BankDetail, Double>() {

			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty) {
					setText(null);
				} else {
					setText(format.format(price));
				}
			}
		});
	}
	
	
	private void populateTableData(int bankId , double openingBalance) {
		if (!datalist.isEmpty())
			datalist.clear();
		String fromDate = txtFromDate.getText();
		String toDate = txtToDate.getText();
		datalist = AccountsDAO.getBankDetailReport(bankId, fromDate, toDate);
		generateTableColumns(datalist);
		calculateTotalAmount(openingBalance);
	}
	
	private void calculateTotalAmount(double openingBalance) {
		totalOrignalAmount = openingBalance;
		if (!datalist.isEmpty()) {
			datalist.forEach(model -> {
				totalOrignalAmount += model.getOrginalAmount(); 
			});
			
		}
		
		labelTotalAnount.setText(formatQuantity.format(totalOrignalAmount));
	}
	

}
