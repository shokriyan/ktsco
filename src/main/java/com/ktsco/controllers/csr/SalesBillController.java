package com.ktsco.controllers.csr;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.ktsco.models.csr.BillDetailModel;
import com.ktsco.models.csr.MainStockModel;
import com.ktsco.modelsdao.CurrencyDAO;
import com.ktsco.modelsdao.CustomerDAO;
import com.ktsco.modelsdao.MainStockDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.modelsdao.SaleBillDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

public class SalesBillController implements Initializable {

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	@FXML
	private Button btnNew, btnSave, btnSaveClose, btnSearch, btnReturn, btnSearchBill, btnDeleteBill;
	@FXML
	private TextField txtCode, txtBillDate, txtCurrencyType, txtmemo;

	@FXML
	private ComboBox<String> comboCustomer, comboPayTerm;

	@FXML
	private TableView<BillDetailModel> tableBillDetail;
	@FXML
	private TableColumn<BillDetailModel, Integer> colLineNumber;
	@FXML
	private TableColumn<BillDetailModel, String> colItems, colUnit;
	@FXML
	private TableColumn<BillDetailModel, String> colQuantity, colUnitPrice, colLineTotal;

	@FXML
	private Label labelBillTotal, labelExRate, labelUSDAmount, labelInfoMessage;
	@FXML
	private MenuItem menuAddRow, menuDeleteRow;

	private ObservableList<BillDetailModel> tableItems = FXCollections.observableArrayList();
	private ObservableList<MainStockModel> stockList = FXCollections.observableArrayList();

	private List<String> customerList = new ArrayList<String>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrerequisitions();
		generateOneRow();
		comboCustomer.setEditable(true);
		TextFields.bindAutoCompletion(comboCustomer.getEditor(), comboCustomer.getItems());

