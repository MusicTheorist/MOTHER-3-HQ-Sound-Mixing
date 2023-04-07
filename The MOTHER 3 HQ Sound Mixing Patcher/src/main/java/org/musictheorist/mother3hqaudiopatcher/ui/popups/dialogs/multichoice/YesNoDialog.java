package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.Dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public sealed class YesNoDialog extends Dialog permits CustomAddressDialog {
    protected YesNoDialog(Stage patcher, PatcherResources patcherResources, String title, String contents, AlertType dialogType, ResourceBundle lang) {
        super(patcher, patcherResources, title, contents, dialogType, lang);
    }

    @Override
    protected final void initButtons(ResourceBundle lang) {
        buttons = new ArrayList<ButtonType>();
        buttons.add(new ButtonType(lang.getString("buttonYes"), ButtonData.YES));
        buttons.add(new ButtonType(lang.getString("buttonNo"), ButtonData.NO));
    }

    public static CustomAddressDialog newCustomAddressDialog(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        return new CustomAddressDialog(patcher, patcherResources, lang);
    }

    public static YesNoDialog newUsedSpaceDialog(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        return new YesNoDialog(patcher,
                               patcherResources,
                               lang.getString("titleUsedMixerSpace"),
                               lang.getString("labelUsedMixerSpace"),
                               AlertType.CONFIRMATION,
                               lang);
    }
}
