package com.ktsco.utils;


public class DateUtils {

	public static String gregoryDate = null;
	public static String jalaliDate = null;

	public static boolean checkEntryDateFormat(String inputDate) {
		boolean result = false;

		if (inputDate.contains("-")) {
			if (inputDate.length() == 10) {
				String[] inputArray = inputDate.split("-");
				if (inputArray[0].length() == 4) {
					if (Integer.parseInt(inputArray[1]) <= 12) {
						if (Integer.parseInt(inputArray[2]) <= 31) {
							result = true;
						} else {
							AlertsUtils.wrongDateEntryAlert("تا ۳۱ برای روز");
						}
					} else {
						AlertsUtils.wrongDateEntryAlert("تا ۱۲ برای ماه ");
					}
				} else {
					AlertsUtils.wrongDateEntryAlert("چهار رقم برای سال");
				}
			} else {
				AlertsUtils.wrongDateEntryAlert("۱۰ رقم");
			}
		} else {
			AlertsUtils.wrongDateEntryAlert("-");
		}

		return result;
	}

	/**
	 * Converting Dates from Jalali to Gregorian Propose of use Store in Database
	 * 
	 * @param inputDate
	 * @return String Gregorian Date
	 */
	public static String convertJalaliToGregory(String inputDate) {
		//List<String> outputList = new ArrayList<String>();

		if (gregoryDate != null)
			gregoryDate = null;

		boolean isDateCorrect = checkEntryDateFormat(inputDate);
		if (isDateCorrect) {
			String[] splittedDate = inputDate.split("-");
			int year = Integer.parseInt(splittedDate[0]);
			int month = Integer.parseInt(splittedDate[1]);
			int day = Integer.parseInt(splittedDate[2]);

			int[] convertedDate = DateConvertor.jalali_to_gregorian(year, month, day);
			int gy = convertedDate[0];
			String gm = String.format("%02d", convertedDate[1]);
			String gd = String.format("%02d" , convertedDate[2]);
//			for (int i = 0; i < convertedDate.length; i++) {
//				if (i>0) {
//					
//				}
//				outputList.add(String.valueOf(convertedDate[i]));
//			}

			gregoryDate = gy + "-" + gm + "-"+gd;
		}

		return gregoryDate;
	}

	/**
	 * Convert dates from Gregory to Jalali Propose of use Showing in GUI
	 * 
	 * @param inputDate
	 * @return String Jalali Date
	 */
	public static String convertGregoryToJalali(String inputDate) {
	//	List<String> outputList = new ArrayList<String>();

		if (jalaliDate != null)
			jalaliDate = null;
		boolean isDateCorrect = checkEntryDateFormat(inputDate);
		if (isDateCorrect) {
			String[] splittedDate = inputDate.split("-");
			int year = Integer.parseInt(splittedDate[0]);
			int month = Integer.parseInt(splittedDate[1]);
			int day = Integer.parseInt(splittedDate[2]);

			int[] convertedDate = DateConvertor.gregorian_to_jalali(year, month, day);
			int jy = convertedDate[0];
			String jm = String.format("%02d", convertedDate[1]);
			String jd = String.format("%02d" , convertedDate[2]);
//			for (int i = 0; i < convertedDate.length; i++) {
//				outputList.add(String.valueOf(convertedDate[i]));
//			}

			jalaliDate = jy + "-" + jm + "-"+jd;
		}

		return jalaliDate;
	}
	

}
