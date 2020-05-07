package com.ktsco.controllers.mgmnt;

import java.text.NumberFormat;

import com.ktsco.models.mgmt.SellSummaryModel;
import com.ktsco.modelsdao.ExpenseDAO;
import com.ktsco.modelsdao.SaleBillDAO;
import com.ktsco.utils.Commons;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ProfitAndLostReportController {
	
	private Label labelIncomeTotalAmount , labelExpenseTotalAmount, labelRemainederAmount;
	
	private double incomeTotalAmount, expenseTotalAmount, remainedTotalAmount = 0 ; 

	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	private String fromDate, toDate = ""; 
	public void setSearchDates(String fromDate, String toDate) {
		this.fromDate = fromDate; 
		this.toDate = toDate;
	}
	
	public VBox profitAndLostDetailReport() {
		calculateRemainedAmout();
		Font fontFamily = new Font("Tahoma" , 30);
		VBox main = new VBox(); 
		VBox.setVgrow(main, Priority.ALWAYS);
		HBox.setHgrow(main, Priority.ALWAYS);
		HBox incomeBox = new HBox(50);
		incomeBox.setPadding(new Insets(10,10,10,10));
		Label lblIncomeLable = new Label("کل درآمد");
		lblIncomeLable.setFont(fontFamily);
		labelIncomeTotalAmount = new Label(formatPrice.format(incomeTotalAmount));
		labelIncomeTotalAmount.setFont(fontFamily);
		incomeBox.getChildren().addAll(lblIncomeLable, labelIncomeTotalAmount);
		
		HBox expenseBox = new HBox(50);
		expenseBox.setPadding(new Insets(10,10,10,10));
		Label lblExpenseTotal = new Label("کل هزینه");
		lblExpenseTotal.setFont(fontFamily);
		labelExpenseTotalAmount = new Label(formatPrice.format(expenseTotalAmount));
		labelExpenseTotalAmount.setFont(fontFamily);
		expenseBox.getChildren().addAll(lblExpenseTotal, labelExpenseTotalAmount);
		
		HBox remainedBox = new HBox(50);
		remainedBox.setPadding(new Insets(10,10,10,10));
		Label lblRemainedTotal = new Label("سود یا زیان");
		lblRemainedTotal.setFont(fontFamily);
		labelRemainederAmount = new Label(formatPrice.format(remainedTotalAmount));
		labelRemainederAmount.setFont(fontFamily);
		remainedBox.getChildren().addAll(lblRemainedTotal, labelRemainederAmount);
		main.getChildren().clear();
		main.getChildren().addAll(incomeBox,expenseBox, remainedBox);
		Commons.amountLabelFormation(labelIncomeTotalAmount, incomeTotalAmount);
		Commons.amountLabelFormation(labelExpenseTotalAmount, expenseTotalAmount);
		Commons.amountLabelFormation(labelRemainederAmount, remainedTotalAmount);
		return main; 
	}
	

	private double getTotalIncomeAmount(String fromDate, String toDate) {
		double totalIncomeAmount = 0; 
		ObservableList<SellSummaryModel> incomeDataList = SaleBillDAO.getSalesSummaryReport("", fromDate, toDate, "");
		if (!incomeDataList.isEmpty()) {
			for(SellSummaryModel model:incomeDataList) {
				totalIncomeAmount += model.getUsdAmount();
			}
		}
		return totalIncomeAmount;
	}
	
	private double getTotalExpenseAmount(String fromDate, String toDate) {
		double totalExpenseAmount = 0; 
		ObservableList<SellSummaryModel> expenseDataList = ExpenseDAO.getExpenseSummaryReport("", fromDate, toDate, "");
		if (!expenseDataList.isEmpty()) {
			for(SellSummaryModel model:expenseDataList) {
				totalExpenseAmount += model.getUsdAmount();
			}
		}
		return totalExpenseAmount;
	}
	
	private void calculateRemainedAmout() {
		
		 incomeTotalAmount = getTotalIncomeAmount(fromDate, toDate); 
		 expenseTotalAmount = getTotalExpenseAmount(fromDate, toDate); 
		 remainedTotalAmount = incomeTotalAmount - expenseTotalAmount;
	}

}
