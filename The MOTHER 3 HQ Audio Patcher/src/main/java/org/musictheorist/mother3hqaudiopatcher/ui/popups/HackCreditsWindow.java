package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class HackCreditsWindow {
    private final Alert window;
    private final HyperlinkActions hyperlinkActions;

    private TextFlow textFlow;

    private Hyperlink ipatixLink;
    private Hyperlink originalMixerLink;
    private Hyperlink decompLink;
    private Hyperlink githubIssuesLink;

    private ButtonType closeButton;

    public HackCreditsWindow(Stage patcher, PatcherResources patcherResources, HyperlinkActions hackCreditsActions, ResourceBundle lang) {
        this.window = new Alert(AlertType.NONE);
        this.hyperlinkActions = hackCreditsActions;

        initTextFlow(lang);
        initHyperlinks();
        initCloseButton(lang);
        initWindow(patcher, patcherResources, lang);
    }

    private void initTextFlow(ResourceBundle lang) {
        Text romHackCredit = new Text(lang.getString("labelROMHackCredit"));
        Text ipatixCredit = new Text(lang.getString("labelIpatixCredit"));
        ipatixLink = new Hyperlink(lang.getString("linkIpatixCredit"));
        Text originalMixerCredit = new Text(lang.getString("labelOriginalMixerCredit"));
        originalMixerLink = new Hyperlink(lang.getString("linkOriginalMixerCredit"));
        Text decompCredit = new Text(lang.getString("labelDecompCredit"));
        decompLink = new Hyperlink(lang.getString("linkDecompCredit"));
        Text extraSupportCredit = new Text(lang.getString("labelExtraSupportCredit"));
        Text specialThanks = new Text(lang.getString("labelSpecialThanks"));
        Text githubIssues = new Text(lang.getString("labelGithubIssues"));
        githubIssuesLink = new Hyperlink(lang.getString("linkGithubIssues"));

        textFlow = new TextFlow(romHackCredit, ipatixCredit, ipatixLink, originalMixerCredit,
                                originalMixerLink, decompCredit, decompLink, extraSupportCredit,
                                specialThanks, githubIssues, githubIssuesLink);
    }

    private void initHyperlinks() {
        ipatixLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hyperlinkActions.openLinkInBrowser(ipatixLink);
            }
        });

        originalMixerLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hyperlinkActions.openLinkInBrowser(originalMixerLink);
            }
        });

        decompLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hyperlinkActions.openLinkInBrowser(decompLink);
            }
        });

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

    private void initWindow(Stage patcher, PatcherResources patcherResources, ResourceBundle lang) {
        window.getButtonTypes().add(closeButton);
        window.setTitle(lang.getString("titleHackCredits"));
        window.setHeaderText(lang.getString("labelHackTitle"));
        DialogPane dialogPane = window.getDialogPane();
        dialogPane.setContent(textFlow);
        dialogPane.setGraphic(patcherResources.getAlertIcon(window.getAlertType()));
        window.initOwner(patcher);
        window.initModality(Modality.APPLICATION_MODAL);
    }

    public void display() {
        window.showAndWait();
    }
}