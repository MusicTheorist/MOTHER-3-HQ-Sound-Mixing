package org.musictheorist.mother3hqaudiopatcher.ui.main;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.patching.PatchStates;
import org.musictheorist.mother3hqaudiopatcher.resources.Languages;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.ui.Layout;
import org.musictheorist.mother3hqaudiopatcher.ui.SpacedNode;
import org.musictheorist.mother3hqaudiopatcher.ui.main.listview.SampleRateListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public final class PatcherWindow {
    private final Stage patcher;
    private final GridPane layout;
    private final PatcherActions patcherActions;

    private Scene scene;

    private Label selectROM;
    private TextField romPath;
    private Button romOpener;
    private Label selectLang;
    private RadioButton japanOption;
    private RadioButton fanOption;
    private ToggleGroup patchGroup;
    private Label adjustRate;
    private SampleRateListView sampleRates;
    private Label japanOnly;
    private Label japanVersion;
    private RadioButton defaultOption;
    private RadioButton altOption;
    private ToggleGroup versionGroup;
    private Button disclaimers;
    private Button sourceCode;
    private Button hackCredits;
    private Label rateInfo;
    private Label firstAsterisk;
    private Label secondAsterisk;
    private MenuButton langMenu;
    private CheckBox backupROM;
    private Button undoPatch;
    private Button patchROM;

    public PatcherWindow(Stage patcher, PatcherActions patcherActions, ResourceBundle lang) {
        this.patcher = patcher;
        this.patcherActions = patcherActions;
        this.layout = new GridPane();

        patcherActions.setGUI(this, patcher);

        initNodes();
        initGrid();
        initSizes();
        initROMPathField();
        initRadioButtons();
        initBackupTickBox();
        initLocalizedNodes(true, false, false, lang);
        initLayout();
        initScene(lang);
        disablePatchNodes(true, true);
    }

    private void initNodes() {
        selectROM = new Label("");
        selectROM.setAlignment(Pos.CENTER_RIGHT);

        romPath = new TextField("");
        romPath.setAlignment(Pos.CENTER_LEFT);

        romOpener = new Button("");
        romOpener.setDefaultButton(true);

        selectLang = new Label("");
        selectLang.setAlignment(Pos.CENTER);

        japanOption = new RadioButton("");
        japanOption.setAlignment(Pos.CENTER_RIGHT);

        fanOption = new RadioButton("");

        patchGroup = new ToggleGroup();
        japanOption.setToggleGroup(patchGroup);
        fanOption.setToggleGroup(patchGroup);

        adjustRate = new Label("");
        adjustRate.setAlignment(Pos.CENTER);

        sampleRates = new SampleRateListView();

        japanOnly = new Label("");
        japanOnly.setAlignment(Pos.CENTER);

        japanVersion = new Label("");
        japanVersion.setAlignment(Pos.CENTER);
        japanVersion.setTextAlignment(TextAlignment.LEFT);

        defaultOption = new RadioButton("");
        defaultOption.setAlignment(Pos.CENTER_RIGHT);

        altOption = new RadioButton("");

        versionGroup = new ToggleGroup();
        defaultOption.setToggleGroup(versionGroup);
        altOption.setToggleGroup(versionGroup);

        disclaimers = new Button("");
        disclaimers.setAlignment(Pos.CENTER);

        sourceCode = new Button("");
        sourceCode.setAlignment(Pos.CENTER);

        hackCredits = new Button("");
        hackCredits.setAlignment(Pos.CENTER);

        rateInfo = new Label("");
        rateInfo.setTextAlignment(TextAlignment.LEFT);

        firstAsterisk = new Label("");
        firstAsterisk.setTextAlignment(TextAlignment.LEFT);

        secondAsterisk = new Label("");
        secondAsterisk.setTextAlignment(TextAlignment.LEFT);

        langMenu = new MenuButton("");
        backupROM = new CheckBox("");
        undoPatch = new Button("");
        patchROM = new Button("");
    }

    void disableROMSelectionNodes(boolean disabled) {
        Color stateColor = disabled ? Color.GRAY : Color.BLACK;
        selectLang.setTextFill(stateColor);
        japanOption.setDisable(disabled);
        fanOption.setDisable(disabled);
    }

    void disableGeneralPatchNodes(boolean disabled) {
        Color stateColor = disabled ? Color.GRAY : Color.BLACK;
        adjustRate.setTextFill(stateColor);
        sampleRates.setDisable(disabled);
        rateInfo.setTextFill(stateColor);
        firstAsterisk.setTextFill(stateColor);
        secondAsterisk.setTextFill(stateColor);
        backupROM.setDisable(disabled);
        patchROM.setDisable(disabled);
    }

    void disableUndoPatchButton(boolean disabled) {
        undoPatch.setDisable(disabled);
    }

    void disableJapanPatchNodes(boolean disabled) {
        Color stateColor = disabled ? Color.GRAY : Color.BLACK;
        japanOnly.setTextFill(stateColor);
        japanVersion.setTextFill(stateColor);
        defaultOption.setDisable(disabled);
        altOption.setDisable(disabled);
    }

    private void disablePatchNodes(boolean noROMLoaded, boolean noPatchFound) {
        disableROMSelectionNodes(noROMLoaded);
        disableJapanPatchNodes(true);
        disableGeneralPatchNodes(true);
        disableUndoPatchButton(noPatchFound);
    }

    private void initLabels(boolean noROMLoaded, ResourceBundle lang) {
        selectROM.setText(lang.getString("labelSelectROM"));
        if(noROMLoaded) romPath.setText(lang.getString("textNoROMLoaded"));

        romOpener.setText(lang.getString("buttonOpenROM"));
        romOpener.setMnemonicParsing(true);

        selectLang.setText(lang.getString("labelSelectROMType"));
        japanOption.setText(lang.getString("radioJapanROM"));
        fanOption.setText(lang.getString("radioFanROM"));

        adjustRate.setText(lang.getString("labelAdjustRate"));

        japanOnly.setText(lang.getString("labelJapanOnly"));
        japanVersion.setText(lang.getString("labelJapanVer"));
        defaultOption.setText(lang.getString("radioJapanDefault"));
        altOption.setText(lang.getString("radioJapanAlt"));

        disclaimers.setText(lang.getString("buttonDisclaimers"));
        disclaimers.setMnemonicParsing(true);

        sourceCode.setText(lang.getString("buttonSourceCode"));
        sourceCode.setMnemonicParsing(true);

        hackCredits.setText(lang.getString("buttonCredits"));
        hackCredits.setMnemonicParsing(true);

        rateInfo.setText(lang.getString("labelRateInfo"));
        firstAsterisk.setText(lang.getString("labelRateAsterisk1"));
        secondAsterisk.setText(lang.getString("labelRateAsterisk2"));

        backupROM.setText(lang.getString("tickBackupROM"));

        undoPatch.setText(lang.getString("buttonUndoPatch"));
        undoPatch.setMnemonicParsing(true);

        patchROM.setText(lang.getString("buttonPatchROM"));
        patchROM.setMnemonicParsing(true);
    }

    private void initGrid() {
        int rowCount = 9;
        int colCount = 5;

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            rowConstraints.setFillHeight(true);
            layout.getRowConstraints().add(rowConstraints);
        }
        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.setFillWidth(true);
            columnConstraints.setHalignment(HPos.CENTER);
            layout.getColumnConstraints().add(columnConstraints);
        }
    }

    private void initSizes() {
        selectROM.setMaxSize(Double.MAX_VALUE, 30);
        romPath.setMaxSize(640, 30);
        romOpener.setMaxSize(Double.MAX_VALUE, 30);
        selectLang.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        japanOption.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        fanOption.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        langMenu.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        japanOnly.setMaxWidth(Double.MAX_VALUE);
        japanVersion.setMaxWidth(Double.MAX_VALUE);
        defaultOption.setMaxWidth(Double.MAX_VALUE);
        altOption.setMaxWidth(Double.MAX_VALUE);
        adjustRate.setMaxWidth(Double.MAX_VALUE);

        int listHeight = (24 * (SampleRates.LENGTH - 4)) - 4;
        sampleRates.setMaxSize(220, listHeight);
    }

    private void initROMPathField() {
        romPath.setEditable(false);
        romPath.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                layout.requestFocus();
            }
        });
    }

    private void initRadioButtons() {
        japanOption.setUserData(PatchStates.JAPAN_PATCH);
        fanOption.setUserData(PatchStates.FAN_PATCH);
        defaultOption.setUserData(false);
        altOption.setUserData(true);
    }

    private void initToggles(ResourceBundle lang) {
        patchGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                patcherActions.changePatchType(patchGroup, lang);
            }
        });
        versionGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                patcherActions.changeJapanPatch(versionGroup);
            }
        });
    }

    void toggleDefaultJapanPatchIfNull() {
        if(versionGroup.getSelectedToggle() == null) {
            versionGroup.selectToggle(defaultOption);
        }
    }

    private void scrollToRate(int rate) {
        if(rate <= 6) sampleRates.scrollTo(0);
        else          sampleRates.scrollTo(sampleRates.getItems().size());
    }
    void selectSampleRate(int rate) {
        sampleRates.getSelectionModel().select(rate);
        scrollToRate(rate);
    }
    void selectDefaultSampleRate() {
        selectSampleRate(SampleRates.DEFAULT.index());
    }

    private String toggleListCell(String key, boolean japanROM, ResourceBundle lang) {
        String item = lang.getString(key);
        return item + ((item.endsWith("d") || japanROM) ? "" : "d");
    }

    private void initSampleRateList(boolean japanROM, ResourceBundle lang) {
        ArrayList<String> rates = new ArrayList<String>();
        rates.add(lang.getString("rate5kHz"));
        rates.add(lang.getString("rate10kHz"));
        rates.add(lang.getString("rate13kHz"));
        rates.add(lang.getString("rateDefault"));
        rates.add(lang.getString("rate18kHz"));
        rates.add(toggleListCell("rate21kHz", japanROM, lang));
        rates.add(toggleListCell("rate26kHz", japanROM, lang));
        rates.add(toggleListCell("rate31kHz", japanROM, lang));
        rates.add(toggleListCell("rate36kHz", japanROM, lang));
        rates.add(toggleListCell("rate40kHz", japanROM, lang));
        rates.add(toggleListCell("rate42kHz", japanROM, lang));

        ObservableList<String> rateItems = FXCollections.observableArrayList(rates);
        sampleRates.setItems(rateItems);
        scrollToRate(sampleRates.getSelectionModel().getSelectedIndex());
    }

    private void initSampleRateMenu(boolean japanROM, ResourceBundle lang) {
        initSampleRateList(japanROM, lang);
        sampleRates.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                patcherActions.changeSampleRate(sampleRates);
            }
        });
    }

    private void initButtons(ResourceBundle lang) {
        romOpener.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patcherActions.chooseROM(lang);
            }
        });

        undoPatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patcherActions.undoPatch(lang);
            }
        });

        patchROM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patcherActions.patchROM(lang);
            }
        });

        disclaimers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                patcherActions.viewDisclaimers(lang);
            }
        });

        sourceCode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patcherActions.viewMixerSourceCode(lang);
            }
        });

        hackCredits.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patcherActions.viewHackCredits(lang);
            }
        });
    }

    private void initLangMenu(ResourceBundle lang) {
        langMenu.setText(lang.getString(lang.getString("langKey")));
        langMenu.getItems().clear();

        Languages[] allLangs = Languages.allLangs();
        for(Languages language : allLangs) {
            MenuItem nextLang = new MenuItem(lang.getString(language.langKey()));
            nextLang.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    patcherActions.changeLanguage(language);
                }
            });
            langMenu.getItems().add(nextLang);
        }
    }

    private void initBackupTickBox() {
        backupROM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patcherActions.toggleROMBackup(backupROM);
            }
        });
        backupROM.setSelected(false);
        backupROM.setAllowIndeterminate(false);
        backupROM.fire();
    }

    private ArrayList<HBox> initHBoxes() {
        ArrayList<HBox> hBoxes = new ArrayList<HBox>();

        Insets langMenuMargin = new Insets(8, 4, 4, 8),
            disclaimersMargin = new Insets(8, 4, 4, 4),
             sourceCodeMargin = new Insets(8, 4, 4, 4),
            hackCreditsMargin = new Insets(8, 8, 4, 4);
        SpacedNode langMenu = new SpacedNode(this.langMenu, langMenuMargin, null),
                disclaimers = new SpacedNode(this.disclaimers, disclaimersMargin, null),
                 sourceCode = new SpacedNode(this.sourceCode, sourceCodeMargin, null),
                hackCredits = new SpacedNode(this.hackCredits, hackCreditsMargin, null);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER_LEFT, langMenu, disclaimers, sourceCode, hackCredits));

        Insets firstLineMargin = new Insets(4, 0, 4, 0);
        SpacedNode firstLine = new SpacedNode(new Separator(), firstLineMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER, firstLine));

        Insets   romPathMargin = new Insets(0, 0, 0, 3),
               romOpenerMargin = new Insets(0, 0, 0, 5);
        SpacedNode selectROM = new SpacedNode(this.selectROM, null, null),
                     romPath = new SpacedNode(this.romPath, romPathMargin, Priority.ALWAYS),
                   romOpener = new SpacedNode(this.romOpener, romOpenerMargin, null);
        Insets firstRowPadding = new Insets(4, 8, 4, 8);
        hBoxes.add(Layout.initHBox(firstRowPadding, Pos.CENTER, selectROM, romPath, romOpener));

        Insets secondLineMargin = new Insets(4, 0, 4, 0);
        SpacedNode secondLine = new SpacedNode(new Separator(), secondLineMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER, secondLine));

        Insets selectLangMargin = new Insets(0, 8, 0, 8);
        SpacedNode selectLang = new SpacedNode(this.selectLang, selectLangMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, null, selectLang));

        Insets japanOptionMargin = new Insets(8, 0, 4, 8),
                 fanOptionMargin = new Insets(8, 8, 4, 18);
        SpacedNode japanOption = new SpacedNode(this.japanOption, japanOptionMargin, Priority.ALWAYS),
                     fanOption = new SpacedNode(this.fanOption, fanOptionMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, null, japanOption, fanOption));

        Insets thirdLineMargin = new Insets(2, 0, 2, 0);
        SpacedNode thirdLine = new SpacedNode(new Separator(), thirdLineMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER, thirdLine));

        Insets japanOnlyMargin = new Insets(0, 18, 4, 18);
        SpacedNode japanOnly = new SpacedNode(this.japanOnly, japanOnlyMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, null, japanOnly));

        Insets japanVersionMargin = new Insets(0, 18, 8, 18);
        SpacedNode japanVersion = new SpacedNode(this.japanVersion, japanVersionMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, null, japanVersion));

        Insets defaultOptionMargin = new Insets(0, 0, 8, 8),
                   altOptionMargin = new Insets(0, 8, 8, 18);
        SpacedNode defaultOption = new SpacedNode(this.defaultOption, defaultOptionMargin, Priority.ALWAYS),
                       altOption = new SpacedNode(this.altOption, altOptionMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, null, defaultOption, altOption));

        Insets adjustRateMargin = new Insets(4, 10, 4, 10);
        SpacedNode adjustRate = new SpacedNode(this.adjustRate, adjustRateMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, null, adjustRate));

        Insets sampleRatesMargin = new Insets(0, 10, 8, 10);
        SpacedNode sampleRates = new SpacedNode(this.sampleRates, sampleRatesMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER, sampleRates));

        Insets rateInfoMargin = new Insets(4, 10, 4, 4);
        SpacedNode rateInfo = new SpacedNode(this.rateInfo, rateInfoMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER_LEFT, rateInfo));

        Insets firstAsteriskMargin = new Insets(4, 8, 4, 4);
        SpacedNode firstAsterisk = new SpacedNode(this.firstAsterisk, firstAsteriskMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER_LEFT, firstAsterisk));

        Insets secondAsteriskMargin = new Insets(4, 8, 4, 4);
        SpacedNode secondAsterisk = new SpacedNode(this.secondAsterisk, secondAsteriskMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER_LEFT, secondAsterisk));

        Insets fourthLineMargin = new Insets(4, 0, 8, 0);
        SpacedNode fourthLine = new SpacedNode(new Separator(), fourthLineMargin, Priority.ALWAYS);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER, fourthLine));

        Insets backupROMMargin = new Insets(0, 10, 10, 10),
               undoPatchMargin = new Insets(2, 5, 10, 0),
                patchROMMargin = new Insets(2, 10, 10, 0);
        SpacedNode backupROM = new SpacedNode(this.backupROM, backupROMMargin, null),
                   undoPatch = new SpacedNode(this.undoPatch, undoPatchMargin, null),
                    patchROM = new SpacedNode(this.patchROM, patchROMMargin, null);
        hBoxes.add(Layout.initHBox(Insets.EMPTY, Pos.CENTER_RIGHT, backupROM, undoPatch, patchROM));

        return hBoxes;
    }

    private ArrayList<VBox> initVBoxes(ArrayList<HBox> hBoxes) {
        ArrayList<VBox> vBoxes = new ArrayList<VBox>();

        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(0, 1)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(1, 2)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(2, 3)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(3, 4)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(4, 6)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(6, 7)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(7, 10)));

        SpacedNode verticalLine = new SpacedNode(new Separator(Orientation.VERTICAL), null, Priority.ALWAYS);
        vBoxes.add(Layout.initVBox(Insets.EMPTY, Pos.CENTER, verticalLine));

        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(10, 12)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(12, 15)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(15, 16)));
        vBoxes.add(Layout.initVBoxWithHBoxes(Pos.CENTER, hBoxes.subList(16, 17)));

        return vBoxes;
    }

    void initLayout() {
        ArrayList<VBox> layoutNodes = initVBoxes(initHBoxes());
        layout.add(layoutNodes.get( 0), 0, 0, 5, 1);
        layout.add(layoutNodes.get( 1), 0, 1, 5, 1);
        layout.add(layoutNodes.get( 2), 0, 2, 5, 1);
        layout.add(layoutNodes.get( 3), 0, 3, 5, 1);
        layout.add(layoutNodes.get( 4), 0, 4, 1, 1);
        layout.add(layoutNodes.get( 5), 0, 5, 1, 1);
        layout.add(layoutNodes.get( 6), 0, 6, 1, 1);
        layout.add(layoutNodes.get( 7), 1, 4, 1, 3);
        layout.add(layoutNodes.get( 8), 2, 4, 1, 3);
        layout.add(layoutNodes.get( 9), 3, 4, 1, 3);
        layout.add(layoutNodes.get(10), 0, 7, 5, 1);
        layout.add(layoutNodes.get(11), 0, 8, 5, 1);
    }

    private void initDragAndDrop(Scene scene, ResourceBundle lang) {
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();

                if(event.getGestureSource() != layout
                && dragboard.hasFiles()
                && patcherActions.isDragOverROM(dragboard.getFiles().get(0))) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;

                if (event.getGestureSource() != layout
                 && dragboard.hasFiles()) {
                    File draggedFile = dragboard.getFiles().get(0);
                    patcherActions.checkROMPathBeforeLoad(draggedFile, false, lang);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    void initLocalizedNodes(boolean noROMLoaded, boolean japanROM, boolean sceneLoaded, ResourceBundle lang) {
        initLabels(noROMLoaded, lang);
        initSampleRateMenu(japanROM, lang);
        initToggles(lang);
        initButtons(lang);
        initLangMenu(lang);
        if(sceneLoaded) initDragAndDrop(scene, lang);
        patcher.setTitle(lang.getString("titleMain"));
        patcher.setOnCloseRequest(event -> {
            patcherActions.closeResources(lang);
        });
    }

    private void initScene(ResourceBundle lang) {
        scene = new Scene(layout);
        String fontFamily = Font.getDefault().getFamily();
        Parent root = scene.getRoot();
        root.setStyle("-fx-font: 14 \"" + fontFamily + "\";");
        initDragAndDrop(scene, lang);
    }

    public void initStage() {
        patcher.setScene(scene);
        patcher.setResizable(false);
        patcher.getIcons().addAll(patcherActions.getIcons());
        langMenu.setGraphic(patcherActions.getLangMenuIcon());
    }

    public void display() {
        patcher.show();
        layout.requestFocus();
    }

    void initNewPatchState(PatchStates patchState, ResourceBundle lang) {
        disableROMSelectionNodes(false);
        disableGeneralPatchNodes(false);
        disableJapanPatchNodes(patchState != PatchStates.JAPAN_PATCH);
        initSampleRateList(patchState == PatchStates.JAPAN_PATCH, lang);
        patchROM.setDefaultButton(true);
        romOpener.setDefaultButton(false);
    }

    void initPatcherState(String absolutePath, PatchStates patchState, boolean altJapanPatch, int rate, ResourceBundle lang) {
        if(patchState == PatchStates.JAPAN_PATCH || patchState == PatchStates.FAN_PATCH) {
            initNewPatchState(patchState, lang);
            disableUndoPatchButton(false);

            if(patchState == PatchStates.JAPAN_PATCH) {
                patchGroup.selectToggle(japanOption);
                versionGroup.selectToggle(altJapanPatch ? altOption : defaultOption);
            }
            else {
                patchGroup.selectToggle(fanOption);
            }
        }
        else {
            disablePatchNodes(false, true);

            if(patchGroup.getSelectedToggle() != null) {
                patchGroup.selectToggle(null);
            }
            if(versionGroup.getSelectedToggle() != null) {
                versionGroup.selectToggle(null);
            }
        }
        romPath.setText(absolutePath);
        selectSampleRate(rate);
    }

    public GridPane getLayout() {
        return layout;
    }
}