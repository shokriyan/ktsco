package com.ktsco.controllers.csr;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.EmpListModel;
import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmpListController implements Initializable {
	private static final Logger log = LoggerFactory.getLogger(EmpListController.class);
	private ObservableList<EmpListModel> empList = FXCollections.observableArrayList();
	public static Stage empListStage = new Stage();
	private static EmpListModel empModel;
	@FXML
	private Button btnClose, btnSearch, btnModify, btnRefresh, btnDelete, btnAdd;

	@FXML
	private TextField txtEmpFullName, txtEmpID, txtEmpPosition;

	@FXML
	private Label labelInfoMessage;

	@FXML
	private TableView<EmpListModel> tableEmpList;

	@FXML
	private TableColumn<EmpListModel, Integer> colEmpID;

	@FXML
	private Text txtPanelName;

	@FXML
	private TableColumn<EmpListModel, String> ColEmpFullName;

	@FXML
	private TableColumn<EmpListModel, String> colEmpPosition;

	@FXML
	void allButtonAction(ActionEvent event) {
		if (event.getSource() == btnClose) {
			if (empListStage.isShowing()) {
				empListStage.hide();
			}
		} else if (event.getSource() == btnAdd) {
			addEmployee();
			reloadPrerequisition();
		} else if (event.getSource() == btnModify) {
			saveUpdateEmployee();
			reloadPrerequisition();
		}else if (event.getSource() == btnDelete) {
			deleteEmployee();
			reloadPrerequisition();
		}else if (event.getSource() == btnSearch) {
			SearchEmployee();
		}else if (event.getSource() == btnRefresh) {
			reloadPrerequisition();
		}
	}

	@FXML
	public void onClickAction() {
		if (!tableEmpList.getSelectionModel().isEmpty()) {
			empModel = tableEmpList.getSelectionModel().getSelectedItem();
			String empID = String.valueOf(empModel.getEmpID());
			String empName = empModel.getEmpFullName();
			String empPosition = empModel.getPosition();

			txtEmpID.setText(empID);
			txtEmpFullName.setText(empName);
			txtEmpPosition.setText(empPosition);
		}else {
			AlertsUtils.selectFromListAlert();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reloadPrerequisition();
	}

	private void reloadPrerequisition() {
		log.debug("Loading all required prerequisitions");
		generateEmpID();
		clearTextFields();
		populateEmpTable();
	}

	private void clearTextFields() {
		txtEmpFullName.clear();
		txtEmpPosition.clear();
	}

	private void generateEmpID() {
		log.debug("Generating Employee ID from text box");
		int employeeID = EmployeDAO.getMaxEmpID();
		txtEmpID.setText(String.valueOf(employeeID));
	}

	private void generateTableColumns(ObservableList<EmpListModel> list) {
		colEmpID.setCellValueFactory(cellData -> cellData.getValue().getEmpIDProperty().asObject());
		ColEmpFullName.setCellValueFactory(cellData -> cellData.getValue().getEmpFullNameProperty());
		colEmpPosition.setCellValueFactory(cellData -> cellData.getValue().getPositionProperty());

		tableEmpList.setItems(list);
	}

	private void populateEmpTable() {
		empList = EmployeDAO.retreiveAllEmpRecord();
		generateTableColumns(empList);
	}

	private void addEmployee() {

		if (!txtEmpFullName.getText().isEmpty() && !txtEmpPosition.getText().isEmpty()) {
			int empID = Integer.parseInt(txtEmpID.getText());
			String empFullName = txtEmpFullName.getText();
			String empPosition = txtEmpPosition.getText();
			boolean exist = EmployeDAO.checkEmployeeNameExist(empFullName);
			if (!exist) {
				boolean success = EmployeDAO.addEmployee(empID, empFullName, empPosition);
				Commons.processMessageLabel(labelInfoMessage, success);
			} else {
				Commons.processMessageLabel(labelInfoMessage, false);
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void saveUpdateEmployee() {
		if (!txtEmpFullName.getText().isEmpty() && !txtEmpPosition.getText().isEmpty()) {
			int empID = Integer.parseInt(txtEmpID.getText());
			String empFullName = txtEmpFullName.getText();
			String empPosition = txtEmpPosition.getText();
			boolean response = AlertsUtils.askForUpdateAlert(empFullName);
			if (response) {
				boolean sucess = EmployeDAO.addEmployee(empID, empFullName, empPosition);
				Commons.processMessageLabel(labelInfoMessage, sucess);
			} else {
				Commons.processMessageLabel(labelInfoMessage, false);
			}
		} else {
			AlertsUtils.emptyFieldAlert();
		}
	}

	private void deleteEmployee() {
		if (!tableEmpList.getSelectionModel().isEmpty()) {
			empModel = tableEmpList.getSelectionModel().getSelectedItem();
			int empID = empModel.getEmpID();
			String empName = empModel.getEmpFullName();
			
			boolean response = AlertsUtils.askForDeleteAlert(empName);
			if (response) {
				boolean success = EmployeDAO.deletEmployee(empID);
				Commons.processMessageLabel(labelInfoMessage, success);
			}else{
				Commons.processMessageLabel(labelInfoMessage, false);
			}
		}else {
			AlertsUtils.selectFromListAlert();
		}
	}
	
	private void SearchEmployee() {
		String lookupValue = txtEmpFullName.getText();
		empList = EmployeDAO.searchForEmployee(lookupValue);
		generateTableColumns(empList);
	}

}
