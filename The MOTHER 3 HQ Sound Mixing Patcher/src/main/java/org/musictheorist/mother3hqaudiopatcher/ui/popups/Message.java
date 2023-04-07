package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.Dialog;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public abstract sealed class Message permits Dialog, Popup {
    protected final Stage patcher;
    protected final String title;
    private final StringBuilder contents;

    protected Message(Stage patcher, String title, String contents) {
        this.patcher = patcher;
        this.title = title;
        this.contents = new StringBuilder(contents);
    }

    protected final void appendToContents(String string) {
        contents.append(string);
    }
    protected final String getContents() {
        return contents.toString();
    }

    protected final Label initLabel(String text) {
        Label popupLabel = new Label(text);
        popupLabel.setAlignment(Pos.CENTER);
        return popupLabel;
    }

    protected abstract void initWindow();
}