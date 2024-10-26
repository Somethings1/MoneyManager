package gui.app;

import java.time.LocalDateTime;
import java.util.List;

import server.model.Account;
import server.model.Category;
import server.model.Transaction;
import server.service.AccountService;
import server.service.CategoryService;
import server.service.TransactionService;

public class App {
	private static App instance;
	private List<Transaction> transactionList;
	private List<Account> accountList;
	private List<Category> incomeCategoryList, expenseCategoryList;
	private AccountService accountService;
	private CategoryService categoryService;
	private TransactionService transactionService;
	private boolean monthView;
	private int month, year;
	
	private App () {
		accountService = new AccountService();
		categoryService = new CategoryService();
		transactionService = new TransactionService();
		
		monthView = false;
		month = LocalDateTime.now().getMonthValue();
		year = LocalDateTime.now().getYear();
		
		loadAccountData();
		loadCategoryData();
		loadTransactionData();
		
	}
	
	/*
	 * PRIVATE METHODS
	*/
	
	private void loadAccountData () {
		accountList = accountService.getAllAccounts();
	}
	
	private void loadCategoryData () {
		incomeCategoryList = categoryService.getAllIncomeCategories();
		expenseCategoryList = categoryService.getAllExpenseCategories();
	}
	
	private void loadTransactionData () {
		if (isMonthView())
			transactionList = transactionService.getTransactionsByMonth(month, year);
		else 
			transactionList = transactionService.getTransactionsByYear(year);
	}
	
	/*
	 * PUBLIC METHODS
	*/
	
	public boolean isMonthView () {
		return monthView;
	}
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		loadTransactionData();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		loadTransactionData();
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public List<Category> getIncomeCategoryList() {
		return incomeCategoryList;
	}

	public List<Category> getExpenseCategoryList() {
		return expenseCategoryList;
	}

	public void setMonthView(boolean monthView) {
		this.monthView = monthView;
		loadTransactionData();
	}

	public static App getInstance () {
		if (instance == null)
			instance = new App();
		return instance;
	}

	public void test () {
		System.out.println("hehe");
	}
}
