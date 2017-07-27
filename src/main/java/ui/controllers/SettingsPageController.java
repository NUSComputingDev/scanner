package ui.controllers;

import database.io.Collected;
import database.io.Survey;
import database.io.XLSMWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import scanners.MatricCardScanner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class SettingsPageController {

    @FXML
    private PasswordField keyA;

    @FXML
    private PasswordField keyB;

    @FXML
    private TextField surveyFile;

    @FXML
    private TextField collectedFile;

    @FXML
    private Label surveyFileError;

    @FXML
    private Label collectedFileError;

    private MatricCardScanner scanner = null;
    private Survey survey = null;
    private Collected collected = null;

    public SettingsPageController() {

    }

    public void setScanner(MatricCardScanner scanner) {
        this.scanner = scanner;
    }

    public void setCollected(Collected collected) {
        this.collected = collected;
    }

    public void setSurvey(Survey survey) { this.survey = survey; }

    @FXML
    public void keySubmission(ActionEvent event) {
        scanner.keyA = keyA.getText();
        scanner.keyB = keyB.getText();

        keyA.clear();
        keyB.clear();
    }

    @FXML void excelSubmission(ActionEvent event) {
        String surveyPath = surveyFile.getText();
        surveyFile.setText("");
        survey.setPath(surveyPath);
        if (!survey.fileExist()) {
            surveyFileError.setText("Invalid path");
        } else {
            surveyFileError.setText("");
            survey.updateList();
        }

        String collectedPath = collectedFile.getText();
        collectedFileError.setText("Optional");
        if (collectedPath.equals("") && !collected.fileExist()) {
            try {
                collected.createNewFile();
            } catch (IOException e) {
                Logger logger = Logger.getLogger("log");
                FileHandler fh = null;
                try {
                    fh = new FileHandler("./scanner.log");
                } catch (IOException e1) {
                    return;
                }
                logger.addHandler(fh);
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
            return;
        } else if (collectedPath.equals("") && collected.fileExist()) {
            collected.updateList();
            return;
        }



        collectedFile.setText("");
        collected.setPath(collectedPath);
        if (!collected.fileExist()) {
            collectedFileError.setText("Invalid path");
        } else {
            collected.updateList();
            collectedFileError.setText("Optional");
        }
    }
}
