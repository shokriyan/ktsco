package com.ktsco.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.MainApp;
import com.ktsco.utils.Constants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TopViewController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(TopViewController.class);

	@FXML
	private Label lblWelcomeMsg;
	@FXML
	private Button btnSignOut;
	@FXML
	private Text textPanelName;
	@FXML
	public Label lblPanelName;
	@FXML
	public Text txtCompanyName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String loggedUser = Constants.getLoggedUser();
		lblWelcomeMsg.setText("Welcome:  " + loggedUser);
		textPanelName.setText(Constants.getPanelName());
		txtCompanyName.setText(Constants.companyName);
		lblPanelName.setText(Constants.getDisplayPanelName());

	}

	@FXML
	public void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnSignOut) {
			if (AdminController.adminStage != null && AdminController.adminStage.isShowing()) {
				Constants.setDisplayPanelName(null);
				log.info("Closing Admin Panel");
				AdminController.adminStage.close();
				MainApp.loginStage.show();

			} else if (FactoryController.factoryStage != null && FactoryController.factoryStage.isShowing()) {
				Constants.setDisplayPanelName(null);
				log.info("Closing CSR Panel");
				FactoryController.factoryStage.close();
				MainApp.loginStage.show();
			} else if (CSRController.csrStage != null && CSRController.csrStage.isShowing()) {
				Constants.setDisplayPanelName(null);
				CSRController.csrStage.close();
				MainApp.loginStage.show();

			} else {
				log.info("Admin Panel is not Showing at this time");
			}
		}
	}
}
