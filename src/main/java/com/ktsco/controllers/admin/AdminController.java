package com.ktsco.controllers.admin;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ktsco.controllers.TopViewController;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.ViewClass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminController implements Initializable {

	public static Stage adminStage = new Stage();
	public static BorderPane adminScene;
	static ViewClass view = new ViewClass();
	static TopViewController topView = new TopViewController();

	@FXML
	private static Button btnUsers;
	@FXML
	private static Button btnSetting;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public static void initialAdminPanel() {
		adminScene = view.setBorderPane(Commons.getFxmlPanel("adminPanelFxml"));
		adminScene.setTop(view.setVboxFxml(Commons.getFxmlPanel("topViewFxml")));
		adminScene.setRight(setSideMenu(Commons.getFxmlPanel("sideMenuFxml")));
		adminStage = view.setSceneShowStage(adminScene, Constants.title, true);
	}

	private static VBox setSideMenu(String fxml) {
		VBox vbox = view.setVboxFxml(fxml);
		List<Button> buttonList = new ArrayList<Button>();
		btnUsers = Commons.addMenuButton("ایجاد کاربر");
		btnUsers.setOnAction(event -> {
			allButtonActions(event);
		});
		btnSetting = Commons.addMenuButton("تنظیمات");
		btnSetting.setOnAction(event -> {
			allButtonActions(event);
		});
		buttonList.add(btnUsers);
		buttonList.add(btnSetting);

		for (int i = 0; i < buttonList.size(); i++) {
			vbox.getChildren().add(buttonList.get(i));
		}

		return vbox;
	}

	@FXML
	public static void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnUsers) {
			Commons.reloadTopView(adminScene, btnUsers);
			adminScene.setCenter(view.setPane(Commons.getFxmlPanel("usersPaneFxml")));
			
		}else if (event.getSource() == btnSetting) {
			adminScene.setCenter(view.setCenterPanel(Commons.getFxmlPanel("settingsPaneFxml")));
			Commons.reloadTopView(adminScene, btnSetting);
		}
	}
}
