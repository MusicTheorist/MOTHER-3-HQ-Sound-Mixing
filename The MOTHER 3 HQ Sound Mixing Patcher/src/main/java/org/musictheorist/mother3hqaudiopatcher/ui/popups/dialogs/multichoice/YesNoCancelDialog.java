package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.patching.Conversions;
import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.Dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public final class YesNoCancelDialog extends Dialog {
    protected YesNoCancelDialog(Stage patcher, PatcherResources patcherResources, String title, String contents, AlertType dialogType, ResourceBundle lang) {
        super(patcher, patcherResources, title, contents, dialogType, lang);
    }

    @Override
    protected void initButtons(ResourceBundle lang) {
        buttons = new ArrayList<ButtonType>();
        buttons.add(new ButtonType(lang.getString("buttonCancel"), ButtonData.CANCEL_CLOSE));
        buttons.add(new ButtonType(lang.getString("buttonYes"), ButtonData.YES));
        buttons.add(new ButtonType(lang.getString("buttonNo"), ButtonData.NO));
    }

    public static YesNoCancelDialog newCustomRemovalDialog(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        return new YesNoCancelDialog(patcher,
                                     patcherResources,
                                     lang.getString("titleRemoveCustomPatch"),
                                     lang.getString("labelRemoveCustomPatch"),
                                     AlertType.NONE,
                                     lang);
    }

    public static YesNoCancelDialog newFoundSpaceDialog(Stage patcher, PatcherResources patcherResources, long address, ResourceBundle lang) {
        return new YesNoCancelDialog(patcher,
                                     patcherResources,
                                     lang.getString("titleFoundFreeSpace"),
                                     lang.getString("labelFoundFreeSpaceA") + "\n\n" + Conversions.getString(address) + "\n\n" + lang.getString("labelFoundFreeSpaceB"),
                                     AlertType.INFORMATION,
                                     lang);
    }
}
