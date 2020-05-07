package com.ktsco.controllers.mgmnt;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.ktsco.models.csr.BankBalanceModel;
import com.ktsco.models.csr.CurrencyModel;
import com.ktsco.models.mgmt.SellSummaryModel;
import com.ktsco.modelsdao.AccountsDAO;
import com.ktsco.modelsdao.CurrencyDAO;
import com.ktsco.modelsdao.ExpenseDAO;
import com.ktsco.modelsdao.SaleBillDAO;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class BalanceSheetReportController {

	ObservableList<CurrencyModel> currencyDataList = FXCollections.observableArrayList();
	ObservableList<BankBalanceModel> bankDetailData = FXCollections.observableArrayList();
	NumberFormat formatPrice = NumberFormat.getCurrencyInstance();
	double bankBalanceTotalValue = 0; 
	double totalReceivableAmount = 0;
	double totalPayableAmount = 0; 
	double balanceSheetFinalAmount = 0; 
	private  String searchToDate = "";
	
	public void setSearchToDate(String toDate) {
		this.searchToDate = toDate;
	}
	
	public BalanceSheetReportController() {

	}

	public ScrollPane createBalanceSheetPage() {
		ScrollPane mainPane = new ScrollPane();
		mainPane.setPadding(new Insets(5, 5, 5, 5));
		VBox.setVgrow(mainPane, Priority.ALWAYS);
		HBox.setHgrow(mainPane, Priority.ALWAYS);
		
		VBox reportPane = balanceSheetReportPane();
		mainPane.setContent(reportPane);
		return mainPane;
	}
	
	private VBox balanceSheetReportPane() {
		VBox report = new VBox(20);
		report.setPadding(new Insets(5, 5, 5, 5));
		VBox.setVgrow(report, Priority.ALWAYS);
		HBox.setHgrow(report, Priority.ALWAYS);
		VBox bankBalancePanel = createBankBalancePanel();
		HBox receivablePanel =createReceivablePanel();
		HBox payablePanel = createPayablePanel();
		HBox balanceSheetFinalPanel = createTotalBalanceSheetPanel();
		report.getChildren().clear();
		report.getChildren().addAll(bankBalancePanel, receivablePanel , payablePanel , balanceSheetFinalPanel);
		return report;
	}

	private HBox bankBalanceHeader() {
		Font fontFamily = new Font("Tahoma", 13);
		HBox bankHeader = new HBox(20);
		HBox.setHgrow(bankHeader, Priority.ALWAYS);
		bankHeader.setPadding(new Insets(5, 5, 5, 5));
		Label lblBankName = new Label();
		lblBankName.setText("نام بانک");
		lblBankName.setPrefWidth(250);
		lblBankName.setFont(fontFamily);

		Label lblBankAccnt = new Label();
		lblBankAccnt.setText("شماره حساب");
		lblBankAccnt.setPrefWidth(250);
		lblBankAccnt.setFont(fontFamily);
		Label lblBankBalance = new Label();
		lblBankBalance.setText("موجودی حساب به دلار");
		lblBankBalance.setPrefWidth(250);
		lblBankBalance.setFont(fontFamily);
		bankHeader.getChildren().addAll(lblBankAccnt, lblBankName, lblBankBalance);
		return bankHeader;
	}
	private HBox bankBalanceFooter(double totalAmount) {
		Font fontFamily = new Font("Tahoma", 15);
		HBox bankFooter= new HBox(20);
		HBox.setHgrow(bankFooter, Priority.ALWAYS);
		bankFooter.setPadding(new Insets(5, 5, 5, 5));
		Region emptyRegion = new Region();
		emptyRegion.setPrefWidth(250);

		Label lblTotalAmount = new Label();
		lblTotalAmount.setText("جمع کل بانک ها");
		lblTotalAmount.setPrefWidth(250);
		lblTotalAmount.setFont(fontFamily);
		Label lblValueTotalAmount = new Label();
		lblValueTotalAmount.setText(formatPrice.format(totalAmount));
		lblValueTotalAmount.setPrefWidth(250);
		lblValueTotalAmount.setFont(fontFamily);
		lblValueTotalAmount.setAlignment(Pos.BASELINE_LEFT);
		Commons.amountLabelFormation(lblValueTotalAmount, totalAmount);
		bankFooter.getChildren().addAll(emptyRegion, lblTotalAmount, lblValueTotalAmount);
		return bankFooter;
	}
	private VBox createBankBalancePanel() {
		bankBalanceTotalValue = 0; 
		generateBankDetailReport();
		VBox bankBalancePanel = new VBox(2);
		VBox.setVgrow(bankBalancePanel, Priority.ALWAYS);
		HBox header = bankBalanceHeader();
		List<HBox> dataRowList = new ArrayList<HBox>();
		Font fontFamily = new Font("Tahoma", 13);
		dataRowList.add(header);
		if (!bankDetailData.isEmpty()) {
			for (BankBalanceModel dataModal : bankDetailData) {
				
				HBox bankRowData = new HBox(20);
				HBox.setHgrow(bankRowData, Priority.ALWAYS);
				bankRowData.setPadding(new Insets(5, 5, 5, 5));
				Label txtBankName = new Label();
				txtBankName.setText(dataModal.getBankName());
				txtBankName.setPrefWidth(250);
				txtBankName.setFont(fontFamily);

				Label txtBankAccount = new Label();
				txtBankAccount.setText(dataModal.getBankAccount());
				txtBankAccount.setPrefWidth(250);
				txtBankAccount.setFont(fontFamily);
				Label txtBalanaceAmount = new Label();
				double balanceAmount = Double.parseDouble(dataModal.getBankBalance());
				txtBalanaceAmount.setText(formatPrice.format(balanceAmount));
				txtBalanaceAmount.setPrefWidth(250);
				txtBalanaceAmount.setAlignment(Pos.BASELINE_LEFT);
				txtBalanaceAmount.setFont(fontFamily);
				
				Commons.amountLabelFormation(txtBalanaceAmount,balanceAmount);
				
				bankRowData.getChildren().addAll(txtBankAccount, txtBankName, txtBalanaceAmount);
				dataRowList.add(bankRowData);
				bankBalanceTotalValue += balanceAmount;
			}
		}
		HBox bankFooter = bankBalanceFooter(bankBalanceTotalValue);
		dataRowList.add(bankFooter);
		bankBalancePanel.getChildren().clear();
		bankBalancePanel.getChildren().addAll(dataRowList);
		return bankBalancePanel;
	}

	private void generateBankDetailReport() {
		bankDetailData.clear();
		getCurrencyRates(searchToDate);
		ObservableList<BankBalanceModel> bankDetailList = AccountsDAO.getBankSummaryReport("", searchToDate);
		
		for (BankBalanceModel modal : bankDetailList) {
			String currencyType = modal.getCurrency();
			double originalBankBalance = Double.parseDouble(modal.getBankBalance());
			BigDecimal rate = findCurrencyRate(currencyType);
			BigDecimal dolorAmount = BigDecimal.valueOf(originalBankBalance).multiply(rate);
			BankBalanceModel dataModal = new BankBalanceModel(0, modal.getBankAccount(), modal.getBankName(),
					currencyType, dolorAmount.doubleValue());
			bankDetailData.add(dataModal);
		}

	}

	private void getCurrencyRates(String date) {
		date= (date.equalsIgnoreCase("")) ? Commons.getTodaysDate() : DateUtils.convertJalaliToGregory(date);
		currencyDataList = CurrencyDAO.getTodaysDateCurrency(date);
	}

	private BigDecimal findCurrencyRate(String currencyType) {
		BigDecimal currencyRate = null;
		if (!currencyDataList.isEmpty()) {
			for (CurrencyModel modal : currencyDataList) {
				if (Commons.getCurrencyKey(modal.getCurrency()).equalsIgnoreCase(currencyType)) {
					currencyRate = new BigDecimal(modal.getRate());
				}
			}
		}
		return currencyRate;
	}

	private void accountReceivableReport() {
		totalReceivableAmount = 0; 
		ObservableList<SellSummaryModel> receivableData = SaleBillDAO.getSalesUSDSummaryReport("", "", searchToDate);
		for (SellSummaryModel modal:receivableData) {
			totalReceivableAmount +=modal.getUsdAmount();
		}
	}
	
	private HBox createReceivablePanel() {
		Font fontFamily = new Font("Tahoma", 15);
		accountReceivableReport();
		HBox receivablePanel = new HBox(20);
		
		Label fieldName = new Label();
		fieldName.setFont(fontFamily);
		fieldName.setText("مجموع کل مبلغ دریافت نشده");
		fieldName.setPrefWidth(250);
		Region region = new Region();
		region.setPrefWidth(250);
		Label fieldValue = new Label();
		fieldValue.setFont(fontFamily);
		fieldValue.setPrefWidth(250);
		fieldValue.setAlignment(Pos.BASELINE_LEFT);
		fieldValue.setText(formatPrice.format(totalReceivableAmount));
		Commons.amountLabelFormation(fieldValue, bankBalanceTotalValue);
		
		receivablePanel.getChildren().clear();
		receivablePanel.getChildren().addAll(fieldName,region, fieldValue);
		return receivablePanel;
		
	}
	
	private void accountPayableReport() {
		totalPayableAmount = 0; 
		ObservableList<SellSummaryModel> receivableData = ExpenseDAO.getExpensePaymentSummaryReport("", "", searchToDate);
		for (SellSummaryModel modal:receivableData) {
			totalPayableAmount +=modal.getUsdAmount();
		}
		
		totalPayableAmount = totalPayableAmount * -1;
	}
	
	private HBox createPayablePanel() {
		Font fontFamily = new Font("Tahoma", 15);
		accountPayableReport();
		HBox reportPanel = new HBox(20);
		
		Label fieldName = new Label();
		fieldName.setFont(fontFamily);
		fieldName.setText("مجموع کل مبلغ پرداخت نشده");
		fieldName.setPrefWidth(250);
		Region region = new Region();
		region.setPrefWidth(250);
		Label fieldValue = new Label();
		fieldValue.setFont(fontFamily);
		fieldValue.setPrefWidth(250);
		fieldValue.setAlignment(Pos.BASELINE_LEFT);
		fieldValue.setText(formatPrice.format(totalPayableAmount));
		Commons.amountLabelFormation(fieldValue, totalPayableAmount);
		
		reportPanel.getChildren().clear();
		reportPanel.getChildren().addAll(fieldName,region, fieldValue);
		return reportPanel;
		
	}
	
	private void balanceSheetReport() {
		double totalIncome = bankBalanceTotalValue + totalReceivableAmount; 
		double totalExpense = totalPayableAmount; 
		
		balanceSheetFinalAmount = totalIncome + totalExpense; 
	}
	
	private HBox createTotalBalanceSheetPanel() {
		Font fontFamily = new Font("Tahoma Bold", 18);
		balanceSheetReport();
		HBox reportPanel = new HBox(20);
		
		Label fieldName = new Label();
		fieldName.setFont(fontFamily);
		fieldName.setText("محاسبه سود خالص");
		fieldName.setPrefWidth(250);
		Region region = new Region();
		region.setPrefWidth(250);
		Label fieldValue = new Label();
		fieldValue.setFont(fontFamily);
		fieldValue.setPrefWidth(250);
		fieldValue.setAlignment(Pos.BASELINE_LEFT);
		fieldValue.setText(formatPrice.format(balanceSheetFinalAmount));
		Commons.amountLabelFormation(fieldValue, balanceSheetFinalAmount);
		
		reportPanel.getChildren().clear();
		reportPanel.getChildren().addAll(fieldName,region, fieldValue);
		return reportPanel;
		
	}
}
