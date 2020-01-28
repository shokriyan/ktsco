package com.ktsco.utils;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class TestClass {

	static NumberFormat formatter = new DecimalFormat("#0.00");
	private static final Logger log = Logger.getLogger(TestClass.class);

	public static void main(String[] args) {
		
		
		log.info("This is Logging");
		
	}
}


