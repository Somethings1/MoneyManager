package gui.pages;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.tbee.javafx.scene.layout.MigPane;

import gui.app.App;
import gui.components.transaction.form.AddTransactionForm;
import gui.components.transaction.list.TransactionListItem;
import gui.components.transaction.list.TransactionListView;
import gui.components.util.BalanceLabel;
import gui.components.util.Modal;
import gui.components.util.RoundedPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import server.model.Account;
import server.model.Transaction;

public class OverviewPage extends MigPane {
	private boolean isMonthView;
	private App app;
	
	private RoundedPane totalIncomePane;
	private RoundedPane totalExpensePane;
	private RoundedPane balancePane;
	private RoundedPane transactionListPane;
	private RoundedPane balanceChart;
	private RoundedPane incomeChart;
	private RoundedPane expenseChart;
	private MigPane header;
	
	public OverviewPage () {
		super("gap 20, fillx, debug", "[fill|fill|fill]", "[fill|fill|fill|fill]");
		app = App.getInstance();
		isMonthView = app.isMonthView();
		loadAllElements();
		buildLayout();
	}
	
	private void loadAllElements() {
		loadHeader();
		loadTotalIncomePane();
		loadTotalExpensePane();
		loadBalancePane();
		loadTransactionList();
		loadBalanceChart();
		loadIncomeChart();
		loadExpenseChart();
	}
	
	private String getMonthYearString(int month, int year) {
        // Get the Month enum from the integer value
        Month monthEnum = Month.of(month); // 1 = January, 2 = February, ..., 11 = November

        // Get the full name of the month
        String monthName = monthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        // Format the output string as "Month, Year"
        return monthName + ", " + year;
    }
	
	private void loadHeader () {
		MigPane header = new MigPane("ins 0");
		Label pageTitle = new Label();
		if (isMonthView)
			pageTitle.setText("Overview of " + getMonthYearString(app.getMonth(), app.getYear()));
		else
			pageTitle.setText("Overview of " + String.valueOf(app.getYear()));
		pageTitle.setStyle("-fx-font: bold 32px 'Montserrat';");
		header.add(pageTitle);
		this.header = header;
	}
	
	private void loadExpenseChart() {
		RoundedPane pane = new RoundedPane("Expense");
		expenseChart = pane;
	}

	private void loadBalanceChart() {
		RoundedPane pane = new RoundedPane("Balance");
		balanceChart = pane;
	}

	private void loadIncomeChart() {
		RoundedPane pane = new RoundedPane("Income");
		incomeChart = pane;
	}

	private void loadTransactionList() {
		RoundedPane pane = new RoundedPane("TransactionList");
    	
        // Set up the layout and scene
        VBox vbox = new VBox();
        List<Transaction> list = app.getTransactionList();
        for (Transaction trans: list) {
        	vbox.getChildren().add(new TransactionListItem(trans));
        }
        TransactionListView scrollPane = new TransactionListView(list);
        StackPane root = new StackPane();
        root.getChildren().add(scrollPane); 
        
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
     // Create a floating round button
        Button floatingButton = new Button("+");
        floatingButton.setFont(new Font("Montserrat", 25));
        floatingButton.setStyle(
                "-fx-background-radius: 50em; " + // Make the button round
                "-fx-min-width: 50px; " +         // Width of the button
                "-fx-min-height: 50px; " +        // Height of the button
                "-fx-background-color: #ff4081; " + // Color of the button
                "-fx-text-fill: white;"
        );

        // Set up the button event handler
        floatingButton.setOnAction(e -> {
    		AddTransactionForm form = new AddTransactionForm();
    		form.setListView(scrollPane);
            Modal modal = new Modal(form, "Add transaction");
            modal.show();
        });
     // Create a StackPane to overlay the button on top of the ScrollPane
           // Add ScrollPane as the background
        root.getChildren().add(floatingButton); 
        // Align the button to the bottom center and set margins
        StackPane.setAlignment(floatingButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(floatingButton, new Insets(0, 0, 20, 0));
        root.setMaxWidth(Double.MAX_VALUE);
        root.setMaxHeight(Double.MAX_VALUE);
        pane.getChildren().add(root);
		transactionListPane = pane;
	}

	private void loadBalancePane() {
		RoundedPane pane = new RoundedPane("Total Balance");
		BalanceLabel amount = new BalanceLabel(0);
		double sum = 0;
		for (Account account: app.getAccountList()) {
			sum += account.getBalance();
		}
		amount.update(sum, Color.BLACK);
		amount.setFont(new Font("Montserrat Bold", 25));
		pane.getChildren().add(amount);
		balancePane = pane;
	}

	private void loadTotalExpensePane() {
		RoundedPane pane = new RoundedPane("Total Expense");
		BalanceLabel amount = new BalanceLabel(0);
		double sum = 0;
		for (Transaction transaction: app.getTransactionList()) {
			if (transaction.getType().equals("Expense"))
				sum -= transaction.getAmount();
		}
		amount.update(sum);
		amount.setFont(new Font("Montserrat Bold", 25));
		pane.getChildren().add(amount);
		totalExpensePane = pane;
	}

	private void loadTotalIncomePane() {
		RoundedPane pane = new RoundedPane("Total Income");
		BalanceLabel amount = new BalanceLabel(0);
		double sum = 0;
		for (Transaction transaction: app.getTransactionList()) {
			if (transaction.getType().equals("Income"))
				sum += transaction.getAmount();
		}
		amount.update(sum);
		amount.setFont(new Font("Montserrat Bold", 25));
		pane.getChildren().add(amount);
		totalIncomePane = pane;
	}

	private void buildLayout () {
		add(header, "span, wrap");
		add(totalIncomePane, "push");
		add(totalExpensePane, "push");
		add(balancePane, "wrap, push");
		add(transactionListPane, "span 2 3");
		add(balanceChart, "wrap, push");
		add(incomeChart, "wrap, push");
		add(expenseChart, "push");
	}
}
