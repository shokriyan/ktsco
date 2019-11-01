package com.ktsco.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewClass {

	private static final Logger log = LoggerFactory.getLogger(ViewClass.class);

	/**
	 * Set Fxml file to BorderPane <br>
	 * 
	 * @param fxml
	 * @return BorderPane
	 */
	public BorderPane setBorderPane(String fxml) {
		log.info("Loading FXML for BorderPane");
		BorderPane borderPane = new BorderPane();
		FXMLLoader loader = new FXMLLoader();
		try {
			borderPane = (BorderPane) loader.load(getClass().getResourceAsStream(fxml));
			log.info("FXMl file " + fxml + "Loaded to border pane");

		} catch (IOException e) {
			log.error("Fail to load fxml file " + fxml + " with error massage " + e.getMessage());
		}

		return borderPane;
	}

	/**
	 * This method will set the scene and show the stage.
	 * 
	 * @param rootNote
	 * @param title
	 * @param decorate
	 * @param wait
	 * @return Stage
	 */
	public Stage setSceneAndShowWaitStage(Parent rootNote, String title, boolean decorate) {
		Stage stage = new Stage();
		Scene scene = new Scene(rootNote);
		log.info("Create Root from {}" + rootNote);
		scene.getStylesheets().add(getClass().getResource(Commons.getFxmlPanel("styleSheetPath")).toExternalForm());
		log.info("Initial Style Sheet from {}" + Commons.getFxmlPanel("styleSheetPath"));
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle(title);
		if (!decorate) {
			log.info("Not Decorating with Stage");
			stage.initStyle(StageStyle.UNDECORATED);
		}
		stage.setAlwaysOnTop(false);
		stage.show();
		return stage;
	}
	
	/**
	 * This method will set the scene and show the stage.
	 * 
	 * @param rootNote
	 * @param title
	 * @param decorate
	 * @param wait
	 * @return Stage
	 */
	public Stage setSceneShowStage(Parent rootNote, String title, boolean decorate) {
		Stage stage = new Stage();
		Scene scene = new Scene(rootNote);
		log.info("Create Root from {}" + rootNote);
		scene.getStylesheets().add(getClass().getResource(Commons.getFxmlPanel("styleSheetPath")).toExternalForm());
		log.info("Initial Style Sheet from {}" + Commons.getFxmlPanel("styleSheetPath"));
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle(title);
		if (!decorate) {
			log.info("Not Decorating Stage");
			stage.initStyle(StageStyle.UNDECORATED);
		}
		stage.show();
		return stage;
	}
	

	/**
	 * This method with take FXML with HBOX and return at HBox
	 * 
	 * @param fxml
	 * @return HBox
	 */
	public HBox setHboxFxml(String fxml) {
		log.info("Setting FXML for HBOX");
		HBox hbox = new HBox();

		FXMLLoader loader = new FXMLLoader();
		try {
			hbox = (HBox) loader.load(getClass().getResourceAsStream(fxml));
			log.info("FXMl file " + fxml + "Loaded");
		} catch (IOException e) {
			log.error("Fail to load fxml file " + fxml + " with error massage " + e.getMessage());
		}

		return hbox;
	}

	/**
	 * this method will take FXML with VBox and return as Vbox
	 * 
	 * @param fxml
	 * @return VBox
	 */
	public VBox setVboxFxml(String fxml) {
		log.info("Setting FXML for VBOX");
		VBox vbox = new VBox();

		FXMLLoader loader = new FXMLLoader();
		try {
			vbox = (VBox) loader.load(getClass().getResourceAsStream(fxml));
			log.info("FXMl file " + fxml + "Loaded");
		} catch (IOException e) {
			log.error("Fail to load fxml file " + fxml + " with error massage " + e.getMessage());
			e.printStackTrace();
		}

		return vbox;
	}

	/**
	 * This method will take FXML with SplitPate and return splitPane
	 * 
	 * @param fxml
	 * @return SplitPane
	 */

	public SplitPane setSplitPane(String fxml) {
		SplitPane splitPane = new SplitPane();
		FXMLLoader loader = new FXMLLoader();
		try {
			splitPane = (SplitPane) loader.load(getClass().getResourceAsStream(fxml));
			log.info("FXMl file " + fxml + "Loaded");
		} catch (IOException e) {
			log.error("Fail to load fxml file " + fxml + " with error massage " + e.getMessage());
		}

		return splitPane;
	}

	/**
	 * This method will take FXML with Pane and return Pane
	 * 
	 * @param fxml
	 * @return Pane
	 */

	public Pane setPane(String fxml) {
		Pane pane = new Pane();
		FXMLLoader loader = new FXMLLoader();
		try {
			pane = (Pane) loader.load(getClass().getResourceAsStream(fxml));
			log.info("FXMl file " + fxml + "Loaded");
		} catch (IOException e) {
			log.error("Fail to load fxml file " + fxml + " with error massage " + e.getMessage());
			e.printStackTrace();
		}

		return pane;
	}

	/**
	 * This Method will take FXML with StackPane and return Stack Pane
	 * 
	 * @param fxml
	 * @return StackPane
	 */

	public StackPane setCenterPanel(String fxml) {
		StackPane stackPane = new StackPane();
		stackPane.setPrefSize(1166, 688);
		stackPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		stackPane.getChildren().clear();
		ViewClass view = new ViewClass();
		stackPane.getChildren().add(view.setPane(fxml));

		return stackPane;
	}

}
