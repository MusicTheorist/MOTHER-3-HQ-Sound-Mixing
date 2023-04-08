package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs;

import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.patching.Conversions;
import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public final class OKDialog extends Dialog {
    OKDialog(Stage patcher, PatcherResources patcherResources, String title, String contents, boolean success, ResourceBundle lang) {
        super(patcher,
              patcherResources,
              title,
              contents,
              success ? AlertType.INFORMATION : AlertType.ERROR,
              lang);
    }

    @Override
    protected void initButtons(ResourceBundle lang) {
        buttons = new ArrayList<ButtonType>();
        buttons.add(new ButtonType(lang.getString("buttonOK"), ButtonData.OK_DONE));
        if(dialogType.equals(AlertType.ERROR)) {
            buttons.add(new ButtonType(lang.getString("buttonMoreDetails"), ButtonData.HELP));
        }
    }

    @Override
    public Optional<ButtonType> display() {
        initWindow();
        Optional<ButtonType> optional = dialog.showAndWait();
        return optional.isEmpty() ? Optional.of(ButtonType.OK) : optional;
    }

    public static OKDialog newAppliedPatchDialog(Stage patcher, PatcherResources patcherResources, boolean success, ResourceBundle lang) {
        return new OKDialog(patcher,
                            patcherResources,
                            lang.getString("titlePatchROM"),
                            success ? lang.getString("labelPatchSuccess") : lang.getString("labelPatchFail"),
                            success,
                            lang);
    }
    public static OKDialog newAppliedPatchDialog(Stage patcher, PatcherResources patcherResources, boolean success, String backupPath, ResourceBundle lang) {
        OKDialog window = newAppliedPatchDialog(patcher, patcherResources, success, lang);
        window.appendToContents("\n" + lang.getString("labelBackupInfo") + " " + backupPath);
        return window;
    }

    public static OKDialog newCustomLocationDialog(Stage patcher, PatcherResources patcherResources, long address, ResourceBundle lang) {
        return new OKDialog(patcher,
                            patcherResources,
                            lang.getString("titleCustomMixerAddress"),
                            lang.getString("labelCustomLocation") + " " + Conversions.getString(address),
                            true,
                            lang);
    }

    public static OKDialog newNoMonospaceDialog(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        return new OKDialog(patcher,
                            patcherResources,
                            lang.getString("titleViewSource"),
                            lang.getString("labelNoMonospace"),
                            false,
                            lang);
    }

    public static OKDialog newRemovedPatchDialog(Stage patcher, PatcherResources patcherResources, boolean success, ResourceBundle lang) {
        return new OKDialog(patcher,
                            patcherResources,
                            lang.getString("titleUndoPatch"),
                            success ? lang.getString("labelUndoSuccess") : lang.getString("labelUndoFail"),
                            success,
                            lang);
    }
    public static OKDialog newRemovedPatchDialog(Stage patcher, PatcherResources patcherResources, boolean success, String backupPath, ResourceBundle lang) {
        OKDialog window = newRemovedPatchDialog(patcher, patcherResources, success, lang);
        window.appendToContents("\n" + lang.getString("labelBackupInfo") + " " + backupPath);
        return window;
    }

    public static OKDialog newRepairedROMDialog(Stage patcher, PatcherResources patcherResources, boolean success, ResourceBundle lang) {
        return new OKDialog(patcher,
                            patcherResources,
                            lang.getString("titleRepairROM"),
                            success ? lang.getString("labelRepairSuccess") : lang.getString("labelRepairFail"),
                            success,
                            lang);
    }
    public static OKDialog newRepairedROMDialog(Stage patcher, PatcherResources patcherResources, boolean success, String backupPath, ResourceBundle lang) {
        OKDialog window = newRepairedROMDialog(patcher, patcherResources, success, lang);
        window.appendToContents("\n" + lang.getString("labelBackupInfo") + " " + backupPath);
        return window;
    }
}