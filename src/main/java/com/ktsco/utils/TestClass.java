package com.ktsco.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import com.ktsco.modelsdao.ReceivableDAO;

public class TestClass {

	static NumberFormat formatter = new DecimalFormat("#0.00");

	public static void main(String[] args) {
		Map<String, Object> billDetail = ReceivableDAO.reteivedBillDetail(11);
		
		for (String keys : billDetail.keySet()) {
			System.out.println(billDetail.get(keys));
		}
		
	}

}
