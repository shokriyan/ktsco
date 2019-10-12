package com.ktsco.utils;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class TestClass {

	static NumberFormat formatter = new DecimalFormat("#0.00");

	public static void main(String[] args) {
		String query = "Select * from accounts";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		List<Map<String, Object>> list = DatabaseUtils.convertResultSetToMap(resultSet);
		System.out.println(list);
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}

	}

}
