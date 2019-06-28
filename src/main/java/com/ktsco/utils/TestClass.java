package com.ktsco.utils;

public class TestClass {

	private static long start;

	public static void main(String[] args) throws Exception {
		getStartTime();
		
		Thread.sleep(4560);
		
		double waitTime = elapsedTime();
		
		System.out.println("Waiting time in Secont " + waitTime);
	}

	public static void getStartTime() {
		start = System.currentTimeMillis();
	}

	public static double elapsedTime() {
		long now = System.currentTimeMillis();
		return (now - start) / 1000.0;
	}

}
