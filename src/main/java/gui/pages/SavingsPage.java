package gui.pages;

import gui.app.App;
import gui.components.chart.CircularProgress;
import gui.components.form.account.AddSavingForm;
import gui.components.util.Modal;
import gui.components.util.RoundedPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import server.model.Account;
import server.service.AccountService;

import java.util.List;
import java.util.stream.Collectors;

public class SavingsPage extends ScrollPane {
    private static final double VBOX_SPACING = 20;
    private static final double SUMMARY_PANE_SPACING = 20;

    private App app;
    private VBox mainLayout;
    private HBox summaryPane;
    private GridPane detailsPane;
    private VBox headerPane;
    private StackPane rootPane;

    public SavingsPage() {
        app = App.getInstance();
        setupLayout();
        setupColumnAdjustment();
        getStyleClass().add("edge-to-edge");
    }

    private void setupLayout() {
        mainLayout = new VBox(VBOX_SPACING);
        mainLayout.getStyleClass().addAll("main-layout");

        // Ensure that mainLayout expands to take all available space
        VBox.setVgrow(mainLayout, Priority.ALWAYS);

        loadHeader();
        loadSummaryPane();
        loadDetailsPane();

        // Add the header, summary, and details panes to the mainLayout
        mainLayout.getChildren().addAll(headerPane, summaryPane, detailsPane);
        setContent(mainLayout); // Set the content of the ScrollPane to the rootPane

        // Ensure the ScrollPane itself expands correctly within the parent container
        setFitToWidth(true);
        setFitToHeight(true);
    }

    private void loadHeader() {
        headerPane = new VBox(10);
        headerPane.getStyleClass().add("header-pane");
        Label pageTitle = new Label("Savings");
        pageTitle.getStyleClass().add("page-title");
        headerPane.getChildren().add(pageTitle);
    }

    private void loadSummaryPane() {
        summaryPane = new HBox(SUMMARY_PANE_SPACING);
        summaryPane.getStyleClass().add("summary-pane");

        RoundedPane totalPane = createSummaryPane("Total", countSavings());
        RoundedPane completedPane = createSummaryPane("Completed", countCompletedSavings());
        RoundedPane inProgressPane = createSummaryPane("In progress", countInProgressSavings());

        summaryPane.getChildren().addAll(totalPane, completedPane, inProgressPane);
    }

    private RoundedPane createSummaryPane(String title, int value) {
        RoundedPane pane = new RoundedPane(title);
        Label label = new Label(String.valueOf(value));
        
        if (title.equals("Total")) label.getStyleClass().add("fill-neutral");
        else if (title.equals("Completed")) label.getStyleClass().add("fill-green");
        else label.getStyleClass().add("fill-blue");
        label.getStyleClass().add("header1");
        
        pane.getChildren().add(label);
        HBox.setHgrow(pane, Priority.ALWAYS);
        pane.setPrefWidth(100);
        return pane;
    }

    private int countSavings() {
        return getSavingsAccounts().size();
    }

    private int countCompletedSavings() {
        return (int) getSavingsAccounts().stream()
                .filter(account -> account.getBalance() >= account.getGoal())
                .count();
    }

    private int countInProgressSavings() {
        return (int) getSavingsAccounts().stream()
                .filter(account -> account.getBalance() < account.getGoal())
                .count();
    }

    private List<Account> getSavingsAccounts() {
        return app.getAccountList().stream()
                .filter(account -> account.getGoal() != 0)
                .collect(Collectors.toList());
    }

    private void loadDetailsPane() {
        detailsPane = new GridPane();
        detailsPane.getStyleClass().add("details-pane");
        detailsPane.setHgap(10);
        detailsPane.setVgap(10);
        updateDetailsPaneColumns(getColumnCountBasedOnWidth(getWidth()));
        populateDetailsPane();
    }

    private void updateDetailsPaneColumns(int columns) {
        detailsPane.getColumnConstraints().clear();
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            detailsPane.getColumnConstraints().add(column);
        }
    }

    private void populateDetailsPane() {
        detailsPane.getChildren().clear();

        List<Account> savingsAccounts = getSavingsAccounts().stream()
                .sorted((a, b) -> Boolean.compare(a.getBalance() >= a.getGoal(), b.getBalance() >= b.getGoal()))
                .collect(Collectors.toList());

        int column = 0;
        int row = 0;
        int columns = detailsPane.getColumnConstraints().size();

        for (Account account : savingsAccounts) {
            VBox savingBox = createSavingBox(account);
            detailsPane.add(savingBox, column, row);
            if (++column >= columns) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createSavingBox(Account account) {
        VBox savingBox = new VBox(10);
        savingBox.setAlignment(Pos.CENTER);
        savingBox.getStyleClass().add("saving-box");

        double progress = Math.min(1.0, account.getBalance() / account.getGoal());
        CircularProgress progressIndicator = new CircularProgress(progress, account.getName());

        savingBox.getChildren().add(progressIndicator);
        return savingBox;
    }

    public void reloadSavingsPage() {
        mainLayout.getChildren().clear();
        loadHeader();
        loadSummaryPane();
        loadDetailsPane();
        mainLayout.getChildren().addAll(headerPane, summaryPane, detailsPane);
    }

    private int getColumnCountBasedOnWidth(double width) {
        return (int) ((width - 1) / 300);
    }

    private void setupColumnAdjustment() {
        widthProperty().addListener((observable, oldWidth, newWidth) -> {
            int columns = getColumnCountBasedOnWidth(newWidth.doubleValue());
            updateDetailsPaneColumns(columns);
            populateDetailsPane();
        });
    }
}
