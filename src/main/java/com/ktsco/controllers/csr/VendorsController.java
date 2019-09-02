package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.models.csr.CustomerModel;
import com.ktsco.modelsdao.VendorsDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VendorsController implements Initializable {

	@FXML
	private TextField txtCode, txtCompany, txtPOC, txtPhone, txtAddress;

	@FXML
	private Button btnClose, btnSave, btnDelete, btnModify, btnSearch, btnRefresh;
	@FXML
	private MenuItem menuEdit;

	@FXML
	private ComboBox<String> comboCurrency;

	@FXML
	private Label labelInfoMessage;

	@FXML
	private TableView<CustomerModel> tableCustomerList;
	@FXML
	private TableColumn<CustomerModel, Integer> colCode;
	@FXML
	private TableColumn<CustomerModel, String> colCompany, colPOC, colPhone, colAddress, colCurrency;

	public static Stage vendorStage = new Stage();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrerequisitions();
		tableCustomerList.setEditable(false);
	}

	@FXML
	private void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			if (vendorStage.isShowing()) {
				vendorStage.getOnHidden().handle(new WindowEvent(vendorStage, WindowEvent.WINDOW_HIDDEN));
				vendorStage.hide();
			}
		} else if (event.getSource() == btnSave) {
			if (AlertsUtils.askForSaveItems()) {
				addCustomerRecord();
			}
		} else if (event.getSource() == menuEdit) {
			tableCustomerList.setEditable(true);
		} else if (event.getSource() == btnModify) {
			if (AlertsUtils.askForUpdateAlert("مشتریان")) {
				updateCustomerRecords();
			}
		} else if (event.getSource() == btnDelete) {
			deleteCustomerRecord();
		} else if (event.getSource() == btnRefresh) {
			loadPrerequisitions();
			clearTextFields();
		} else if (event.getSource() == btnSearch) {
			searchCustomerRecord();
		}
	}

	@FXML
	private void onCompnayCellEdit(CellEditEvent<CustomerModel, String> editedCell) {
		CustomerModel model = tableCustomerList.getSelectionModel().getSelectedItem();
		model.setCompany(editedCell.getNewValue());
	}

	@FXML
	private void onPOCCellEdit(CellEditEvent<CustomerModel, String> editedCell) {
		CustomerModel model = tableCustomerList.getSelectionModel().getSelectedItem();
		model.setPOC(editedCell.getNewValue());
	}

	@FXML
	private void onPhoneCellEdit(CellEditEvent<CustomerModel, String> editedCell) {
		CustomerModel model = tableCustomerList.getSelectionModel().getSelectedItem();
		model.setPhone(editedCell.getNewValue());
	}

	@FXML
	private void onAddressCellEdit(CellEditEvent<CustomerModel, String> editedCell) {
		CustomerModel model = tableCustomerList.getSelectionModel().getSelectedItem();
		model.setAddress(editedCell.getNewValue());
	}

	@FXML
	private void onCurrencyCellEdit(CellEditEvent<CustomerModel, String> editedCell) {
		CustomerModel model = tableCustomerList.getSelectionModel().getSelectedItem();
		model.setCurrency(editedCell.getNewValue());
	}

	private void clearTextFields() {
		txtCompany.clear();
		txtPOC.clear();
		txtPhone.clear();
		txtAddress.clear();
		populateCurrencyCombo();
	}

	@FXML
	private void onTableClickAction() {

	}

	private void loadPrerequisitions() {
		stablishingCustomerCode();
		populateCustomerTable();
		populateCurrencyCombo();
	}

	private void generateTableColumns(ObservableList<CustomerModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty().asObject());
		colCompany.setCellValueFactory(cellData -> cellData.getValue().getCompanyProperty());
		colCompany.setCellFactory(TextFieldTableCell.forTableColumn());
		colPOC.setCellValueFactory(cellData -> cellData.getValue().getPOCProperty());
		colPOC.setCellFactory(TextFieldTableCell.forTableColumn());
		colPhone.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
		colPhone.setCellFactory(TextFieldTableCell.forTableColumn());
		colAddress.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
		colAddress.setCellFactory(TextFieldTableCell.forTableColumn());
		colCurrency.setCellValueFactory(cellData -> cellData.getValue().getCurrencyProperty());
		ObservableList<String> currencyItems = Commons.getObservableValuesFromMap(Constants.currencies);
		colCurrency.setCellFactory(ComboBoxTableCell.forTableColumn(currencyItems));

		tableCustomerList.setItems(list);
	}

	private void populateCustomerTable() {
		ObservableList<CustomerModel> list = FXCollections.observableArrayList();
		list = VendorsDAO.getAllVendorRecords();
		generateTableColumns(list);
	}

	private void stablishingCustomerCode() {
		int code = VendorsDAO.stablishVendorCode();
		txtCode.setText(String.valueOf(code));
	}

	private void populateCurrencyCombo() {
		List<String> curList = Commons.getListValuesFromMap(Constants.currencies);
		Commons.populateAllComboBox(comboCurrency, curList);
		comboCurrency.setValue("");
	}

	private void addCustomerRecord() {
		int code = Integer.parseInt(txtCode.getText());
		String company = txtCompany.getText().trim();
		String poc = txtPOC.getText().trim();
		String phone = txtPhone.getText().trim();
		String address = txtAddress.getText().trim();
		String currency = Commons.getCurrencyKey(comboCurrency.getValue()).trim();
		boolean isSuccess = false;
		if (code != 0 && !"".equalsIgnoreCase(company) && !"".equalsIgnoreCase(currency)) {
			boolean isExist = VendorsDAO.checkforDuplicateVendor(company, currency);
			if (!isExist) {
				isSuccess = VendorsDAO.insertVendorRecords(code, company, poc, phone, address, currency);
				clearTextFields();
			} else
				AlertsUtils.repeatItemAlerts(company);

		} else {
			AlertsUtils.emptyFieldAlert();
		}
		Commons.processMessageLabel(labelInfoMessage, isSuccess);
		loadPrerequisitions();
	}

	private void updateCustomerRecords() {
		if (!tableCustomerList.getItems().isEmpty()) {
			ObservableList<CustomerModel> list = tableCustomerList.getItems();
			for (CustomerModel model : list) {

				int code = model.getCode();
				String company = model.getCompany().trim();
				String poc = model.getPOC().trim();
				String phone = model.getPhone().trim();
				String address = model.getAddress().trim();
				String currency = Commons.getCurrencyKey(model.getCurrency()).trim();
				boolean isSuccess = false;
				if (code != 0 && !"".equalsIgnoreCase(company) && !"".equalsIgnoreCase(currency)) {

					isSuccess = VendorsDAO.insertVendorRecords(code, company, poc, phone, address, currency);
					clearTextFields();

				} else {
					AlertsUtils.emptyFieldAlert();
				}
				Commons.processMessageLabel(labelInfoMessage, isSuccess);
				loadPrerequisitions();
			}
		}
	}

	private void deleteCustomerRecord() {
		if (!tableCustomerList.getSelectionModel().isEmpty()) {
			CustomerModel model = tableCustomerList.getSelectionModel().getSelectedItem();
			int code = model.getCode();
			String company = model.getCompany();
			boolean isSuccess = false;
			if (AlertsUtils.askForDeleteAlert(company)) {
				isSuccess = VendorsDAO.deleteVendorRecord(code);
			}

			Commons.processMessageLabel(labelInfoMessage, isSuccess);
			loadPrerequisitions();
		}
	}

	private void searchCustomerRecord() {
		String company = txtCompany.getText();
		String poc = txtPOC.getText();
		String phone = txtPhone.getText();
		String address = txtAddress.getText();
		String currency = comboCurrency.getValue();

		ObservableList<CustomerModel> list = VendorsDAO.searchVendorRecord(company, poc, phone, address, currency);
		generateTableColumns(list);
	}

}
