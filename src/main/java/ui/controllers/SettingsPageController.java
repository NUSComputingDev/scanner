package ui.controllers;

import database.io.XLSMWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import scanners.MatricCardScanner;

import java.io.File;
import java.io.FileNotFoundException;

public class SettingsPageController {

    @FXML
    private PasswordField keyA;

    @FXML
    private PasswordField keyB;

    @FXML
    private TextField fileName;

    @FXML
    private TextField sheetNumber;

    @FXML
    private TextField row;

    @FXML
    private TextField column;

    @FXML
    private Label fileNameError;

    @FXML
    private Label sheetNumberError;

    @FXML
    private Label rowError;

    @FXML
    private Label columnError;

    private MatricCardScanner scanner = null;
    private XLSMWriter writer = null;

    public SettingsPageController() {

    }

    public void setScanner(MatricCardScanner scanner) {
        this.scanner = scanner;
    }

    public void setWrite(XLSMWriter writer) {
        this.writer = writer;
    }

    @FXML
    public void keySubmission(ActionEvent event) {
        scanner.keyA = keyA.getText();
        scanner.keyB = keyB.getText();

        keyA.clear();
        keyB.clear();
    }

    @FXML void excelSubmission(ActionEvent event) {
        Integer column = null;
        Integer row = null;
        Integer sheetNumber = 0;


        String filePath = fileName.getText();
        if (new File(filePath).isFile()) {
            writer.setFile(filePath);
            fileNameError.setText("");
        } else {
            fileNameError.setText("Invalid path");
        }



        try {
            if (this.column.getText().equals("")) {
                columnError.setText("required");
            } else {
                column = Integer.parseInt(this.column.getText());
                columnError.setText("");
            }
        } catch (NumberFormatException nfe) {
            columnError.setText("use integers");
        }

        try {
            if (this.row.getText().equals("")) {
                rowError.setText("required");
            } else {
                row = Integer.parseInt(this.row.getText());
                rowError.setText("");
            }
        } catch (NumberFormatException nfe) {
            rowError.setText("use integers");
        }

        try {
            if (!this.sheetNumber.getText().equals("")) {
                sheetNumber = Integer.parseInt(this.sheetNumber.getText());
                sheetNumberError.setText("default is 0");
            }

        } catch (NumberFormatException nfe) {
            sheetNumberError.setText("use integers");
        }

        if (row != null && column != null) {
            writer.setConfiguration(sheetNumber, row, column);
        }
    }
}
