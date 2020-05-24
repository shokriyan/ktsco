package com.ktsco.enums;

public enum Dictionary {
	
	Search("جستجو"),
	Refresh("بارگزاری مجدد"),
	FromDate("از تاریخ"),
	ToDate("تا تاریخ"),
	EmployeeList("لیست کارمندان"),
	FactoryExportSearch("جستجو خروجی کارخانه"),
	ExportId("کد خروجی"),
	Date("تاریخ"),
	Employee("کارمند"),
	Quantity("تعداد"),
	Product("محصول"),
	Edit("ویرایش"),
	ProductList("لیست محصولات"), 
	Update("اعمال تغییرات"),
	Cancel("انصراف"),
	ExportUpdateMessage("ویرایش خروجی کارخانه"),
	Return("بازگشت"),
	Delete("حذف"),
	SuccessMessage("با موفقیت انجام شد"),
	Code("کد"),
	ProductCode("کد محصول"),
	UnitMeasure("واحد شمارش"), 
	UnitPrice("فیات"),
	LineTotal("جمع ردیف"),
	LineNumber("ردیف")
	;
	private final String value;
	
	Dictionary(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
