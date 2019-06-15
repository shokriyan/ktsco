package com.ktsco.controllers.factory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.modelsdao.EmployeDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.utils.Commons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductionController implements Initializable {

	@FXML
	private Button btnProducts, btnArrowDown;
	@FXML
	private Label labelMessage;
	@FXML
	private TextField txtNo, txtDate, txtStockQuantity;
	@FXML
	private TableView<?> tableProductList;
	@FXML
	private TableColumn<?, ?> colProductItemList, colUMList, colImportQtyList;

	@FXML
	private TableView<?> tableProductionStock;
	@FXML
	private TableColumn<?, ?> colProductStock, colUMStock, colTotalQtyStock;
	
	@FXML
	private ComboBox<String> comboEmployee, comboProducts; 

	@Override

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		comboProducts.setEditable(true);
		loadPrerequisitions();
	}
	
	private void loadPrerequisitions() {
		populateResponsiblePersonCombo();
		populateProductsCombo();
	}


	public void allButtonsAction(ActionEvent event) {

	}
	
	//Populating Employee List for ComboBox Responsible Person
	private void populateResponsiblePersonCombo() {
		List<String> empList = EmployeDAO.getEmployeeName();
		Commons.populateAllComboBox(comboEmployee, empList);
	}
	
	private void populateProductsCombo() {
		List<String> productList = ProductDAO.getProductList();
		Commons.populateAllComboBox(comboProducts, productList);
	}

}
