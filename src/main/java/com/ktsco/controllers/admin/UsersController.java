package com.ktsco.controllers.admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.models.admin.UsersModels;
import com.ktsco.modelsdao.UsersDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UsersController implements Initializable {

	@FXML
	private Button btnSave, btnChangeAccess, btnReload, btnDelete;

	@FXML
	private Label labelInformation;

	@FXML
	private TextField txtFullName, txtUsername, txtPassword;
	@FXML
	private CheckBox chboxAdmin, chboxCsr, chboxFactory, chboxMgmt;

	@FXML
	private TableView<UsersModels> tableUserDetail;

	@FXML
	private TableColumn<UsersModels, Integer> colCode;

	@FXML
	private TableColumn<UsersModels, String> colFullName, colUsername, colPassword;

	@FXML
	private TableColumn<UsersModels, String> colAdmin, colFactory, colCsr, colMgmnt;
	@FXML
	private MenuItem menuEdit;

	private ObservableList<UsersModels> usersData = FXCollections.observableArrayList();
	int selectedUsedID = 0;
	String fullname = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnChangeAccess.setDisable(true);
		populateTableData();
	}

	@FXML
	private void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnSave) {
			String username = txtUsername.getText();
			saveUser(username);
			populateTableData();
		} else if (event.getSource() == btnReload) {
			populateTableData();
			clearFields();
		} else if (event.getSource() == btnDelete) {
			deleteUser();
			populateTableData();
		} else if (event.getSource() == menuEdit) {
			populateFieldForEdit();
		} else if (event.getSource() == btnChangeAccess) {
			updateUser();
			populateTableData();
		}
	}

	private boolean checkForEmptyFields() {
		boolean isPass = false;
		if (Commons.isTextFieldHasValue(txtUsername)) {
			if (Commons.isTextFieldHasValue(txtPassword))
				isPass = true;
		}
		return isPass;

	}

	public void clearFields() {
		txtFullName.clear();
		txtPassword.clear();
		txtUsername.clear();
		chboxAdmin.setSelected(false);
		chboxCsr.setSelected(false);
		chboxFactory.setSelected(false);
		chboxMgmt.setSelected(false);
		btnChangeAccess.setDisable(true);
		btnSave.setDisable(false);
		btnDelete.setDisable(false);
		txtUsername.setDisable(false);
		selectedUsedID = 0;
	}

	private void createUpdateUser() {
		String fullName = txtFullName.getText();
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		int adminAccess = Commons.getCheckBoxValue(chboxAdmin);
		int csrAccess = Commons.getCheckBoxValue(chboxCsr);
		int factoryAccess = Commons.getCheckBoxValue(chboxFactory);
		int mgmntAccess = Commons.getCheckBoxValue(chboxMgmt);
		boolean isSuccess = UsersDAO.insertUserDate(selectedUsedID, fullName, username, password, adminAccess,
				csrAccess, factoryAccess, mgmntAccess);
		Commons.processMessageLabel(labelInformation, isSuccess);
		if (isSuccess)
			clearFields();

	}

	private void saveUser(String username) {
		if (checkForEmptyFields()) {
			if (AlertsUtils.askForSaveItems()) {
				if (!isUserExist(username)) {
					createUpdateUser();
				} else {
					Commons.processMessageLabel(labelInformation, false);
					AlertsUtils.repeatItemAlerts(username);
				}
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void generateTableColumns(ObservableList<UsersModels> list) {
		colCode.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
		colFullName.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
		colUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
		colPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
		colAdmin.setCellValueFactory(cellDate -> cellDate.getValue().adminAccessProperty());
		colCsr.setCellValueFactory(cellData -> cellData.getValue().csrAccessProperty());
		colFactory.setCellValueFactory(cellData -> cellData.getValue().factoryAccessProperty());
		colMgmnt.setCellValueFactory(cellData -> cellData.getValue().mgmntAccessProperty());
		tableUserDetail.setItems(list);
	}

	private void populateTableData() {
		usersData = UsersDAO.getUsersList();
		generateTableColumns(usersData);
	}

	private boolean isUserExist(String username) {
		boolean isExist = false;
		for (UsersModels model : usersData) {
			if (model.getUsername().equals(username)) {
				isExist = true;
				break;
			}

		}
		return isExist;
	}

	private void deleteUser() {
		if (!tableUserDetail.getSelectionModel().isEmpty()) {
			UsersModels model = tableUserDetail.getSelectionModel().getSelectedItem();
			int userID = model.getUserId();
			String fullName = model.getFullname();
			if (AlertsUtils.askForDeleteAlert(fullName)) {
				boolean isSucess = UsersDAO.deleteUser(userID);
				Commons.processMessageLabel(labelInformation, isSucess);
			} else {
				Commons.processMessageLabel(labelInformation, false);
			}
		}
	}

	private void fillFormWhileSelected(int userID) {
		usersData.forEach(model -> {
			if (model.getUserId() == userID) {
				txtFullName.setText(model.getFullname());
				txtUsername.setText(model.getUsername());
				txtPassword.setText(model.getPassword());
				chboxAdmin.setSelected(Commons.changeSymboleToBoolean(model.getAdminAccess()));
				chboxCsr.setSelected(Commons.changeSymboleToBoolean(model.getCsrAccess()));
				chboxFactory.setSelected(Commons.changeSymboleToBoolean(model.getFactoryAccess()));
				chboxMgmt.setSelected(Commons.changeSymboleToBoolean(model.getMgmntAccess()));
			}
		});
	}

	private void populateFieldForEdit() {
		btnDelete.setDisable(true);
		btnSave.setDisable(true);
		txtUsername.setDisable(true);
		btnChangeAccess.setDisable(false);
		if (!tableUserDetail.getSelectionModel().isEmpty()) {
			selectedUsedID = tableUserDetail.getSelectionModel().getSelectedItem().getUserId();
			fullname = tableUserDetail.getSelectionModel().getSelectedItem().getFullname();
			fillFormWhileSelected(selectedUsedID);
		}
	}

	private void updateUser() {
		if (checkForEmptyFields()) {
			if (AlertsUtils.askForUpdateAlert(fullname)) {
				createUpdateUser();
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

}
