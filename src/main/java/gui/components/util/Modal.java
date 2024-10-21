package gui.components.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Modal {

    private final Stage stage;

    public Modal(Node content, String title) {
        // Create a new Stage
        stage = new Stage();
        stage.setTitle(title);

        // Set the modality to block input events to other windows
        stage.initModality(Modality.APPLICATION_MODAL);

        // Create the content of the modal
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(20));
        
        // Add the provided content Node
        layout.getChildren().addAll(content);

        // Set the scene
        Scene scene = new Scene(layout);
        stage.setScene(scene);
    }

    // Method to show the modal
    public void show() {
        stage.showAndWait(); // Show the modal and wait for it to close
    }

    // Method to close the modal
    public void close() {
        stage.close(); // Close the stage
    }
}
