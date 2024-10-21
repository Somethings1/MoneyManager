import gui.pages.SidebarNavigationPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SidebarNavigationPane page = new SidebarNavigationPane();
        Scene scene = new Scene(page, 1000, 600);

        // Set up the stage
        primaryStage.setTitle("Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
