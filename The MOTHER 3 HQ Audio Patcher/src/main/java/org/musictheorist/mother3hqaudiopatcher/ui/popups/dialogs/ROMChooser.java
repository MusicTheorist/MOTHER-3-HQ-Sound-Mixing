package org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs;

import java.io.File;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;

public final class ROMChooser {
    private ROMChooser() {}

    public static FileChooser initDialog(File recentFolder, ResourceBundle lang) {
        FileChooser fileDialog = new FileChooser();
        fileDialog.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(lang.getString("chooserFilter"), "*.gba")
        );
        if(recentFolder != null) {
            fileDialog.setInitialDirectory(recentFolder);
        }
        return fileDialog;
    }
}