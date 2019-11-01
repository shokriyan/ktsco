package com.ktsco.controllers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.MainApp;
import com.ktsco.controllers.admin.AdminController;
import com.ktsco.controllers.csr.CSRController;
import com.ktsco.controllers.factory.FactoryController;
import com.ktsco.controllers.mgmnt.MgmntController;
import com.ktsco.modelsdao.UsersDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
		generateDepositTypes();
		
		textFieldChangeListener(txtUsername);
		textFieldChangeListener(txtPassword); 
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

	private void generateDepositTypes() {
		Constants.depositTypeList.add("دریافت بانکی");
		Constants.depositTypeList.add("دریافت نقدی");
	}

	private void generatePayTerms() {
		Constants.payTerms.add(0 + " - " + "نقد");
		Constants.payTerms.add(7 + " - " + "یک هفته");
		Constants.payTerms.add(30 + " - " + "یک ماه");
		Constants.payTerms.add(60 + " - " + "دو ماه");
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
	
	@FXML
	private void onComboChange(ActionEvent event) {
		lblErrorMsg.setText("");
		lblErrorMsg.setVisible(false);
	}

	/**
	 * All Buttons Function for this controller should be in this method.
	 * 
	 * @param event
	 */
	@FXML
	private void onButtonClick(ActionEvent event) {

		if (event.getSource() == btnExit) {
			System.exit(0);
		} else if (event.getSource() == btnSubmit) {
			String username = txtUsername.getText();
			String password = txtPassword.getText();
			String requestedPanel = Commons.getAccessType(cmbAccess.getValue());
			loginByUserName(username, password, requestedPanel);
		}

	}

	private void createUnitMeasureList() {
		String[] list = { "بندل", "کیلو", "تن", "عدد", "متر", "دستگاه" };

		for (int i = 0; i < list.length; i++) {
			Constants.unitMeasureList.add(list[i]);
		}

	}
	
	private void clearFields() {
		txtUsername.clear();
		txtPassword.clear();
	}
	private void loginByUserName(String username, String password, String requestedPanel) {
		if (checkEmptyField()) {
			if (isUserAdmin(username)) {
				if (isAdminPass(password, requestedPanel)) {
					openAdminPanel();
				}
			} else {
				Map<String, Object> userDetail = UsersDAO.getUserInfoByUsername(username);
				
				if (!userDetail.isEmpty()) {
					if (userDetail.get("password").equals(password)) {
						Constants.setLoggedUser(userDetail.get("fullname").toString());
						boolean adminAccess = Commons.getAccessVerification(userDetail.get("admin_access"));
						boolean csrAccess = Commons.getAccessVerification(userDetail.get("csr_access"));
						boolean factoryAccess = Commons.getAccessVerification(userDetail.get("factory_access"));
						boolean mgmtAccess = Commons.getAccessVerification(userDetail.get("mgmt_access"));

						if (requestedPanel.equalsIgnoreCase("admin") && adminAccess) {
							openAdminPanel();
						} else if (requestedPanel.equalsIgnoreCase("factory") && factoryAccess) {
							openFacotryPanel();
						} else if (requestedPanel.equalsIgnoreCase("csr") && csrAccess) {
							openCSRPanel();
						} else if (requestedPanel.equalsIgnoreCase("management") && mgmtAccess) {
							openMgmtPanel();
						} else {
							Commons.setErrorMessage(lblErrorMsg);
						}
					} else {
						Commons.setErrorMessage(lblErrorMsg);
					}
				} else {
					Commons.setErrorMessage(lblErrorMsg);
				}
			}
		}

	}

	private boolean isAdminPass(String password, String requestedPanel) {
		boolean isAdminPass = false;
		if (password.equals("admin")) {
			if (requestedPanel.equalsIgnoreCase("admin")) {
				isAdminPass = true;
			}else {
				Commons.setErrorMessage(lblErrorMsg);
			}
		}
		
		return isAdminPass;
	}

	private boolean isUserAdmin(String username) {

		return (username.equalsIgnoreCase("admin")) ? true : false;
	}

	private void openMgmtPanel() {
		if (MainApp.loginStage.isShowing()) {
			clearFields();
			Constants.setPanelName("پنل بخش مدیریت");
			MainApp.loginStage.hide();
			MgmntController.initialMgmntPanel();
		} else {
			log.debug("Login Window is Not Showing at this point");
		}
	}

	private void openCSRPanel() {
		if (MainApp.loginStage.isShowing()) {
			clearFields();
			Constants.setPanelName("پنل بخش اداری");
			MainApp.loginStage.hide();
			CSRController.initialCSRPanel();
		} else {
			log.debug("Login Window is Not Showing at this point");
		}
	}

	private void openFacotryPanel() {
		if (MainApp.loginStage.isShowing()) {
			clearFields();
			Constants.setPanelName("پنل بخش کارخانه");
			MainApp.loginStage.hide();
			FactoryController.initialFactoryPanel();
		} else {
			log.debug("Login Window is Not Showing at this point");
		}
	}

	private void openAdminPanel() {
		if (MainApp.loginStage.isShowing()) {
			clearFields();
			Constants.setPanelName("پنل مدیریت تنظیمات برنامه");
			MainApp.loginStage.hide();
			AdminController.initialAdminPanel();
		} else {
			log.debug("Login Window is Not Showing at this point");
		}
	}

	private boolean checkEmptyField() {
		boolean isPass = false;
		if (Commons.isTextFieldHasValue(txtUsername)) {
			if (Commons.isTextFieldHasValue(txtPassword)) {
				isPass = true;
			} else {
				Commons.setErrorMessage(lblErrorMsg);
			}
		} else {
			Commons.setErrorMessage(lblErrorMsg);
		}
		return isPass;
	}
	
	private void textFieldChangeListener(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				lblErrorMsg.setText("");
				lblErrorMsg.setVisible(false);
			}
		});
	}

}