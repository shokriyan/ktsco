package com.ktsco.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.MainApp;
import com.ktsco.controllers.admin.AdminController;
import com.ktsco.controllers.csr.CSRController;
import com.ktsco.controllers.factory.FactoryController;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtPassword;
	@FXML
	private ComboBox<String> cmbAccess = new ComboBox<String>();
	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnExit;
	@FXML
	private Label lblErrorMsg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Initiating Combo Box Values");
		populateComboItem();
		log.info("Initializing the List of Unit Measures");
		createUnitMeasureList();
		generateCurrencies();
		generatePayTerms();
	}

	/**
	 * Populating Combo box with it's Items.<br>
	 * Note: the items are prefix
	 */
	private void populateComboItem() {
		SetAccessItems("management", "مدیریت");
		SetAccessItems("csr", "اداری");
		SetAccessItems("factory", "کارخانه");
		SetAccessItems("admin", "ادمین");

		Commons.populateAccessCombo(cmbAccess);
	}
	
	private void generateCurrencies() {
		Constants.currencies.put("USD", "دالر");
		Constants.currencies.put("AFN", "افغانی");
		Constants.currencies.put("EUR", "یورو");
		Constants.currencies.put("PKR", "کلدار");
		Constants.currencies.put("TOM", "تومان");
	}
	
	private void generatePayTerms() {
		Constants.payTerms.add(0 + " - "+ "نقد");
		Constants.payTerms.add(7 + " - "+ "یک هفته");
		Constants.payTerms.add(30 + " - "+ "یک ماه");
		Constants.payTerms.add(60 + " - "+ "دو ماه");
	}

	/**
	 * This Method assign value for Global Access Map<br>
	 * 
	 * @param key
	 * @param value
	 */
	private static void SetAccessItems(String key, String value) {
		Constants.accessValue.put(key, value);
	}

	/**
	 * All Buttons Function for this controller should be in this method.
	 * 
	 * @param event
	 */

	public void onButtonClick(ActionEvent event) {

		if (event.getSource() == btnExit) {
			System.exit(0);
		} else if (event.getSource() == btnSubmit) {
			String accessType = Commons.getAccessType(cmbAccess.getValue());
			Constants.setLoggedUser(txtUsername.getText());

			log.info("Opennin panel for {}" + accessType);

			if (accessType.equalsIgnoreCase("admin")) {
				if (MainApp.loginStage.isShowing()) {
					Constants.setPanelName("پنل مدیریت تنظیمات برنامه");
					MainApp.loginStage.hide();
					AdminController.initialAdminPanel();
				} else {
					log.debug("Login Window is Not Showing at this point");
				}
			} else if (accessType.equalsIgnoreCase("factory")) {
				if (MainApp.loginStage.isShowing()) {
					Constants.setPanelName("پنل بخش کارخانه");
					MainApp.loginStage.hide();
					FactoryController.initialFactoryPanel();
				} else {
					log.debug("Login Window is Not Showing at this point");
				}
			} else if (accessType.equalsIgnoreCase("csr")) {
				if (MainApp.loginStage.isShowing()) {
					Constants.setPanelName("پنل بخش اداری");
					MainApp.loginStage.hide();
					CSRController.initialCSRPanel();
				} else {
					log.debug("Login Window is Not Showing at this point");
				}
			}
		}

	}

	private void createUnitMeasureList() {
		String[] list = { "بندل", "کیلو", "تن", "عدد", "متر",  "دستگاه" };

		for (int i = 0; i < list.length; i++) {
			Constants.unitMeasureList.add(list[i]);
		}

	}
	

}