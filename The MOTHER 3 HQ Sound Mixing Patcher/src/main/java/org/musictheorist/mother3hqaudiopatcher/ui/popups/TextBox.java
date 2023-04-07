package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import java.util.ResourceBundle;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public final class TextBox {
    private TextBox() {}

    private static TextArea createTextBox(String contents, boolean wrapText) {
        TextArea textBox = new TextArea(contents);
        textBox.setEditable(false);
        textBox.setWrapText(wrapText);
        return textBox;
    }

    private static Text layoutText(TextArea textBox) {
        Text text = new Text(textBox.getText());
        text.setFont(textBox.getFont());

        StackPane pane = new StackPane(text);
        pane.layout();

        return text;
    }

    private static double getSizeFromKey(String key, ResourceBundle lang) {
        String value = lang.getString(key);
        return Double.parseDouble(value);
    }

    private static void setSizes(TextArea textBox, double width, double height) {
        textBox.setPrefSize(width, height);
        textBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public static TextArea stackTrace(String contents, double widthPadding, double heightPadding) {
        TextArea textBox = createTextBox(contents, false);
        Text text = layoutText(textBox);

        double width = widthPadding + text.getLayoutBounds().getWidth();
        double height = heightPadding + text.getLayoutBounds().getHeight();
        setSizes(textBox, width, height);

        textBox.positionCaret(0);

        return textBox;
    }

    public static TextArea readOnly(String contentsKey, String widthKey, String heightKey, ResourceBundle lang) {
        String contents = lang.getString(contentsKey);
        TextArea textBox = createTextBox(contents, true);

        double width = getSizeFromKey(widthKey, lang);
        double height = getSizeFromKey(heightKey, lang);
        textBox.setPrefSize(width, height);

        return textBox;
    }

    public static void adjustCodeView(TextArea codeView) {
        Text code = layoutText(codeView);

        double width = code.getLayoutBounds().getWidth();
        setSizes(codeView, width / 2, width / 4);

        int caretPosition = codeView.caretPositionProperty().get();
        // Not happy with this workaround, but oh well.
        codeView.positionCaret(Integer.MAX_VALUE);
        codeView.positionCaret(caretPosition);
    }
    public static TextArea initCodeView(String code, ChoiceBox<String> fontMenu) {
        TextArea codeView = createTextBox(code, false);

        Font font = Font.font(fontMenu.getSelectionModel().getSelectedItem());
        codeView.setFont(font);

        adjustCodeView(codeView);

        return codeView;
    }
}