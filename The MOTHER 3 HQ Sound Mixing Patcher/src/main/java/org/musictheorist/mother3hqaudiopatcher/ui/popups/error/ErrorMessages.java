package org.musictheorist.mother3hqaudiopatcher.ui.popups.error;

import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.HyperlinkActions;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class ErrorMessages {
    private PatcherResources patcherResources;

    private boolean needsLocale;
    private boolean moreDetails;
    private AlertType alertType;

    public ErrorMessages(PatcherResources patcherResources) {
        this.patcherResources = patcherResources;
    }

    public void toggleLocale(boolean needsLocale) {
        this.needsLocale = needsLocale;
    }

    public void toggleStackTrace(boolean moreDetails) {
        this.moreDetails = moreDetails;
    }

    public void nextMessageInfo(boolean nextMessageInfo) {
        alertType = nextMessageInfo ? AlertType.INFORMATION : AlertType.WARNING;
    }

    private ArrayList<ButtonType> initButtons(boolean stackTraceEnabled, ResourceBundle lang) {
        ArrayList<ButtonType> buttons = new ArrayList<ButtonType>();
        buttons.add(new ButtonType(lang.getString("buttonOK"), ButtonData.OK_DONE));
        if(stackTraceEnabled) {
            buttons.add(new ButtonType(lang.getString("buttonMoreDetails"), ButtonData.HELP));
        }
        return buttons;
    }

    private void initWindow(Alert alert, String titleKey, Node content, Stage patcher, ResourceBundle lang) {
        String title = lang.getString(titleKey);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(patcher);
    }

    private void displayStackTrace(Throwable error, Stage patcher, ResourceBundle lang) {
        HyperlinkActions hyperlinkActions = new HyperlinkActions(patcherResources);
        StackTraceWindow stackTraceWindow = new StackTraceWindow(patcher, patcherResources, hyperlinkActions, error, lang);
        stackTraceWindow.display();
    }

    private void displaySummary(String summary, boolean needsLocale, boolean moreDetails, Throwable error, AlertType msgType, String titleKey, Stage patcher, ResourceBundle lang) {
        String errorMsg = needsLocale ? lang.getString(summary) : summary;
        Label errorLabel = new Label(errorMsg);
        errorLabel.setAlignment(Pos.CENTER);

        boolean stackTraceEnabled = moreDetails && msgType.equals(AlertType.WARNING);
        ArrayList<ButtonType> buttons = initButtons(stackTraceEnabled, lang);

        Alert alert = new Alert(msgType, "", buttons.toArray(ButtonType[]::new));
        AlertType iconType = alertType == AlertType.INFORMATION ? AlertType.NONE : AlertType.WARNING;
        alert.setGraphic(patcherResources.getAlertIcon(iconType));
        initWindow(alert, titleKey, errorLabel, patcher, lang);

        Optional<ButtonType> optional = alert.showAndWait();
        if(!optional.isEmpty() && optional.get().getButtonData().equals(ButtonData.HELP)) {
            displayStackTrace(error, patcher, lang);
        }
    }

    public void invokeStackTrace(Throwable error, Stage patcher, ResourceBundle lang) {
        displayStackTrace(error, patcher, lang);
    }

    public void invokeDefault(String summaryKey, Throwable error, Stage patcher, ResourceBundle lang) {
        displaySummary(summaryKey,
                       true,
                       true,
                       error,
                       AlertType.WARNING,
                       "titleError",
                       patcher,
                       lang);
    }
    public void invokeCustom(String summaryKey, Throwable error, Stage patcher, ResourceBundle lang) {
        String titleKey = alertType == AlertType.INFORMATION ? "titleInfo" : "titleError";
        displaySummary(summaryKey,
                       needsLocale,
                       moreDetails,
                       error,
                       alertType,
                       titleKey,
                       patcher,
                       lang);
    }
}