package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import logic.validation.Validity;
import main.SoCScanner;
import objects.Scan;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import scanners.MatricCardScanner;
import scanners.MatricCardScannerExceptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class MainPageController {

    @FXML
    private Label matricNumberLabel;

    @FXML
    private Label resultMessage;

    @FXML
    private Button scanButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label degreeLabel;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    private static ObservableList<Scan> scansToDisplay = null;
    private static SoCScanner mainApplication = null;
    private MatricCardScanner scanner = null;
    private Validity validity = null;
    private Scan currentCard = null;

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

    public void setValidity(Validity validity) { this.validity = validity; }


    @FXML
    public void initialize() {
        listView.setItems(scansToDisplay);
        initScanListView();
    }

    @FXML
    public void verifyCard(ActionEvent event) {
        disableButtons();
        if (scanner.isAuthenticationEmpty()) {
            resultMessage.setText("Authentication empty");
            return;
        }

        try {
            Scan scanObj = getCardInfo();
            currentCard = scanObj;
            matricNumberLabel.setText(scanObj.getMatricNumber());

            String faculty = validity.facultyOfPerson(scanObj);
            if (faculty.equals("Non-SoC")) {
                degreeLabel.setText(faculty);
                resultMessage.setText("Non-SoC student");

                invalidPerson();
                return;
            } else {
                degreeLabel.setText(faculty);
                scanObj.setFaculty(faculty);
            }

            if (!validity.doneSurvey(scanObj)) {
                resultMessage.setText("Survey not done!!");
                invalidPerson();
                return;
            } else {
                String name = validity.getName(scanObj);
                nameLabel.setText(name);
                scanObj.setFullName(name);
            }

            if (validity.hasCollected(scanObj)) {
                resultMessage.setText("Student has collected before!!!");
                invalidPerson();
                return;
            }

            enableButtons();
            resultMessage.setText("Person is legit!!");

        } catch (MatricCardScannerExceptions.NoTerminalException nte) {
            enableScan();
            resultMessage.setText("No card detected");
        } catch (InvalidFormatException | IOException e) {
            enableScan();
            resultMessage.setText("File write error");
        } catch (EncryptionOperationNotPossibleException eonpe) {
            enableScan();
            resultMessage.setText("Authentication failed");
        } catch (Exception e) {
            enableScan();
            resultMessage.setText("Unexpected error");
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

    private Scan getCardInfo() throws Exception {
        Scan scanObj = null;
        try {
            scanObj = scanner.scanCardInfo();
        } catch (Exception exception) {
            throw exception;
        }
        return scanObj;
    }

    private void invalidPerson() {
        enableButtons();
        acceptButton.setDisable(true);
    }

    @FXML
    public void acceptPerson(ActionEvent event) {
        currentCard.setDetailsField("collected");
        currentCard.setDateTimeStamp(validity.getTimeStamp());
        scansToDisplay.add(0, currentCard);
        validity.recordCollection(currentCard);
        resetDisplay();
    }

    @FXML
    public void rejectPerson(ActionEvent event) {
        currentCard.setDetailsField("rejected");
        currentCard.setDateTimeStamp(validity.getTimeStamp());
        scansToDisplay.add(0,currentCard);
        resetDisplay();
    }

    private void resetDisplay() {
        matricNumberLabel.setText("Card Number");
        nameLabel.setText("Full Name");
        degreeLabel.setText("Degree(s) and Major(s)");
        resultMessage.setText("Click Scan to get data");
    }

    private void disableButtons() {
        scanButton.setDisable(true);
        acceptButton.setDisable(true);
        rejectButton.setDisable(true);
    }

    private void enableButtons() {
        scanButton.setDisable(false);
        acceptButton.setDisable(false);
        rejectButton.setDisable(false);
    }

    private void enableScan() {
        scanButton.setDisable(false);
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
