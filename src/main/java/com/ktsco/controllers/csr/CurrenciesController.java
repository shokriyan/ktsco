package com.ktsco.controllers.csr;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.CurrencyEntryModel;
import com.ktsco.models.csr.CurrencyModel;
import com.ktsco.modelsdao.CurrencyDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class CurrenciesController implements Initializable {

	private static Logger log = LoggerFactory.getLogger(CurrenciesController.class);
	public static Stage currencyStage = new Stage();

	@FXML
	private Button btnClose, btnSave, btnDelete, btnModify, btnSearch, btnRefresh, btnCalculate, btnReset;

	@FXML
	private Label labelInfoMessage;

	@FXML
	private TextField txtDate, txtValueEntry, txtResult;

	@FXML
	private TableView<CurrencyEntryModel> tableEntry;

	@FXML
	private TableView<CurrencyModel> tableCurrencyList;

	@FXML
	private TableColumn<CurrencyEntryModel, String> colCurrencyEntry, colRateEntry;
	@FXML
	private TableColumn<CurrencyModel, String> colCurrency, colDate, colRate;
	@FXML
	private TableColumn<CurrencyModel, Integer> colCode;

	@FXML
	private MenuItem menuEdit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableEntry.setEditable(true);
		tableCurrencyList.setEditable(false);
		loadPrerequisitions();
	}

	private void loadPrerequisitions() {
		setTodayDate();
		populateTableEntry();
		populateCurrencyListTable();
	}
	

	@FXML
	private void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			if (currencyStage.isShowing()) {
				currencyStage.hide();
				log.debug("Closing Currency Panel");
			}
		} else if (event.getSource() == btnSearch) {
			log.debug("Search Button Clicked");
			populateCurrencyListTable();
			tableCurrencyList.setEditable(false);
		} else if (event.getSource() == btnRefresh) {
			log.debug("Referesh Button Clicked");
			txtDate.clear();
			loadPrerequisitions();
			tableCurrencyList.setEditable(false);
		} else if (event.getSource() == btnSave) {
			log.debug("Save Button Clicked");
			if (AlertsUtils.askForSaveItems()) {
				saveCurrencyRecors();
			}
		} else if (event.getSource() == btnDelete) {
			log.debug("Delete Button Clicked");
			deleteCurrencyRecord();
			loadPrerequisitions();
		} else if (event.getSource() == btnCalculate) {
			log.debug("Calulate Button Clicked");
			calculateRate();
		} else if (event.getSource() == btnReset) {
			txtValueEntry.clear();
			txtResult.clear();
		}else if (event.getSource() == menuEdit) {
			log.debug("Edit Menu Button Clicked");
			tableCurrencyList.setEditable(true);
		}
	}

	@FXML
	private void onRateEntryEdit(CellEditEvent<CurrencyEntryModel, String> editCell) {
		CurrencyEntryModel model = tableEntry.getSelectionModel().getSelectedItem();
		model.setRate(editCell.getNewValue());
	}

	@FXML
	private void onRateChangeEdit(CellEditEvent<CurrencyModel, String> editCell) {
		CurrencyModel model = tableCurrencyList.getSelectionModel().getSelectedItem();
		model.setRate(editCell.getNewValue());
		int id = model.getIDNumber();
		String inputRate = model.getRate();
		String currency = model.getCurrency();
		String date = model.getEntryDate();
		if (AlertsUtils.askForUpdateAlert(currency + "\n" + date)) {
			boolean isSuccess = CurrencyDAO.updateCurrencyRecord(id, inputRate);
			Commons.processMessageLabel(labelInfoMessage, isSuccess);
		}
	}

	private void generateEntryTable(ObservableList<CurrencyEntryModel> list) {
		colCurrencyEntry.setCellValueFactory(cellData -> cellData.getValue().getCurrencyProperty());
		colRateEntry.setCellValueFactory(cellData -> cellData.getValue().getRateProperty());
		colRateEntry.setCellFactory(TextFieldTableCell.forTableColumn());
		tableEntry.setItems(list);
	}

	private void generateListTable(ObservableList<CurrencyModel> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().getIdNumberProperty().asObject());
		colCurrency.setCellValueFactory(cellData -> cellData.getValue().getCurrencyProperty());
		colDate.setCellValueFactory(cellData -> cellData.getValue().getEntryDateProperty());
		colRate.setCellValueFactory(cellData -> cellData.getValue().getRateProperty());
		colRate.setCellFactory(TextFieldTableCell.forTableColumn());
		tableCurrencyList.setItems(list);
	}

	private void populateTableEntry() {
		List<String> currencies = Commons.getListValuesFromMap(Constants.currencies);
		ObservableList<CurrencyEntryModel> list = FXCollections.observableArrayList();
		for (String currency : currencies) {
			CurrencyEntryModel model = new CurrencyEntryModel(currency, "0");
			list.add(model);
		}

		generateEntryTable(list);
	}

	private void populateCurrencyListTable() {
		String date = txtDate.getText();
		ObservableList<CurrencyModel> list = CurrencyDAO.retrieveCurrencyRecords(date);
		generateListTable(list);
	}

	private void saveCurrencyRecors() {
		String inputDate = txtDate.getText();
		boolean isSuccess = false;
		if (!"".equalsIgnoreCase(inputDate)) {
			if (!CurrencyDAO.checkExistingDate(inputDate)) {
				ObservableList<CurrencyEntryModel> list = tableEntry.getItems();
				boolean isZero = false;
				for (CurrencyEntryModel model : list) {
					if ("0".equalsIgnoreCase(model.getRate())) {
						isZero = true;
						break;
					}
				}
				if (!isZero) {
					for (CurrencyEntryModel model : list) {
						String currency = model.getCurrency();
						String rate = model.getRate();

						isSuccess = CurrencyDAO.insertCurrencyRecords(currency, inputDate, rate);
					}
					if (isSuccess) {
						loadPrerequisitions();
						txtDate.clear();
					}
				} else {
					AlertsUtils.zeroEntryAlert();
				}
				Commons.processMessageLabel(labelInfoMessage, isSuccess);
			} else {
				AlertsUtils.repeatItemAlerts(inputDate);
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void deleteCurrencyRecord() {
		if (!tableCurrencyList.getSelectionModel().isEmpty()) {
			CurrencyModel model = tableCurrencyList.getSelectionModel().getSelectedItem();
			int id = model.getIDNumber();
			String currency = model.getCurrency();
			String date = model.getEntryDate();
			boolean isSuccess = false;
			if (AlertsUtils.askForDeleteAlert(currency + " \n" + date)) {
				isSuccess = CurrencyDAO.deleteCurrencyRecord(id);
			}
			Commons.processMessageLabel(labelInfoMessage, isSuccess);
		} else {
			AlertsUtils.selectFromListAlert();
		}
	}

	private void calculateRate() {
		try {
			txtResult.setStyle("-fx-text-fill : black");
			double value = Double.parseDouble(txtValueEntry.getText());
			BigDecimal bg1, bg2, bg3;
			bg1 = new BigDecimal(1);
			bg2 = new BigDecimal(value);
			bg3 = bg1.divide(bg2, 9, RoundingMode.HALF_UP);
			txtResult.setText(String.valueOf(bg3));
		} catch (NumberFormatException e) {
			txtResult.setStyle("-fx-text-fill : Red");
			txtResult.setText("عدد وارد کنید");
		}
	}
	
	private void setTodayDate() {
		String todayDate = DateUtils.convertGregoryToJalali(Commons.getTodaysDate());
		txtDate.setText(todayDate);
	}

}
