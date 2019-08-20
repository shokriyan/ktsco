package com.ktsco.controllers.csr;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.ktsco.models.csr.ReceivableModel;
import com.ktsco.modelsdao.ReceivableDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class ReceivableController implements Initializable {

	@FXML
	private Button btnReceiveForm, btnReceiveSearch, btnDepositReceived;
	@FXML
	private TableView<ReceivableModel> tableAccountReceivable ;
	@FXML
	private TableColumn<ReceivableModel, Integer> colCode;
	@FXML
	private TableColumn<ReceivableModel, String> colCompany, colBillDate, colDueDate, colBillTotal, colCurrency;
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
		if (event.getSource() == btnReceiveForm) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receiveBillPanel"));
		}else if (event.getSource() == menuOpenReceivePanel) {
			if (!tableAccountReceivable.getSelectionModel().isEmpty()) {
				ReceivableModel model = tableAccountReceivable.getSelectionModel().getSelectedItem();
				ReceiveControler.saleBillID = model.getBillID();
				Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receiveBillPanel"));
			}
		}else if (event.getSource() == btnReceiveSearch) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receiveSearchPanel"));
		}else if (event.getSource() == btnDepositReceived) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("receivedDepositPanel"));
		}

	}
	
	private void reloadPrerequisitions() {
		populateTableDate();
	}

	private void generateTable(ObservableList<ReceivableModel> list) {
		colCode.setCellValueFactory(cellDate -> cellDate.getValue().getBillIDProperty().asObject());
		colCompany.setCellValueFactory(cellDate -> cellDate.getValue().getCompanyProperty());
		colBillDate.setCellValueFactory(cellDate -> cellDate.getValue().getBilldateProperty());
		colDueDate.setCellValueFactory(cellDate -> cellDate.getValue().getDuedateProperty());
		colBillTotal.setCellValueFactory(cellDate -> cellDate.getValue().getBillTotalProperty());
		colCurrency.setCellValueFactory(cellDate -> cellDate.getValue().getCurrencyProperty());
		colDueDate.setCellFactory(column -> {
			return new TableCell<ReceivableModel, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						// Convert Due date to Date
						try {
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
							Date dueDate = format.parse(DateUtils.convertJalaliToGregory(item));
							Date todayDate = format.parse(String.valueOf(LocalDate.now()));

							// Style all dates in March with a different color.
							if (dueDate.before(todayDate)) {
								setText(String.valueOf(item));
								setTextFill(Color.YELLOW);
								setStyle("-fx-background-color: red");
							} else {
								setText(String.valueOf(item));
								setTextFill(Color.BLACK);
								setStyle("");
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			};
		});
		
		tableAccountReceivable.setItems(list);
		
		

	}

	private void populateTableDate() {
		tableDate = ReceivableDAO.retriveReceivableList();
		generateTable(tableDate);
	}

}
