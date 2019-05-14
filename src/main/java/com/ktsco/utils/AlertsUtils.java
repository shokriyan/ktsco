package com.ktsco.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertsUtils {
	private static final Logger log = LoggerFactory.getLogger(AlertsUtils.class);

	/**
	 * Create Custom Alert for Errors Mostly use for SQL errors
	 * 
	 * @param alertTitle
	 * @param alertMsg
	 */
	public static void ErrorAlert(String alertTitle, String alertMsg) {
		Alert alert = new Alert(AlertType.ERROR);
		log.info("Create Error Type alert with masage " + alertMsg);
		alert.setTitle(alertTitle);
		alert.setContentText(alertMsg);
		alert.show();

	}

	/**
	 * Create Custom alert for warnings.
	 * 
	 * @param alertTitle
	 * @param alertMsg
	 */
	public static void warningAlert(String alertTitle, String alertMsg) {
		Alert alert = new Alert(AlertType.WARNING);
		log.info("Create Warning Type alert with masage " + alertMsg);
		alert.setTitle(alertTitle);
		alert.setContentText(alertMsg);
		alert.show();

	}

	public static boolean ResposeAlert(String alertTitle, String alertMsg) {
		boolean response = false;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		log.debug("Alert response created {} , {}", alertTitle, alertMsg);
		alert.setTitle(alertTitle);
		alert.setContentText(alertMsg);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			response = true;
		} else if (result.get() == ButtonType.CANCEL) {
			response = false;
		}
		return response;
	}

}
