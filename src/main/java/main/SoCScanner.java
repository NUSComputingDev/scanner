package main;

import java.util.logging.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.controllers.RootController;
import utilities.external.ResizeHelper;

public class SoCScanner extends Application {

    // Directories
    private static final String DIRECTORY_CSS_DEFAULT = "/ui/styles/default.css";
    private static final String DIRECTORY_ROOT = "/ui/views/rootview.fxml";

    // Programmed Styles
    private static final String TRANSPARENT = "-fx-background-color: transparent;";

    // Window dimensions
    private static final double MIN_HEIGHT = 420;
    private static final double MIN_WIDTH = 800;
    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 1000;

    // Windows, scenes, views
    private static Scene scene = null;
    private static BorderPane rootwrapper = null;
    private static SplitPane rootview = null;

    private RootController rootController = null;

    // Resize Listener
    private ResizeHelper.ResizeListener resizeListener = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {

            // Initialise root view
            rootwrapper = new BorderPane();
            rootwrapper.setStyle(TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(DIRECTORY_ROOT));
            rootview = loader.load();
            rootController = loader.getController();
            rootController.setMainApp(this);
            rootwrapper.setCenter(rootview);

            // Initialise scene
            scene = new Scene(rootwrapper, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            scene.setFill(Color.TRANSPARENT);

            // Set CSS stylesheet
            scene.getStylesheets().add(DIRECTORY_CSS_DEFAULT);

            // Set window-less, border-less stage
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);

            // Set resizability
            resizeListener = ResizeHelper.addResizeListener(primaryStage);
            rootController.setResizeListener();
            primaryStage.minHeightProperty().set(MIN_HEIGHT);
            primaryStage.minWidthProperty().set(MIN_WIDTH);
            primaryStage.show();


        } catch (Exception exception) {
            Logger.getLogger("").severe(exception.getStackTrace().toString());
            exception.printStackTrace();
        }

    }

    public Scene getScene() {
        return scene;
    }

    public ResizeHelper.ResizeListener getResizeListener() {
        return resizeListener;
    }
}
