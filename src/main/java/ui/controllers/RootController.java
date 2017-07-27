package ui.controllers;

import java.util.logging.*;

import database.io.Collected;
import database.io.Survey;
import database.io.XLSMWriter;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import logic.validation.Validity;
import main.SoCScanner;
import scanners.MatricCardScanner;
import utilities.external.ResizeHelper;

import static javafx.scene.input.MouseEvent.*;

public class RootController {


    // View components::root
    @FXML
    private SplitPane rootView;
    @FXML
    private FlowPane sidebar;
    @FXML
    private BorderPane centerView;
    @FXML
    private HBox menu;

    // View components::sidebar
    @FXML
    private ImageView logo;
    @FXML
    private TextFlow organisationNameArea;
    @FXML
    private Text organisationName;
    @FXML
    private HBox mainButton;
    @FXML
    private ImageView mainButtonIcon;
    @FXML
    private Label mainButtonLabel;
    @FXML
    private HBox loadSaveButton;
    @FXML
    private ImageView loadSaveButtonIcon;
    @FXML
    private Label loadSaveButtonLabel;
    @FXML
    private HBox statisticsButton;
    @FXML
    private ImageView statisticsButtonIcon;
    @FXML
    private Label statisticsButtonLabel;
    @FXML
    private HBox settingsButton;
    @FXML
    private ImageView settingsButtonIcon;
    @FXML
    private Label settingsButtonLabel;


    // View components::center-view
    @FXML
    private BorderPane page;


    // View components::menu
    @FXML
    private Circle maximiseButton;
    @FXML
    private Circle minimiseButton;
    @FXML
    private Circle closeButton;


    // View components::snackbar
    // ...


    // Button effects
    private static final ColorAdjust BUTTON_EFFECT_BRIGHTEN = new ColorAdjust(0, 0.4, 0.4, 0.2);
    private static final ColorAdjust BUTTON_EFFECT_NORMAL = new ColorAdjust(0, 0, 0, 0);
    private static final ColorAdjust BUTTON_EFFECT_DARKEN = new ColorAdjust(0, 0, -0.5, 0);
    private static final ColorAdjust BUTTON_EFFECT_HIGHLIGHT = new ColorAdjust(-0.9, 0.4, 0.4, 0.2);
    private static final ColorAdjust BUTTON_EFFECT_SELECTED = new ColorAdjust(-0.9, 0.4, 0.4, 0.2);
//    private static final ColorAdjust BUTTON_EFFECT_SELECTED = new ColorAdjust(-0.6, 0.4, 0.4, 0.2);


    // Directories
    private static final String DIRECTORY_MAIN = "/ui/views/mainview.fxml";
    private static final String DIRECTORY_DATA = "/ui/views/dataview.fxml";
    private static final String DIRECTORY_ANALYTICS = "/ui/views/analyticsview.fxml";
    private static final String DIRECTORY_SETTINGS = "/ui/views/settingsview.fxml";


    // Variables
    private static SoCScanner mainApp = null;
    private HBox selectedButton = null;


    // Pages
    @FXML
    private static BorderPane mainPage;
    private static BorderPane loadSavePage = null;
    private static BorderPane statisticsPage = null;
    private static BorderPane settingsPage = null;

    // Controllers
    MainPageController mainPageController = null;
    SettingsPageController settingsPageController = null;

    // Scanner
    MatricCardScanner scanner = null;
    Collected collected = null;
    Survey survey = null;
    Validity validity = null;

    public RootController() {

    }

