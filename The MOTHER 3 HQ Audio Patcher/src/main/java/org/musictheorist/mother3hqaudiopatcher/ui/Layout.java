package org.musictheorist.mother3hqaudiopatcher.ui;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class Layout {
    private Layout() {}

    public static HBox initHBox(Insets padding, Pos alignment, SpacedNode... nodes) {
        if(padding == null) padding = Insets.EMPTY;

        HBox newRow = new HBox();
        newRow.setPadding(padding);
        newRow.setAlignment(alignment);

        for(SpacedNode next : nodes) {
            newRow.getChildren().add(next.node());
            HBox.setMargin(next.node(), next.margin());
            HBox.setHgrow(next.node(), next.grow());
        }

        return newRow;
    }

    public static VBox initVBoxWithHBoxes(Pos alignment, List<HBox> list) {
        VBox newColumn = new VBox();
        newColumn.setAlignment(alignment);

        for(HBox next : list) {
            newColumn.getChildren().add(next);
        }

        return newColumn;
    }

    public static VBox initVBox(Insets padding, Pos alignment, SpacedNode... nodes) {
        if(padding == null) padding = Insets.EMPTY;

        VBox newColumn = new VBox();
        newColumn.setPadding(padding);
        newColumn.setAlignment(alignment);

        for(SpacedNode next : nodes) {
            newColumn.getChildren().add(next.node());
            VBox.setMargin(next.node(), next.margin());
            VBox.setVgrow(next.node(), next.grow());
        }

        return newColumn;
    }
}