		txtBillDate.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() >= 10) {
					String getDate = DateUtils.convertJalaliToGregory(newValue);
					String currencyType = Commons.getCurrencyKey(txtCurrencyType.getText());
					String exRate = CurrencyDAO.getCurrencyRate(currencyType, getDate);
					if (!"".equalsIgnoreCase(exRate)) {
						if (!"".equalsIgnoreCase(currencyType)) {
							labelExRate.setText(exRate);
						}
					} else {
						AlertsUtils.CurrencyEntryAlert(currencyType);
					}
					calucalteBillTotal();
				}
			}
		});
	}

	@FXML
	private void onCustomerComboAction(ActionEvent event) {
		if (null != comboCustomer.getValue() && !"".equalsIgnoreCase(comboCustomer.getValue())) {
			txtCurrencyType.setText(comboCustomer.getValue().split("-")[2].trim());
		} else {
			txtCurrencyType.setText("");
		}
		String getDate = DateUtils.convertJalaliToGregory(txtBillDate.getText());
		String currencyType = Commons.getCurrencyKey(txtCurrencyType.getText());

		String exRate = CurrencyDAO.getCurrencyRate(currencyType, getDate);
		if (!"".equalsIgnoreCase(exRate)) {
			if (!"".equalsIgnoreCase(currencyType)) {
				labelExRate.setText(exRate);
			} else
				labelExRate.setText("0");
		} else {

			AlertsUtils.CurrencyEntryAlert(currencyType);
		}

		calucalteBillTotal();
	}

	private void checkCurrencyType() {
		String getDate = DateUtils.convertJalaliToGregory(txtBillDate.getText());

		String exRate = CurrencyDAO.getCurrencyRate(getDate);
		if ("".equalsIgnoreCase(exRate)) {
			AlertsUtils.CurrencyEntryAlert("");
		}
		calucalteBillTotal();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("SalepanelFxml"));
		} else if (event.getSource() == menuAddRow) {
			generateOneRow();
			calucalteBillTotal();
		} else if (event.getSource() == btnNew) {
			comboCustomer.setValue("");
			loadPrerequisitions();
			tableItems.clear();
			generateOneRow();
			calucalteBillTotal();
			txtmemo.clear();
		} else if (event.getSource() == btnSave) {
			if (checkFlag()) {
				boolean result = insertIntoSaleBill();
				if (result) {
					boolean isSuccess = insertIntoSaleDetail();
					if (isSuccess) {
						regenrateDetailTable();
					} else {
						// Call to Delete Bill from Database
					}

					Commons.processMessageLabel(labelInfoMessage, isSuccess);
				} else {
					Commons.processMessageLabel(labelInfoMessage, result);
				}

			} else {
				AlertsUtils.emptyFieldAlert();
			}
		} else if (event.getSource() == btnSaveClose) {
			if (checkFlag()) {
				boolean result = insertIntoSaleBill();
				if (result) {
					boolean isSuccess = insertIntoSaleDetail();
					if (isSuccess) {
						regenrateDetailTable();
						Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("SalepanelFxml"));

					} else {
						// Call to Delete Bill from Database
					}

					Commons.processMessageLabel(labelInfoMessage, isSuccess);
				} else {
					Commons.processMessageLabel(labelInfoMessage, result);
				}

			} else {
				AlertsUtils.emptyFieldAlert();
			}
		} else if (event.getSource() == menuDeleteRow) {
			if (tableItems.size() >= 0) {
				if (!tableBillDetail.getSelectionModel().isEmpty()) {
					BillDetailModel model = tableBillDetail.getSelectionModel().getSelectedItem();
					int id = model.getID();
					if (id == 0) {
						int index = tableBillDetail.getSelectionModel().getSelectedIndex();
						tableItems.remove(index);
					} else {
						boolean response = AlertsUtils.askForDeleteAlert(model.getItems());
						if (response) {
							boolean isSuccess = SaleBillDAO.deleteSaleDetail(id);
							regenrateDetailTable();
							Commons.processMessageLabel(labelInfoMessage, isSuccess);

						}
					}
					calucalteBillTotal();

				}
			}
		} else if (event.getSource() == btnSearchBill) {
			setBillData();
			regenrateDetailTable();
			calucalteBillTotal();
		} else if (event.getSource() == btnDeleteBill) {
			deleteSaleBill();
			loadPrerequisitions();
			tableItems.clear();
			generateOneRow();
			calucalteBillTotal();
		} else if (event.getSource() == btnSearch) {
			SaleSearchController.saleSearchStage = Commons
					.openPanelsUndecorate(Commons.getFxmlPanel("SalesSearchPanel"));
		}

	}

	private void loadPrerequisitions() {
		stockList = MainStockDAO.retrieveStockItems();
		setTodayDate();
		populateCustomerCombo();
		populatePayTermCombo();
		generateBillID();
		checkCurrencyType();
	}

	private void populateCustomerCombo() {
		customerList = CustomerDAO.getCustomersList();
		Commons.populateAllComboBox(comboCustomer, customerList);
		comboCustomer.setValue("");
	}

	private void populatePayTermCombo() {
		Commons.populateAllComboBox(comboPayTerm, Constants.payTerms);
		comboPayTerm.setValue(Constants.payTerms.get(2));

	}

	private void setTodayDate() {
		String todayDate = DateUtils.convertGregoryToJalali(Commons.getTodaysDate());
		txtBillDate.setText(todayDate);
	}

	private void generateBillID() {
		int id = SaleBillDAO.getLastBillID();
		txtCode.setText(String.valueOf(id));
	}

	private void generateTableColumns(ObservableList<BillDetailModel> list) {
		colLineNumber.setCellValueFactory(cellData -> cellData.getValue().getLineNumberProperty().asObject());
		ObservableList<String> items = ProductDAO.getProductObservableList();
		colItems.setCellValueFactory(cellData -> cellData.getValue().getItemsProperty());
		colItems.setCellFactory(ComboBoxTableCell.forTableColumn(items));
		colUnit.setCellValueFactory(cellData -> cellData.getValue().getUnitProperty());
		colQuantity.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
		colQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
		colUnitPrice.setCellValueFactory(cellData -> cellData.getValue().getUnitPriceProperty());
		colUnitPrice.setCellFactory(TextFieldTableCell.forTableColumn());
		colLineTotal.setCellValueFactory(cellData -> cellData.getValue().getLineTotalProperty());
		tableBillDetail.setEditable(true);
		tableBillDetail.setItems(list);
	}

	private void generateOneRow() {
		int size = tableItems.size();
		BillDetailModel model = new BillDetailModel(size + 1);
		tableItems.add(model);
		generateTableColumns(tableItems);
	}

	@FXML
	private void onItemChangeAction(CellEditEvent<BillDetailModel, String> editedCell) {
		BillDetailModel model = tableBillDetail.getSelectionModel().getSelectedItem();
		model.setItems(editedCell.getNewValue());

		String unit = ProductDAO.getUnitMeasure(editedCell.getNewValue());
		model.setUnit(unit);
		generateTableColumns(tableItems);
	}

	@FXML
	private void onQauntityChangeAction(CellEditEvent<BillDetailModel, String> editedCell) {
		BillDetailModel model = tableBillDetail.getSelectionModel().getSelectedItem();
		model.setQuantity(editedCell.getNewValue());
		checkForSaleQuantityinStock(model.getItems(), Double.parseDouble(model.getQuantity()));
		String linetotal = Commons.calculateLineTotal(editedCell.getNewValue(), model.getUnitPrice().replace(",", ""));
		model.setLineTotal(linetotal);
		generateTableColumns(tableItems);
		calucalteBillTotal();
	}

	@FXML
	private void onUnitPriceChangeAction(CellEditEvent<BillDetailModel, String> editedCell) {
		BillDetailModel model = tableBillDetail.getSelectionModel().getSelectedItem();
		model.setUnitPrice(editedCell.getNewValue());

		String linetotal = Commons.calculateLineTotal(model.getQuantity().replace(",", ""), editedCell.getNewValue());
		model.setLineTotal(linetotal);
		generateTableColumns(tableItems);
		calucalteBillTotal();
	}

	private void calucalteBillTotal() {
		decimalFormat.setRoundingMode(RoundingMode.UP);
		Double total = 0.00;
		for (BillDetailModel model : tableItems) {

			total += Double.parseDouble(model.getLineTotal().replace(",", ""));

		}

		Double exRate = Double.parseDouble(labelExRate.getText());

		labelBillTotal.setText(decimalFormat.format(total));

		labelUSDAmount.setText("$ " + decimalFormat.format(total * exRate));
	}

	private boolean checkFlag() {
		boolean isSuccess = false;

		// Empty Customer field

		if (!comboCustomer.getValue().equalsIgnoreCase("")) {
			if (!txtBillDate.getText().equalsIgnoreCase("")) {
				if (!comboPayTerm.getValue().equalsIgnoreCase("")) {
					for (BillDetailModel model : tableItems) {
						if (!"".equalsIgnoreCase(model.getItems())) {
							if (!model.getQuantity().equalsIgnoreCase("0")
									&& !model.getUnitPrice().equalsIgnoreCase("0")) {
								isSuccess = true;
							} else {
								isSuccess = false;
							}
						} else {
							isSuccess = false;
						}
					}
				}
			}
		}

		return isSuccess;
	}

	private boolean insertIntoSaleBill() {
		boolean isSuccess = false;
		int billID = Integer.parseInt(txtCode.getText());
		int customerID = Integer.parseInt(comboCustomer.getValue().split("-")[0].trim());

		String billDate = txtBillDate.getText();
		int payTerm = Integer.parseInt(comboPayTerm.getValue().split("-")[0].trim());
		String currencyType = Commons.getCurrencyKey(txtCurrencyType.getText());
		String billMemo = txtmemo.getText();

		isSuccess = SaleBillDAO.insertIntoSaleBill(billID, customerID, billDate, payTerm, currencyType, billMemo);
		return isSuccess;
	}

	private boolean insertIntoSaleDetail() {
		boolean isSuccess = false;
		for (BillDetailModel model : tableItems) {
			int id = model.getID();
			int billID = Integer.parseInt(txtCode.getText());
			String product = model.getItems();
			String quantity = model.getQuantity();
			String unitPrice = model.getUnitPrice();

			isSuccess = SaleBillDAO.insertBillDetail(id, billID, product, quantity, unitPrice);
			if (!isSuccess)
				break;
		}
		return isSuccess;
	}

	private void regenrateDetailTable() {
		int billID = Integer.parseInt(txtCode.getText());
		tableItems = SaleBillDAO.retrieveSaleDateAfterSave(billID);
		generateTableColumns(tableItems);
	}

	private void setBillData() {
		int billID = Integer.parseInt(txtCode.getText());
		Map<String, Object> billData = SaleBillDAO.retieveSaleBillDate(billID);
		String billDate = billData.get("billDate").toString();
		txtBillDate.setText(DateUtils.convertGregoryToJalali(billDate));
		if (!billData.isEmpty()) {
			String CustomerName = null;
			for (String values : customerList) {
				if (values.startsWith(billData.get("customerCode").toString())) {
					CustomerName = values;
					break;
				}
			}
			comboCustomer.setValue(CustomerName);
			String dueDate = billData.get("dueDate").toString();
			int days = (int) Commons.calucateDayBtwDates(billDate, dueDate);
			if (days <= 0)
				comboPayTerm.setValue(Constants.payTerms.get(0));
			else if (days > 2 && days <= 9)
				comboPayTerm.setValue(Constants.payTerms.get(1));
			else if (days > 8 && days <= 32)
				comboPayTerm.setValue(Constants.payTerms.get(2));
			else
				comboPayTerm.setValue(Constants.payTerms.get(3));

			txtCurrencyType.setText(comboCustomer.getValue().split("-")[2].trim());
			txtmemo.setText(billData.get("billMemo").toString());
		}
	}

	private void deleteSaleBill() {
		int billID = Integer.parseInt(txtCode.getText());
		boolean response = AlertsUtils.askForDeleteAlert("فاکتور شماره" + "\n" + billID);
		if (response) {
			boolean isSuccess = SaleBillDAO.deleteSaleBill(billID);
			Commons.processMessageLabel(labelInfoMessage, isSuccess);
		}

	}

	private void checkForSaleQuantityinStock(String item, double saleQuantity) {
		for (MainStockModel model : stockList) {
			if (model.getProduct().equalsIgnoreCase(item)) {
				double stockQauntity = model.getReminder();
				if ((stockQauntity - saleQuantity) < 0) {
					AlertsUtils.warningForStockRemain(item, String.valueOf(stockQauntity));
				}
			}
		}
	}
	

}
