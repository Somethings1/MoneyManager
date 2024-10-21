package gui.components.chart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import server.model.Category;
import server.model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryPieChart extends PieChart {

    private List<Category> categories;
    private List<Transaction> transactions;

    public CategoryPieChart(List<Category> categories, List<Transaction> transactions) {
        super();
        this.categories = categories;
        this.transactions = transactions;
        setLegendVisible(false);
        setStartAngle(90);
        setPrefWidth(400);
        setPrefHeight(400);
        updateChart();
    }

    // Updates the pie chart data
    public void updateChart() {
        Map<String, Double> categorySums = new HashMap<>();
        
        // Calculate total amount per category
        for (Transaction transaction : transactions) {
            String categoryName = findCategoryName(transaction.getCategory());
            if (categoryName != null) {
                categorySums.put(categoryName, categorySums.getOrDefault(categoryName, 0.0) + transaction.getAmount());
            }
        }

        // Create PieChart.Data and add to the chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categorySums.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        setData(pieChartData);
    }

    // Finds the category name by ID
    private String findCategoryName(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return category.getName();
            }
        }
        return null;
    }

    // Optionally, add setters to update the lists if needed
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        updateChart();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        updateChart();
    }
}
