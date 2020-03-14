package com.ktsco.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TestClass {
	

	public static void main(String[] args) {
		
		String username = "username";
		String password = "password";
		
		if (!(username.isEmpty() || password.isEmpty())) {
			System.out.println("If part");
		}else {
			System.out.println("Else Part");
		}
	}
}


