package gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import server.model.Transaction;
import server.model.Category;
import server.service.CategoryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryListView extends ScrollPane {
    private List<Transaction> transactions;
    private PieChart pieChart;
    private ChoiceBox<String> categoryChoice;
    private Label totalLabel;
    private ListView<String> categoryListView;

    public CategoryListView(List<Transaction> transactions) {
        this.transactions = transactions;
        createElements();
        calculateTotals();
    }

    private void createElements() {
        VBox vbox = new VBox(10);
        
        categoryChoice = new ChoiceBox<>();
        categoryChoice.getItems().addAll("Income", "Expense");
        categoryChoice.setValue("Income"); // Set default value
        categoryChoice.setOnAction(event -> updateView());
        
        totalLabel = new Label();
        
        pieChart = new PieChart();
        pieChart.setPrefSize(300, 150); // Set preferred size for the pie chart
        pieChart.setMaxSize(300, 150); // Set max size to avoid overflow
        
        categoryListView = new ListView<>();
        
        vbox.getChildren().addAll(categoryChoice, totalLabel, pieChart, categoryListView);
        
        setContent(vbox);
    }

    private void updateView() {
        calculateTotals();
    }

    private void calculateTotals() {
        String selectedType = categoryChoice.getValue();
        Map<String, Double> categoryTotals = transactions.stream()
            .filter(transaction -> transaction.getType().equalsIgnoreCase(selectedType))
            .collect(Collectors.groupingBy(
                transaction -> new CategoryService().getCategory(transaction.getCategory()).getName(),
                Collectors.summingDouble(Transaction::getAmount)
            ));
        
        updatePieChart(categoryTotals);
        updateTotalLabel(categoryTotals);
        updateCategoryListView(selectedType);
    }

    private void updatePieChart(Map<String, Double> categoryTotals) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        categoryTotals.forEach((category, total) -> pieChartData.add(new PieChart.Data(category, total)));
        
        pieChart.setData(pieChartData);
    }

    private void updateTotalLabel(Map<String, Double> categoryTotals) {
        double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        totalLabel.setText("Total: " + total);
        totalLabel.setTextFill(Color.BLACK);
    }

    private void updateCategoryListView(String selectedType) {
        // Get all categories of the selected type
        List<Category> categories = new CategoryService().getAllCategories().stream()
                .filter(category -> category.getType().equalsIgnoreCase(selectedType))
                .collect(Collectors.toList());

        ObservableList<String> categoryItems = FXCollections.observableArrayList();

        for (Category category : categories) {
            // Calculate total amount for each category from the transaction list
            double totalAmount = transactions.stream()
                .filter(transaction -> transaction.getCategory() == category.getId())
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            categoryItems.add(category.getName() + ": " + totalAmount);
        }

        categoryListView.setItems(categoryItems);
    }
}
