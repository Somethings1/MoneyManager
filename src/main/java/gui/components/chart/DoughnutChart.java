package gui.components.chart;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class DoughnutChart extends PieChart {
    private final Circle innerCircle;

    public DoughnutChart(ObservableList<Data> pieData) {
        super(pieData);

        innerCircle = new Circle();
        
        innerCircle.getStyleClass().add("doughnut-chart-inner-circle");

        // Call to update the labels when the data is set.
        setLabelsVisible(true);
        setStartAngle(90);
    }

    @Override
    protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
        super.layoutChartChildren(top, left, contentWidth, contentHeight);
        addInnerCircleIfNotPresent();
        updateInnerCircleLayout();
    }

    private void addInnerCircleIfNotPresent() {
        if (getData().size() > 0) {
            Node pie = getData().get(0).getNode();
            if (pie.getParent() instanceof Pane) {
                Pane parent = (Pane) pie.getParent();
                if (!parent.getChildren().contains(innerCircle)) {
                    parent.getChildren().add(innerCircle);
                }
            }
        }
    }

    private void updateInnerCircleLayout() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (Data data : getData()) {
            Node node = data.getNode();
            Bounds bounds = node.getBoundsInParent();

            if (bounds.getMinX() < minX) minX = bounds.getMinX();
            if (bounds.getMinY() < minY) minY = bounds.getMinY();
            if (bounds.getMaxX() > maxX) maxX = bounds.getMaxX();
            if (bounds.getMaxY() > maxY) maxY = bounds.getMaxY();
        }

        innerCircle.setCenterX(minX + (maxX - minX) / 2);
        innerCircle.setCenterY(minY + (maxY - minY) / 2);
        innerCircle.setRadius((maxX - minX) / 4);
    }
}
