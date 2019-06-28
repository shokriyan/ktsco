package com.ktsco.controllers.admin;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.admin.UsersModels;
import com.ktsco.modelsdao.UsersDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.ViewClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UsersController implements Initializable {

	public static final Logger log = LoggerFactory.getLogger(UsersController.class);

	static ViewClass view = new ViewClass();

	VBox usersScene;
	public static Stage usersStage = new Stage();

	@FXML
	private Button btnAddNewUser;
	@FXML
	private Button btnEditUser;
	@FXML
	private Button btnDeleteUser;
	@FXML
	private Button btnReload;

	@FXML
	private TableView<UsersModels> userTable;
	@FXML
	private TableColumn<UsersModels, Integer> tcUserID;
	@FXML
	private TableColumn<UsersModels, String> tcUsername, tcPassword, tcAccess;

	private ObservableList<UsersModels> usersObservable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Initialzing the list of Users");
		populateUserList();

	}

	@FXML
	public void allButtonsAction(ActionEvent event) {
		if (event.getSource() == btnReload) {
			log.info("Reloading the users list after click on reload button");
			populateUserList();
		} else if (event.getSource() == btnAddNewUser) {
			Constants.buttonText = btnAddNewUser.getText();
			usersScene = view.setVboxFxml(Commons.getFxmlPanel("createNewUserFxml"));
			usersStage = view.setSceneAndShowStage(usersScene, "", false, false);
			usersStage.setOnHidden(event1 -> populateUserList());
		} else if (event.getSource() == btnEditUser) {
			Constants.buttonText = btnEditUser.getText();
			usersScene = view.setVboxFxml(Commons.getFxmlPanel("editUsersFxml"));
			usersStage = view.setSceneAndShowStage(usersScene, "", false, false);
			usersStage.setOnHidden(event1 -> populateUserList());
		} else if (event.getSource() == btnDeleteUser) {
			String userName = getSelectedUsername();
			boolean response = AlertsUtils.askForDeleteAlert(userName);
			if (response) {
				deleteSelectedUser();
			}
				
		}

	}

	public void populateUserList() {
		usersObservable = FXCollections.observableArrayList();
		usersObservable = UsersDAO.selectAllRows();

		tcUserID.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
		tcUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
		tcPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
		tcAccess.setCellValueFactory(cellData -> cellData.getValue().accessTypeProperty());

		userTable.setItems(usersObservable);

	}
	
	private void deleteSelectedUser() {
		
		int userId = getSelectedUserid(); 
		UsersDAO.deleteSelectedUser(userId);
		populateUserList();
		
	}
	
	
	private int getSelectedUserid() {
		if (!userTable.getSelectionModel().isEmpty()) {
			UsersModels userModel = userTable.getSelectionModel().getSelectedItem();
			return userModel.getUserID();
		}else {
			return 0; 
		}
	}
	
	private String getSelectedUsername() {
		if (!userTable.getSelectionModel().isEmpty()) {
			UsersModels userModel = userTable.getSelectionModel().getSelectedItem();
			return userModel.getUsername();
		}else 
			return null;
	}

}
