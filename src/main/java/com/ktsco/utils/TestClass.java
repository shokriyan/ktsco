package com.ktsco.utils;


import com.ktsco.models.ProdDetailModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TestClass extends Application{

//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		
//		HBox box = new HBox(); 
//		Label label = new Label("Hello");
//		TZDATA.init();
//		CalendarPicker<PersianCalendar> picker = CalendarPicker.persianWithSystemDefaults();
//		picker.setLocale(new Locale("fa", "Ir"));
//		Button button = new Button("Click");
//		box.getChildren().addAll(label, picker, button);
//		Scene scene = new Scene(box, 400 , 400);
//		
//		primaryStage.setScene(scene);
//		primaryStage.show();
//		
//		button.setOnAction(event->{
//			ObjectProperty<PersianCalendar> valueProp = picker.valueProperty();
//			 valueProp.getValue();
//			System.out.println(valueProp.getValue().toString());
//		});
//	}
	public static void main (String [] args) {
		launch(args);
	}
	@Override
	public void start(Stage prime) throws Exception {
		prime.setTitle("Tilte");
		prime.setWidth(600);
		prime.setHeight(600);
		setTableAppearance();
		fillTableData();
		table.setItems(list);
		TableColumn<Data, Integer> colid = new TableColumn<TestClass.Data, Integer>("ID");
		colid.setCellValueFactory(new PropertyValueFactory<> ("id"));
		TableColumn<Data, String> colname = new TableColumn<TestClass.Data, String>("Name"); 
		colname.setCellValueFactory(new PropertyValueFactory<>("name"));
		table.getColumns().addAll(colid, colname);
		addButtonONTable();
		Scene scene = new Scene(new Group(table));
		prime.setScene(scene);
		prime.show();
		
		
		
	}
	
	
	private final TableView<Data> table = new TableView<Data>();
	private final ObservableList<Data> list = FXCollections.observableArrayList();
	
	private void setTableAppearance() {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setPrefSize(600, 600);
	}
	
	private void fillTableData () {
		list.addAll(new Data (1, "Mohammad"),
				new Data (2, "Sam"),
				new Data (3, "Hamed"));
	}
	
	private void addButtonONTable() {
		TableColumn<Data, Void> colBtn = new TableColumn<TestClass.Data, Void>("Action");
		Callback<TableColumn<Data, Void>, TableCell<Data, Void>>cellFactory = new Callback<TableColumn<Data,Void>, TableCell<Data,Void>>() {
			
			@Override
			public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
				final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

                    private final Button btn = new Button("Action");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Data data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
		};
		
		colBtn.setCellFactory(cellFactory);
		table.getColumns().add(colBtn);
	}
	
	public class Data {

	    private int id;
	    private String name;

	    private Data(int id, String name) {
	        this.id = id;
	        this.name = name;
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int ID) {
	        this.id = ID;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String nme) {
	        this.name = nme;
	    }

	    @Override
	    public String toString() {
	        return "id: " + id + " - " + "name: " + name;
	    }

	}

}

