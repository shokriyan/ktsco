package com.ktsco.controllers.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SettingController implements Initializable {
	private static final Logger log = LoggerFactory.getLogger(SettingController.class);
	@FXML
	private Tab tabSqlServer;
	@FXML
	private Tab tabInformation;

	@FXML
	private Button btnStartStop;
	@FXML
	private Button btnStatus;
	@FXML
	private TextArea txtAreaResults;
	@FXML
	private TextField txtServerAddress;
	
	private String responseValue = null; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtServerAddress.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		txtServerAddress.setText("/usr/local/Cellar/mysql/8.0.16/support-files/");
		responseValue = terminalCommandExecute("status");
		txtAreaResults.setText(responseValue);
		setButtonTextbasedOnStatus(responseValue);
	
	}
	
	/**
	 * This method for setting the button label as different status
	 * @param response
	 */
	private void setButtonTextbasedOnStatus(String response) {
		
		if (response.trim().startsWith("SUCCESS")) {
			btnStartStop.setText("خاموش کردن");
		}else {
			btnStartStop.setText("راه اندازی مجدد");
		}
		
	}
	/**
	 * THis is method to handle all the buttons acction
	 * @param event
	 */
	@FXML
	public void allButtonAction(ActionEvent event) {
		
		if (event.getSource() == btnStatus) {
			log.debug("Button clicked {} " , btnStatus);
			executeAndShowResponse("status");
			setButtonTextbasedOnStatus(responseValue);
			log.info("SQL Server with response {}" + responseValue);
		}else if (event.getSource() == btnStartStop) {
			log.debug("Button clicked {} " , btnStartStop);
			if (btnStartStop.getText().equalsIgnoreCase("Start")) {
				executeAndShowResponse("start");
				setButtonTextbasedOnStatus(responseValue);
				log.info("SQL Server Started with response {}" + responseValue);
			}else {
				executeAndShowResponse("stop");
				setButtonTextbasedOnStatus(responseValue);
				log.info("SQL Server Stoped with response {}" + responseValue);
			}
		}
		
	}
	/**
	 * this method is for executing command and set response 
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
		
		while ((line = br.readLine())!= null) {
			sb.append(line);
		}
		
		}catch (Exception e) {
			log.error("Error to Executeing the command " + e.getMessage());
		}
		
		
		
		return String.valueOf(sb);
		
		
	}

}
