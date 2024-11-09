package gui.pages;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import gui.app.App;
import gui.components.chart.BudgetProgressBar;
import gui.components.chart.DoughnutChart;
import gui.components.util.BalanceLabel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import server.model.Transaction;
import server.model.Category;

public class AnalysisPage extends VBox {
    private static final String INCOME_MODE = "Income";
    private static final String EXPENSE_MODE = "Expense";

    private App app;
    private HBox contentPane;
    private DoughnutChart categoryChart;
    private VBox categoryListPane;
    private Label categoryLabel;
    private String currentMode;

    // Toggle buttons moved to private fields
    private ToggleButton incomeButton;
    private ToggleButton expenseButton;

    public AnalysisPage() {
        app = App.getInstance();
        currentMode = EXPENSE_MODE; // Default mode
        setupLayout();
    }

    private void setupLayout() {
        getStyleClass().add("main-layout");
        refreshPage();
    }

    private VBox createHeaderPane() {
        VBox headerPane = new VBox();
        headerPane.getStyleClass().add("analysis-header");

        // Page title
        Label titleLabel = new Label("Analysis for " + getMonthYearString());
        titleLabel.getStyleClass().add("page-title");
        headerPane.getChildren().add(titleLabel);

        // Combined row for navigation and mode toggle buttons
        HBox buttonRow = new HBox();
        buttonRow.getStyleClass().add("button-row");

        // Initialize toggle buttons
        ToggleGroup modeToggleGroup = new ToggleGroup();
        incomeButton = new ToggleButton(INCOME_MODE);
        incomeButton.setToggleGroup(modeToggleGroup);
        incomeButton.getStyleClass().addAll("analysis-toggle-button", "income-toggle", "fill-neutral", "border-neutral");

        expenseButton = new ToggleButton(EXPENSE_MODE);
        expenseButton.setToggleGroup(modeToggleGroup);
        expenseButton.getStyleClass().addAll("analysis-toggle-button", "expense-toggle", "fill-neutral", "border-neutral");

        incomeButton.setOnAction(e -> changeMode(INCOME_MODE));
        expenseButton.setOnAction(e -> changeMode(EXPENSE_MODE));
        updateToggleButtonStyle(currentMode);

        // Add toggle buttons to the row
        buttonRow.getChildren().addAll(incomeButton, expenseButton);

        // Spacer to push navigation buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buttonRow.getChildren().add(spacer);

        // Navigation buttons
        Button prevButton = createPrevButton();
        Button nextButton = createNextButton();
        buttonRow.getChildren().addAll(prevButton, nextButton);
        updateNavigationButtonsState(nextButton);

        // Add the combined row to the header pane
        headerPane.getChildren().add(buttonRow);

        return headerPane;
    }

    private Button createPrevButton() {
        Button prevButton = new Button("<");
        prevButton.getStyleClass().addAll("nav-button", "prev-button", "fill-neutral", "border-neutral");
        prevButton.setOnAction(e -> navigateToPreviousMonth());
        return prevButton;
    }

    private Button createNextButton() {
        Button nextButton = new Button(">");
        nextButton.getStyleClass().addAll("nav-button", "next-button", "fill-neutral", "border-neutral");
        nextButton.setOnAction(e -> navigateToNextMonth());
        return nextButton;
    }

    private void updateNavigationButtonsState(Button nextButton) {
        nextButton.setDisable(app.latestTime());
    }

    private void navigateToPreviousMonth() {
        app.prevTimeStamp();
        refreshPage();
    }

    private void navigateToNextMonth() {
        app.nextTimeStamp();
        refreshPage();
    }

    private String getMonthYearString() {
        if (app.isMonthView()) {
            Month monthEnum = Month.of(app.getMonth());
            return monthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " + app.getYear();
        } else {
            return String.valueOf(app.getYear());
        }
    }

    private void changeMode(String newMode) {
        if (!currentMode.equals(newMode)) {
            currentMode = newMode;
            refreshPage();
            updateToggleButtonStyle(currentMode);
        }
    }

    private void updateToggleButtonStyle(String activeMode) {
        incomeButton.getStyleClass().remove("active");
        expenseButton.getStyleClass().remove("active");

        if (INCOME_MODE.equals(activeMode)) {
            incomeButton.getStyleClass().add("active");
        } else if (EXPENSE_MODE.equals(activeMode)) {
            expenseButton.getStyleClass().add("active");
        }
    }

