package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import main.SoCScanner;
import objects.Scan;

public class MainPageController {

    @FXML
    private ImageView idIcon;

    private static ObservableList<Scan> scansToDisplay = null;
    private static SoCScanner mainApplication = null;

    // Stub Data
    private static Scan[] stubScans = new Scan[8];

    static {

        Scan scan1 = new Scan("A0000001W", "12 June - 01:23AM", "Note this...", "443");
        Scan scan2 = new Scan("A0000002W", "12 June - 01:23AM", "Note thisthis...", "443");
        Scan scan3 = new Scan("A0000003W", "12 June - 01:23AM", "Note thisthisthis...", "443");
        Scan scan4 = new Scan("A0000004W", "12 June - 01:23AM", "Note thisthisthisthis...", "443");
        Scan scan5 = new Scan("A0000005W", "12 June - 01:23AM", "Note thisthisthisthisthis...", "443");
        Scan scan6 = new Scan("A0000006W", "12 June - 01:23AM", "Note thisthisthisthisthisthis...", "443");
        Scan scan7 = new Scan("A0000007W", "12 June - 01:23AM", "Note thisthisthisthisthisthisthis...", "443");
        Scan scan8 = new Scan("A0000008W", "12 June - 01:23AM", "Note thisthisthisthisthisthisthisthisisthisthisthisthisisthisthisthisthis...", "443");

        stubScans[0] = scan1;
        stubScans[1] = scan2;
        stubScans[2] = scan3;
        stubScans[3] = scan4;
        stubScans[4] = scan5;
        stubScans[5] = scan6;
        stubScans[6] = scan7;
        stubScans[7] = scan8;

    }

    @FXML
    protected ListView<Scan> listView;

    public MainPageController() {
        // Initialise models
        scansToDisplay = FXCollections.observableArrayList();

        // Stub
        scansToDisplay.addAll(stubScans);
    }

    public void setMainApp(SoCScanner mainApp) {
        this.mainApplication = mainApp;
    }


    @FXML
    public void initialize() {
        listView.setItems(scansToDisplay);
        initScanListView();
    }


    public void initScanListView() {
        listView.setCellFactory(new Callback<ListView<Scan>, ListCell<Scan>>() {
            @Override
            public ListCell<Scan> call(ListView<Scan> listView) {
                return new ScanListCell();
            }
        });
    }

    public void animate() {

    }

}
