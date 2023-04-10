package org.musictheorist.mother3hqaudiopatcher.ui.popups.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.HyperlinkActions;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.TextBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class StackTraceWindow {
    private static final double WIDTH_PADDING = 20;
    private static final double HEIGHT_PADDING = 15;

    private final Alert window;
    private final HyperlinkActions hyperlinkActions;
    private final GridPane layout;

    private TextFlow textFlow;
    private TextArea stackTrace;

    private Hyperlink githubIssuesLink;

    private ButtonType closeButton;

    public StackTraceWindow(Stage patcher, PatcherResources patcherResources, HyperlinkActions hyperlinkActions, Throwable error, ResourceBundle lang) {
        this.window = new Alert(AlertType.NONE);
        this.hyperlinkActions = hyperlinkActions;
        this.layout = new GridPane();

        initStackTrace(error, lang);
        initTextFlow(lang);
        initHyperlink();
        initCloseButton(lang);
        initLayout();
        initWindow(patcher, patcherResources, lang);
    }

    private void initStackTrace(Throwable error, ResourceBundle lang) {
        if(error.getLocalizedMessage() != null) {
            try {
                LocalizedException localized = new LocalizedException(lang.getString(error.getLocalizedMessage()), error);
                error = localized;
            } catch(MissingResourceException e) {}
        }

        StringWriter errorWriter = new StringWriter();
        error.printStackTrace(new PrintWriter(errorWriter));
        stackTrace = TextBox.stackTrace(errorWriter.toString(), WIDTH_PADDING, HEIGHT_PADDING);
    }

    private void initTextFlow(ResourceBundle lang) {
        Text githubIssues = new Text(lang.getString("labelStackTrace"));
        githubIssuesLink = new Hyperlink(lang.getString("linkGithubIssues"));

        textFlow = new TextFlow(githubIssues, githubIssuesLink);
        textFlow.setPrefWidth(stackTrace.getPrefWidth());
    }

    private void initHyperlink() {
        githubIssuesLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hyperlinkActions.openLinkInBrowser(githubIssuesLink);
            }
        });
    }

    private void initCloseButton(ResourceBundle lang) {
        closeButton = new ButtonType(lang.getString("buttonClose"), ButtonData.OK_DONE);
    }

    private void initLayout() {
        layout.add(textFlow, 0, 0);
        layout.add(stackTrace, 0, 1);
    }

    private void initWindow(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        window.getButtonTypes().add(closeButton);
        window.setTitle(lang.getString("titleError"));
        window.setGraphic(patcherResources.getAlertIcon(AlertType.ERROR));
        window.setHeaderText(null);
        window.getDialogPane().setContent(layout);
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(patcher);
    }

    public void display() {
        layout.requestFocus();
        window.showAndWait();
    }
}