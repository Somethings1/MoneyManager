package gui.components.chart;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;

public class CircularProgress extends StackPane {
    private Circle backgroundCircle;
    private Circle progressCircle;
    private Label progressText;  // Text to display the percentage
    private Label titleText;     // Text to display the title
    private double progress;

    public CircularProgress(double progress, String title) {
        setupCircles();
        setupText(title);
        this.progress = progress;
        setProgress();
        getStyleClass().add("circular-progress");
    }

    private void setupCircles() {
        // Background circle (always visible)
        backgroundCircle = new Circle();
        backgroundCircle.setStrokeWidth(40);
        backgroundCircle.setFill(Color.TRANSPARENT);
        backgroundCircle.getStyleClass().add("background-circle");

        // Progress circle (will show the progress)
        progressCircle = new Circle();
        progressCircle.setStrokeWidth(40);
        progressCircle.setFill(Color.TRANSPARENT);
        progressCircle.setStrokeLineCap(StrokeLineCap.ROUND);
        progressCircle.getStyleClass().add("progress-circle");
        progressCircle.setRotate(-90);

        // Add both circles to the StackPane
        getChildren().addAll(backgroundCircle, progressCircle);
        
        setMinSize(200, 200); // Ensure it can grow to fill available space
        setMaxSize(300, 300); // Ensure it can grow to fill available space
    }

    private void setupText(String title) {
        // Title text to display above the progress percentage
        titleText = new Label(title);
        titleText.getStyleClass().add("header2");
        
        // Text to show the progress percentage in the center
        progressText = new Label();
        progressText.getStyleClass().add("header1");
        
        // Use a VBox to stack titleText above progressText
        VBox textBox = new VBox(5); // Spacing of 5 between title and percentage
        textBox.getChildren().addAll(titleText, progressText);
        textBox.setAlignment(javafx.geometry.Pos.CENTER); // Center align the text
        
        // Add the VBox to the StackPane (centered automatically)
        getChildren().add(textBox);
    }

    public void setProgress() {
        // Ensure progress is between 0 and 1
        progress = Math.max(0.0, Math.min(1.0, progress));

        // Use parent container size to adjust the radius
        double radius = Math.min(getWidth(), getHeight()) / 2.0;  // Using the smaller dimension for the radius

        // Set the radius of both circles
        backgroundCircle.setRadius(radius);
        progressCircle.setRadius(radius);

        // Update the progress circle's stroke dash array to represent progress
        double circumference = 2 * Math.PI * radius;
        progressCircle.getStrokeDashArray().clear();
        progressCircle.getStrokeDashArray().addAll(progress * circumference, circumference);

        // Update the progress text
        updateProgressText();
        
        if (progress == 1) {
        	progressCircle.getStyleClass().add("progress-complete");
        }
    }

    private void updateProgressText() {
        // Calculate percentage
        int percentage = (int) (progress * 100);
        progressText.setText(percentage + "%");  // Set the text to show the percentage
    }

    // Optional: You can listen for changes in size and update the progress
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        // Update progress when the size of the CircularProgress changes
        if (getWidth() > 0 && getHeight() > 0) {
            setProgress();  // Update progress on resize
        }
    }
}
