package org.musictheorist.mother3hqaudiopatcher.ui.main.listview;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

// Many thanks to Zephyr (https://stackoverflow.com/a/51574405)
// and user1803551 + James_D (https://stackoverflow.com/a/44018139)
// on Stack Overflow for their helpful examples!
final class ToggleableRightAlignedListCell extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            if(item.endsWith("d")) {
                item = item.substring(0, item.length() - 1);
                setDisable(true);
            }
            else {
                setDisable(false);
            }

            // Create the HBox
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            // Create centered Label
            Label label = new Label(item);
            label.setAlignment(Pos.CENTER_RIGHT);

            hBox.getChildren().add(label);
            setGraphic(hBox);
        }
    }
}