    @FXML
    public void initialize() {

        try {

            setMenuButtonCallbacks();

            scanner = new MatricCardScanner();
            collected = new Collected();
            survey = new Survey();
            validity = new Validity();

            // Set up logic
            validity.setSurvey(survey);
            validity.setCollected(collected);

            // Set pages
            FXMLLoader loader = new FXMLLoader(getClass().getResource(DIRECTORY_MAIN));
            mainPage = loader.load();
            mainPageController = loader.getController();
            mainPageController.setScanner(scanner);
            mainPageController.setValidity(validity);

            loadSavePage = FXMLLoader.load(getClass().getResource(DIRECTORY_DATA));
//            RootController rootControl = new RootController();
//            rootControl.setMainApp(this);

            statisticsPage = FXMLLoader.load(getClass().getResource(DIRECTORY_ANALYTICS));
//            RootController rootControl = new RootController();
//            rootControl.setMainApp(this);

            FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource(DIRECTORY_SETTINGS));
            settingsPage = settingsLoader.load();
            settingsPageController = settingsLoader.getController();
            settingsPageController.setScanner(scanner);
            settingsPageController.setCollected(collected);
            settingsPageController.setSurvey(survey);
//            RootController rootControl = new RootController();
//            rootControl.setMainApp(this);

            // TODO: Sidebar controls (Clickable icons, highlighting, page navigation, resizability, page indexing)

            setSideMenuItem(mainButton, mainPage);
            setSideMenuItem(loadSaveButton, loadSavePage);
            setSideMenuItem(statisticsButton, statisticsPage);
            setSideMenuItem(settingsButton, settingsPage);

            // TODO: Tooltip controls (Implement snackbar popup)


        } catch (Exception exception) {
            Logger.getLogger("").severe(exception.getStackTrace().toString());
            exception.printStackTrace();
        }

    }

    private void setSideMenuItem(HBox item, BorderPane page) {
        item.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();

                if (type.equals(MOUSE_ENTERED) && selectedButton != item) {
                    item.effectProperty().setValue(BUTTON_EFFECT_HIGHLIGHT);
                } else if (type.equals(MOUSE_EXITED) && selectedButton != item) {
                    item.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                } else if (type.equals(MOUSE_CLICKED)) {
                    selectedButton = item;
                    item.effectProperty().setValue(BUTTON_EFFECT_SELECTED);

                    if (item != mainButton) {
                        mainButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    }

                    if (item != loadSaveButton) {
                        loadSaveButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    }

                    if (item != statisticsButton) {
                        statisticsButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    }

                    if (item != settingsButton) {
                        settingsButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    }

                    // Set page
                    centerView.setCenter(page);

                    if (page == mainPage) {
                        mainPageController.animate();
                    }

                }
            }
        });
    }

    private void setMenuButtonCallbacks() {
        closeButton.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();

                if (type.equals(MOUSE_ENTERED)) {
                    closeButton.effectProperty().setValue(BUTTON_EFFECT_BRIGHTEN);
                } else if (type.equals(MOUSE_EXITED)) {
                    closeButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                } else if (type.equals(MOUSE_PRESSED)) {
                    closeButton.effectProperty().setValue(BUTTON_EFFECT_DARKEN);
                } else if (type.equals(MOUSE_RELEASED)) {
                    closeButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    Stage stage = (Stage) rootView.getScene().getWindow();
                    stage.hide();
                    System.exit(0);
                }
            }
        });

        minimiseButton.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();

                if (type.equals(MOUSE_ENTERED)) {
                    minimiseButton.effectProperty().setValue(BUTTON_EFFECT_BRIGHTEN);
                } else if (type.equals(MOUSE_EXITED)) {
                    minimiseButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                } else if (type.equals(MOUSE_PRESSED)) {
                    minimiseButton.effectProperty().setValue(BUTTON_EFFECT_DARKEN);
                } else if (type.equals(MOUSE_RELEASED)) {
                    minimiseButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    Stage stage = (Stage) rootView.getScene().getWindow();
                    stage.setIconified(true);
                }
            }
        });

        maximiseButton.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();

                if (type.equals(MOUSE_ENTERED)) {
                    maximiseButton.effectProperty().setValue(BUTTON_EFFECT_BRIGHTEN);
                } else if (type.equals(MOUSE_EXITED)) {
                    maximiseButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                } else if (type.equals(MOUSE_PRESSED)) {
                    maximiseButton.effectProperty().setValue(BUTTON_EFFECT_DARKEN);
                } else if (type.equals(MOUSE_RELEASED)) {
                    maximiseButton.effectProperty().setValue(BUTTON_EFFECT_NORMAL);
                    Stage stage = (Stage) rootView.getScene().getWindow();
                    stage.setMaximized(!stage.isMaximized());
                }
            }
        });

    }

    @FXML
    public void setMainApp(SoCScanner mainApp) {

        if (this.mainApp == null) {
            this.mainApp = mainApp;
        }

    }

    public void setResizeListener() {
        ResizeHelper.addListenerDeeplyToView(mainPage, mainApp.getResizeListener());
        ResizeHelper.addListenerDeeplyToView(loadSavePage, mainApp.getResizeListener());
        ResizeHelper.addListenerDeeplyToView(statisticsPage, mainApp.getResizeListener());
        ResizeHelper.addListenerDeeplyToView(settingsPage, mainApp.getResizeListener());
    }

}
