package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class Popup extends Message {
    Stage window;
    private boolean closeable;

    protected Popup(Stage patcher, String title, String contents) {
        super(patcher, title, contents);
        closeable = false;
    }

    @Override
    protected void initWindow() {
        window = new Stage();
        window.initOwner(patcher);
        window.getIcons().addAll(patcher.getIcons());
        window.initModality(Modality.WINDOW_MODAL);
        window.setResizable(false);

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(!closeable) event.consume();
            }
        });

        double popupWidth = window.getOwner().getWidth() / 3d;
        double popupHeight = window.getOwner().getHeight() / 4d;

        Label popupLabel = initLabel(getContents());

        Scene popupScene = new Scene(popupLabel, popupWidth, popupHeight);
        window.setScene(popupScene);
        window.sizeToScene();

        window.setTitle(this.title);
    }

    public void display() {
        initWindow();
        window.show();
    }

    public void close() {
        closeable = true;
        window.close();
    }

    public static Popup newApplyingPatchPopup(Stage patcher, ResourceBundle lang) {
        return new Popup(patcher, lang.getString("titlePatchROM"), lang.getString("labelPatchROM"));
    }

    public static Popup newCheckingROMPopup(Stage patcher, ResourceBundle lang) {
        return new Popup(patcher, lang.getString("titleSelectROM"), lang.getString("labelCheckROM"));
    }

    public static Popup newLoadingFontsPopup(Stage patcher, ResourceBundle lang) {
        return new Popup(patcher, lang.getString("titleViewSource"), lang.getString("labelLoadFonts"));
    }

    public static Popup newRemovingPatchPopup(Stage patcher, ResourceBundle lang) {
        return new Popup(patcher, lang.getString("titleUndoPatch"), lang.getString("labelUndoPatch"));
    }

    public static Popup newScanningROMPopup(Stage patcher, ResourceBundle lang) {
        return new Popup(patcher, lang.getString("titleScanROM"), lang.getString("labelScanROM"));
    }
}