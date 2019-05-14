package com.ktsco.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.modelsdao.UsersDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Constants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class DeleteUsersController implements Initializable{
	
private final Logger log = LoggerFactory.getLogger(EditUsersController.class);
	
	@FXML
	private Text topViewLabel;
	@FXML
	private ComboBox<String> comboUserName;
	@FXML
	private TextField txtPassword;
	@FXML
	private Label lblErrorMsg;
	@FXML
	private TextField txtAccessType;

	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnCancel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		topViewLabel.setText(Constants.buttonText);
		UsersDAO.populateUsersList(comboUserName);
	}
	
	public void allButtonsAction(ActionEvent event ) {
		if (event.getSource() == btnCancel) {
			log.info("Closing the Window");
			UsersController.usersStage.getOnHidden()
					.handle(new WindowEvent(UsersController.usersStage, WindowEvent.WINDOW_HIDDEN));
			UsersController.usersStage.close();
		}else if (event.getSource() == btnSubmit) {
			submitToDelete();
		}
	}
	
	public void submitToDelete() {
		int userId = UsersDAO.getUserIDFromComboValue(comboUserName);
		lblErrorMsg.setText("کاربر انتخاب شده حذف شد " + " " + comboUserName.getValue());
		UsersDAO.deleteSelectedUser(userId);
		txtPassword.clear();
		txtAccessType.clear();
		UsersDAO.populateUsersList(comboUserName);
	
	}
	
	public void onUserComboValueChange() {
		int userid = UsersDAO.getUserIDFromComboValue(comboUserName);
		String password = null; 
		String accessType = null;
		
		ResultSet result = UsersDAO.getPasswordAndAccessByUserID(userid);
		try {
			while (result.next()) {
				password = result.getString("password");
				accessType = result.getString("access");
			}
			
		}catch(SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}
		
		txtPassword.setText(password);
		txtAccessType.setText(Constants.accessValue.get(accessType));
		
		
	}
	
}
