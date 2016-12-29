package ui.controllers;

import java.util.logging.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import objects.Scan;

public class ScanListCell extends ListCell<Scan> {

    private static final String DIRECTORY_CELL = "/ui/views/scancell.fxml";

    @Override
    public void updateItem(Scan scan, boolean empty) throws IllegalArgumentException {

        if (scan != null) {
            super.updateItem(scan, empty);

            try {

                FXMLLoader fxmlLoader = new FXMLLoader();
                VBox cell = fxmlLoader.load(getClass().getResource(DIRECTORY_CELL).openStream());
                ScanNodeController cellController = fxmlLoader.getController();
                cellController.initCell(scan, this.getIndex(), getListView());

                this.setGraphic(cell);

            } catch (Exception exception) {
                Logger.getLogger("").info(exception.getStackTrace().toString());
                exception.printStackTrace();
            }
        } else {
            nullifyItem();
        }
    }

    private void nullifyItem() {
        this.setText(null);
        this.setGraphic(null);
    }
}
