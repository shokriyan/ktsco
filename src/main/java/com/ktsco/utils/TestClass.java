package com.ktsco.utils;

public class TestClass {


	public static void main(String[] args) throws Exception {
		String value = "سلام ٪٪××";
			byte[] bytes = value.getBytes("UFT-8");
			
			String decodedValue = new String(bytes, "UFT-8");
		System.out.println(decodedValue);
	}

}
