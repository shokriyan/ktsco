package com.ktsco;

import javafx.application.Application;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.ViewClass;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    static ViewClass view = new ViewClass();

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    public SplitPane loginScene = new SplitPane(); 
    public static Stage loginStage = new Stage(); 
    
    public void start(Stage stage) throws Exception {

        log.info("Starting KTSCO Management application");
        loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        loginScene = view.setSplitPane(Commons.getFxmlPanel("loginPanelFxml"));
        loginStage = view.setSceneShowStage(loginScene, Constants.title, false);
        DatabaseUtils.OpenConnection();
    }
}
