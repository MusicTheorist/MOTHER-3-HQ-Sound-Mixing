package org.musictheorist.mother3hqaudiopatcher.ui.popups.sourceview;

import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.error.ErrorMessages;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public final class MixerCodeActions {
    private final PatcherResources patcherResources;
    private MixerCodeWindow mixerCodeWindow;

    private Stage codeView;

    private ErrorMessages errorMessages;

    public MixerCodeActions(PatcherResources patcherResources, ErrorMessages errorMessages) {
        this.patcherResources = patcherResources;
        this.errorMessages = errorMessages;
    }

    void setGUI(MixerCodeWindow mixerCodeWindow, Stage codeView) {
        this.mixerCodeWindow = mixerCodeWindow;
        this.codeView = codeView;
    }

    ObservableList<String> getFonts() {
        return patcherResources.getFonts();
    }
    int getKnownFontIndex() {
        return patcherResources.getKnownFontIndex();
    }

    void changeFont(ChoiceBox<String> fontMenu, TextArea mixerCodeView) {
        String fontName = fontMenu.getSelectionModel().getSelectedItem();
        double currentSize = mixerCodeView.getFont().getSize();
        mixerCodeWindow.adjustFont(mixerCodeView, fontName, currentSize);
    }

    void changeSize(ComboBox<String> sizeMenu, TextArea mixerCodeView, ResourceBundle lang) {
        try {
            double newSize = Double.valueOf(sizeMenu.getValue());
            if(newSize < 8 || newSize > 40) {
                newSize = newSize < 8 ? 8 : 40;
                sizeMenu.setValue(String.valueOf((int) newSize));
            }
            String currentFont = mixerCodeView.getFont().getFamily();
            mixerCodeWindow.adjustFont(mixerCodeView, currentFont, newSize);
        } catch (NumberFormatException e) {
            errorMessages.nextMessageInfo(false);
            errorMessages.toggleStackTrace(false);
            errorMessages.invokeCustom("errorSizeNaN", e, codeView, lang);
        }
    }
}