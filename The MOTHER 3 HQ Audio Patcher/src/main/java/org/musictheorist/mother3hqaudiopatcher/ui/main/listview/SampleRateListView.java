package org.musictheorist.mother3hqaudiopatcher.ui.main.listview;

import javafx.scene.control.ListView;

public final class SampleRateListView extends ListView<String> {
    public SampleRateListView() {
        this.setCellFactory(stringListView -> new ToggleableRightAlignedListCell());
    }
}