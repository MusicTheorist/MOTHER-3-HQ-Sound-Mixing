package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;

import javafx.application.HostServices;
import javafx.scene.control.Hyperlink;

public final class HyperlinkActions {
    private final PatcherResources patcherResources;

    public HyperlinkActions(PatcherResources patcherResources) {
        this.patcherResources = patcherResources;
    }

    public void openLinkInBrowser(Hyperlink link) {
        HostServices hostServices = patcherResources.getHostServices();
        hostServices.showDocument(link.getText());
    }
}