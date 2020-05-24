package com.ktsco.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.text.Font;

public class Constants {
	
	public static Map<String, String> accessValue = new HashMap<String, String>();
	public static Map<String, String> currencies = new HashMap<String, String>();
	public static List<String> depositTypeList = new ArrayList<String>();
	public static List<String> payTerms = new ArrayList<String>();
	
	
	public static List<String> unitMeasureList = new ArrayList<String>();
	public static final String configFilePath = "/configuration/config.properties";
	public static final String fxmlsPropFilePath = "/configuration/fxmls.properties";
	public static final String farsiTextPath = "/configuration/farsiTexts.properties";

	public static final String title = Commons.getConfigurationPropertyValue("applicationTitle");
	public static final String companyName = "شرکت خراسان تک سیم";
	
	public static String buttonText; 
	
	public static boolean reload; 
	
	private static String loggedUser; 
	private static String panelName; 
	private static String displayPanelName;
	public final static Font pageTitleFont = new Font("Tahoma" , 18);
	public final static Font elementFonts = new Font("Tahoma" , 13);
	public final static Insets globalPadding = new Insets(5,5,5,5);
	public final static double globalSpacing = 20; 
	public final static double mainPanelPrefHeight = 520;
	public final static double mainPanelPrefWidth = 1080;
	public final static double fieldsPrefHeight = 28;
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
