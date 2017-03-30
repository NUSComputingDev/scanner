package ui.controllers;

import database.io.XLSMWriter;
import database.io.XMLSheetReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import main.SoCScanner;
import objects.Scan;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import scanners.MatricCardScanner;
import scanners.MatricCardScannerExceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class MainPageController {

    @FXML
    private ImageView idIcon;

    @FXML
    private Label matricNumberLabel;

    @FXML
    private Label accessCodeLabel;

    private static ObservableList<Scan> scansToDisplay = null;
    private static SoCScanner mainApplication = null;
    private MatricCardScanner scanner = null;
    private XLSMWriter writer = null;
    private Boolean isScanning = false;

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
//        scansToDisplay.addAll(stubScans);
    }

    public void setMainApp(SoCScanner mainApp) {
        this.mainApplication = mainApp;
    }

    public void setScanner(MatricCardScanner scanner) {
        this.scanner = scanner;
    }

    public void setWriter(XLSMWriter writer) {
        this.writer = writer;
    }


    @FXML
    public void initialize() {
        listView.setItems(scansToDisplay);
        initScanListView();
    }

    @FXML
    public void scanCard(ActionEvent event) {
        if (scanner.keyA.isEmpty() || scanner.keyB.isEmpty()) {
            accessCodeLabel.setText("Authentication empty");
            return;
        }

        if (!isScanning) {
            isScanning = true;
            try {
                Scan scanObj = getCardInfo();

                matricNumberLabel.setText(scanObj.getMatricNumber());
                isScanning = false;
                accessCodeLabel.setText("Authenticated");
                scansToDisplay.add(0, scanObj);

                if (writer.isValid()) {
                    writer.write(scanObj.getMatricNumber());
                }
            } catch (MatricCardScannerExceptions.NoTerminalException nte) {
                isScanning = false;
                accessCodeLabel.setText("No card detected");
            } catch (InvalidFormatException | IOException e) {
                isScanning = false;
                accessCodeLabel.setText("File write error");
            } catch (EncryptionOperationNotPossibleException eonpe) {
                isScanning = false;
                accessCodeLabel.setText("Authentication failed");
            } catch (Exception e) {
                isScanning = false;
                accessCodeLabel.setText("Unexpected error");
                try {
                    Logger logger = Logger.getLogger("log");
                    FileHandler fh = new FileHandler("./scanner.log");
                    logger.addHandler(fh);
                    logger.severe(Arrays.toString(e.getStackTrace()));

                } catch (Exception except) {
                    return;
                }

            }
        }
    }

    private Scan getCardInfo() throws Exception {
        Scan scanObj = null;
        try {
            scanObj = scanner.scanCardInfo();
        } catch (Exception exception) {
            throw exception;
        }

        return scanObj;
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
