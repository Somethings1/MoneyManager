package gui.pages;

import org.tbee.javafx.scene.layout.MigPane;

import gui.app.App;
import gui.components.chart.CategoryPieChart;
import gui.components.chart.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import server.model.Category;
import server.model.Transaction;

public class BudgetPage extends MigPane {
	private App app;

	private MigPane header;
	private MigPane incomePane;
	private MigPane expensePane;

	public BudgetPage() {
		super("gap 20, fill, ins 20, debug", "[fill, grow][fill, grow]", "[][grow, fill]");
		app = App.getInstance();
		loadAllElements();
		buildLayout();
	}

	private void loadAllElements() {
		loadHeader();
		loadIncomePane();
		loadExpensePane();
	}

	private void loadHeader() {
		MigPane header = new MigPane("ins 0");
		Label title = new Label("Budget");
		title.setStyle("-fx-font: bold 32px 'Montserrat';");
		header.add(title);
		this.header = header;
	}

	private void loadIncomePane() {
		MigPane pane = new MigPane("flowy, fillx, ins 0", "");
		Label title = new Label("Income categories");
		title.setStyle("-fx-font: bold 25px 'Montserrat';");
		CategoryPieChart incomePieChart = new CategoryPieChart(
				app.getIncomeCategoryList(), app.getTransactionList());
		
		pane.add(title);
		pane.add(incomePieChart);
		
		for (Category cat: app.getIncomeCategoryList()) {
			pane.add(createCategoryItem(cat), "growx");
		}
		
		this.incomePane = pane;
	}
	
	private MigPane createCategoryItem (Category category) {
		MigPane pane = new MigPane("fillx, ins 10, gap 10");
		double totalAmount = getTotalAmount(category);
		Label title = new Label(category.getName());
		Label detail = new Label(String.valueOf(totalAmount) + " / " + String.valueOf(category.getBudget()));
		ProgressBar bar = new ProgressBar();
		bar.setProgress(totalAmount / category.getBudget());
		
		pane.add(title);
		pane.add(detail, "wrap, tag right");
		pane.add(bar, "span, growx");
		return pane;
	}
	
	private double getTotalAmount(Category category) {
		double totalAmount = 0;
		for (Transaction transaction: app.getTransactionList()) {
			if (transaction.getCategoryName().equals(category.getName())) {
				totalAmount += transaction.getAmount();
			}
		}
		return totalAmount;
	}

	private void loadExpensePane() {
		MigPane pane = new MigPane("flowy, fillx, ins 0", "[300:350:]");
		Label title = new Label("Expense categories");
		title.setStyle("-fx-font: bold 25px 'Montserrat';");
		CategoryPieChart incomePieChart = new CategoryPieChart(
				app.getExpenseCategoryList(), app.getTransactionList());
		
		pane.add(title);
		pane.add(incomePieChart);
		
		for (Category cat: app.getExpenseCategoryList()) {
			pane.add(createCategoryItem(cat), "growx");
		}
		this.expensePane = pane;
	}
	
	

	private void buildLayout() {
		add(header, "span");
		add(incomePane);
		add(expensePane);
	}
}
