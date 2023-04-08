package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.Layout;
import org.musictheorist.mother3hqaudiopatcher.ui.SpacedNode;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class DisclaimersWindow {
    private final Alert window;
    private final GridPane layout;

    private Label leftLabel;
    private TextArea leftBox;
    private Label rightLabel;
    private TextArea rightBox;

    private ButtonType confirmButton;

    public DisclaimersWindow(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        this.window = new Alert(AlertType.NONE);
        this.layout = new GridPane();

        initLabels(lang);
        initGrid();
        initTextAreas(lang);
        initConfirmButton(lang);
        initLayout();
        initWindow(patcher, patcherResources, lang);
    }

    private void initLabels(ResourceBundle lang) {
        leftLabel = new Label(lang.getString("labelGeneralDisclaimer"));
        rightLabel = new Label(lang.getString("labelSpeedrunDisclaimer"));
    }

    private void initGrid() {
        int colCount = 2;

        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.setFillWidth(true);
            layout.getColumnConstraints().add(columnConstraints);
        }
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints.setFillHeight(true);
        layout.getRowConstraints().add(rowConstraints);
    }

    private void initTextAreas(ResourceBundle lang) {
        leftBox  = TextBox.readOnly( "textGeneralDisclaimer", "widthDisclaimers", "heightDisclaimers", lang);
        rightBox = TextBox.readOnly("textSpeedrunDisclaimer", "widthDisclaimers", "heightDisclaimers", lang);
    }

    private void initConfirmButton(ResourceBundle lang) {
        confirmButton = new ButtonType(lang.getString("buttonUnderstood"), ButtonData.OK_DONE);
    }

    private ArrayList<VBox> initColumns() {
        ArrayList<VBox> cols = new ArrayList<VBox>();

        Insets leftLabelMargin = new Insets(0, 0, 3, 0);
        SpacedNode leftLabel = new SpacedNode(this.leftLabel, leftLabelMargin, null),
                     leftBox = new SpacedNode(this.leftBox, null, null);
        Insets leftPadding = new Insets(0, 10, 0, 0);
        cols.add(Layout.initVBox(leftPadding, Pos.CENTER, leftLabel, leftBox));

        Insets rightLabelMargin = new Insets(0, 0, 3, 0);
        SpacedNode rightLabel = new SpacedNode(this.rightLabel, rightLabelMargin, null),
                     rightBox = new SpacedNode(this.rightBox, null, null);
        Insets rightPadding = new Insets(0, 0, 0, 10);
        cols.add(Layout.initVBox(rightPadding, Pos.CENTER, rightLabel, rightBox));

        return cols;
    }

    private void initLayout() {
        ArrayList<VBox> layoutNodes = initColumns();
        layout.add(layoutNodes.get(0), 0, 0, 1, 1);
        layout.add(layoutNodes.get(1), 1, 0, 1, 1);
    }

    private void initWindow(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        window.getButtonTypes().add(confirmButton);
        window.setTitle(lang.getString("titleDisclaimers"));
        window.setHeaderText(null);
        DialogPane dialogPane = window.getDialogPane();
        dialogPane.setContent(layout);
        dialogPane.setGraphic(patcherResources.getAlertIcon(AlertType.CONFIRMATION));
        window.initOwner(patcher);
        window.initModality(Modality.APPLICATION_MODAL);
    }

    public void display() {
        window.showAndWait();
    }
}