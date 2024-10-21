package gui.pages;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class SidebarNavigationPane extends MigPane {  // Change to extend BorderPane
    
    private StackPane contentPane;
    private Pane overviewPane;
    private Pane budgetPane;
    private Pane accountPane;
    private Pane savingsPane;
    private Pane settingsPane;

    public SidebarNavigationPane() {
    	super("fill, gap 0, ins 0, debug", "[][fill, grow]", "[fill]");
        // Set up the layout
        setPadding(new Insets(0));
        setStyle("-fx-background-color: #FFFFFF;"); // Background color of the Pane

        // Initialize navigation panes
        initializeNavPanes();
        
        // Create the sidebar
        MigPane sidebar = createSidebar();

        // Create the content pane
        contentPane = new StackPane();
        // Bind the height of the ScrollPane to the height of the scene
        //VBox.setVgrow(contentPane, javafx.scene.layout.Priority.ALWAYS);
        

        // Load the default page (Overview)
        loadPage(createOverviewPage());

        // Set the sidebar to the left and contentPane to the center of the BorderPane
        add(sidebar);
        add(contentPane);
    }

    // Create the sidebar with navigation panes
    private MigPane createSidebar() {
        MigPane sidebar = new MigPane("ins 0, gap 0, fillx, flowy", "[fill]", "[]");
        //sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(150); // Set the preferred width for the sidebar

        // Add navigation panes
        sidebar.add(overviewPane);
        sidebar.add(budgetPane);
        sidebar.add(accountPane);
        sidebar.add(savingsPane);
        sidebar.add(settingsPane);
        return sidebar;
    }

    // Load the selected MigPane content into the StackPane
    private void loadPage(MigPane page) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(page);
    }

    // Initialize navigation panes
    private void initializeNavPanes() {
        overviewPane = createNavPane("Overview");
        overviewPane.setStyle("-fx-background-color: #4682B4;");
        budgetPane = createNavPane("Budget");
        accountPane = createNavPane("Account");
        savingsPane = createNavPane("Savings");
        settingsPane = createNavPane("Settings");

        // Set event handlers for changing pages
        setNavPaneEventHandlers(overviewPane, createOverviewPage());
        setNavPaneEventHandlers(budgetPane, createBudgetPage());
        setNavPaneEventHandlers(accountPane, createAccountPage());
        setNavPaneEventHandlers(savingsPane, createSavingsPage());
        setNavPaneEventHandlers(settingsPane, createSettingsPage());
    }

    // Create a Pane for navigation with hover effects
    private MigPane createNavPane(String title) {
        MigPane navPane = new MigPane();
        navPane.setPrefHeight(40); // Dark Slate Gray

        Label paneText = new Label(title);
        //paneText.setPadding(new Insets(5));
        navPane.add(paneText);

        // Center the text in the pane
        //navPane.setPadding(new Insets(5));
        //paneText.layoutXProperty().bind(navPane.widthProperty().subtract(paneText.getLayoutBounds().getWidth()).divide(2));
        //paneText.layoutYProperty().bind(navPane.heightProperty().subtract(paneText.getLayoutBounds().getHeight()).divide(2));

        return navPane;
    }

    // Set event handlers for each navigation pane
    private void setNavPaneEventHandlers(Pane pane, MigPane page) {
        pane.setOnMouseClicked(e -> {
            clearActiveStyles(); // Clear previous active styles
            loadPage(page); // Load the corresponding page
            pane.setStyle("-fx-background-color: #4682B4;"); // Active background color
        });

        pane.setOnMouseEntered(e -> {
            if (!pane.getStyle().contains("4682B4")) { // If not active
                pane.setStyle("-fx-background-color: #6CA6D5;"); // Hover background color
            }
        });

        pane.setOnMouseExited(e -> {
            if (!pane.getStyle().contains("4682B4")) { // If not active
                pane.setStyle("-fx-background-color: white#2F4F4F;"); // Reset to original color
            }
        });
    }

    // Clear active styles from all panes
    private void clearActiveStyles() {
        overviewPane.setStyle("-fx-background-color: white;");
        budgetPane.setStyle("-fx-background-color: white;");
        accountPane.setStyle("-fx-background-color: white;");
        savingsPane.setStyle("-fx-background-color: white;");
        settingsPane.setStyle("-fx-background-color: white;");
    }

    // Create the content pages as MigPane instances
    private MigPane createOverviewPage() {
        MigPane overviewPage = new OverviewPage();
        return overviewPage;
    }

    private MigPane createBudgetPage() {
        MigPane budgetPage = new BudgetPage();
        // Add more components to the budgetPage as needed
        return budgetPage;
    }

    private MigPane createAccountPage() {
        MigPane accountPage = new MigPane();
        accountPage.add(new Text("Account Page Content"), "wrap");
        // Add more components to the accountPage as needed
        return accountPage;
    }

    private MigPane createSavingsPage() {
        MigPane savingsPage = new MigPane();
        savingsPage.add(new Text("Savings Page Content"), "wrap");
        // Add more components to the savingsPage as needed
        return savingsPage;
    }

    private MigPane createSettingsPage() {
        MigPane settingsPage = new MigPane();
        settingsPage.add(new Text("Settings Page Content"), "wrap");
        // Add more components to the settingsPage as needed
        return settingsPage;
    }
}
