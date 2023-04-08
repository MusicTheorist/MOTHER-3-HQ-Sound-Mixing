package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public final class CorruptDataDialog extends Dialog {
    public CorruptDataDialog(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        super(patcher, patcherResources, lang.getString("titleRepairROM"), lang.getString("labelCorruptPatch"), AlertType.WARNING, lang);
    }

    @Override
    protected void initButtons(ResourceBundle lang) {
        buttons = new ArrayList<ButtonType>();
        buttons.add(new ButtonType(lang.getString("buttonNoRepair"), ButtonData.CANCEL_CLOSE));
        buttons.add(new ButtonType(lang.getString("buttonRepairWithBackup"), ButtonData.YES));
        buttons.add(new ButtonType(lang.getString("buttonJustRepair"), ButtonData.NO));
    }
}