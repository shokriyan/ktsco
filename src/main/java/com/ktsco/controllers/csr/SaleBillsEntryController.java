package com.ktsco.controllers.csr;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.ktsco.enums.Dictionary;
import com.ktsco.models.csr.BillEntryModal;
import com.ktsco.models.csr.MainStockModel;
import com.ktsco.models.factory.ProductModel;
import com.ktsco.modelsdao.CurrencyDAO;
import com.ktsco.modelsdao.CustomerDAO;
import com.ktsco.modelsdao.MainStockDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.modelsdao.SaleBillDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.BaseComponent;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SaleBillsEntryController extends BaseComponent implements Initializable {

	@FXML
	private Button btnNew, btnSave, btnSaveClose, btnSearch, btnReturn, btnSearchBill, btnDeleteBill;
	@FXML
	private TextField txtCode, txtBillDate, txtCurrencyType, txtmemo;

	@FXML
	private ComboBox<String> comboCustomer, comboPayTerm;

	@FXML
	private ScrollPane entryFormPane;

	@FXML
	private Label labelBillTotal, labelExRate, labelUSDAmount, labelInfoMessage;
	@FXML
	private MenuItem menuAddRow, menuDeleteRow;

	private ObservableList<BillEntryModal> entryList = FXCollections.observableArrayList();
	private ObservableList<MainStockModel> stockList = FXCollections.observableArrayList();
	private ObservableList<ProductModel> productsList = FXCollections.observableArrayList();
	ObservableList<String> productsItems = FXCollections.observableArrayList();
	private List<String> customerList = new ArrayList<String>();

	NumberFormat formatNumber = new DecimalFormat("#0.00");
	NumberFormat formatPrice = DecimalFormat.getCurrencyInstance();

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
					calculateBillTotal();
				}
			}
		});

		formEntryPanel();
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

		calculateBillTotal();
	}

	private void checkCurrencyType() {
		String getDate = DateUtils.convertJalaliToGregory(txtBillDate.getText());

		String exRate = CurrencyDAO.getCurrencyRate(getDate);
		if ("".equalsIgnoreCase(exRate)) {
			AlertsUtils.CurrencyEntryAlert("");
		}
		calculateBillTotal();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnReturn) {
			Commons.setCenterPanel(CSRController.csrBorderScene, Commons.getFxmlPanel("SalepanelFxml"));
		} else if (event.getSource() == btnNew) {
			comboCustomer.setValue("");
			productsItems.clear();
			loadPrerequisitions();
			entryList.clear();
			generateOneRow();
			calculateBillTotal();
			formEntryPanel();
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
		} else if (event.getSource() == btnSearchBill) {
			setBillData();
			regenrateDetailTable();
			formEntryPanel();
			calculateBillTotal();
		} else if (event.getSource() == btnDeleteBill) {
			deleteSaleBill();
			loadPrerequisitions();
			entryList.clear();
			generateOneRow();
			calculateBillTotal();
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
		productsList = ProductDAO.selectAll();
		for (ProductModel modal : productsList) {
			String productId = modal.getProdIdProperty().asString().get();
			String productName = modal.getProdNameProperty().get();

			productsItems.add(String.join(" ", productId, productName));
		}
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

	private void generateOneRow() {
		int size = entryList.size();
		BillEntryModal modal = new BillEntryModal();
		modal.setLineNo(size + 1);
		entryList.add(modal);
	}

	private void calculateBillTotal() {
		Double total = 0.00;
		for (BillEntryModal modal:entryList) {

			total += modal.getLinetotal();

		}

		Double exRate = Double.parseDouble(labelExRate.getText());

		labelBillTotal.setText(formatNumber.format(total));

		labelUSDAmount.setText(formatPrice.format(total * exRate));
	}

	private boolean checkFlag() {
		boolean isSuccess = false;

		// Empty Customer field

		if (!comboCustomer.getValue().equalsIgnoreCase("")) {
			if (!txtBillDate.getText().equalsIgnoreCase("")) {
				if (!comboPayTerm.getValue().equalsIgnoreCase("")) {
					for (BillEntryModal model : entryList) {
						if (!"".equalsIgnoreCase(model.getItems())) {
							if (model.getQuantity() != 0 && model.getUnitprice() != 0) {
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
		for (BillEntryModal modal : entryList) {
			int billID = Integer.parseInt(txtCode.getText());
			isSuccess = SaleBillDAO.insertBillDetail(modal, billID);
			if (!isSuccess)
				break;
		}
		return isSuccess;
	}

	private void regenrateDetailTable() {
		int billID = Integer.parseInt(txtCode.getText());
		entryList = SaleBillDAO.retrieveBillEntryData(billID);
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

	public HBox createFormHeader() {
		HBox header = new HBox(Constants.globalSpacing);
		Label lineNoLabel = createLabel(Dictionary.LineNumber.getValue(), 60);
		Label productLabel = createLabel(Dictionary.Product.getValue(), 250);
		Label unitLabel = createLabel(Dictionary.UnitMeasure.getValue(), 100);
		Label quanityLabel = createLabel(Dictionary.Quantity.getValue(), 150);
		Label unitPriceLabel = createLabel(Dictionary.UnitPrice.getValue(), 150);
		Label lineTotalLabel = createLabel(Dictionary.LineTotal.getValue(), 150);

		HBox.setHgrow(header, Priority.ALWAYS);
		header.getChildren().clear();
		header.getChildren().addAll(lineNoLabel, productLabel, unitLabel, quanityLabel, unitPriceLabel, lineTotalLabel);
		return header;
	}

	public VBox createform() {
		VBox formEntry = new VBox(5);
		VBox.setVgrow(formEntry, Priority.ALWAYS);
		List<HBox> entryLines = new ArrayList<HBox>();
		for (BillEntryModal items : entryList) {
			HBox entryLine = new HBox(Constants.globalSpacing);
			TextField txtLineNo = createTextField("", 60);
			txtLineNo.setEditable(false);
			txtLineNo.setText(items.lineNoProperty().asString().get());
			ComboBox<String> comboProduct = createComboBox(250);
			comboProduct.setEditable(true);
			comboProduct.getItems().addAll(productsItems);
			TextFields.bindAutoCompletion(comboProduct.getEditor(), productsItems);
			TextField txtUnitMeasure = createTextField("", 100);
			txtUnitMeasure.setText(items.unitProperty().get());
			
			comboProduct.setValue(items.itemsProperty().get());
			comboProduct.setOnAction(event -> {
				int index = comboProduct.getSelectionModel().getSelectedIndex();
				ProductModel productModal = productsList.get(index);
				items.setProdCode(productModal.getProdId());
				items.setItems(productModal.getProdName());
				items.setUnit(productModal.getProdUm());
				txtUnitMeasure.setText(items.unitProperty().get());
			});
			txtUnitMeasure.setEditable(false);
			TextField txtQuantity = createTextField("", 150);
			txtQuantity.setText(formatNumber.format(items.quantityProperty().get()));
			TextField txtUnitPrice = createTextField("", 150);
			txtUnitPrice.setText(formatNumber.format(items.unitpriceProperty().get()));

			TextField txtLineTotal = createTextField("", 150);
			txtLineTotal.setText(formatNumber.format(items.linetotalProperty().get()));
			txtLineTotal.setEditable(false);
			numberOnlyTextField(txtQuantity);
		
			txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
				items.setQuantity(Double.parseDouble(newValue));
			});
			
			txtUnitPrice.textProperty().addListener((observable, oldValue, newValue) -> {
				items.setUnitprice(Double.parseDouble(newValue));
			});
			txtQuantity.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
			    @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue) {
			           if (!txtQuantity.getText().isEmpty()) {
			        	   double lineTotal = items.getQuantity() * items.getUnitprice();
			        	   txtLineTotal.setText(formatNumber.format(lineTotal));
			        	   checkForSaleQuantityinStock(items.getItems(), items.getQuantity());
			           }
			        }
			    }
			});
			txtUnitPrice.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
			    @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue) {
			           if (!txtUnitPrice.getText().isEmpty()) {
			        	   double lineTotal = items.getQuantity() * items.getUnitprice();
			        	   txtLineTotal.setText(formatNumber.format(lineTotal));
			        	  
			           }
			        }
			    }
			});
			txtLineTotal.textProperty().addListener((observable, oldValue, newValue) -> {
				items.setLinetotal(Double.parseDouble(newValue));
				calculateBillTotal();
				
			});
			
			Button addButton = createButton(0, "+");
			addButton.setOnAction(event-> {
				addNewRos();
			});

			Button removeLine = createButton(0, "-");
			removeLine.setOnAction(event -> {
				entryList.remove(items);
				calculateBillTotal();
				formEntryPanel();
			});
			entryLine.getChildren().clear();
			entryLine.getChildren().addAll(txtLineNo, comboProduct, txtUnitMeasure, txtQuantity, txtUnitPrice,
					txtLineTotal, addButton,removeLine);
			entryLines.add(entryLine);
		}
		formEntry.getChildren().clear();
		formEntry.getChildren().addAll(entryLines);
		calculateBillTotal();
		return formEntry;
	}

	private void addNewRos() {
		generateOneRow();
		createform();
		formEntryPanel();
		calculateBillTotal();
		
	}

	public void formEntryPanel() {
		VBox formPanel = new VBox();
		formPanel.setPadding(Constants.globalPadding);
		HBox formHeader = createFormHeader();
		VBox EntryForm = createform();
		formPanel.getChildren().clear();
		formPanel.getChildren().addAll(formHeader, EntryForm);
		entryFormPane.setContent(formPanel);
		VBox.setVgrow(entryFormPane, Priority.ALWAYS);
	}

	public void numberOnlyTextField(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("-?([1-9][0-9]*)?")) {
                	textField.setText(oldValue);
                }
            }
        });
	}
	
	public void calculateLineTotal(TextField textField,double doubleQuantity) {
		textField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        if (!newPropertyValue) {
		           if (!textField.getText().isEmpty()) {
		        	   
		        	  
		        	  
		           }
		        }
		    }
		});
	}

}
