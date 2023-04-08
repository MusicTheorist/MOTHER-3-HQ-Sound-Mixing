package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice;

import java.util.Optional;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public final class CustomAddressDialog extends YesNoDialog {
    CustomAddressDialog(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        super(patcher,
              patcherResources,
              lang.getString("titleCustomMixerAddress"),
              lang.getString("labelCustomMixerAddress"),
              AlertType.CONFIRMATION,
              lang);
    }

    @Override
    public Optional<ButtonType> display() {
        initWindow();

        // Thanks to crusam for this suggestion! (https://stackoverflow.com/a/29536464)
        dialog.getButtonTypes().clear();
        dialog.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        Button yesButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(false);

        Button noButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.NO);
        noButton.setDefaultButton(true);

        return dialog.showAndWait();
    }
}
