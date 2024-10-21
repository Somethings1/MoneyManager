import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class SmoothLineChart extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smooth Line Chart Example");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Smooth Line Chart");

        // Create a series and add sample data
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data Series");

        // Add sample data points (You can replace these with your data)
        for (int i = 0; i <= 10; i++) {
            series.getData().add(new XYChart.Data<>(i, Math.sin(i))); // Example data (sine wave)
        }

        lineChart.getData().add(series);

        // Create the smooth line path
        Path smoothLine = createSmoothLine(series.getData());

        // Create a StackPane to overlay the path on the line chart
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(lineChart, smoothLine);

        BorderPane root = new BorderPane();
        root.setCenter(stackPane);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Path createSmoothLine(List<XYChart.Data<Number, Number>> dataPoints) {
        Path path = new Path();
        path.setStroke(Color.BLUE);
        path.setStrokeWidth(2);

        if (dataPoints.isEmpty()) return path;

        // Start the path at the first point
        MoveTo moveTo = new MoveTo(dataPoints.get(0).getXValue().doubleValue(), dataPoints.get(0).getYValue().doubleValue());
        path.getElements().add(moveTo);

        // Add cubic Bezier curves between points
        for (int i = 1; i < dataPoints.size() - 1; i++) {
            double x0 = dataPoints.get(i - 1).getXValue().doubleValue();
            double y0 = dataPoints.get(i - 1).getYValue().doubleValue();
            double x1 = dataPoints.get(i).getXValue().doubleValue();
            double y1 = dataPoints.get(i).getYValue().doubleValue();
            double x2 = dataPoints.get(i + 1).getXValue().doubleValue();
            double y2 = dataPoints.get(i + 1).getYValue().doubleValue();

            // Control points for the cubic Bezier curve
            double cx1 = x1; // First control point
            double cy1 = y0; // Vertical control point for smoothness
            double cx2 = x1; // Second control point
            double cy2 = y1; // Horizontal control point for smoothness

            path.getElements().add(new LineTo(cx1, cy1));
            path.getElements().add(new LineTo(cx2, cy2));
            path.getElements().add(new LineTo(x2, y2));
        }

        return path;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
