package org.musictheorist.mother3hqaudiopatcher;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.main.PatcherActions;
import org.musictheorist.mother3hqaudiopatcher.ui.main.PatcherWindow;
import org.musictheorist.mother3hqaudiopatcher.ui.main.PopupsTester;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.error.ErrorMessages;

import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {
    // Set this to "true" to easily test all popups at startup *and* whenever the patcher's language is changed. Useful when translating the patcher!
    private static boolean cycleAllPopups = false;

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

        patcherWindow.initStage(true);
        patcherWindow.display();

        if(App.cycleAllPopups) {
            PopupsTester popupTests = new PopupsTester(stage, patcherResources, patcherActions, errorMessages);
            patcherActions.initPopupTests(popupTests);
            popupTests.run(patcherActions.getPopupHandler(), lang);
        }
    }

    public static void main(String[] args) {
        System.setProperty("prism.text", "t2k");
        if(args.length > 0 && args[0].equals("test_popups")) {
            App.cycleAllPopups = true;
        }
        launch();
    }
}