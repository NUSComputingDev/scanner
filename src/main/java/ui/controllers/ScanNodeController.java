package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import objects.Scan;

public class ScanNodeController {

    private static final double SIZE_PADDING_NAME = 140.0;

    private static final double SIZE_PADDING_DETAILS = 40.0;

    private static final String HEADER_DETAILS = "Details:\n";

    private Scan scan = null;

    private int index = -1;

    @FXML
    private Circle indexTag;

    @FXML
    private TextFlow nameArea;

    @FXML
    private Text name;

    @FXML
    private Label matricNumber;

    @FXML
    private Label accessCode;

    @FXML
    private Label dateTimeStamp;

    @FXML
    private TextFlow detailsArea;

    @FXML
    private Text details;

    public ScanNodeController() {

    }

    public void initCell(Scan scan, int index, ListView list) {

        matricNumber.setText(scan.getMatricNumber());
        accessCode.setText(scan.getAccessCode());
        dateTimeStamp.setText(scan.getDateTimeStamp());
        details.setText(HEADER_DETAILS + scan.getDetailsField());

        nameArea.maxWidthProperty().bind(list.widthProperty().subtract(SIZE_PADDING_NAME));
        detailsArea.maxWidthProperty().bind(list.widthProperty().subtract(SIZE_PADDING_DETAILS));

    }

    @FXML
    public void initialize() {

    }

}
