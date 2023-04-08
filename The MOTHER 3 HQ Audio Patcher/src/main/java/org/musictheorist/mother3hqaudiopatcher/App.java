package org.musictheorist.mother3hqaudiopatcher;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.main.PatcherActions;
import org.musictheorist.mother3hqaudiopatcher.ui.main.PatcherWindow;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.error.ErrorMessages;

import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {
    @Override
    public void start(Stage stage) {
        final String baseName = "lang/patcher";
        ResourceBundle lang;
        try {
            lang = ResourceBundle.getBundle(baseName, Locale.getDefault());
        } catch (MissingResourceException e) {
            lang = ResourceBundle.getBundle(baseName, Locale.ENGLISH);
        }

        final PatcherResources patcherResources = new PatcherResources(this);
        final PatcherActions patcherActions = new PatcherActions(patcherResources);
        final PatcherWindow patcherWindow = new PatcherWindow(stage, patcherActions, lang);

        final ErrorMessages errorMessages = new ErrorMessages(patcherResources);
        patcherActions.initErrorMessages(errorMessages);

        try {
            patcherResources.loadImages();
        } catch (IOException e) {
            errorMessages.invokeDefault("errorImagesIO", e, null, lang);
        }

        patcherWindow.initStage();
        patcherWindow.display();
    }

    public static void main(String[] args) {
        System.setProperty("prism.text", "t2k");
        launch();
    }
}