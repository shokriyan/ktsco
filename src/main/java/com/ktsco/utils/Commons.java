package com.ktsco.utils;

import java.util.List;
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

public class Commons {

	private static final Logger log = LoggerFactory.getLogger(Commons.class);
	private static ViewClass view = new ViewClass();

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
		button.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		button.setAlignment(Pos.CENTER_LEFT);
		button.setPrefWidth(198);
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
		borderPane.setTop(view.setVboxFxml(Constants.topViewFxml));
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
			}else {
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

}
