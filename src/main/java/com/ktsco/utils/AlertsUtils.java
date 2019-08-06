package com.ktsco.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class AlertsUtils {
	private static final Logger log = LoggerFactory.getLogger(AlertsUtils.class);
	
	private static String alertTitle; 
	private static String alertMessage; 

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
	
	/**
	 * Create Error Alert 
	 * @param title
	 * @param alertMessage
	 * @return
	 */
	private static Alert errorAlerts(String title, String alertMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(alertMessage);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.initModality(Modality.APPLICATION_MODAL);
		return alert;
	}
	
	/**
	 * Create Warning Alert
	 * @param title
	 * @param alertMessage
	 * @return
	 */
	
	private static Alert warningAlerts(String title, String alertMessage) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(alertMessage);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.initModality(Modality.APPLICATION_MODAL);
		return alert;
	}
	/**
	 * Create Info Alert
	 * @param alertTitle
	 * @param alertMessage
	 * @return
	 */
	
	private static Alert infoAlerts(String alertTitle, String alertMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(alertTitle);
		alert.setContentText(alertMessage);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.initModality(Modality.APPLICATION_MODAL);
		return alert;
	}
	/**
	 * Create Confirmation Alert.
	 * 
	 * @param alertTitle
	 * @param alertMessage
	 * @return
	 */
	private static Alert confirmAlerts(String alertTitle, String alertMessage) {
		Alert alert = new Alert (AlertType.CONFIRMATION);
		alert.setTitle(alertTitle);
		alert.setContentText(alertMessage);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.initModality(Modality.APPLICATION_MODAL);
		return alert;
	}
	/**
	 * Alerts for All Database Errors
	 */
	public static void databaseErrorAlert() {
		alertTitle = "Database Error";
		alertMessage = "خطا در ارتباط با دیتابیس	"; 
		
		Alert alert = errorAlerts(alertTitle, alertMessage);
		alert.show();
	}
	
	/**
	 * Alert for All Empty Field
	 */
	public static void emptyFieldAlert() {
		alertTitle = "Empty Field";
		alertMessage = "اطلاعات وارد کنید";
		Alert alert = warningAlerts(alertTitle, alertMessage);
		alert.show();
	}
	
	/**
	 * Alert for All Confirmation before actions
	 */
	public static boolean askForDeleteAlert(String object) {
		boolean response = false; 
		alertTitle = "Delete Items";
		alertMessage = "آیا میخواهید حذف شود" + "\n" + object;
		Alert alert = confirmAlerts(alertTitle, alertMessage);
		Optional<ButtonType> buttonKeys = alert.showAndWait();
		if (buttonKeys.get() == ButtonType.OK)
			response = true;
		else if (buttonKeys.get() == ButtonType.CANCEL)
			response = false; 
		
		return response;
	}
	
	public static boolean askForUpdateAlert (String object) {
		boolean response = false; 
		alertTitle = "Update Items";
		alertMessage = "ثبت تغییرات" + "\n" + object;
		Alert alert = confirmAlerts(alertTitle, alertMessage);
		Optional<ButtonType> buttonKeys = alert.showAndWait();
		if (buttonKeys.get() == ButtonType.OK)
			response = true;
		else if (buttonKeys.get() == ButtonType.CANCEL)
			response = false; 
		return response; 
	}
	
	/**
	 * Alert for Empty Select list
	 */
	public static void selectFromListAlert() {
		alertTitle = "Select Items";
		alertMessage = "لطفا از لیست انتخاب کنید";
		Alert alert = infoAlerts(alertTitle, alertMessage);
		alert.show(); 
		
	}
	
	/**
	 * Alerts for Number formats
	 */
	public static void numberEntryFormatErrorAlerts() {
		alertTitle = "Wrong Number Format";
		alertMessage = "عدد وارد کنید";
		Alert alert = errorAlerts(alertTitle, alertMessage);
		alert.show();
	}
	
	/**
	 * Alerts for repeated Items. 
	 * @param object
	 */
	public static void repeatItemAlerts(String object) {
		alertTitle = "Repeated Items";
		alertMessage = "آیتم مورد نظر موجود میباشد" + "\n" + object;
		Alert alert = errorAlerts(alertTitle, alertMessage);
		alert.show();
	}
	/**
	 * Alerts for Non Exist Items 
	 * @param object
	 */
	public static void nonExistItemsAlerts(String object) {
		alertTitle = "Not Exist";
		alertMessage = "آیتم موجود نمی باشد" + "\n" + object;
		Alert alert = warningAlerts(alertTitle, alertMessage);
		alert.show();
	}
	
	/**
	 * Alerts to Confirm for Save Items.
	 * @return
	 */
	public static boolean askForSaveItems () {
		boolean response = false; 
		alertTitle = "Update Items";
		alertMessage = "ثبت اطلاعات" + "\n";
		Alert alert = confirmAlerts(alertTitle, alertMessage);
		Optional<ButtonType> buttonKeys = alert.showAndWait();
		if (buttonKeys.get() == ButtonType.OK)
			response = true;
		else if (buttonKeys.get() == ButtonType.CANCEL)
			response = false; 
		return response; 
	}
	
	public static boolean askForReloadPage () {
		boolean response = false; 
		alertTitle = "Reloading Page";
		alertMessage = "بارگذاری مجدد در صورت تایید " +"\n"+ "اطلاعات ذخیره نشده پاک میشود";
		Alert alert = confirmAlerts(alertTitle, alertMessage);
		Optional<ButtonType> buttonKeys = alert.showAndWait();
		if (buttonKeys.get() == ButtonType.OK)
			response = true;
		else if (buttonKeys.get() == ButtonType.CANCEL)
			response = false; 
		return response; 
	}
	/**
	 * Date Wrong Entry Alerts 
	 * 
	 * @param expectedParameter Entry Expected Parameter
	 */
	public static void wrongDateEntryAlert(String expectedParameter) {
		alertTitle = "Wrong Date Entry";
		alertMessage = "اشتباه ورودی تاریخ" + "\n" + expectedParameter + "استفاده شود";
		Alert alert = errorAlerts(alertTitle, alertMessage);
		alert.show();
	}

	public static void SuccessfullyDoneAlrt() {
		
		alertTitle = "Success";
		alertMessage = "با موفقیت انجام شد";
		Alert alert = infoAlerts(alertTitle, alertMessage);
		alert.show();		
	}

	public static void zeroEntryAlert() {
		alertTitle = "Error";
		alertMessage = "صفر قابل قبول نیست";
		Alert alert = errorAlerts(alertTitle, alertMessage);
		alert.show();
	}
	public static void CurrencyEntryAlert(String currencyType) {
		alertTitle = "Error";
		alertMessage = "نرخ ارز برای تاریخ انتخاب شده موجود نمیباشد" + "\n" + currencyType;
		Alert alert = errorAlerts(alertTitle, alertMessage);
		alert.show();
		
	}
	
	public static void warningForStockRemain(String object, String quantity) {
		alertTitle = "Warning";
		alertMessage = "موجودی انبار کم است" + "\n" + object + "\n" + quantity;
		Alert alert = warningAlerts(alertTitle, alertMessage);
		alert.show();
	}
	
	

}
