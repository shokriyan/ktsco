package com.ktsco.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.modelsdao.UsersDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DatabaseUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class CreateNewUserController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(CreateNewUserController.class);

	@FXML
	private Text topViewLabel;
	@FXML
	private TextField txtUserName;
	@FXML
	private TextField txtPassword;
	@FXML
	private Label lblErrorMsg;
	@FXML
	private ComboBox<String> comboAccessType;

	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnCancel;

	@FXML
	public void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnCancel) {
			log.info("Closing the Window");
			UsersController.usersStage.getOnHidden()
					.handle(new WindowEvent(UsersController.usersStage, WindowEvent.WINDOW_HIDDEN));
			UsersController.usersStage.close();
		} else if (event.getSource() == btnSubmit) {
			addNewUser();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		topViewLabel.setText(Constants.buttonText);
		Commons.populateAccessCombo(comboAccessType);
	}

	private void addNewUser() {
		txtUserName.setOnKeyReleased(event -> lblErrorMsg.setText(null));
		txtPassword.setOnKeyReleased(event -> lblErrorMsg.setText(null));
		
		if (!txtPassword.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
			String username = txtUserName.getText();
			String password = txtPassword.getText();
			String accessType = Commons.getAccessType(comboAccessType.getValue());
			
			boolean isExist = checkingForExistingUsername(username);
			if (!isExist) {
			
			log.info("Executing the the query");
			UsersDAO.createNewUser(username, password, accessType);
			txtUserName.clear();
			txtPassword.clear();
			}else {
				log.info("Username is Exist {}" + username);
				AlertsUtils.repeatItemAlerts(username);
			}

		} else {
			log.info("Username, Password or Access type can't be empty ");
			lblErrorMsg.setText("هیچ کدام از مشخصات نمیتوانند خالی باشند");
		}

	}
	
	private boolean checkingForExistingUsername(String newUsername) {
		boolean result = false; 
		String query = "select username from users";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				if (resultSet.getString(1).equalsIgnoreCase(newUsername))
					result = true;
			}
		}catch (SQLException e) {
			result = false;
		}
		
		return result; 
	}

}
