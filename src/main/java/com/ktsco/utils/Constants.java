package com.ktsco.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
	
	public static Map<String, String> accessValue = new HashMap<String, String>();
	public static List<String> unitMeasureList = new ArrayList<String>();
	
	public static final String loginPanelFxml = "/fxml/Login.fxml";
	public static final String adminPanelFxml = "/fxml/AdminPanel.fxml";
	public static final String topViewFxml = "/fxml/TopViewPanel.fxml";
	public static final String styleSheetPath = "/styles/styles.css";
	public static final String sideMenuFxml = "/fxml/SideMenuPanel.fxml";
	public static final String usersPaneFxml = "/fxml/UsersPane.fxml";
	public static final String settingsPaneFxml = "/fxml/SettingsPane.fxml";
	public static final String createNewUserFxml = "/fxml/CreateNewUser.fxml";
	public static final String editUsersFxml = "/fxml/EditUsers.fxml";
	public static final String deleteusersFxml = "/fxml/DeleteUsers.fxml";
	public static final String factoryPanel = "/fxml/FactoryPanel.fxml";
	public static final String productPanelFxml = "/fxml/ProductsPanel.fxml";
	public static final String categoryPanelFxml = "/fxml/CategoryPanel.fxml";
	public static final String inventoryListPanelFxml = "/fxml/InventoryListPanel.fxml";
	
	
	public static final String title = "KTSCO Management";
	public static final String companyName = "شرکت خراسان تک سیم";
	

	public static String buttonText; 
	
	public static boolean reload; 
	
	private static String loggedUser; 
	private static String panelName; 
	private static String displayPanelName; 
	public static String getDisplayPanelName() {
		return displayPanelName;
	}
	public static void setDisplayPanelName(String displayPanelName) {
		Constants.displayPanelName = displayPanelName;
	}
	public static String getPanelName() {
		return panelName;
	}
	public static void setPanelName(String panelName) {
		Constants.panelName = panelName;
	}
	public static String getLoggedUser() {
		return loggedUser; 
	}
	public static void setLoggedUser(String loggedUser) {
		Constants.loggedUser = loggedUser;
	}

	
}
