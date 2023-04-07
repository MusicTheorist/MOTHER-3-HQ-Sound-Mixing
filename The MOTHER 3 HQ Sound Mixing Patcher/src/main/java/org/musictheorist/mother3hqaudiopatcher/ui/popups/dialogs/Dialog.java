package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs;

import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.Message;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract non-sealed class Dialog extends Message {
    final AlertType dialogType;
    final PatcherResources patcherResources;

    protected Alert dialog;
    protected ArrayList<ButtonType> buttons;

    protected Dialog(Stage patcher, PatcherResources patcherResources, String title, String contents, AlertType dialogType, ResourceBundle lang) {
        super(patcher, title, contents);
        this.dialogType = dialogType;
        this.patcherResources = patcherResources;

        initButtons(lang);
    }

    @Override
    protected final void initWindow() {
        dialog = new Alert(dialogType, "", buttons.toArray(ButtonType[]::new));
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(initLabel(getContents()));
        dialogPane.setGraphic(patcherResources.getAlertIcon(dialogType));
        dialog.initOwner(patcher);
        dialog.initModality(Modality.APPLICATION_MODAL);
    }

    public Optional<ButtonType> display() {
        initWindow();
        return dialog.showAndWait();
    }

    protected abstract void initButtons(ResourceBundle lang);
}