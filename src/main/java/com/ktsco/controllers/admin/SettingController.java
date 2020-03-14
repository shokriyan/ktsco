package com.ktsco.controllers.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.admin.MainCatModel;
import com.ktsco.modelsdao.CategoryDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SettingController implements Initializable {
	private static final Logger log = LoggerFactory.getLogger(SettingController.class);
	@FXML
	private Tab tabSqlServer;
	@FXML
	private Tab databaseSettingTab, mainCategory;

	@FXML
	private Button btnStartStop, btnSave;
	@FXML
	private Button btnStatus, btnUpdate, btnRefresh;
	@FXML
	private TextArea txtAreaResults;
	@FXML
	private TextField txtServerAddress, txtServerName, txtServerPort, txtDBName, txtUsername, txtPassword, txtTImeZone;

	@FXML
	private TextField txtCode, txtItemsDesc;
	@FXML
	private Label labelInformation;

	@FXML
	private TableView<MainCatModel> tableDetail;
	@FXML
	private TableColumn<MainCatModel, Integer> colCod;
	@FXML
	private TableColumn<MainCatModel, String> colDesc;
	
	@FXML
	private MenuItem menuDelete; 

	private String responseValue = null;
	private ObservableList<MainCatModel> tableDataList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainCategory.setOnSelectionChanged(event -> {
			if (event.getSource() == mainCategory) {
				populateMainCatTable();
			}
		});
		txtServerAddress.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		txtServerAddress.setText("/usr/local/Cellar/mysql/8.0.16/support-files/");
		responseValue = terminalCommandExecute("status");
		txtAreaResults.setText(responseValue);
		setButtonTextbasedOnStatus(responseValue);
		setTextWithProperties();
		populateMainCatTable();

	}

	/**
	 * This method for setting the button label as different status
	 * 
	 * @param response
	 */
	private void setButtonTextbasedOnStatus(String response) {

		if (response.trim().startsWith("SUCCESS")) {
			btnStartStop.setText("خاموش کردن");
		} else {
			btnStartStop.setText("راه اندازی مجدد");
		}

	}

	/**
	 * THis is method to handle all the buttons acction
	 * 
	 * @param event
	 */
	@FXML
	public void allButtonAction(ActionEvent event) {

		if (event.getSource() == btnStatus) {
			log.info("Button clicked {} ", btnStatus);
			executeAndShowResponse("status");
			setButtonTextbasedOnStatus(responseValue);
			log.info("SQL Server with response {}" + responseValue);
		} else if (event.getSource() == btnStartStop) {
			log.info("Button clicked {} ", btnStartStop);
			if (btnStartStop.getText().equalsIgnoreCase("Start")) {
				executeAndShowResponse("start");
				setButtonTextbasedOnStatus(responseValue);
				log.info("SQL Server Started with response {}" + responseValue);
			} else {
				executeAndShowResponse("stop");
				setButtonTextbasedOnStatus(responseValue);
				log.info("SQL Server Stoped with response {}" + responseValue);
			}
		} else if (event.getSource() == btnRefresh) {
			setTextWithProperties();
		} else if (event.getSource() == btnUpdate) {
			boolean response = AlertsUtils.askForSaveItems();
			if (response)
				updateDatabaseConfiguration();
		} else if (event.getSource() == btnSave) {
			saveMainCatItem();
			populateMainCatTable();
		}else if (event.getSource() == menuDelete) {
			deleteManCatItem();
			//populateMainCatTable();
		}

	}

	/**
	 * this method is for executing command and set response
	 * 
	 * @param commandType
	 */
	public void executeAndShowResponse(String commandType) {
		responseValue = terminalCommandExecute(commandType);
		txtAreaResults.setText(responseValue);
	}

	/**
	 * this method will execute the command
	 * 
	 * @param commandType
	 * @return String
	 */
	private String terminalCommandExecute(String commandType) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		Runtime run = Runtime.getRuntime();
		String commad = (txtServerAddress.getText()) + "mysql.server " + commandType;
		log.info("Shell command to execute " + commad);
		Process process;
		try {
			process = run.exec(commad);
			process.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			log.error("Error to Executeing the command " + e.getMessage());
		}

		return String.valueOf(sb);

	}
	

	private void setTextWithProperties() {
		String serverName = Commons.getConfigurationPropertyValue("serverName");
		String serverPort = Commons.getConfigurationPropertyValue("serverPort");
		String databaseName = Commons.getConfigurationPropertyValue("databaseName");
		String user = Commons.getConfigurationPropertyValue("user");
		String password = Commons.getConfigurationPropertyValue("password");
		String serverTimeZon = Commons.getConfigurationPropertyValue("serverTimeZon");

		txtServerName.setText(serverName);
		txtServerPort.setText(serverPort);
		txtDBName.setText(databaseName);
		txtUsername.setText(user);
		txtPassword.setText(password);
		txtTImeZone.setText(serverTimeZon);

	}

	private void updateDatabaseConfiguration() {

		Commons.updateConfigurationPropertyValue(txtServerName.getText(), "serverName");
		Commons.updateConfigurationPropertyValue(txtServerPort.getText(), "serverPort");
		Commons.updateConfigurationPropertyValue(txtDBName.getText(), "databaseName");
		Commons.updateConfigurationPropertyValue(txtUsername.getText(), "user");
		Commons.updateConfigurationPropertyValue(txtPassword.getText(), "password");
		Commons.updateConfigurationPropertyValue(txtTImeZone.getText(), "serverTimeZon");

		AlertsUtils.SuccessfullyDoneAlrt();

	}

	private void generateMainCatTable(ObservableList<MainCatModel> list) {
		colCod.setCellValueFactory(cellData -> cellData.getValue().codeProperty().asObject());
		colDesc.setCellValueFactory(cellData -> cellData.getValue().descProperty());
		tableDetail.setItems(list);
	}

	private void populateMainCatTable() {
		tableDataList = CategoryDAO.getMainCatItems();
		generateMainCatTable(tableDataList);
	}

	private boolean checkCodeExist(int code) {
		boolean exist = false;
		for (MainCatModel model : tableDataList) {
			if (code == model.getCode())
				exist = true;
		}
		return exist;
	}

	private boolean textFieldNullCheck() {
		boolean isPassed = true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtCode.getText())) ? false : true;
		isPassed = (isPassed == false || "".equalsIgnoreCase(txtItemsDesc.getText())) ? false : true;
		return isPassed;

	}

	private void saveMainCatItem() {
		if (textFieldNullCheck()) {
			int code = Integer.parseInt(txtCode.getText());
			String desc = txtItemsDesc.getText();
			if (!checkCodeExist(code)) {
				boolean isSuccess = CategoryDAO.insertMainCatData(code, desc);
				Commons.processMessageLabel(labelInformation, isSuccess);
			}else {
				AlertsUtils.repeatItemAlerts(code + " " + desc);
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}
	
	private void deleteManCatItem() {
		if (!tableDetail.getSelectionModel().isEmpty()) {
			MainCatModel model = tableDetail.getSelectionModel().getSelectedItem();
			int code = model.getCode();
			String desc = model.getDesc(); 
			
			if (AlertsUtils.askForDeleteAlert(code + " " + desc)) {
				CategoryDAO.deleceMainCatItems(code);
			}
		}
	}

}
