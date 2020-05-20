package com.ktsco.controllers.factory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.ktsco.controllers.mgmnt.SearchPanelControllers;
import com.ktsco.enums.Dictionary;
import com.ktsco.models.csr.EmpListModel;
import com.ktsco.models.factory.FactoryExportModal;
import com.ktsco.models.factory.ProductModel;
import com.ktsco.modelsdao.EmployeeDAO;
import com.ktsco.modelsdao.ProductDAO;
import com.ktsco.modelsdao.ProductionExportDAO;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DateUtils;
import com.ktsco.utils.ViewClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class FactoryExportSearchController {
	ViewClass view = new ViewClass();
	SearchPanelControllers searchController = new SearchPanelControllers();
	public Pane exportSearchPane;
	ObservableList<EmpListModel> employeeList = FXCollections.observableArrayList();
	ObservableList<ProductModel> productList = FXCollections.observableArrayList();
	NumberFormat formatQuantity = new DecimalFormat("#0.00");
	ObservableList<FactoryExportModal> tableData = FXCollections.observableArrayList();
	TableView<FactoryExportModal> tableView = new TableView<FactoryExportModal>();
	HBox editBox;
	List<String> employeeComboValues = new ArrayList<String>();
	List<String> productsComboValues = new ArrayList<String>();
	private Button btnSearch, btnRefresh, btnReturn;

	public FactoryExportSearchController() {
		exportSearchPane = new Pane();
		exportSearchPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

		exportSearchPane.setPrefHeight(Constants.mainPanelPrefHeight);
		exportSearchPane.setPrefWidth(Constants.mainPanelPrefWidth);
		exportSearchPane.setMinHeight(Control.USE_COMPUTED_SIZE);
		exportSearchPane.setMinWidth(Control.USE_COMPUTED_SIZE);
		exportSearchPane.setMaxHeight(Double.MAX_VALUE);
		exportSearchPane.setMaxWidth(Double.MAX_VALUE);
		VBox.setVgrow(exportSearchPane, Priority.ALWAYS);
		HBox.setHgrow(exportSearchPane, Priority.ALWAYS);
		employeeList = EmployeeDAO.retreiveAllEmpRecord();
		productList = ProductDAO.selectAll();
		btnSearch = new Button();
		btnRefresh = new Button();
		btnReturn = new Button();
		editBox = new HBox(Constants.globalSpacing);
		editBox.setPrefHeight(50);
		tableData = ProductionExportDAO.retrieveFactoryExportSearchItems("", "");
	}

	public Pane createExportSearchPane() {
		VBox mainPanel = new VBox(Constants.globalSpacing);
		mainPanel.setPadding(Constants.globalPadding);
		mainPanel.setPrefSize(Constants.mainPanelPrefWidth, Constants.mainPanelPrefHeight);
		mainPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		VBox.setVgrow(mainPanel, Priority.ALWAYS);
		HBox.setHgrow(mainPanel, Priority.ALWAYS);
		HBox searchBox = createSearchPanel();
		HBox pageTitleBox = pageTitlePanel();
		HBox searchTable = populateTableData();
		editBox.managedProperty().bind(editBox.visibleProperty());
		editBox.setVisible(false);
		mainPanel.getChildren().clear();
		mainPanel.getChildren().addAll(searchBox, pageTitleBox, editBox, searchTable);
		exportSearchPane.getChildren().clear();
		exportSearchPane.getChildren().add(mainPanel);

		return exportSearchPane;
	}

	public HBox createSearchPanel() {
		HBox searchbox = new HBox();
		retrieveEmoloyeeList();
		HBox.setHgrow(searchbox, Priority.ALWAYS);
		HBox searchItems = searchController.productionExport();
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		HBox searchButtons = SearchBottonPanel();

		searchbox.getChildren().clear();
		searchbox.getChildren().addAll(searchItems, region, searchButtons);
		return searchbox;
	}

	private HBox SearchBottonPanel() {
		HBox buttonBox = new HBox(Constants.globalSpacing);
		buttonBox.setPadding(Constants.globalPadding);
		btnSearch.setText(Dictionary.Search.getValue());
		btnRefresh.setText(Dictionary.Refresh.getValue());
		btnReturn.setText(Dictionary.Return.getValue());

		btnSearch.setPrefWidth(110);
		btnRefresh.setPrefWidth(110);
		btnReturn.setPrefWidth(110);

		btnSearch.setOnAction(event -> allButtonActions(event));
		btnRefresh.setOnAction(event -> allButtonActions(event));
		btnReturn.setOnAction(event -> allButtonActions(event));

		buttonBox.getChildren().clear();
		buttonBox.getChildren().addAll(btnSearch, btnRefresh, btnReturn);
		return buttonBox;
	}

	public void retrieveEmoloyeeList() {
		employeeList.forEach(list -> {
			int employeeId = list.getEmpID();
			String employeeFullName = list.getEmpFullName();
			String comboValue = String.join(" - ", String.valueOf(employeeId), employeeFullName);
			employeeComboValues.add(comboValue);
		});
		Commons.populateAllComboBox(searchController.comboItems(), employeeComboValues);
		searchController.comboItems().setValue("");

		productList.forEach(list -> {
			String productName = list.getProdName();
			productsComboValues.add(productName);
		});

	}

	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			searchExportData();
		} else if (event.getSource() == btnRefresh) {
			refreshPage();
		} else if (event.getSource() == btnReturn) {
			Pane production = view.setPane(Commons.getFxmlPanel("productionPanelFxml"));
			FactoryController.factoryBorderPane.setCenter(production);
		}
	}

	private HBox pageTitlePanel() {
		HBox pageTitleBox = new HBox();
		pageTitleBox.setPadding(Constants.globalPadding);
		HBox.setHgrow(pageTitleBox, Priority.ALWAYS);
		pageTitleBox.setAlignment(Pos.CENTER);
		pageTitleBox.getStyleClass().add("subtitleStyle");
		Text pageTitle = new Text();
		pageTitle.setFont(Constants.pageTitleFont);
		pageTitle.setText(Dictionary.FactoryExportSearch.getValue());
		pageTitleBox.getChildren().clear();
		pageTitleBox.getChildren().add(pageTitle);
		return pageTitleBox;
	}

	private TableView<FactoryExportModal> generateSearchTable(ObservableList<FactoryExportModal> list) {
//		VBox.setVgrow(tableView, Priority.ALWAYS);
		HBox.setHgrow(tableView, Priority.ALWAYS);
		tableView.setPrefHeight(380);
		tableView.setPrefWidth(1080);
		tableView.setMaxWidth(Double.MAX_VALUE);
		tableView.setMaxHeight(Double.MAX_VALUE);
		tableView.prefHeightProperty().bind(exportSearchPane.prefHeightProperty());
		TableColumn<FactoryExportModal, Integer> colExportId = new TableColumn<FactoryExportModal, Integer>();
		TableColumn<FactoryExportModal, String> colEmployeeName = new TableColumn<FactoryExportModal, String>();
		TableColumn<FactoryExportModal, String> colExportDate = new TableColumn<FactoryExportModal, String>();
		TableColumn<FactoryExportModal, String> colProductName = new TableColumn<FactoryExportModal, String>();
		TableColumn<FactoryExportModal, Double> colExportQuantity = new TableColumn<FactoryExportModal, Double>();
		TableColumn<FactoryExportModal, Button> colEditButton = new TableColumn<FactoryExportModal, Button>();

		colExportId.setText(Dictionary.ExportId.getValue());
		colEmployeeName.setText(Dictionary.Employee.getValue());
		colExportDate.setText(Dictionary.Date.getValue());
		colProductName.setText(Dictionary.Product.getValue());
		colExportQuantity.setText(Dictionary.Quantity.getValue());
		colEditButton.setText(Dictionary.Edit.getValue());
		List<TableColumn<FactoryExportModal, ?>> columnList = new ArrayList<TableColumn<FactoryExportModal, ?>>();
		columnList.add(colExportId);
		columnList.add(colEmployeeName);
		columnList.add(colExportDate);
		columnList.add(colProductName);
		columnList.add(colExportQuantity);
		tableView.getColumns().clear();
		tableView.getColumns().addAll(columnList);
		for (TableColumn<FactoryExportModal, ?> column : columnList) {
			column.prefWidthProperty().bind(tableView.prefWidthProperty().divide(6));
		}
		addEditButtonToTable(tableView);
		addDeleteButtonToTable(tableView);
		colExportId.setCellValueFactory(data -> data.getValue().exportIdProperty().asObject());
		colEmployeeName.setCellValueFactory(data -> data.getValue().employeeNameProperty());
		colExportDate.setCellValueFactory(data -> data.getValue().exportDateProperty());
		colProductName.setCellValueFactory(data -> data.getValue().productNameProperty());
		colExportQuantity.setCellValueFactory(data -> data.getValue().exportQuantityProperty().asObject());
		formatColumn(colExportQuantity, formatQuantity);

		tableView.setItems(list);
		return tableView;
	}

	private void formatColumn(TableColumn<FactoryExportModal, Double> column, NumberFormat format) {

		column.setCellFactory(tc -> new TableCell<FactoryExportModal, Double>() {

			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty) {
					setText(null);
				} else {
					setText(format.format(price));
				}
			}
		});
	}

	private HBox populateTableData() {
		HBox tablebox = new HBox(Constants.globalSpacing);
		tablebox.setPadding(Constants.globalPadding);

		TableView<FactoryExportModal> generatedTable = generateSearchTable(tableData);

		tablebox.getChildren().clear();
		tablebox.getChildren().add(generatedTable);

		return tablebox;
	}

	private void addEditButtonToTable(TableView<FactoryExportModal> tableView) {
		TableColumn<FactoryExportModal, Void> colBtn = new TableColumn<FactoryExportModal, Void>(
				Dictionary.Edit.getValue());

		Callback<TableColumn<FactoryExportModal, Void>, TableCell<FactoryExportModal, Void>> cellFactory = new Callback<TableColumn<FactoryExportModal, Void>, TableCell<FactoryExportModal, Void>>() {
			@Override
			public TableCell<FactoryExportModal, Void> call(final TableColumn<FactoryExportModal, Void> param) {
				final TableCell<FactoryExportModal, Void> cell = new TableCell<FactoryExportModal, Void>() {

					private Button btnEdit = new Button(Dictionary.Edit.getValue());
					{
						btnEdit.setOnAction(event -> {
							getTableView().getSelectionModel().select(getIndex());
							FactoryExportModal data = getTableView().getItems().get(getIndex());
							populateEditValues(data);
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btnEdit);
						}
					}
				};
				return cell;
			}
		};

		colBtn.setCellFactory(cellFactory);

		tableView.getColumns().add(colBtn);

	}

	private void addDeleteButtonToTable(TableView<FactoryExportModal> tableView) {
		TableColumn<FactoryExportModal, Void> colBtn = new TableColumn<FactoryExportModal, Void>(
				Dictionary.Delete.getValue());

		Callback<TableColumn<FactoryExportModal, Void>, TableCell<FactoryExportModal, Void>> cellFactory = new Callback<TableColumn<FactoryExportModal, Void>, TableCell<FactoryExportModal, Void>>() {
			@Override
			public TableCell<FactoryExportModal, Void> call(final TableColumn<FactoryExportModal, Void> param) {
				final TableCell<FactoryExportModal, Void> cell = new TableCell<FactoryExportModal, Void>() {

					private Button btnDelete = new Button(Dictionary.Delete.getValue());
					{
						btnDelete.setOnAction(event -> {
							getTableView().getSelectionModel().select(getIndex());
							FactoryExportModal data = getTableView().getItems().get(getIndex());
							deleteExportItem(data);
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btnDelete);
						}
					}

				};
				return cell;
			}
		};

		colBtn.setCellFactory(cellFactory);

		tableView.getColumns().add(colBtn);

	}

	private void deleteExportItem(FactoryExportModal data) {
		boolean isOkay = AlertsUtils.askForDeleteAlert(data.getProductName());
		if (isOkay) {
			boolean isSuccess = ProductionExportDAO.deleteExportData(data);
			if (isSuccess) {
				Commons.processToastMessage(isSuccess);
				searchExportData();
			}
		}

	}

	private void populateEditValues(FactoryExportModal data) {
		editBox.setVisible(true);
		editBox.setAlignment(Pos.CENTER_LEFT);
		VBox employeeBox = new VBox();
		Label labelEmployeeList = new Label();
		labelEmployeeList.setFont(Constants.elementFonts);
		labelEmployeeList.setText(Dictionary.EmployeeList.getValue());
		ComboBox<String> comboEmployeeList = new ComboBox<String>();
		Commons.populateAllComboBox(comboEmployeeList, employeeComboValues);
		int employeeId = data.getEmployeeId();
		String employeeFullName = data.getEmployeeName();
		String comboValue = String.join(" - ", String.valueOf(employeeId), employeeFullName);
		comboEmployeeList.setValue(comboValue);
		employeeBox.getChildren().clear();
		employeeBox.getChildren().addAll(labelEmployeeList, comboEmployeeList);
		HBox.setHgrow(employeeBox, Priority.ALWAYS);

		VBox entryDateBox = new VBox();
		Label labelentryDate = new Label();
		labelentryDate.setFont(Constants.elementFonts);
		labelentryDate.setText(Dictionary.Date.getValue());
		TextField txtEntryDate = new TextField();
		txtEntryDate.setFont(Constants.elementFonts);
		txtEntryDate.setText(DateUtils.convertGregoryToJalali(data.getExportDate()));
		entryDateBox.getChildren().clear();
		entryDateBox.getChildren().addAll(labelentryDate, txtEntryDate);
		HBox.setHgrow(entryDateBox, Priority.ALWAYS);

		VBox productListBox = new VBox();
		Label labelProductList = new Label();
		labelProductList.setFont(Constants.elementFonts);
		labelProductList.setText(Dictionary.ProductList.getValue());
		ComboBox<String> comboProductList = new ComboBox<String>();

		Commons.populateAllComboBox(comboProductList, productsComboValues);
		comboProductList.setValue(data.getProductName());
		productListBox.getChildren().clear();
		productListBox.getChildren().addAll(labelProductList, comboProductList);
		HBox.setHgrow(productListBox, Priority.ALWAYS);

		VBox quantityBox = new VBox();
		Label labelQuantity = new Label();
		labelQuantity.setFont(Constants.elementFonts);
		labelQuantity.setText(Dictionary.Quantity.getValue());
		TextField txtQuantity = new TextField();
		txtQuantity.setFont(Constants.elementFonts);
		txtQuantity.setText(String.valueOf(data.getExportQuantity()));
		quantityBox.getChildren().clear();
		quantityBox.getChildren().addAll(labelQuantity, txtQuantity);
		HBox.setHgrow(quantityBox, Priority.ALWAYS);

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);

		Button btnUpdate = new Button();
		btnUpdate.setText(Dictionary.Update.getValue());
		HBox.setHgrow(btnUpdate, Priority.ALWAYS);
		btnUpdate.setPrefWidth(120);
		Button btnCancel = new Button();
		btnCancel.setText(Dictionary.Cancel.getValue());
		HBox.setHgrow(btnCancel, Priority.ALWAYS);
		btnCancel.setPrefWidth(120);
		editBox.getChildren().clear();
		editBox.getChildren().addAll(employeeBox, entryDateBox, productListBox, quantityBox, region, btnUpdate,
				btnCancel);

		btnCancel.setOnAction(event -> {
			editBox.getChildren().clear();
			editBox.setVisible(false);
		});

		btnUpdate.setOnAction(Event -> {
			FactoryExportModal exportModal = new FactoryExportModal();
			int employeeIndex = (comboEmployeeList.getValue().equalsIgnoreCase("")) ? 0
					: comboEmployeeList.getSelectionModel().getSelectedIndex();
			if (employeeIndex != 0) {
				exportModal.setEmployeeId(employeeList.get(employeeIndex - 1).getEmpID());
				exportModal.setEmployeeName(employeeList.get(employeeIndex - 1).getEmpFullName());
			}
			int productIndex = (comboProductList.getValue().equalsIgnoreCase("")) ? 0
					: comboProductList.getSelectionModel().getSelectedIndex();
			if (productIndex != 0) {
				exportModal.setProductId(productList.get(productIndex - 1).getProdId());
				exportModal.setProductName(productList.get(productIndex - 1).getProdName());
			}

			exportModal.setExportDate(DateUtils.convertJalaliToGregory(txtEntryDate.getText()));
			exportModal.setExportQuantity(Double.parseDouble(txtQuantity.getText()));
			exportModal.setExportId(data.getExportId());
			exportModal.setSequenceId(data.getSequenceId());

			updateExportEntry(exportModal);
		});
	}

	private void searchExportData() {
		editBox.setVisible(false);
		String fromDateText = searchController.getFromDateText();
		String toDateText = searchController.getToDateText();
		tableData = ProductionExportDAO.retrieveFactoryExportSearchItems(fromDateText, toDateText);
		int selectedIndex = searchController.comboItems().getSelectionModel().getSelectedIndex();
		int employeeId = (searchController.getComboValue().equalsIgnoreCase("")) ? 0
				: employeeList.get(selectedIndex - 1).getEmpID();
		int exportCode = (searchController.getCodeText().equalsIgnoreCase("")) ? 0
				: Integer.parseInt(searchController.getCodeText());

		FilteredList<FactoryExportModal> filteredData = new FilteredList<FactoryExportModal>(tableData);
		filteredData.setPredicate(items -> {
			if (employeeId == 0 && exportCode == 0) {
				return true;
			}

			else if (items.getEmployeeId() == employeeId)
				return true;
			else if (items.getExportId() == exportCode)
				return true;

			return false;
		});
		SortedList<FactoryExportModal> sortedData = new SortedList<FactoryExportModal>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);
	}

	private void refreshPage() {
		searchController.getFromDateField().clear();
		searchController.getToDateField().clear();
		searchController.getCodeField().clear();
		searchController.comboItems().setValue("");
		editBox.setVisible(false);
		searchExportData();
	}

	private void updateExportEntry(FactoryExportModal exportModal) {
		boolean isOkay = AlertsUtils.askForUpdateAlert(Dictionary.ExportUpdateMessage.getValue());
		if (isOkay) {
			ProductionExportDAO.updateExport(exportModal);
			searchExportData();
		}

	}

}
