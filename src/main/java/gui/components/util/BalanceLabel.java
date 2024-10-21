package gui.components.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class BalanceLabel extends Label {

    public BalanceLabel(double number) {
        this(number, getColorBasedOnValue(number));
    }

    public BalanceLabel(double number, Color color) {
        super(formatNumber(number));
        
    }

    private static Color getColorBasedOnValue(double number) {
        return number >= 0 ? Color.BLUE : Color.RED;
    }

    private static String formatNumber(double number) {
        // Format the number as a string, removing the negative sign for negative numbers
        return String.format("%.2f", Math.abs(number)); // Use Math.abs to get the absolute value
    }
    
    public void update (double number, Color color) {
    	setTextFill(color);
        setAlignment(Pos.CENTER_RIGHT); // Align text to the right
        setMinWidth(Region.USE_PREF_SIZE);
        super.setText(formatNumber(number));
    }
    
    public void update (double number) {
    	update(number, getColorBasedOnValue(number));
    }
}
