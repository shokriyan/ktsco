package com.ktsco.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestClass {


	public static void main(String[] args) throws Exception {
		System.out.println(DateUtils.convertGregoryToJalali(getTodaysDate()));
	}
	
	public static String getTodaysDate () {
		String todayDate = null; 
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/DD");
		LocalDate now = LocalDate.now();
		
		todayDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);
		
		return todayDate;
	}

}