    private HBox createContentPane(ObservableList<PieChart.Data> chartData) {
        contentPane = new HBox();
        contentPane.getStyleClass().add("analysis-content");

        if (chartData.isEmpty()) {
            // Create a label to display "No data"
            Label noDataLabel = new Label("No data");
            noDataLabel.getStyleClass().add("no-data-label"); // Optional: add a style class for better styling
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;"); // Style the label as needed
            Pane pane = new Pane(noDataLabel);
            HBox.setHgrow(pane, Priority.ALWAYS);

            // Center the label within the content pane
            contentPane.getChildren().add(pane);
        } else {
        	
            categoryChart = new DoughnutChart(chartData);
            
            HBox.setHgrow(categoryChart, Priority.ALWAYS);

            contentPane.getChildren().add(categoryChart);
        }
        
     // Create category list pane with a CSS-defined fixed width
        categoryListPane = createCategoryListPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(categoryListPane);
        scrollPane.getStyleClass().addAll("scroll-pane", "edge-to-edge", "category-list-scroll", "no-fill");
        contentPane.getChildren().add(scrollPane);
        return contentPane;
    }


    private ObservableList<PieChart.Data> prepareChartData() {
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        List<Transaction> transactions = app.getTransactionList().stream()
            .filter(t -> currentMode.equals(t.getType()) && !t.getType().equals("Transfer"))
            .collect(Collectors.toList());

        Map<String, Double> categoryTotals = new HashMap<>();

        List<Category> categories = currentMode.equals(INCOME_MODE)
            ? app.getIncomeCategoryList()
            : app.getExpenseCategoryList();

        for (Transaction transaction : transactions) {
            int categoryId = transaction.getCategory();
            String categoryName = categories.stream()
                .filter(cat -> cat.getId() == categoryId)
                .map(Category::getName)
                .findFirst()
                .orElse("Unknown");

            categoryTotals.merge(categoryName, transaction.getAmount(), Double::sum);
        }

        categoryTotals.forEach((name, total) -> 
            chartData.add(new PieChart.Data(name, total))
        );

        return chartData;
    }

    private VBox createCategoryListPane() {
        // Create the main VBox for the category list
        categoryListPane = new VBox();
        categoryListPane.getStyleClass().add("category-list");

        // Title for the category list
        categoryLabel = new Label("Categories");
        categoryLabel.getStyleClass().add("category-list-title");
        categoryListPane.getChildren().add(categoryLabel);

        // Get categories based on the current mode
        List<Category> categories = currentMode.equals(INCOME_MODE)
            ? app.getIncomeCategoryList()
            : app.getExpenseCategoryList();

        // Prepare totals per category
        Map<Integer, Double> categoryTotals = app.getTransactionList().stream()
            .filter(t -> currentMode.equals(t.getType()) && !t.getType().equals("Transfer"))
            .collect(Collectors.groupingBy(Transaction::getCategory, 
                Collectors.summingDouble(Transaction::getAmount)));

        // Separate categories with and without a budget
        List<Category> categoriesWithBudget = categories.stream()
            .filter(category -> category.getBudget() != 0)
            .collect(Collectors.toList());
        List<Category> categoriesWithoutBudget = categories.stream()
            .filter(category -> category.getBudget() == 0)
            .collect(Collectors.toList());

        // Add categories with budget first
        for (Category category : categoriesWithBudget) {
            addCategoryItem(category, categoryTotals.getOrDefault(category.getId(), 0.0));
        }

        // Add categories without budget next
        for (Category category : categoriesWithoutBudget) {
            addCategoryItem(category, categoryTotals.getOrDefault(category.getId(), 0.0));
        }
        
        return categoryListPane;
    }



    private void addCategoryItem(Category category, double total) {
        HBox categoryItem = new HBox();
        categoryItem.getStyleClass().addAll("item", "category-item");

        // Label for category name
        Label nameLabel = new Label(category.getName());
        nameLabel.getStyleClass().add("category-name");

        // Spacer to push the total label to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        categoryItem.getChildren().addAll(nameLabel, spacer);

        if (category.getBudget() != 0) {
            BudgetProgressBar progressBar = new BudgetProgressBar(200, 30);
            progressBar.getStyleClass().add("budget-progress");

            // Check if we're at the latest time to set progress correctly
            if (app.latestTime()) {
                LocalDate today = LocalDate.now();
                int currentDay = today.getDayOfMonth();
                int totalDaysInMonth = today.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                progressBar.updateProgress(total, category.getBudget(), currentDay, totalDaysInMonth);
            } else {
                progressBar.updateProgress(total, category.getBudget(), 1, 1);
            }

            categoryItem.getChildren().addAll(progressBar);
        } else {
            BalanceLabel totalLabel = new BalanceLabel(total, true);
            totalLabel.getStyleClass().add("category-total");
            categoryItem.getChildren().add(totalLabel);
        }

        categoryListPane.getChildren().add(categoryItem);
    }


    private void refreshPage() {
        getChildren().clear();
        getChildren().add(createHeaderPane());
        ObservableList<PieChart.Data> chartData = prepareChartData();
        getChildren().add(createContentPane(chartData));
        OverviewPage.getInstance().requestReloading();
    }
}