package org.musictheorist.mother3hqaudiopatcher.ui.popups.sourceview;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.ui.Layout;
import org.musictheorist.mother3hqaudiopatcher.ui.SpacedNode;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.TextBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class MixerCodeWindow {
    private final Stage window;
    private final MixerCodeActions mixerCodeActions;
    private final String mixerCode;
    private final GridPane layout;

    private Label selectFont;
    private ChoiceBox<String> fontMenu;
    private Label selectSize;
    private ArrayList<String> sizes;
    private ComboBox<String> sizeMenu;
    private TextArea mixerCodeView;

    public MixerCodeWindow(Stage patcher, MixerCodeActions mixerCodeActions, String mixerCode, ResourceBundle lang) {
        this.window = new Stage();
        this.mixerCodeActions = mixerCodeActions;
        this.mixerCode = mixerCode;
        this.layout = new GridPane();

        mixerCodeActions.setGUI(this, this.window);

        initNodes(lang);
        initGrid();
        initSizes();
        initCodeView();
        initFontSelection();
        initSizeSelection(lang);
        initLayout();
        initStage(patcher, lang);
    }

    private void initNodes(ResourceBundle lang) {
        selectFont = new Label("");
        selectFont.setText(lang.getString("labelCodeFont"));
        selectFont.setAlignment(Pos.CENTER_RIGHT);

        selectSize = new Label("");
        selectSize.setText(lang.getString("labelCodeSize"));
        selectSize.setAlignment(Pos.CENTER_RIGHT);

        fontMenu = new ChoiceBox<String>();

        sizeMenu = new ComboBox<String>();
    }

    private void initGrid() {
        int rowCount = 2;

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            RowConstraints rowConstraints = new RowConstraints();
            if(rowIndex == 1) {
                rowConstraints.setVgrow(Priority.ALWAYS);
                rowConstraints.setFillHeight(true);
            }
            layout.getRowConstraints().add(rowConstraints);
        }
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setFillWidth(true);
        columnConstraints.setHalignment(HPos.CENTER);
        layout.getColumnConstraints().add(columnConstraints);
    }

    private void initSizes() {
        selectFont.setMaxSize(Double.MAX_VALUE, 30);
        fontMenu.setMaxSize(Double.MAX_VALUE, 30);
        selectSize.setMaxSize(Double.MAX_VALUE, 30);
        sizeMenu.setMaxSize(75, 30);
    }

    private ArrayList<HBox> initHBoxes() {
        ArrayList<HBox> rows = new ArrayList<HBox>();

        Insets selectFontMargin = new Insets(0, 2, 0, 3),
               selectSizeMargin = new Insets(0, 2, 0, 6);
        SpacedNode selectFont = new SpacedNode(this.selectFont, selectFontMargin, null),
                     fontMenu = new SpacedNode(this.fontMenu, null, null),
                   selectSize = new SpacedNode(this.selectSize, selectSizeMargin, null),
                     sizeMenu = new SpacedNode(this.sizeMenu, null, null);
        Insets firstRowPadding = new Insets(3, 0, 0, 0);
        rows.add(Layout.initHBox(firstRowPadding, Pos.CENTER_LEFT, selectFont, fontMenu, selectSize, sizeMenu));

        Insets mixerCodeViewMargin = new Insets(3);
        SpacedNode mixerCodeView = new SpacedNode(this.mixerCodeView, mixerCodeViewMargin, Priority.SOMETIMES);
        rows.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER, mixerCodeView));

        return rows;
    }

    private void initLayout() {
        ArrayList<HBox> layoutNodes = initHBoxes();
        layout.add(layoutNodes.get(0), 0, 0, 1, 1);
        layout.add(layoutNodes.get(1), 0, 1, 1, 1);
    }

    private void initStage(Stage patcher, ResourceBundle lang) {
        window.setScene(new Scene(layout));
        window.initOwner(patcher);
        window.setTitle(lang.getString("titleViewSource"));
        window.getIcons().addAll(patcher.getIcons());
        window.initModality(Modality.WINDOW_MODAL);
        window.setResizable(true);
        window.sizeToScene();
    }

    private void initCodeView() {
        mixerCodeView = TextBox.initCodeView(mixerCode, fontMenu);
    }
    void adjustFont(TextArea mixerCodeView, String fontName, double fontSize) {
        mixerCodeView.setFont(Font.font(fontName, FontWeight.NORMAL, fontSize));
        TextBox.adjustCodeView(mixerCodeView);
    }

    private void initFontSelection() {
        ObservableList<String> fonts = mixerCodeActions.getFonts();
        fontMenu.setItems(fonts);
        fontMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mixerCodeActions.changeFont(fontMenu, mixerCodeView);
            }
        });
        int knownFontIndex = mixerCodeActions.getKnownFontIndex();
        if(knownFontIndex != -1) {
            fontMenu.getSelectionModel().select(knownFontIndex);
        }
        else {
            fontMenu.getSelectionModel().select(0);
        }
    }

    private void initSizeSelection(ResourceBundle lang) {
        sizes = new ArrayList<String>();
        for(int i = 8; i <= 40; i += 4) {
            sizes.add(Integer.toString(i));
        }
        sizeMenu.setItems(FXCollections.observableArrayList(sizes));
        sizeMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mixerCodeActions.changeSize(sizeMenu, mixerCodeView, lang);
            }
        });
        sizeMenu.getSelectionModel().select(1);
        sizeMenu.setEditable(true);
    }

    public void display() {
        window.show();
        layout.requestFocus();
    }
}