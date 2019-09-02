package com.ktsco.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Scanner;

import com.ktsco.modelsdao.ReceivableDAO;

public class TestClass {

	static NumberFormat formatter = new DecimalFormat("#0.00");

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Inter a name");
			 
			System.out.println(scanner.nextLine());
	}

}
