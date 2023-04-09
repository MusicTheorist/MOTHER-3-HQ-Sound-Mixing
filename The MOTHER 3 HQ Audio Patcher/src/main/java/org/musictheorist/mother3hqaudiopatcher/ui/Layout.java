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

        HBox newBox = new HBox();
        newBox.setPadding(padding);
        newBox.setAlignment(alignment);

        for(SpacedNode next : nodes) {
            newBox.getChildren().add(next.node());
            HBox.setMargin(next.node(), next.margin());
            HBox.setHgrow(next.node(), next.grow());
        }

        return newBox;
    }

    public static VBox initVBoxWithHBoxes(Pos alignment, List<HBox> list) {
        VBox newBox = new VBox();
        newBox.setAlignment(alignment);

        for(HBox next : list) {
            newBox.getChildren().add(next);
        }

        return newBox;
    }

    public static VBox initVBox(Insets padding, Pos alignment, SpacedNode... nodes) {
        if(padding == null) padding = Insets.EMPTY;

        VBox newBox = new VBox();
        newBox.setPadding(padding);
        newBox.setAlignment(alignment);

        for(SpacedNode next : nodes) {
            newBox.getChildren().add(next.node());
            VBox.setMargin(next.node(), next.margin());
            VBox.setVgrow(next.node(), next.grow());
        }

        return newBox;
    }
}