
package com.ktsco.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.enums.Dictionary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Commons {

	private static final Logger log = LoggerFactory.getLogger(Commons.class);
	private static ViewClass view = new ViewClass();
	private static DecimalFormat df = new DecimalFormat("#.##");
	private static DecimalFormat df2 = new DecimalFormat("#.##########");

	/**
	 * This function will return the key of access type based on its value <br>
	 * key will store in database and value with show on UI
	 * 
	 * @param cmbAccess
	 * @return String access Key
	 */
	public static String getAccessType(String cmbAccess) {
		String returnKey = null;

		Set<String> accesskeys = Constants.accessValue.keySet();

		for (String keys : accesskeys) {
			if (Constants.accessValue.get(keys).equalsIgnoreCase(cmbAccess)) {

				returnKey = keys;
				log.info("return access key {}" + returnKey);
				break;
			} else
				returnKey = null;
		}

		return returnKey;
	}

	/**
	 * Function to Create button for menues
	 * 
	 * @param label
	 * @return Button
	 */
	public static Button addMenuButton(String label) {

		Button button = new Button();
		button.setText(label);
		button.setFont(Font.font("Tahome"));
		button.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		button.setAlignment(Pos.CENTER_LEFT);
		button.setPrefWidth(198);
		button.setPrefHeight(30);
		button.setStyle("-fx-border-color: #B0C4DE");
		return button;

	}

	/**
	 * This method with table Combobox for access type <br>
	 * and populate the items for UI
	 * 
	 * @param cmbAccess
	 */

	public static void populateAccessCombo(ComboBox<String> cmbAccess) {
		Set<String> accessKey = Constants.accessValue.keySet();
		ObservableList<String> comboList = FXCollections.observableArrayList();
		for (String keys : accessKey) {
			String accessValue = Constants.accessValue.get(keys);
			comboList.add(accessValue);
		}

		cmbAccess.setItems(comboList);
		log.info("Populate the Items with {}" + comboList);
		cmbAccess.setValue(Constants.accessValue.get("csr"));
	}

	/**
	 * Reloading Top View for labels
	 * 
	 * @param borderPane
	 * @param button
	 */

	public static void reloadTopView(BorderPane borderPane, Button button) {
		Constants.setDisplayPanelName(button.getText());
		borderPane.setTop(view.setVboxFxml(Commons.getFxmlPanel("topViewFxml")));
	}

	public static void populateAllComboBox(ComboBox<String> combo, List<String> items) {
		ObservableList<String> list = FXCollections.observableArrayList();
		list.add("");

		for (int i = 0; i < items.size(); i++) {
			list.add(items.get(i));
		}

		combo.setItems(list);

	}

	/**
	 * get check box selected value
	 * 
	 * @param checkbox
	 * @return if true return 1 if false return 0
	 */
	public static int getCheckBoxValue(CheckBox checkbox) {
		if (checkbox.isSelected())
			return 1;
		else
			return 0;
	}

	public static void processMessageLabel(Label label, boolean response) {
		Service<Void> service = new ProcessService();
		if (!service.isRunning()) {
			service.start();
			if (response) {
				label.setStyle("-fx-text-fill : Green");
				label.setText("با موفقیت انجام شد");
				label.setVisible(true);
				service.setOnSucceeded(event -> {
					label.setVisible(false);
					service.reset();
				});
			} else {
				label.setStyle("-fx-text-fill : Red");
				label.setText("خطا در پروسس اطلاعات");
				label.setVisible(true);
				service.setOnSucceeded(event -> {
					label.setVisible(false);
					service.reset();
				});
			}
		}

	}

	public static void processToastMessage(boolean response) {
		Service<Void> service = new ProcessService();
		if (!service.isRunning()) {
			service.start();
			if (response) {
				Alert successToast = AlertsUtils.toastAlert("", Dictionary.SuccessMessage.getValue());
				successToast.setResult(ButtonType.OK);
				successToast.show();
				service.setOnSucceeded(event -> {
					successToast.setResult(ButtonType.CLOSE);
					service.reset();
				});
			} 
		}

	}
	
	public static void setErrorMessage(Label label) {
		label.setStyle("-fx-text-fill : Red");
		label.setText("تلاش ناموفق");
		label.setVisible(true);
	}

	/**
	 * Login Message for Database Closing
	 * 
	 * @param message
	 * @return
	 */
	public static String dbClosingLog(String message) {
		String logMessage = "Error at Closing ResultSet or PreparedStatement with error message" + message;
		return logMessage;
	}

	/**
	 * Log Message for Executing
	 * 
	 * @param query
	 * @param message
	 * @return
	 */
	public static String dbExcutionLog(String query, String message) {
		String logMessage = "Error at Executing query " + query + " With Error Message " + message;
		return logMessage;
	}

	public static double setDoubleFormat(double reqQty) {
		return Double.parseDouble(df.format(reqQty));
	}

	public static double setCurrencyFormat(double reqQty) {
		return Double.parseDouble(df2.format(reqQty));
	}

	public Properties loadPropertyFile(String filePath) {
		Properties properties = new Properties();
		try {

			InputStream fileInputStream = Commons.class.getResourceAsStream(filePath);

			properties.load(fileInputStream);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return properties;
	}

	public static String getConfigurationPropertyValue(String key) {
		String output = "";
		Commons commons = new Commons();
		Properties properties = commons.loadPropertyFile(Constants.configFilePath);
		output = String.valueOf(properties.get(key));
		log.info("Property output is ::::" + output);
		return output;
	}

	public static void updateConfigurationPropertyValue(String newValue, String key) {
		Commons commons = new Commons();
		Properties properties = commons.loadPropertyFile(Constants.configFilePath);
		try {

			URL resourceUrl = Commons.class.getResource(Constants.configFilePath);
			File file = new File(resourceUrl.toURI());
			FileOutputStream output = new FileOutputStream(file);
			properties.setProperty(key, newValue);
			properties.store(output, null);
			output.close();
		} catch (IOException | URISyntaxException e) {
			log.error(e.getMessage());
		}
	}

	public static String getFxmlPanel(String key) {
		String output = "";
		Commons commons = new Commons();
		Properties properties = commons.loadPropertyFile(Constants.fxmlsPropFilePath);
		output = String.valueOf(properties.get(key));
		log.info("FXML output is ::::" + output);
		return output;
	}

	public static String getComboValue(ComboBox<String> combo) {
		String value = "";
		if (!"".equalsIgnoreCase(combo.getValue()))
			value = combo.getValue();

		return value;
	}

	public static String checkAndConvertNumbers(String input) {
		String newValue = "";

		try {
			double convertedQty = Commons.setDoubleFormat(Double.parseDouble(input));
			newValue = String.valueOf(convertedQty);
		} catch (NumberFormatException e) {
			AlertsUtils.numberEntryFormatErrorAlerts();
		}
		return newValue;
	}

	public static Stage openPanelsUndecorate(String fxml) {
		ViewClass view = new ViewClass();
		log.info("Loading FXML to penel {}", fxml);
		VBox scene = view.setVboxFxml(fxml);
		log.info("Loading stage and show");
		return view.setSceneAndShowWaitStage(scene, "", false);
	}

	public static List<String> getListValuesFromMap(Map<String, String> map) {

		List<String> list = new ArrayList<String>();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			list.add(map.get(key));
		}

		return list;

	}

	public static ObservableList<String> getObservableValuesFromMap(Map<String, String> map) {

		ObservableList<String> list = FXCollections.observableArrayList();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			list.add(map.get(key));
		}

		return list;

	}

	public static String getCurrencyKey(String lookupValue) {
		String currency = "";
		if (!Constants.currencies.isEmpty()) {
			Set<String> keys = Constants.currencies.keySet();
			for (String key : keys) {
				String value = Constants.currencies.get(key);
				if (lookupValue.equalsIgnoreCase(value)) {
					currency = key;
					break;
				}
			}
			if ("".equalsIgnoreCase(currency)) {
				log.error("Can't find the match " + lookupValue);
			}
		} else {
			log.error("Currency Map is Empty ");
		}

		return currency;
	}

	public static String getCurrencyValue(String lookupKey) {
		String currency = "";
		if (!Constants.currencies.isEmpty()) {
			Set<String> keys = Constants.currencies.keySet();
			for (String key : keys) {

				if (lookupKey.equalsIgnoreCase(key)) {
					currency = Constants.currencies.get(key);
					;
					break;
				}
			}
			if ("".equalsIgnoreCase(currency)) {
				log.info("Can't find the match " + lookupKey);
			}
		} else {
			log.error("Currency Map is Empty ");
		}

		return currency;
	}

	public static String getTodaysDate() {
		String todayDate = null;
		LocalDate now = LocalDate.now();
		todayDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);

		return todayDate;
	}

	public static void setCenterPanel(BorderPane borderPane, String fxml) {
		ViewClass views = new ViewClass();
		Pane centerPane = views.setPane(fxml);
		borderPane.setCenter(centerPane);
		log.info("Pane Size ::: " + centerPane.getHeight()  + " - " + centerPane.getWidth());
	}

	public static Integer getPayTermValue(String lookupValue) {
		int payTerm = 0;
		payTerm = Integer.parseInt(lookupValue.split("-")[0].trim());
		return payTerm;
	}

	public static double calculateLineTotal(double quantity, double unitprice) {
		double result = 0;
		try {
			result = quantity * unitprice;
		} catch (NumberFormatException e) {
			AlertsUtils.numberEntryFormatErrorAlerts();
		}
		return result;

	}

	public static String calculateDueDate(String inputDate, int payTerm) {
		String calculatedDueDate = "";
		Calendar c = Calendar.getInstance();
		Date dueDate;
		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
		;
		switch (payTerm) {
		case 0:
			calculatedDueDate = inputDate;
			break;
		case 7:
			c.add(Calendar.DAY_OF_MONTH, 7);
			dueDate = c.getTime();
			calculatedDueDate = dmyFormat.format(dueDate);
			break;
		case 30:
			c.add(Calendar.MONTH, 1);
			dueDate = c.getTime();
			calculatedDueDate = dmyFormat.format(dueDate);
			break;

		case 60:
			c.add(Calendar.MONTH, 2);
			dueDate = c.getTime();
			calculatedDueDate = dmyFormat.format(dueDate);
			break;
		default:
			throw new RuntimeException("Wrong Entry");

		}

		return calculatedDueDate;

	}

	public static long calucateDayBtwDates(String startDate, String endDate) {
		long days = 0;
		LocalDate from = LocalDate.parse(startDate);
		LocalDate to = LocalDate.parse(endDate);
		days = ChronoUnit.DAYS.between(from, to);
		return days;
	}

	public static void copyToClipboard(Object text) {
		String textToCopy = String.valueOf(text);
		StringSelection stringSelection = new StringSelection(textToCopy);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public static int getDepositType(String lookupValue) {
		int size = Constants.depositTypeList.size();
		int depType = 1;
		for (int i = 0; i < size; i++) {
			if (Constants.depositTypeList.get(i).equalsIgnoreCase(lookupValue))
				depType = i;
			else
				log.info("can't find a match " + lookupValue);
		}

		return depType;
	}

	public static boolean isTextFieldHasValue(TextField textField) {
		boolean hasValue = false;
		if (textField != null) {
			if (!textField.getText().equalsIgnoreCase(""))
				hasValue = true;
			else
				hasValue = false;
		}

		return hasValue;
	}

	public static boolean isPassEmptyVerification(boolean isPass, TextField textField) {
		isPass = (isPass || isTextFieldHasValue(textField)) ? true : false;
		return isPass;
	}

	public static String accessSymployes(int access) {
		String symbole = null;
		if (access == 0)
			symbole = "✗";
		else if (access == 1)
			symbole = "✓";
		else
			symbole = null;
		return symbole;
	}

	public static boolean changeSymboleToBoolean(String symbole) {
		return (symbole.equals("✗")) ? false : true;
	}

	public static boolean getAccessVerification(Object object) {
		int tinyint = Integer.parseInt(object.toString());
		return (tinyint == 0) ? false : true;
	}
	
	public static void amountLabelFormation(Label label, double amount) {
		if (amount >= 0 )
			label.setTextFill(Color.BLACK);
		else
			label.setTextFill(Color.RED);
			
	}
	
	public static void amountTextFieldFormation(TextField textField, double amount) {
		if (amount >= 0 )
			textField.setStyle("-fx-text-inner-color: black;");
		else
			textField.setStyle("-fx-text-inner-color: red;");
			
	}

}
