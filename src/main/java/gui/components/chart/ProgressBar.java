package gui.components.chart;

import javafx.scene.layout.Region;
import net.miginfocom.layout.CC;
import org.tbee.javafx.scene.layout.MigPane;

public class ProgressBar extends MigPane {

    private Region progressRegion;
    private Region progressLine; // Use Region to represent the line
    private double progress; // 0.0 to 1.0 (represents 0% to 100%)
    private double width;
    private double height;

    public ProgressBar() {
        super("fillx, insets 0", "[grow,fill]", "[]");

        this.width = getWidth();
        this.height = 30; // Set height of the progress bar

        // Set the preferred size for the MigPane
        setPrefSize(width, height);
        setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // Create the progress bar region
        progressRegion = new Region();
        progressRegion.setPrefHeight(height);
        progressRegion.setStyle("-fx-background-color: #00bfff;");

        // Add the progress bar region to the MigPane
        add(progressRegion, new CC().growX().pushX());

        // Initialize with 0 progress
        setProgress(0);
    }

    // Method to update the progress
    public void setProgress(double progress) {
        this.progress = Math.max(0, Math.min(progress, 1)); // Ensure progress is between 0 and 1

        // Update progress bar color: red if progress >= 1, else blue
        if (this.progress >= 1.0) {
            progressRegion.setStyle("-fx-background-color: red;");
        } else {
            progressRegion.setStyle("-fx-background-color: #00bfff;");
        }

        // Update the width of the progress bar based on the progress
        progressRegion.setPrefWidth(this.progress * width);
    }

    // Method to draw a vertical line at a given progress point (0 to 1)
    public void setProgressLine(double markerProgress) {
        if (progressLine != null) {
            getChildren().remove(progressLine); // Remove any previous line
        }

        markerProgress = Math.max(0, Math.min(markerProgress, 1)); // Clamp between 0 and 1
        double linePosition = markerProgress * width;

        // Create a new Region to act as the vertical line
        progressLine = new Region();
        progressLine.setPrefWidth(1);  // Set the width of the vertical line
        progressLine.setPrefHeight(height); // Set the height of the line to match the progress bar
        progressLine.setStyle("-fx-background-color: black;");

        // Add the line to the MigPane and set its horizontal position using gapLeft
        add(progressLine, new CC().gapLeft(String.valueOf((int) linePosition)).alignY("top").growX(0));
    }

    // Optionally, you can add getters for current progress
    public double getProgress() {
        return progress;
    }
}
