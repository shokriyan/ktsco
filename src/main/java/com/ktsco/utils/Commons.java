package com.ktsco.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class Commons {

	private static final Logger log = LoggerFactory.getLogger(Commons.class);
	private static ViewClass view = new ViewClass();
	private static DecimalFormat df = new DecimalFormat("#.##");

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
				label.setText("خطا در پروسس اطلاعات انجام نشد");
				label.setVisible(true);
				service.setOnSucceeded(event -> {
					label.setVisible(false);
					service.reset();
				});
			}
		}

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
	
	public static void updateConfigurationPropertyValue(String newValue , String key) {
		Commons commons = new Commons();
		Properties properties = commons.loadPropertyFile(Constants.configFilePath);
		try {
			
			URL resourceUrl = Commons.class.getResource(Constants.configFilePath);
			File file = new File(resourceUrl.toURI());
			FileOutputStream output = new FileOutputStream(file);
			properties.setProperty(key, newValue);
			properties.store(output, null);
			output.close();
		}catch (IOException | URISyntaxException e) {
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
		}catch (NumberFormatException e) {
			AlertsUtils.numberEntryFormatErrorAlerts();
		}
		
		return newValue;
	}

}
