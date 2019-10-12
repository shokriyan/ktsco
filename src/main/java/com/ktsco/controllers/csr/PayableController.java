package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.csr.ReceivableModel;
import com.ktsco.modelsdao.PayableDAO;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PayableController implements Initializable {

	@FXML
	private Button btnPayment, btnReceiveSearch;
	@FXML
	private TableView<ReceivableModel> tableAccountReceivable ;
	@FXML
	private TableColumn<ReceivableModel, Integer> colCode;
	@FXML
	private TableColumn<ReceivableModel, String> colCompany, colBillDate, colBillTotal, colCurrency;
	@FXML
	private MenuItem menuOpenReceivePanel; 
	ObservableList<ReceivableModel> tableDate = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		reloadPrerequisitions();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnPayment) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("PaybillPanel"));
		}else if (event.getSource() == menuOpenReceivePanel) {
			if (!tableAccountReceivable.getSelectionModel().isEmpty()) {
				ReceivableModel model = tableAccountReceivable.getSelectionModel().getSelectedItem();
				PaymentController.expenseID = model.getBillID();
				Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("PaybillPanel"));
			}
		}else if (event.getSource() == btnReceiveSearch) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("paymentSearchPanel"));
		}

	}
	
	private void reloadPrerequisitions() {
		populateTableDate();
	}

	private void generateTable(ObservableList<ReceivableModel> list) {
		colCode.setCellValueFactory(cellDate -> cellDate.getValue().getBillIDProperty().asObject());
		colCompany.setCellValueFactory(cellDate -> cellDate.getValue().getCompanyProperty());
		colBillDate.setCellValueFactory(cellDate -> cellDate.getValue().getBilldateProperty());
		colBillTotal.setCellValueFactory(cellDate -> cellDate.getValue().getBillTotalProperty());
		colCurrency.setCellValueFactory(cellDate -> cellDate.getValue().getCurrencyProperty());
		
		tableAccountReceivable.setItems(list);
		
		

	}

	private void populateTableDate() {
		tableDate = PayableDAO.retrievePayableList();
		generateTable(tableDate);
	}
	
	

}
